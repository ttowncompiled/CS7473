package test;

import util.BitString;
import util.Utils;

public class UtilsTest {
	
	public static final String ADDR_1 = "192.168.1.200";
	public static final String ADDR_2 = "192.168.1.203";
	public static final String ADDR_3 = "192.168.1.34";

	public static void main(String[] args) {
		UtilsTest.convertTest();
	}
	
	public static void convertTest() {
		System.out.println(UtilsTest.ADDR_1);
		System.out.println(Utils.convertIPAddress(UtilsTest.ADDR_1));
		System.out.println(Utils.convertToIPAddress(Utils.convertIPAddress(UtilsTest.ADDR_1)));
		System.out.println(BitString.fromInt(Utils.convertIPAddress(UtilsTest.ADDR_1)).toHexString().spaced());
		System.out.println(UtilsTest.ADDR_2);
		System.out.println(Utils.convertIPAddress(UtilsTest.ADDR_2));
		System.out.println(Utils.convertToIPAddress(Utils.convertIPAddress(UtilsTest.ADDR_2)));
		System.out.println(BitString.fromInt(Utils.convertIPAddress(UtilsTest.ADDR_2)).toHexString().spaced());
		System.out.println(UtilsTest.ADDR_3);
		System.out.println(Utils.convertIPAddress(UtilsTest.ADDR_3));
		System.out.println(Utils.convertToIPAddress(Utils.convertIPAddress(UtilsTest.ADDR_3)));
		System.out.println(BitString.fromInt(Utils.convertIPAddress(UtilsTest.ADDR_3)).toHexString().spaced());
	}
}
