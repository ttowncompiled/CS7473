package main;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.cli.ParseException;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

import lib.Triple;
import lib.headers.ARPHeader;
import lib.headers.EthernetHeader;
import lib.headers.Header;
import lib.headers.ICMPHeader;
import lib.headers.IPHeader;
import lib.headers.TCPHeader;
import lib.headers.UDPHeader;
import lib.packets.Packet;
import util.*;

public class Sniffer {
	
	public static final String NAME = "Sniffer";
	private static Pcap adapter = null;
	private static int count = 0;

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		SnifferCLI cli = new SnifferCLI(Sniffer.NAME, args);
		if (cli.hasHelp()) {
			Sniffer.help(cli);
		} else if (cli.hasShow()) {
			Sniffer.show(cli);
		} else if (cli.hasInput()) {
			Sniffer.sniffFile(cli);
		} else {
			Sniffer.sniffNetwork(cli);
		}
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
	
	private static void sniffFile(SnifferCLI cli) throws FileNotFoundException, IOException {
		HexString[] hexes = HexFile.parse(cli.getInput());
		if (cli.hasOutput()) {
			FileWriter writer = new FileWriter(cli.getOutput(), false);
			writer.append("");
			writer.close();
		}
		HashMap<String, ArrayList<Packet>> fragments = new HashMap<>();
		for (int i = 0; i < hexes.length && (! cli.hasCount() || Sniffer.count < cli.getCount()); i++) {
			HexString hex = hexes[i];
			Packet p = null;
			if (Sniffer.isEthernetPacket(hex) && (! cli.hasType() || cli.hasValidType())) {
				p = Sniffer.processEthernetPacket(cli, hex);
			}
			if (p == null || (cli.hasType() && (! cli.hasValidType() || ! Sniffer.checkType(p, cli.getType())))) {
				continue;
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
			if (p.getNext().getType().equals(Config.ARP)) {
				Triple t = Triple.ARPTriple(p.getNext());
			}
			if (p.getNext().getType().equals(Config.IP)) {
				IPHeader header = (IPHeader) p.getNext().getHeader();
				if (! (header.getFlags()[2] == true || header.getFragmentationOffset() > 0)) {
					continue;
				}
				String key = Utils.key(header.getIdentification());
				if (! fragments.containsKey(key)) {
					fragments.put(key, new ArrayList<>());
				}
				fragments.get(key).add(p.getNext());
			}
		}
	}
	
	private static void sniffNetwork(SnifferCLI cli) throws IOException {
		if (cli.hasDev()) {
			Sniffer.adapter = Sniffer.openAdapter(cli.getDev());
		} else {
			Sniffer.adapter = Sniffer.openAdapter();
		}
		if (Sniffer.adapter == null) {
			return;
		}
		if (cli.hasOutput()) {
			FileWriter writer = new FileWriter(cli.getOutput(), false);
			writer.append("");
			writer.close();
		}
		Sniffer.adapter.loop(Pcap.LOOP_INFINITE, new PcapPacketHandler<SnifferCLI>() {
			public void nextPacket(PcapPacket packet, SnifferCLI cli) {
				System.out.println(">>> Packet received.");
				if (cli.hasCount() && Sniffer.count > cli.getCount()) {
					return;
				}
				ByteBuffer bbuff = ByteBuffer.allocate(packet.size());
				packet.transferTo(bbuff);
				HexString hex = BitString.fromBytes(bbuff.array()).toHexString();
				Packet p = null;
				try {
					if (Sniffer.isEthernetPacket(hex) && (! cli.hasType() || cli.hasValidType())) {
						p = Sniffer.processEthernetPacket(cli, hex);
					}
					if (p == null || (cli.hasType() && (! cli.hasValidType() || ! Sniffer.checkType(p, cli.getType())))) {
						return;
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
				} catch (IOException e) {
					Sniffer.adapter.close();
				}
			}
		}, cli);
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
		Packet p = null;
		if (header.getProtocol() == Config.IP_ICMP_PROTOCOL) {
			p = Sniffer.processICMPPacket(cli, hex);
		} else if (header.getProtocol() == Config.IP_TCP_PROTOCOL) {
			p = Sniffer.processTCPPacket(cli, hex);
		} else if (header.getProtocol() == Config.IP_UDP_PROTOCOL) {
			p = Sniffer.processUDPPacket(cli, hex);
		}
		return p != null ? new Packet(header, p) : null;
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
		return Sniffer.isEthernetEtherType(header.getEtherType());
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
	
	private static void log(SnifferCLI cli, Packet p) throws IOException {
		Sniffer.log(cli, p, false);
	}
	
	private static void log(SnifferCLI cli, Packet p, boolean headerOnly) throws IOException {
		if (headerOnly) {
			if ((cli.hasOutput() && ! cli.hasHuman()) || cli.hasHex()) {
				HexString hex = p.getHeader().toHexString();
				p = p.getNext();
				while (p != null) {
					hex = hex.concat(p.getHeader().toHexString());
					p = p.getNext();
				}
				Sniffer.log(cli, hex);
			} else {
				while (p != null) {
					Sniffer.log(cli, p.getHeader());
					p = p.getNext();
				}
			}
		} else {
			if ((cli.hasOutput() && ! cli.hasHuman()) || cli.hasHex()) {
				Sniffer.log(cli, p.toHexString());
			} else {
				Sniffer.log(cli, p.toString(), p.extractPlaintext());
			}
		}
	}
	
	private static void log(SnifferCLI cli, Header h) throws IOException {
		if ((cli.hasOutput() && ! cli.hasHuman()) || cli.hasHex()) {
			Sniffer.log(cli, h.toHexString());
		} else {
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
		Sniffer.check(cli);
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
		Sniffer.check(cli);
	}
	
	private static void check(SnifferCLI cli) {
		Sniffer.count++;
		if (cli.hasCount() && Sniffer.count >= cli.getCount()) {
			Sniffer.exit();
		}
	}
	
	private static void exit() {
		if (Sniffer.adapter != null) {
			Sniffer.adapter.close();
		}
		System.exit(0);
	}
}
