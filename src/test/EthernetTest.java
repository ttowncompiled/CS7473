package test;

import lib.headers.EthernetHeader;

public class EthernetTest {
	
	public static EthernetHeader getEthernetHeader() {
		return new EthernetHeader((long) 0, (long) 0, (short) 0);
	}

	public static void main(String[] args) {
		EthernetTest.EthernetHeaderTest();
	}
	
	public static void EthernetHeaderTest() {
		EthernetTest.getEthernetHeader().show();
	}
}
