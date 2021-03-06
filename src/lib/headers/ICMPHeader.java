package lib.headers;

import util.BitString;
import util.Config;
import util.HexString;
import util.Utils;

public class ICMPHeader extends Header {
	
	public static final int MIN_BITS = 32;
	public static final int MAX_BITS = 32;
	public static final int MAX_HEX = ICMPHeader.MAX_BITS/4;

	private byte ICMPType;
	private byte code;
	private short checksum;
	
	public static ICMPHeader parse(BitString packet) {
		if (packet.length() < ICMPHeader.MIN_BITS) {
			return null;
		}
		byte ICMPtype = packet.substring(0, 8).toByte();
		byte code = packet.substring(8, 16).toByte();
		short checksum = packet.substring(16, 32).toShort();
		return new ICMPHeader(ICMPtype, code, checksum);
	}
	
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
	
	public int getHeaderBitLength() {
		return ICMPHeader.MAX_BITS;
	}
	
	public int getHeaderByteLength() {
		return this.getHeaderBitLength()/8;
	}
	
	public int getHeaderHexLength() {
		return ICMPHeader.MAX_HEX;
	}
	
	public HexString toHexString() {
		return BitString.fromByte(this.ICMPType)
						.concat(BitString.fromByte(this.code))
						.concat(BitString.fromShort(this.checksum))
						.toHexString();
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
		rep.append("+");
		
		return rep.toString();
	}
	
	public String toString() {
		StringBuilder rep = new StringBuilder();
		
		rep.append(ICMPHeader.bitIndices());
		rep.append(ICMPHeader.separator()).append("\n");
		
		rep.append("|")
		   .append(Utils.center("Type", 15)).append("|")
		   .append(Utils.center("Code", 15)).append("|")
		   .append(Utils.center("Checksum", 31))
		   .append("|\n");
		rep.append(ICMPHeader.separator()).append("\n");
		rep.append("|")
		   .append(Utils.center(Integer.toUnsignedString(Byte.toUnsignedInt(this.ICMPType)), 15)).append("|")
		   .append(Utils.center(Integer.toUnsignedString(Byte.toUnsignedInt(this.code)), 15)).append("|")
		   .append(Utils.center(Integer.toUnsignedString(Short.toUnsignedInt(this.checksum)), 31))
		   .append("|\n");
		rep.append(ICMPHeader.separator());
		
		return rep.toString();
	}
}
