package util;

public class Utils {
	
	public static String bitString(boolean b) {
		return b ? "1" : "0";
	}

	public static String bitString(byte b) {
		return Utils.bitString(b, 8);
	}
	
	public static String bitString(byte b, int n) {
		String byteString = Integer.toBinaryString(Byte.toUnsignedInt(b));
		StringBuilder rep = new StringBuilder();
		if (byteString.length() < n) {
			for (int i = byteString.length(); i < n; i++) {
				rep.append("0");
			}
		}
		rep.append(byteString);
		return rep.toString();
	}
	
	public static String bitString(short s) {
		return Utils.bitString(s, 16);
	}
	
	public static String bitString(short s, int n) {
		String shortString = Integer.toBinaryString(Short.toUnsignedInt(s));
		StringBuilder rep = new StringBuilder();
		if (shortString.length() < n) {
			for (int i = shortString.length(); i < n; i++) {
				rep.append("0");
			}
		}
		rep.append(shortString);
		return rep.toString();
	}
	
	public static String bitString(int num) {
		return Utils.bitString(num, 32);
	}
	
	public static String bitString(int num, int n) {
		String intString = Integer.toBinaryString(num);
		StringBuilder rep = new StringBuilder();
		if (intString.length() < n) {
			for (int i = intString.length(); i < n; i++) {
				rep.append("0");
			}
		}
		rep.append(intString);
		return rep.toString();
	}
	
	public static String spaced(String S) {
		StringBuilder rep = new StringBuilder();
		for (int i = 0; i < S.length()-1; i++) {
			rep.append(S.charAt(i)).append(" ");
		}
		rep.append(S.charAt(S.length()-1));
		return rep.toString();
	}
}
