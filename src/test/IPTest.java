package test;

import lib.BitString;
import lib.IPHeader;
import lib.IPPacket;
import lib.Showable;

public class IPTest {
	
	public static IPHeader getIPHeader() {
		return new IPHeader((byte) 4, (byte) 0, (byte) 0, (short) 0, (short) 0, new boolean[3], (short) 0, (byte) 0, (byte) 0, (short) 0, 0, 0, 0);
	}
	
	public static IPPacket getIPPacket() {
		return new IPPacket(IPTest.getIPHeader(), new BitString(""));
	}

	public static void main(String[] args) {
		IPTest.IPHeaderTest();
		IPTest.IPPacketTest();
	}
	
	public static void IPHeaderTest() {
		Showable header = IPTest.getIPHeader();
		header.show();
	}
	
	public static void IPPacketTest() {
		Showable packet = IPTest.getIPPacket();
		packet.show();
	}
}
