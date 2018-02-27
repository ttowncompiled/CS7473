package lib.headers;

import util.BitString;
import util.Config;

public class ICMPHeader extends Header {

	private byte ICMPType;
	private byte code;
	private short checksum;
	
	public ICMPHeader(byte type, byte code, short checksum) {
		super(Config.ICMP);
		this.ICMPType = type;
		this.code = code;
		this.checksum = checksum;
	}
	
	public byte getICMPType() {
		return this.ICMPType;
	}
	
	public byte getCode() {
		return this.code;
	}
	
	public short getChecksum() {
		return this.checksum;
	}
	
	private static String bitIndices() {
		StringBuilder rep = new StringBuilder();
		
		rep.append(" ");
		for (int i = 0; i < 63; i++) {
			if (i % 2 == 0) {
				rep.append(i % 20 == 0 ? i/20 : " ");
			} else {
				rep.append(" ");
			}
		}
		rep.append(" \n");
		
		rep.append(" ");
		for (int i = 0; i < 63; i++) {
			if (i % 2 == 0) {
				rep.append(i/2 % 10);
			} else {
				rep.append(" ");
			}
		}
		rep.append(" \n");
		
		return rep.toString();
	}
	
	private static String separator() {
		StringBuilder rep = new StringBuilder();
		
		rep.append("+");
		for (int i = 0; i < 63; i++) {
			rep.append(i % 2 == 0 ? "-" : "+");
		}
		rep.append("+\n");
		
		return rep.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder rep = new StringBuilder();
		
		rep.append(ICMPHeader.bitIndices());
		rep.append(ICMPHeader.separator());
		rep.append("|").append(BitString.fromByte(this.ICMPType).spaced()).append("|").append(BitString.fromByte(this.code).spaced()).append("|").append(BitString.fromShort(this.checksum).spaced()).append("|\n");
		rep.append(ICMPHeader.separator());
		
		return rep.toString();
	}
}
