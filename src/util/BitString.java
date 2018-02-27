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
	
	public BitString substring(int j, int k) {
		return new BitString(this.S.substring(j, Math.min(this.S.length(), k)), this.spaced);
	}
	
	public BitString spaced() {
		return new BitString(this.S, true);
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
		int term = 1;
		for (int j = 0; j < this.S.length() && j < n; j++) {
			i += S.charAt(S.length()-1-j) == '1' ? term : 0;
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
