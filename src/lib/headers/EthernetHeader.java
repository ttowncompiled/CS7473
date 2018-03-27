package lib.headers;

import util.BitString;
import util.Config;
import util.HexString;

public class EthernetHeader extends Header {
	
	public static final int MIN_BITS = 112;
	public static final int MAX_BITS = 112;
	public static final int MAX_HEX = EthernetHeader.MAX_BITS/4;

	private long destinationMACAddress;
	private long sourceMACAddress;
	private short etherType;
	
	public static EthernetHeader parse(BitString packet) {
		if (packet.length() < EthernetHeader.MIN_BITS) {
			return null;
		}
		long destination = packet.substring(0, 48).toLong();
		long source = packet.substring(48, 96).toLong();
		short etherType = packet.substring(96, 112).toShort();
		return new EthernetHeader(destination, source, etherType);
	}
	
	public EthernetHeader(long destination, long source, short etherType) {
		super(Config.ETHERNET);
		this.destinationMACAddress = destination;
		this.sourceMACAddress = source;
		this.etherType = etherType;
	}
	
	public long getDesintationMACAddress() {
		return this.destinationMACAddress;
	}
	
	public long getSourceMACAddress() {
		return this.sourceMACAddress;
	}
	
	public short getEtherType() {
		return this.etherType;
	}
	
	public int getHeaderBitLength() {
		return EthernetHeader.MAX_BITS;
	}
	
	public int getHeaderByteLength() {
		return this.getHeaderBitLength()/8;
	}
	
	public int getHeaderHexLength() {
		return EthernetHeader.MAX_HEX;
	}
	
	public HexString toHexString() {
		return BitString.fromLong(this.destinationMACAddress, 48)
						.concat(BitString.fromLong(this.sourceMACAddress, 48))
						.concat(BitString.fromShort(this.etherType))
						.toHexString();
	}
	
	private static String bitIndices() {
		StringBuilder rep = new StringBuilder();
		
		rep.append(" ");
		for (int i = 0; i < 223; i++) {
			if (i % 2 == 0) {
				rep.append(i % 20 == 0 ? i/20 % 10 : " ");
			} else {
				rep.append(" ");
			}
		}
		rep.append(" \n");
		
		rep.append(" ");
		for (int i = 0; i < 223; i++) {
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
		for (int i = 0; i < 223; i++) {
			rep.append(i % 2 == 0 ? "-" : "+");
		}
		rep.append("+");
		
		return rep.toString();
	}
	
	public String toString() {
		StringBuilder rep = new StringBuilder();
		
		rep.append(EthernetHeader.bitIndices());
		rep.append(EthernetHeader.separator()).append("\n");
		rep.append("|").append(BitString.fromLong(this.destinationMACAddress, 48).spaced()).append("|").append(BitString.fromLong(this.sourceMACAddress, 48).spaced()).append("|").append(BitString.fromShort(this.etherType).spaced()).append("|\n");
		rep.append(EthernetHeader.separator());
		
		return rep.toString();
	}
}
