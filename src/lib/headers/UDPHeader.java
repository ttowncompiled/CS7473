package lib.headers;

import util.BitString;
import util.Config;
import util.HexString;

public class UDPHeader extends Header {
	
	public static final int MAX_BITS = 64;
	public static final int MAX_HEX = UDPHeader.MAX_BITS/4;

	private short sourcePortAddress;
	private short destinationPortAddress;
	private short length;
	private short checksum;
	
	public static UDPHeader parse(BitString packet) {
		short source = packet.substring(0, 16).toShort();
		short dest = packet.substring(16, 32).toShort();
		short length = packet.substring(32, 48).toShort();
		short checksum = packet.substring(48, 64).toShort();
		return new UDPHeader(source, dest, length, checksum);
	}
	
	public UDPHeader(short source, short dest, short length, short checksum) {
		super(Config.UDP);
		this.sourcePortAddress = source;
		this.destinationPortAddress = dest;
		this.length = length;
		this.checksum = checksum;
	}
	
	public short getSourcePortAddress() {
		return this.sourcePortAddress;
	}
	
	public short getDestinationPortAddress() {
		return this.destinationPortAddress;
	}
	
	public short getLength() {
		return this.length;
	}
	
	public short getChecksum() {
		return this.checksum;
	}
	
	public int getHeaderBitLength() {
		return UDPHeader.MAX_BITS;
	}
	
	public int getHeaderHexLength() {
		return UDPHeader.MAX_HEX;
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
	
	public HexString toHexString() {
		return BitString.fromShort(this.sourcePortAddress)
						.concat(BitString.fromShort(this.destinationPortAddress))
						.concat(BitString.fromShort(this.length))
						.concat(BitString.fromShort(this.checksum))
						.toHexString();
	}
	
	public String toString() {
		StringBuilder rep = new StringBuilder();
		
		rep.append(UDPHeader.bitIndices());
		rep.append(UDPHeader.separator()).append("\n");
		rep.append("|").append(BitString.fromShort(this.sourcePortAddress).spaced()).append("|").append(BitString.fromShort(this.destinationPortAddress).spaced()).append("|\n");
		rep.append(UDPHeader.separator()).append("\n");
		rep.append("|").append(BitString.fromShort(this.length).spaced()).append("|").append(BitString.fromShort(this.checksum).spaced()).append("|\n");
		rep.append(UDPHeader.separator());
		
		return rep.toString();
	}
}
