package analysis;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import lib.headers.ARPHeader;
import lib.headers.EthernetHeader;
import lib.packets.Packet;
import util.BitString;
import util.HexFile;
import util.HexString;
import util.Utils;

public class ARPAnalysis {
	
	private static final String PATH = "C:\\Users\\ianri\\Workspace\\CS7473\\data\\arp.dat";
	private static final String OUT = "C:\\Users\\ianri\\Workspace\\CS7473\\analysis\\arp.txt";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		ARPAnalysis.analysis();
	}
	
	public static void analysis() throws FileNotFoundException, IOException {
		FileWriter writer = new FileWriter(ARPAnalysis.OUT, false);
		HashMap<String, HashMap<String, String>> addrs = new HashMap<>();
		HexString[] hexStrings = HexFile.parse(ARPAnalysis.PATH);
		for (int i = 0; i < hexStrings.length; i++) {
			HexString hex = hexStrings[i];
			EthernetHeader eth = EthernetHeader.parse(hex.substring(EthernetHeader.MAX_HEX).toBitString());
			hex = hex.remove(eth.getHeaderHexLength());
			ARPHeader arp = ARPHeader.parse(hex.substring(ARPHeader.MAX_HEX).toBitString());
			hex = hex.remove(arp.getHeaderHexLength());
			Packet p = Packet.build(hex, eth, arp);
			String srcAddr = Utils.convertToIPAddress(arp.getSenderProtocolAddress());
			String destAddr = Utils.convertToIPAddress(arp.getTargetProtocolAddress());
			if (! addrs.containsKey(srcAddr)) {
				addrs.put(srcAddr, new HashMap<>());
			}
			if (addrs.containsKey(srcAddr) && ! addrs.get(srcAddr).containsKey(destAddr)) {
				addrs.get(srcAddr).put(destAddr, srcAddr);
			}
			writer.append("SENDER: " + BitString.fromLong(arp.getSenderHardwareAddress()).toHexString().toMACAddress() + "\n");
			writer.append("TARGET: " + BitString.fromLong(arp.getTargetHardwareAddress()).toHexString().toMACAddress() + "\n");
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
