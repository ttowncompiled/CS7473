package test;

import lib.headers.ICMPHeader;

public class ICMPTest {

	public static ICMPHeader getICMPHeader() {
		return new ICMPHeader((byte) 0, (byte) 0, (short) 0);
	}

	public static void main(String[] args) {
		ICMPTest.ICMPHeaderTest();
	}
	
	public static void ICMPHeaderTest() {
		ICMPTest.getICMPHeader().show();
	}
}
