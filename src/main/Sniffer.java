package main;

import java.io.FileNotFoundException;

import org.apache.commons.cli.ParseException;

import lib.headers.EthernetHeader;
import lib.headers.ICMPHeader;
import lib.headers.IPHeader;
import lib.headers.TCPHeader;
import lib.headers.UDPHeader;
import lib.packets.Packet;
import util.*;

public class Sniffer {
	
	public static final String NAME = "Sniffer";

	public static void main(String[] args) throws FileNotFoundException, ParseException {
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
	
	private static void sniffFile(CommandCLI cli) throws FileNotFoundException {
		HexString[] hexes = HexFile.parse(cli.getInput());
		for (int i = 0; i < hexes.length && (! cli.hasCount() || i < cli.getCount()); i++) {
			HexString hex = hexes[i];
			if (Sniffer.isEthernetPacket(hex) && (! cli.hasType() || cli.hasValidType())) {
				Packet p = Sniffer.processEthernetPacket(cli, hex);
				if (p != null) {
					p.show();
				}
			}
		}
	}
	
	private static Packet processEthernetPacket(CommandCLI cli, HexString hex) {
		EthernetHeader header = EthernetHeader.parse(hex.substring(EthernetHeader.MAX_HEX).toBitString());
		hex = hex.remove(header.getHeaderHexLength());
		Packet p = null;
		if (Sniffer.isIPPacket(hex) && (! cli.hasType() || Sniffer.isIPType(cli.getType()))) {
			p = Sniffer.processIPPacket(cli, hex);
		}
		return p != null ? new Packet(header, p) : null;
	}
	
	private static Packet processIPPacket(CommandCLI cli, HexString hex) {
		IPHeader header = IPHeader.parse(hex.substring(IPHeader.MAX_HEX).toBitString());
		hex = hex.remove(header.getHeaderHexLength());
		Packet p = null;
		if (header.getProtocol() == Config.IP_ICMP_PROTOCOL && (! cli.hasType() || cli.getType().equals(Config.ICMP))) {
			p = Sniffer.processICMPPacket(cli, hex);
		} else if (header.getProtocol() == Config.IP_TCP_PROTOCOL && (! cli.hasType() || cli.getType().equals(Config.TCP))) {
			p = Sniffer.processTCPPacket(cli, hex);
		} else if (header.getProtocol() == Config.IP_UDP_PROTOCOL && (! cli.hasType() || cli.getType().equals(Config.UDP))) {
			p = Sniffer.processUDPPacket(cli, hex);
		}
		return p != null ? new Packet(header, p) : null;
	}
	
	private static Packet processICMPPacket(CommandCLI cli, HexString hex) {
		ICMPHeader header = ICMPHeader.parse(hex.substring(ICMPHeader.MAX_HEX).toBitString());
		hex = hex.remove(header.getHeaderHexLength());
		return Packet.build(hex, header);
	}
	
	private static Packet processTCPPacket(CommandCLI cli, HexString hex) {
		TCPHeader header = TCPHeader.parse(hex.substring(TCPHeader.MAX_HEX).toBitString());
		hex = hex.remove(header.getHeaderHexLength());
		return Packet.build(hex, header);
	}
	
	private static Packet processUDPPacket(CommandCLI cli, HexString hex) {
		UDPHeader header = UDPHeader.parse(hex.substring(UDPHeader.MAX_HEX).toBitString());
		hex = hex.remove(header.getHeaderHexLength());
		return Packet.build(hex, header);
	}
	
	private static void sniffNetwork(CommandCLI cli) {
		
	}
	
	private static boolean isEthernetPacket(HexString hex) {
		EthernetHeader header = EthernetHeader.parse(hex.substring(EthernetHeader.MAX_HEX).toBitString());
		return Sniffer.isEthernetEtherType(header.getEtherType());
	}
	
	private static boolean isIPPacket(HexString hex) {
		IPHeader header = IPHeader.parse(hex.substring(IPHeader.MAX_HEX).toBitString());
		return header.getVersion() == 4 && Sniffer.isIPProtocol(header.getProtocol());
	}
	
	private static boolean isEthernetEtherType(int etherType) {
		return etherType == Config.ETH_ARP_ETHERTYPE || etherType == Config.ETH_IP_ETHERTYPE;
	}
	
	private static boolean isIPProtocol(int protocol) {
		return protocol == Config.IP_ICMP_PROTOCOL || protocol == Config.IP_TCP_PROTOCOL || protocol == Config.IP_UDP_PROTOCOL;
	}
	
	private static boolean isIPType(String type) {
		return type.equals(Config.IP) || type.equals(Config.ICMP) || type.equals(Config.TCP) || type.equals(Config.UDP);
	}
}
