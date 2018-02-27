package test;

import lib.headers.UDPHeader;

public class UDPTest {

	public static UDPHeader getUDPHeader() {
		return new UDPHeader((short) 0, (short) 0, (short) 0, (short) 0);
	}

	public static void main(String[] args) {
		UDPTest.UDPHeaderTest();
	}
	
	public static void UDPHeaderTest() {
		UDPTest.getUDPHeader().show();
	}
}
