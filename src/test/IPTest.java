package test;

import lib.IPHeader;

public class IPTest {
	
	public static IPHeader getIPHeader() {
		return new IPHeader((byte) 4, (byte) 0, (byte) 0, (short) 0, (short) 0, new boolean[3], (short) 0, (byte) 0, (byte) 0, (short) 0, 0, 0, 0);
	}

	public static void main(String[] args) {
		IPTest.IPHeaderTest();
	}
	
	public static void IPHeaderTest() {
		IPTest.getIPHeader().show();
	}
}
