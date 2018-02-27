package test;

import java.io.FileNotFoundException;

import lib.headers.EthernetHeader;
import lib.headers.IPHeader;
import lib.headers.TCPHeader;
import lib.packets.Packet;
import util.Config;
import util.HexFile;
import util.HexString;

public class TelnetTest {

	public static void main(String[] args) throws FileNotFoundException {
		HexString[] hexStrings = HexFile.parse(Config.HTTP_PATH);
		for (int i = 0; i < hexStrings.length; i++) {
			HexString hex = hexStrings[i];
			EthernetHeader eth = EthernetHeader.parse(hex.substring(EthernetHeader.MAX_HEX).toBitString());
			hex = hex.remove(eth.getHeaderHexLength());
			IPHeader ip = IPHeader.parse(hex.substring(IPHeader.MAX_HEX).toBitString());
			hex = hex.remove(ip.getHeaderHexLength());
			TCPHeader tcp = TCPHeader.parse(hex.substring(TCPHeader.MAX_HEX).toBitString());
			hex = hex.remove(tcp.getHeaderHexLength());
			Packet.build(hex, eth, ip, tcp).show();
		}
	}
}
