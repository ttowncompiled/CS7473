package analysis;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import lib.headers.EthernetHeader;
import lib.headers.IPHeader;
import lib.headers.TCPHeader;
import lib.packets.Packet;
import util.HexFile;
import util.HexString;
import util.Utils;

public class HTTPAnalysis {

	private static final String PATH = "C:\\Users\\ianri\\Workspace\\CS7473\\data\\http.dat";
	private static final String OUT = "C:\\Users\\ianri\\Workspace\\CS7473\\analysis\\http.txt";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		HTTPAnalysis.analysis();
	}
	
	public static void analysis() throws FileNotFoundException, IOException {
		FileWriter writer = new FileWriter(HTTPAnalysis.OUT, false);
		HashMap<String, HashMap<String, String>> addrs = new HashMap<>();
		HexString[] hexStrings = HexFile.parse(HTTPAnalysis.PATH);
		for (int i = 0; i < hexStrings.length; i++) {
			HexString hex = hexStrings[i];
			EthernetHeader eth = EthernetHeader.parse(hex.substring(EthernetHeader.MAX_HEX).toBitString());
			hex = hex.remove(eth.getHeaderHexLength());
			IPHeader ip = IPHeader.parse(hex.substring(IPHeader.MAX_HEX).toBitString());
			hex = hex.remove(ip.getHeaderHexLength());
			TCPHeader tcp = TCPHeader.parse(hex.substring(TCPHeader.MAX_HEX).toBitString());
			hex = hex.remove(tcp.getHeaderHexLength());
			Packet p = Packet.build(hex, eth, ip, tcp);
			String srcAddr = Utils.convertToIPAddress(ip.getSourceIPAddress());
			String destAddr = Utils.convertToIPAddress(ip.getDestinationIPAddress());
			if (! addrs.containsKey(srcAddr)) {
				addrs.put(srcAddr, new HashMap<>());
			}
			if (addrs.containsKey(srcAddr) && ! addrs.get(srcAddr).containsKey(destAddr)) {
				addrs.get(srcAddr).put(destAddr, srcAddr);
			}
			String plaintext = p.extractPlaintext();
			if (! plaintext.trim().isEmpty()) {
				writer.append(plaintext + "\n");
			}
		}
		writer.append(">>> Connections:\n");
		for (String skey : addrs.keySet()) {
			for (String dkey : addrs.get(skey).keySet()) {
				writer.append(skey + " -> " + dkey + "\n");
			}
		}
		writer.close();
	}
}
