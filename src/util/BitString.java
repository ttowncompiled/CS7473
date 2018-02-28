package util;

import lib.Showable;

public class BitString implements Showable {
	
	private String S;
	private boolean spaced;
	
	public static BitString fromBoolean(boolean b) {
		return new BitString(b ? "1" : "0");
	}
	
	public static BitString fromBits(boolean[] b) {
		StringBuilder rep = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			rep.append(b[i] ? "1" : "0");
		}
		return new BitString(rep.toString());
	}
	
	public static BitString fromByte(byte b) {
		return BitString.fromByte(b, 8);
	}
	
	public static BitString fromByte(byte b, int n) {
		String byteString = Integer.toBinaryString(Byte.toUnsignedInt(b));
		StringBuilder rep = new StringBuilder();
		if (byteString.length() < n) {
			for (int i = byteString.length(); i < n; i++) {
				rep.append("0");
			}
		}
		rep.append(byteString);
		return new BitString(rep.toString());
	}
	
	public static BitString fromBytes(byte[] b) {
		if (b == null || b.length == 0) {
			return new BitString("");
		}
		BitString bits = BitString.fromByte(b[0]);
		for (int i = 1; i < b.length; i++) {
			bits = bits.concat(BitString.fromByte(b[i]));
		}
		return bits;
	}
	
	public static BitString fromShort(short s) {
		return BitString.fromShort(s, 16);
	}
	
	public static BitString fromShort(short s, int n) {
		String shortString = Integer.toBinaryString(Short.toUnsignedInt(s));
		StringBuilder rep = new StringBuilder();
		if (shortString.length() < n) {
			for (int i = shortString.length(); i < n; i++) {
				rep.append("0");
			}
		}
		rep.append(shortString);
		return new BitString(rep.toString());
	}
	
	public static BitString fromInt(int i) {
		return BitString.fromInt(i, 32);
	}
	
	public static BitString fromInt(int i, int n) {
		String intString = Integer.toBinaryString(i);
		StringBuilder rep = new StringBuilder();
		if (intString.length() < n) {
			for (int j = intString.length(); j < n; j++) {
				rep.append("0");
			}
		}
		rep.append(intString);
		return new BitString(rep.toString());
	}
	
	public static BitString fromLong(long l) {
		return BitString.fromLong(l, 64);
	}
	
	public static BitString fromLong(long l, int n) {
		String longString = Long.toBinaryString(l);
		StringBuilder rep = new StringBuilder();
		if (longString.length() < n) {
			for (int i = longString.length(); i < n; i++) {
				rep.append("0");
			}
		}
		rep.append(longString);
		return new BitString(rep.toString());
	}
	
	public BitString(String S) {
		this.S = S;
		this.spaced = false;
	}

	public BitString(String S, boolean spaced) {
		this.S = S;
		this.spaced = spaced;
	}
	
	public int length() {
		return this.S.length();
	}
	
	public BitString substring(int j, int k) {
		return new BitString(this.S.substring(j, Math.min(this.S.length(), k)), this.spaced);
	}
	
	public BitString substring(int n) {
		return new BitString(this.S.substring(0, Math.min(this.S.length(), n)), this.spaced);
	}
	
	public BitString remove(int n) {
		if (n >= this.S.length()) {
			return new BitString("", this.spaced);
		}
		return new BitString(this.S.substring(n, this.S.length()));
	}
	
	public BitString concat(BitString b) {
		return new BitString(this.S.concat(b.S), this.spaced);
	}
	
	public BitString spaced() {
		return new BitString(this.S, true);
	}
	
	public boolean isEmpty() {
		return this.S.isEmpty();
	}
	
	public boolean isSpaced() {
		return this.spaced;
	}
	
	public boolean toBool() {
		return S.charAt(S.length()-1) == '1';
	}
	
	public boolean[] toBits() {
		boolean[] bits = new boolean[S.length()];
		for (int i = 0; i < S.length(); i++) {
			bits[i] = S.charAt(i) == '1';
		}
		return bits;
	}
	
	private long toLong(int n) {
		long i = 0;
		long term = 1;
		for (int j = 0; j < this.S.length() && j < n; j++) {
			i += this.S.charAt(this.S.length()-1-j) == '1' ? term : 0;
			term *= 2;
		}
		return i;
	}
	
	public byte toByte() {
		return (byte) this.toLong(8);
	}
	
	public short toShort() {
		return (short) this.toLong(16);
	}
	
	public int toInt() {
		return (int) this.toLong(32);
	}
	
	public long toLong() {
		return this.toLong(64);
	}
	
	private static String convert(String s) {
		switch (s) {
			case "0000":
				return "0";
			case "0001":
				return "1";
			case "0010":
				return "2";
			case "0011":
				return "3";
			case "0100":
				return "4";
			case "0101":
				return "5";
			case "0110":
				return "6";
			case "0111":
				return "7";
			case "1000":
				return "8";
			case "1001":
				return "9";
			case "1010":
				return "a";
			case "1011":
				return "b";
			case "1100":
				return "c";
			case "1101":
				return "d";
			case "1110":
				return "e";
			case "1111":
				return "f";
			default:
				return "";
		}
	}
	
	public HexString toHexString() {
		if (this.S.length() % 4 > 0) {
			return null;
		}
		StringBuilder rep = new StringBuilder();
		for (int i = 0; i < this.S.length()/4; i++) {
			rep.append(BitString.convert(this.S.substring(4*i, 4*(i+1))));
		}
		return new HexString(rep.toString(), this.spaced);
	}
	
	@Override
	public String toString() {
		if (! this.spaced) {
			return this.S;
		}
		StringBuilder rep = new StringBuilder();
		for (int i = 0; i < this.S.length()-1; i++) {
			rep.append(this.S.charAt(i)).append(" ");
		}
		rep.append(this.S.charAt(this.S.length()-1));
		return rep.toString();
	}
	
	public void show() {
		System.out.println(this.toString());
	}
}
