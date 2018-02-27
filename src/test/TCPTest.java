package test;

import lib.headers.TCPHeader;

public class TCPTest {

	public static TCPHeader getTCPHeader() {
		return new TCPHeader((short) 0, (short) 0, 0, 0, (byte) 8, (byte) 0, new boolean[9], (short) 0, (short) 0, (short) 0, new int[3]);
	}

	public static void main(String[] args) {
		TCPTest.TCPHeaderTest();
	}
	
	public static void TCPHeaderTest() {
		TCPTest.getTCPHeader().show();
	}
}
