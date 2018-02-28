package util;

import lib.Showable;

public class HexString implements Showable {

	private String S;
	private boolean spaced;
	
	public HexString(String S) {
		this.S = S;
		this.spaced = false;
	}
	
	public HexString(String S, boolean spaced) {
		this.S = S;
		this.spaced = spaced;
	}
	
	public int length() {
		return this.S.length();
	}
	
	public HexString substring(int j, int k) {
		return new HexString(this.S.substring(j, Math.min(this.S.length(), k)), this.spaced);
	}
	
	public HexString substring(int n) {
		return this.substring(0, n);
	}
	
	public HexString remove(int n) {
		if (n >= this.S.length()) {
			return new HexString("", this.spaced);
		}
		return this.substring(n, this.S.length());
	}
	
	public HexString concat(HexString h) {
		return new HexString(this.S.concat(h.S), this.spaced);
	}
	
	public HexString spaced() {
		return new HexString(this.S, true);
	}
	
	public boolean isEmpty() {
		return this.S.isEmpty();
	}
	
	public boolean isSpaced() {
		return this.spaced;
	}
	
	public byte[] toBytes() {
		if (this.S.isEmpty()) {
			return null;
		}
		byte[] bytes = new byte[(this.S.length()+1)/2];
		for (int i = 0; i < this.S.length(); i+=2) {
			bytes[i/2] = this.substring(i, i+2).toBitString().toByte();
		}
		if (this.S.length() % 2 == 1) {
			bytes[bytes.length-1] = this.substring(this.S.length()-1, this.S.length()).toBitString().toByte();
		}
		return bytes;
	}
	
	private static String convert(char c) {
		switch(c) {
			case '0':
				return "0000";
			case '1':
				return "0001";
			case '2':
				return "0010";
			case '3':
				return "0011";
			case '4':
				return "0100";
			case '5':
				return "0101";
			case '6':
				return "0110";
			case '7':
				return "0111";
			case '8':
				return "1000";
			case '9':
				return "1001";
			case 'a':
				return "1010";
			case 'b':
				return "1011";
			case 'c':
				return "1100";
			case 'd':
				return "1101";
			case 'e':
				return "1110";
			case 'f':
				return "1111";
			default:
				return "";
		}
	}
	
	public BitString toBitString() {
		StringBuilder rep = new StringBuilder();
		for (int i = 0; i < this.S.length(); i++) {
			rep.append(HexString.convert(this.S.charAt(i)));
		}
		return new BitString(rep.toString(), this.spaced);
	}
	
	@Override
	public String toString() {
		if (! this.spaced) {
			return this.S;
		}
		StringBuilder rep = new StringBuilder();
		for (int i = 0; i < this.S.length()-2; i+=2) {
			rep.append(this.S.charAt(i)).append(this.S.charAt(i+1)).append(" ");
		}
		rep.append(this.S.charAt(this.S.length()-2)).append(this.S.charAt(this.S.length()-1));
		return rep.toString();
	}
	
	public void show() {
		System.out.println(this.toString());
	}
}
