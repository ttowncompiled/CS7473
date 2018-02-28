import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.cli.ParseException;

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

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		CommandCLI cli = new CommandCLI(Sniffer.NAME, args);
		if (cli.hasHelp()) {
			Sniffer.help(cli);
		} else if (cli.hasInput()) {
			Sniffer.sniffFile(cli);
		} else {
			Sniffer.sniffNetwork(cli);
		}
	}
	
	private static void help(CommandCLI cli) {
		cli.help();
	}
	
	private static void sniffFile(CommandCLI cli) throws FileNotFoundException, IOException {
		HexString[] hexes = HexFile.parse(cli.getInput());
		if (cli.hasOutput()) {
			FileWriter writer = new FileWriter(cli.getOutput(), false);
			writer.append("");
			writer.close();
		}
		for (int i = 0; i < hexes.length && (! cli.hasCount() || i < cli.getCount()); i++) {
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
		}
	}
	
	private static Packet processEthernetPacket(CommandCLI cli, HexString hex) {
		EthernetHeader header = EthernetHeader.parse(hex.substring(EthernetHeader.MAX_HEX).toBitString());
		hex = hex.remove(header.getHeaderHexLength());
		Packet p = null;
		if (header.getEtherType() == Config.ETH_IP_ETHERTYPE) {
			p = Sniffer.processIPPacket(cli, hex);
		} else if (header.getEtherType() == Config.ETH_ARP_ETHERTYPE) {
			p = Sniffer.processARPPacket(cli, hex);
		}
		return p != null ? new Packet(header, p) : null;
	}
	
	private static Packet processIPPacket(CommandCLI cli, HexString hex) {
		IPHeader header = IPHeader.parse(hex.substring(IPHeader.MAX_HEX).toBitString());
		hex = hex.remove(header.getHeaderHexLength());
		if (! cli.hasSourceOrDest() && (cli.hasSource() && cli.hasValidSource() && header.getSourceIPAddress() != cli.getSource() || cli.hasSourceAndDest() && cli.hasValidSourceAndDest() && header.getSourceIPAddress() != cli.getSourceAndDest()[0])) {
			return null;
		}
		if (! cli.hasSourceOrDest() && (cli.hasDest() && cli.hasValidDest() && header.getDestinationIPAddress() != cli.getDest() || cli.hasSourceAndDest() && cli.hasValidSourceAndDest() && header.getDestinationIPAddress() != cli.getSourceAndDest()[1])) {
			return null;
		}
		if (cli.hasSourceOrDest() && cli.hasValidSourceOrDest()) {
			int[] sord = cli.getSourceOrDest();
			if (header.getSourceIPAddress() != sord[0] || header.getDestinationIPAddress() != sord[1]) {
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
	
	private static Packet processARPPacket(CommandCLI cli, HexString hex) {
		ARPHeader header = ARPHeader.parse(hex.substring(ARPHeader.MAX_HEX).toBitString());
		hex = hex.remove(header.getHeaderHexLength());
		Packet p = null;
		if (header.getProtocolType() == Config.ETH_IP_ETHERTYPE) {
			p = Sniffer.processIPPacket(cli, hex);
		}
		return p != null ? p : null;
	}
	
	private static Packet processICMPPacket(CommandCLI cli, HexString hex) {
		ICMPHeader header = ICMPHeader.parse(hex.substring(ICMPHeader.MAX_HEX).toBitString());
		hex = hex.remove(header.getHeaderHexLength());
		return Packet.build(hex, header);
	}
	
	private static Packet processTCPPacket(CommandCLI cli, HexString hex) {
		TCPHeader header = TCPHeader.parse(hex.substring(TCPHeader.MAX_HEX).toBitString());
		hex = hex.remove(header.getHeaderHexLength());
		if (cli.hasSourcePort() && ! (cli.getSourcePortStart() <= header.getSourcePortAddress() && header.getSourcePortAddress() <= cli.getSourcePortEnd())) {
			return null;
		}
		if (cli.hasDestPort() && ! (cli.getDestPortStart() <= header.getDestinationPortAddress() && header.getDestinationPortAddress() <= cli.getDestPortEnd()) ) {
			return null;
		}
		return Packet.build(hex, header);
	}
	
	private static Packet processUDPPacket(CommandCLI cli, HexString hex) {
		UDPHeader header = UDPHeader.parse(hex.substring(UDPHeader.MAX_HEX).toBitString());
		hex = hex.remove(header.getHeaderHexLength());
		if (cli.hasSourcePort() && ! (cli.getSourcePortStart() <= header.getSourcePortAddress() && header.getSourcePortAddress() <= cli.getSourcePortEnd())) {
			return null;
		}
		if (cli.hasDestPort() && ! (cli.getDestPortStart() <= header.getDestinationPortAddress() && header.getDestinationPortAddress() <= cli.getDestPortEnd()) ) {
			return null;
		}
		return Packet.build(hex, header);
	}
	
	private static void sniffHeaderInfo(Packet p, CommandCLI cli) throws IOException {
		Sniffer.log(cli,  p, true);
	}
	
	private static void sniffHeaderInfo(Packet p, String type, CommandCLI cli) throws IOException {
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
	
	private static void sniffNetwork(CommandCLI cli) {
		
	}
	
	private static boolean isEthernetPacket(HexString hex) {
		EthernetHeader header = EthernetHeader.parse(hex.substring(EthernetHeader.MAX_HEX).toBitString());
		return Sniffer.isEthernetEtherType(header.getEtherType());
	}
	
	private static boolean isEthernetEtherType(int etherType) {
		return etherType == Config.ETH_ARP_ETHERTYPE || etherType == Config.ETH_IP_ETHERTYPE;
	}
	
	private static String formatHexString(HexString hex) {
		StringBuilder rep = new StringBuilder();
		for (int i = 0; i < hex.length(); i += 32) {
			rep.append(hex.substring(i, i+32).spaced().toString()).append("\n");
		}
		return rep.append("\n").toString();
	}
	
	private static void log(CommandCLI cli, Packet p) throws IOException {
		Sniffer.log(cli, p, false);
	}
	
	private static void log(CommandCLI cli, Packet p, boolean headerOnly) throws IOException {
		if (headerOnly) {
			if (cli.hasOutput()) {
				HexString hex = p.getHeader().toHexString();
				p = p.getNext();
				while (p != null) {
					hex = hex.concat(p.getHeader().toHexString());
					p = p.getNext();
				}
				FileWriter writer = new FileWriter(cli.getOutput(), true);
				writer.append(Sniffer.formatHexString(hex));
				writer.close();
			} else {
				while (p != null) {
					System.out.println(p.getHeader());
					p = p.getNext();
				}
			}
		} else {
			if (cli.hasOutput()) {
				FileWriter writer = new FileWriter(cli.getOutput(), true);
				writer.append(Sniffer.formatHexString(p.toHexString()));
				writer.close();
			} else {
				System.out.println(p);
			}
		}
	}
	
	private static void log(CommandCLI cli, Header h) throws IOException {
		if (cli.hasOutput()) {
			FileWriter writer = new FileWriter(cli.getOutput(), true);
			writer.append(Sniffer.formatHexString(h.toHexString()));
			writer.close();
		} else {
			System.out.println(h);
		}
	}
}
