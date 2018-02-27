package test;

import java.io.FileNotFoundException;

import lib.headers.EthernetHeader;
import lib.headers.IPHeader;
import lib.headers.TCPHeader;
import lib.packets.DataPacket;
import lib.packets.Packet;
import util.*;

public class HTTPTest {
	
	public static void main(String[] args) throws FileNotFoundException {
		HexString[] hexStrings = HexFile.parse(Config.HTTP_PATH);
		Packet[] packets = new Packet[hexStrings.length];
		for (int i = 0; i < hexStrings.length; i++) {
			HexString hex = hexStrings[i];
			EthernetHeader eth = EthernetHeader.parse(hex.substring(EthernetHeader.MAX_HEX).toBitString());
			hex = hex.remove(EthernetHeader.MAX_HEX);
			IPHeader ip = IPHeader.parse(hex.substring(IPHeader.MAX_HEX).toBitString());
			hex = hex.remove(8*ip.getIPHeaderLength());
			TCPHeader tcp = TCPHeader.parse(hex.substring(TCPHeader.MAX_HEX).toBitString());
			hex = hex.remove(8*tcp.getDataOffset());
			packets[i] = new Packet(eth, new Packet(ip, new Packet(tcp, hex.toString().length() > 0 ? new DataPacket(hex.toBitString()) : null)));
			packets[i].show();
		}
	}
}
