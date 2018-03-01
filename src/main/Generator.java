package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.cli.ParseException;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

import util.GeneratorCLI;
import util.HexFile;
import util.HexString;

public class Generator {

	public static final String NAME = "Generator";
	private static Pcap adapter = null;

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		GeneratorCLI cli = new GeneratorCLI(Generator.NAME, args);
		if (cli.hasHelp()) {
			Generator.help(cli);
		} else if (cli.hasShow()) {
			Generator.show();
		}
		if (! cli.hasInput()) {
			return;
		}
		if (cli.hasDev()) {
			Generator.adapter = Generator.openAdapter(cli.getDev());
		} else {
			Generator.adapter = Generator.openAdapter();
		}
		if (Generator.adapter == null) {
			return;
		}
		HexString[] hexes = HexFile.parse(cli.getInput());
		if (cli.hasCount()) {
			for (int i = 0; i < cli.getCount(); i++) {
				for (int j = 0; j < hexes.length; j++) {
					Generator.send(cli, Generator.adapter, hexes[j]);
				}
			}
		} else {
			for (int i = 0; i < hexes.length; i++) {
				Generator.send(cli, Generator.adapter, hexes[i]);
			}
		}
	}
	
	private static void help(GeneratorCLI cli) {
		cli.help();
	}
	
	private static void show() {
		ArrayList<PcapIf> devices = new ArrayList<>();
		StringBuilder errBuf = new StringBuilder();
		int result = Pcap.findAllDevs(devices, errBuf);
		if (result == Pcap.ERROR || devices.isEmpty()) {
			return;
		}
		for (int i = 0; i < devices.size(); i++) {
			Generator.log(devices.get(i).getName(), devices.get(i).toString());
		}
	}
	
	private static void send(GeneratorCLI cli, Pcap adapter, HexString hex) {
		byte[] bytes = hex.toBytes();
		int result = adapter.sendPacket(bytes);
		if (result != Pcap.ERROR) {
			System.out.println(">>> Packet sent.");
		}
	}
	
	private static Pcap openAdapter() throws IOException {
		return Generator.openAdapter(null);
	}
	
	private static Pcap openAdapter(String name) throws IOException {
		PcapIf device = Generator.getNetworkDevice(name);
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
	
	private static void log(String... S) {
		for (int i = 0; i < S.length; i++) {
			System.out.println(S[i]);
		}
	}
}
