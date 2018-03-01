package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HexFile {
	
	private static HexString convert(String line) {
		Scanner scanner = new Scanner(line);
		StringBuilder rep = new StringBuilder();
		while (scanner.hasNext()) {
			rep.append(scanner.next());
		}
		scanner.close();
		return new HexString(rep.toString());
	}

	public static HexString[] parse(String filepath) throws FileNotFoundException {
		ArrayList<HexString> packets = new ArrayList<>();
		Scanner scanner = new Scanner(new File(filepath));
		StringBuilder rep = new StringBuilder();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();
			if (! line.equals("")) {
				rep.append(line);
				continue;
			}
			packets.add(HexFile.convert(rep.toString()));
			rep = new StringBuilder();
		}
		if (rep.toString().length() > 0) {
			packets.add(HexFile.convert(rep.toString()));
		}
		scanner.close();
		if (packets.isEmpty()) {
			return null;
		}
		HexString[] hexes = new HexString[packets.size()];
		for (int i = 0; i < packets.size(); i++) {
			hexes[i] = packets.get(i);
		}
		return hexes;
	}
}
