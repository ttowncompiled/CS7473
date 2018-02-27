package test;

import lib.headers.ARPHeader;

public class ARPTest {

	public static ARPHeader getARPHeader() {
		return new ARPHeader((short) 0, (short) 0, (byte) 0, (byte) 0, (short) 0, (long) 0, 0, (long) 0, 0);
	}

	public static void main(String[] args) {
		ARPTest.ARPHeaderTest();
	}
	
	public static void ARPHeaderTest() {
		ARPTest.getARPHeader().show();
	}
}
