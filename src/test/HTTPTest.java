package test;

import java.io.FileNotFoundException;

import lib.headers.EthernetHeader;
import lib.headers.IPHeader;
import util.*;

public class HTTPTest {
	
	public static void main(String[] args) throws FileNotFoundException {
		HexString[] packets = HexFile.parse(Config.HTTP_PATH);
		for (int i = 0; i < packets.length; i++) {
			EthernetHeader.parse(packets[i].substring(0, 14).toBitString()).show();
			IPHeader.parse(packets[i].substring(14, 38).toBitString()).show();
		}
	}
}
