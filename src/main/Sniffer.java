package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.cli.ParseException;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

import lib.Triple;
import lib.headers.*;
import lib.packets.*;
import lib.rules.Rule;
import lib.rules.RuleModule;
import util.*;

public class Sniffer {
	
	public static final String NAME = "Sniffer";
	private static Pcap adapter = null;

	private static int count = 0;
	private static HashMap<String, ArrayList<IPPacket>> fragments = new HashMap<>();
	private static HashMap<String, Timestamp> timestamps = new HashMap<>();

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		SnifferCLI cli = new SnifferCLI(Sniffer.NAME, args);
		RuleModule rules = Sniffer.readRules(cli);
		if (cli.hasHelp()) {
			Sniffer.help(cli);
		} else if (cli.hasShow()) {
			Sniffer.show(cli);
		} else if (cli.hasInput()) {
			Sniffer.sniffFile(cli, rules);
		} else {
			Sniffer.sniffNetwork(cli, rules);
		}
	}
	
	private static RuleModule readRules(SnifferCLI cli) throws FileNotFoundException {
		if (! cli.hasRules()) {
			return null;
		}
		RuleModule rules = new RuleModule();
		Scanner scanner = new Scanner(new File(Config.RULES_PATH));
		while (scanner.hasNextLine()) {
			Rule rule = Rule.parseRule(scanner.nextLine());
			rules.addRule(rule);
		}
		scanner.close();
		return rules;
	}
	
	private static void help(SnifferCLI cli) {
		cli.help();
	}
	
	private static void show(SnifferCLI cli) throws IOException {
		ArrayList<PcapIf> devices = new ArrayList<>();
		StringBuilder errBuf = new StringBuilder();
		int result = Pcap.findAllDevs(devices, errBuf);
		if (result == Pcap.ERROR || devices.isEmpty()) {
			return;
		}
		for (int i = 0; i < devices.size(); i++) {
			Sniffer.log(cli, devices.get(i).getName(), devices.get(i).toString());
		}
	}
	
	private static void start(SnifferCLI cli) throws FileNotFoundException, IOException {
		if (cli.hasOutput()) {
			FileWriter writer = new FileWriter(cli.getOutput(), false);
			writer.append("");
			writer.close();
		}
	}
	
	private static void sniffFile(SnifferCLI cli, RuleModule rules) throws FileNotFoundException, IOException {
		HexString[] hexes = HexFile.parse(cli.getInput());
		Sniffer.start(cli);
		for (int i = 0; i < hexes.length && (! cli.hasCount() || Sniffer.count < cli.getCount()); i++) {
			HexString hex = hexes[i];
			if (Sniffer.isEthernetPacket(hex) && (! cli.hasType() || cli.hasValidType())) {
				Packet p = Sniffer.processEthernetPacket(cli, hex);
				Sniffer.processPacket(cli, rules, p);
			}
		}
	}
	
	private static void sniffNetwork(SnifferCLI cli, RuleModule rules) throws IOException {
		if (cli.hasDev()) {
			Sniffer.adapter = Sniffer.openAdapter(cli.getDev());
		} else {
			Sniffer.adapter = Sniffer.openAdapter();
		}
		if (Sniffer.adapter == null) {
			return;
		}
		Sniffer.start(cli);
		Sniffer.adapter.loop(Pcap.LOOP_INFINITE, new PcapPacketHandler<SnifferCLI>() {
			public void nextPacket(PcapPacket packet, SnifferCLI cli) {
				System.out.println(">>> Packet received.");
				if (cli.hasCount() && Sniffer.count > cli.getCount()) {
					return;
				}
				ByteBuffer bbuff = ByteBuffer.allocate(packet.size());
				packet.transferTo(bbuff);
				HexString hex = BitString.fromBytes(bbuff.array()).toHexString();
				try {
					if (Sniffer.isEthernetPacket(hex) && (! cli.hasType() || cli.hasValidType())) {
						Packet p = Sniffer.processEthernetPacket(cli, hex);
						Sniffer.processPacket(cli, rules, p);
					}
				} catch (IOException e) {
					Sniffer.adapter.close();
				}
			}
		}, cli);
	}
	
	private static void processPacket(SnifferCLI cli, RuleModule rules, Packet p) throws IOException {
		if (p == null || (cli.hasType() && (! cli.hasValidType() || ! Sniffer.checkType(p, cli.getType())))) {
			return;
		}
		p.show();
		Sniffer.checkRules(cli, rules, p.getNext());
		if (p.getNext().getType().equals(Config.ARP)) {
			Sniffer.log(cli, Triple.ARPTriple(p.getNext()));
		}
		if (p.getNext().getType().equals(Config.IP)) {
			IPPacket ip = (IPPacket) p.getNext();
			if (ip.getHeader().isFragment()) {
				String key = Utils.key(ip.getHeader().getIdentification());
				Sniffer.insert(key, ip);
				if (Sniffer.checkTimeout(cli, key)) {
					ArrayList<IPPacket> frags = Sniffer.take(key);
					Sniffer.log(cli, Triple.TimeOutTriple(frags.get(0), frags));
					return;
				} else if (Sniffer.checkAssembly(key)) {
					ArrayList<IPPacket> frags = Sniffer.take(key);
					if (! Sniffer.checkSize(frags)) {
						Sniffer.log(cli, Triple.TooLargeTriple(frags.get(0), frags));
						return;
					} else {
						Triple t = Sniffer.assemble(cli, frags);
						Sniffer.log(cli, t);
						p = new Packet(p.getHeader(), t.getDatagram());
						Sniffer.checkRules(cli, rules, t.getDatagram());
					}
				} else {
					return;
				}
			}
		}
		if (cli.hasHeaderInfo() && ! cli.hasType()) {
			Sniffer.sniffHeaderInfo(p, cli);
		}
		if (cli.hasHeaderInfo() && cli.hasType() && cli.hasValidType()) {
			Sniffer.sniffHeaderInfo(p, cli.getType(), cli);
		}
		if (! cli.hasHeaderInfo()) {
			Sniffer.log(cli, p);
		}
		Sniffer.checkCount(cli);
	}
	
	private static void checkRules(SnifferCLI cli, RuleModule rules, Packet p) throws IOException {
		ArrayList<Rule> violations = rules.checkPacket(p);
		for (Rule r : violations) {
			Sniffer.log(cli, r.toString());
		}
	}
	
	private static boolean checkTimeout(SnifferCLI cli, String key) {
		if (! Sniffer.timestamps.containsKey(key)) {
			return false;
		}
		Timestamp stamp = new Timestamp(System.currentTimeMillis());
		return stamp.getTime() - Sniffer.timestamps.get(key).getTime() >= 1000*cli.getTimeout();
	}
	
	private static boolean checkAssembly(String key) {
		if (Sniffer.fragments.get(key) == null || Sniffer.fragments.get(key).isEmpty()) {
			return false;
		}
		ArrayList<IPPacket> frags = Sniffer.fragments.get(key);
		if (! frags.get(0).getHeader().isFirstFragment()) {
			return false;
		}
		if (! frags.get(frags.size()-1).getHeader().isLastFragment()) {
			return false;
		}
		for (int i = 1; i < frags.size(); i++) {
			if (frags.get(i-1).getHeader().getLengthWithOffset() < frags.get(i).getHeader().getByteFragmentationOffset()) {
				return false;
			}
		}
		return true;
	}
	
	private static boolean checkSize(ArrayList<IPPacket> frags) {
		return frags.get(frags.size()-1).getHeader().getLengthWithOffset() <= Config.IP_MAX_LENGTH;
	}
	
	private static Triple assemble(SnifferCLI cli, ArrayList<IPPacket> frags) {
		if (Sniffer.checkOverlap(frags)) {
			return Triple.OverlapTriple(Sniffer.assembleDatagram(cli, frags), frags);
		}
		return Triple.NoOverlapTriple(Sniffer.assembleDatagram(cli, frags), frags);
	}
	
	private static boolean checkOverlap(ArrayList<IPPacket> frags) {
		for (int i = 1; i < frags.size(); i++) {
			if (frags.get(i-1).getHeader().getLengthWithOffset() > frags.get(i).getHeader().getByteFragmentationOffset()) {
				return true;
			}
		}
		return false;
	}
	
	private static IPPacket assembleDatagram(SnifferCLI cli, ArrayList<IPPacket> frags) {
		HexString hex = frags.get(0).getNext().toHexString().substring(frags.get(0).getHeader().getSegmentHexLength());
		for (int i = 1; i < frags.size(); i++) {
			HexString h = frags.get(i).getNext().toHexString().substring(frags.get(i).getHeader().getSegmentHexLength());
			int off = frags.get(i-1).getHeader().getLengthWithOffset() - frags.get(i).getHeader().getByteFragmentationOffset();
			hex = hex.concat(h.remove(2*off));
		}
		byte protocol = frags.get(0).getHeader().getProtocol();
		Packet p = null;
		if (protocol == Config.IP_ICMP_PROTOCOL) {
			p = Sniffer.processICMPPacket(cli, hex);
		} else if (protocol == Config.IP_TCP_PROTOCOL) {
			p = Sniffer.processTCPPacket(cli, hex);
		} else if (protocol == Config.IP_UDP_PROTOCOL) {
			p = Sniffer.processUDPPacket(cli, hex);
		}
		return p != null ? new IPPacket(IPHeader.Datagram(frags.get(0).getHeader(), (short) (frags.get(0).getHeader().getHeaderByteLength() + p.getByteLength())), p) : null;
	}
	
	private static Packet processEthernetPacket(SnifferCLI cli, HexString hex) {
		EthernetHeader header = EthernetHeader.parse(hex.substring(EthernetHeader.MAX_HEX).toBitString());
		if (header == null) {
			return null;
		}
		hex = hex.remove(header.getHeaderHexLength());
		Packet p = null;
		if (header.getEtherType() == Config.ETH_IP_ETHERTYPE) {
			p = Sniffer.processIPPacket(cli, hex);
		} else if (header.getEtherType() == Config.ETH_ARP_ETHERTYPE) {
			p = Sniffer.processARPPacket(cli, hex);
		}
		return p != null ? new Packet(header, p) : null;
	}
	
	private static Packet processIPPacket(SnifferCLI cli, HexString hex) {
		IPHeader header = IPHeader.parse(hex.substring(IPHeader.MAX_HEX).toBitString());
		if (header == null) {
			return null;
		}
		hex = hex.remove(header.getHeaderHexLength());
		if (! cli.hasSourceOrDest() && (cli.hasSource() && ! cli.hasValidSource() || cli.hasSourceAndDest() && ! cli.hasValidSourceAndDest() || cli.hasSource() && header.getSourceIPAddress() != cli.getSource() || cli.hasSourceAndDest() && header.getSourceIPAddress() != cli.getSourceAndDest()[0])) {
			return null;
		}
		if (! cli.hasSourceOrDest() && (cli.hasDest() && ! cli.hasValidDest() || cli.hasSourceAndDest() && ! cli.hasValidSourceAndDest() || cli.hasDest() && header.getDestinationIPAddress() != cli.getDest() || cli.hasSourceAndDest() && header.getDestinationIPAddress() != cli.getSourceAndDest()[1])) {
			return null;
		}
		if (cli.hasSourceOrDest()) {
			int[] sord = cli.getSourceOrDest();
			if (! cli.hasValidSourceOrDest() || header.getSourceIPAddress() != sord[0] && header.getDestinationIPAddress() != sord[1]) {
				return null;
			}
		}
		if (header.isFragment()) {
			return new IPPacket(header, new DataPacket(hex));
		}
		Packet p = null;
		if (header.getProtocol() == Config.IP_ICMP_PROTOCOL) {
			p = Sniffer.processICMPPacket(cli, hex);
		} else if (header.getProtocol() == Config.IP_TCP_PROTOCOL) {
			p = Sniffer.processTCPPacket(cli, hex);
		} else if (header.getProtocol() == Config.IP_UDP_PROTOCOL) {
			p = Sniffer.processUDPPacket(cli, hex);
		}
		return p != null ? new IPPacket(header, p) : null;
	}
	
	private static Packet processARPPacket(SnifferCLI cli, HexString hex) {
		ARPHeader header = ARPHeader.parse(hex.substring(ARPHeader.MAX_HEX).toBitString());
		if (header == null) {
			return null;
		}
		hex = hex.remove(header.getHeaderHexLength());
		if (! cli.hasSourceOrDest() && (cli.hasSource() && ! cli.hasValidSource() || cli.hasSourceAndDest() && ! cli.hasValidSourceAndDest() || cli.hasSource() && header.getSenderProtocolAddress() != cli.getSource() || cli.hasSourceAndDest() && header.getSenderProtocolAddress() != cli.getSourceAndDest()[0])) {
			return null;
		}
		if (! cli.hasSourceOrDest() && (cli.hasDest() && ! cli.hasValidDest() || cli.hasSourceAndDest() && ! cli.hasValidSourceAndDest() || cli.hasDest() && header.getTargetProtocolAddress() != cli.getDest() || cli.hasSourceAndDest() && header.getTargetProtocolAddress() != cli.getSourceAndDest()[1])) {
			return null;
		}
		if (cli.hasSourceOrDest()) {
			int[] sord = cli.getSourceOrDest();
			if (! cli.hasValidSourceOrDest() || header.getSenderProtocolAddress() != sord[0] && header.getTargetProtocolAddress() != sord[1]) {
				return null;
			}
		}
		return Packet.build(hex, header);
	}
	
	private static Packet processICMPPacket(SnifferCLI cli, HexString hex) {
		ICMPHeader header = ICMPHeader.parse(hex.substring(ICMPHeader.MAX_HEX).toBitString());
		if (header == null) {
			return null;
		}
		hex = hex.remove(header.getHeaderHexLength());
		return Packet.build(hex, header);
	}
	
	private static Packet processTCPPacket(SnifferCLI cli, HexString hex) {
		TCPHeader header = TCPHeader.parse(hex.substring(TCPHeader.MAX_HEX).toBitString());
		if (header == null) {
			return null;
		}
		hex = hex.remove(header.getHeaderHexLength());
		if (cli.hasSourcePort() && ! (cli.getSourcePortStart() <= header.getSourcePortAddress() && header.getSourcePortAddress() <= cli.getSourcePortEnd())) {
			return null;
		}
		if (cli.hasDestPort() && ! (cli.getDestPortStart() <= header.getDestinationPortAddress() && header.getDestinationPortAddress() <= cli.getDestPortEnd()) ) {
			return null;
		}
		return Packet.build(hex, header);
	}
	
	private static Packet processUDPPacket(SnifferCLI cli, HexString hex) {
		UDPHeader header = UDPHeader.parse(hex.substring(UDPHeader.MAX_HEX).toBitString());
		if (header == null) {
			return null;
		}
		hex = hex.remove(header.getHeaderHexLength());
		if (cli.hasSourcePort() && ! (cli.getSourcePortStart() <= header.getSourcePortAddress() && header.getSourcePortAddress() <= cli.getSourcePortEnd())) {
			return null;
		}
		if (cli.hasDestPort() && ! (cli.getDestPortStart() <= header.getDestinationPortAddress() && header.getDestinationPortAddress() <= cli.getDestPortEnd()) ) {
			return null;
		}
		return Packet.build(hex, header);
	}
	
	private static void sniffHeaderInfo(Packet p, SnifferCLI cli) throws IOException {
		Sniffer.log(cli,  p, true);
	}
	
	private static void sniffHeaderInfo(Packet p, String type, SnifferCLI cli) throws IOException {
		while (p != null && ! p.getType().equals(type)) {
			p = p.getNext();
		}
		if (p != null) {
			Sniffer.log(cli, p.getHeader());
		}
	}
	
	private static boolean checkType(Packet p, String type) {
		while (p != null) {
			if (p.getType().equals(type)) {
				return true;
			}
			p = p.getNext();
		}
		return false;
	}
	
	private static boolean isEthernetPacket(HexString hex) {
		EthernetHeader header = EthernetHeader.parse(hex.substring(EthernetHeader.MAX_HEX).toBitString());
		return header != null ? Sniffer.isEthernetEtherType(header.getEtherType()) : false;
	}
	
	private static boolean isEthernetEtherType(int etherType) {
		return etherType == Config.ETH_ARP_ETHERTYPE || etherType == Config.ETH_IP_ETHERTYPE;
	}
	
	private static Pcap openAdapter() throws IOException {
		return Sniffer.openAdapter(null);
	}
	
	private static Pcap openAdapter(String name) throws IOException {
		PcapIf device = Sniffer.getNetworkDevice(name);
		if (device == null) {
			return null;
		}
		return Pcap.openLive(device.getName(), Pcap.DEFAULT_SNAPLEN, Pcap.MODE_PROMISCUOUS, Pcap.DEFAULT_TIMEOUT, new StringBuilder());
	}
	
	private static PcapIf getNetworkDevice(String name) throws IOException {
		ArrayList<PcapIf> devices = new ArrayList<>();
		StringBuilder errBuf = new StringBuilder();
		int result = Pcap.findAllDevs(devices, errBuf);
		if (result == Pcap.ERROR || devices.isEmpty()) {
			return null;
		}
		if (name == null) {
			return devices.get(0);
		}
		for (int i = 0; i < devices.size(); i++) {
			if (devices.get(i).getName().equals(name)) {
				return devices.get(i);
			}
		}
		return null;
	}
	
	private static void log(SnifferCLI cli, Triple t) throws IOException {
		if (! cli.hasTripleOutputType()) {
			return;
		}
		if (cli.hasOutput()) {
			FileWriter writer = new FileWriter(cli.getOutput(), true);
			writer.append(t.toString());
			writer.append("\n");
			writer.close();
		} else {
			t.show();
		}
	}
	
	private static void log(SnifferCLI cli, Packet p) throws IOException {
		Sniffer.log(cli, p, false);
	}
	
	private static void log(SnifferCLI cli, Packet p, boolean headerOnly) throws IOException {
		if (headerOnly) {
			if (cli.hasHexOutputType()) {
				HexString hex = p.getHeader().toHexString();
				p = p.getNext();
				while (p != null) {
					hex = hex.concat(p.getHeader().toHexString());
					p = p.getNext();
				}
				Sniffer.log(cli, hex);
			} else if (cli.hasHumanOutputType()) {
				while (p != null) {
					Sniffer.log(cli, p.getHeader());
					p = p.getNext();
				}
			}
		} else {
			if (cli.hasHexOutputType()) {
				Sniffer.log(cli, p.toHexString());
			} else if (cli.hasHumanOutputType()) {
				Sniffer.log(cli, p.toString(), p.extractPlaintext());
			}
		}
	}
	
	private static void log(SnifferCLI cli, Header h) throws IOException {
		if (cli.hasHexOutputType()) {
			Sniffer.log(cli, h.toHexString());
		} else if (cli.hasHumanOutputType()) {
			Sniffer.log(cli, h.toString());
		}
	}
	
	private static void log(SnifferCLI cli, HexString hex) throws IOException {
		if (cli.hasOutput()) {
			FileWriter writer = new FileWriter(cli.getOutput(), true);
			writer.append(Utils.formatHexString(hex));
			writer.close();
		} else {
			System.out.println(Utils.formatHexString(hex));
		}
	}
	
	private static void log(SnifferCLI cli, String... S) throws IOException {
		if (cli.hasOutput()) {
			FileWriter writer = new FileWriter(cli.getOutput(), true);
			for (int i = 0; i < S.length; i++) {
				writer.append(S[i]);
				writer.append("\n");
			}
			writer.append("\n");
			writer.close();
		} else {
			for (int i = 0; i < S.length; i++) {
				System.out.println(S[i]);
			}
			System.out.println("");
		}
	}
	
	private static void checkCount(SnifferCLI cli) {
		Sniffer.count++;
		if (cli.hasCount() && Sniffer.count >= cli.getCount()) {
			Sniffer.exit();
		}
	}
	
	private static void insert(String key, IPPacket p) {
		if (! Sniffer.fragments.containsKey(key)) {
			Sniffer.fragments.put(key, new ArrayList<>());
			Sniffer.timestamps.put(key, new Timestamp(System.currentTimeMillis()));
		}
		for (int i = 0; i < Sniffer.fragments.get(key).size(); i++) {
			if (p.getHeader().getFragmentationOffset() < Sniffer.fragments.get(key).get(i).getHeader().getFragmentationOffset()) {
				Sniffer.fragments.get(key).add(i, p);
				return;
			}
		}
		Sniffer.fragments.get(key).add(p);
	}
	
	private static ArrayList<IPPacket> take(String key) {
		Sniffer.timestamps.remove(key);
		return Sniffer.fragments.remove(key);
	}
	
	private static void exit() {
		if (Sniffer.adapter != null) {
			Sniffer.adapter.close();
		}
		System.exit(0);
	}
}
