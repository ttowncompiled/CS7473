package lib.headers;

import util.BitString;
import util.Config;
import util.HexString;
import util.Utils;

public class ARPHeader extends Header {
	
	public static final int MIN_BITS = 224;
	public static final int MAX_BITS = 224;
	public static final int MAX_HEX = ARPHeader.MAX_BITS/4;

	private short hardwareType;
	private short protocolType;
	private byte hardwareAddressLength;
	private byte protocolAddressLength;
	private short operation;
	private long senderHardwareAddress;
	private int senderProtocolAddress;
	private long targetHardwareAddress;
	private int targetProtocolAddress;
	
	public static ARPHeader parse(BitString packet) {
		if (packet.length() < ARPHeader.MIN_BITS) {
			return null;
		}
		short HTYPE = packet.substring(0, 16).toShort();
		short PTYPE = packet.substring(16, 32).toShort();
		byte HLEN = packet.substring(32, 40).toByte();
		byte PLEN = packet.substring(40, 48).toByte();
		short OPER = packet.substring(48, 64).toShort();
		long SHA = packet.substring(64, 112).toLong();
		int SPA = packet.substring(112, 144).toInt();
		long THA = packet.substring(144, 192).toLong();
		int TPA = packet.substring(192, 224).toInt();
		return new ARPHeader(HTYPE, PTYPE, HLEN, PLEN, OPER, SHA, SPA, THA, TPA);
	}

	public ARPHeader(short HTYPE, short PTYPE, byte HLEN, byte PLEN, short OPER, long SHA, int SPA, long THA, int TPA) {
		super(Config.ARP);
		this.hardwareType = HTYPE;
		this.protocolType = PTYPE;
		this.hardwareAddressLength = HLEN;
		this.protocolAddressLength = PLEN;
		this.operation = OPER;
		this.senderHardwareAddress = SHA;
		this.senderProtocolAddress = SPA;
		this.targetHardwareAddress = THA;
		this.targetProtocolAddress = TPA;
	}
	
	public short getHardwareType() {
		return this.hardwareType;
	}
	
	public short getProtocolType() {
		return this.protocolType;
	}
	
	public byte getHardwareAddressLength() {
		return this.hardwareAddressLength;
	}
	
	public byte getProtocolAddressLength() {
		return this.protocolAddressLength;
	}
	
	public short getOperation() {
		return this.operation;
	}
	
	public long getSenderHardwareAddress() {
		return this.senderHardwareAddress;
	}
	
	public int getSenderProtocolAddress() {
		return this.senderProtocolAddress;
	}
	
	public long getTargetHardwareAddress() {
		return this.targetHardwareAddress;
	}
	
	public int getTargetProtocolAddress() {
		return this.targetProtocolAddress;
	}
	
	public int getHeaderBitLength() {
		return ARPHeader.MAX_BITS;
	}
	
	public int getHeaderByteLength() {
		return this.getHeaderBitLength()/8;
	}
	
	public int getHeaderHexLength() {
		return ARPHeader.MAX_HEX;
	}
	
	public HexString toHexString() {
		return BitString.fromShort(this.hardwareType)
				 		.concat(BitString.fromShort(this.protocolType))
				 		.concat(BitString.fromByte(this.hardwareAddressLength))
				 		.concat(BitString.fromByte(this.protocolAddressLength))
				 		.concat(BitString.fromShort(this.operation))
				 		.concat(BitString.fromLong(this.senderHardwareAddress, 48))
				 		.concat(BitString.fromInt(this.senderProtocolAddress))
				 		.concat(BitString.fromLong(this.targetHardwareAddress, 48))
				 		.concat(BitString.fromInt(this.targetProtocolAddress))
				 		.toHexString();
	}
	
	private static String bitIndices() {
		StringBuilder rep = new StringBuilder();
		
		rep.append(" ");
		for (int i = 0; i < 31; i++) {
			if (i % 2 == 0) {
				rep.append(i % 20 == 0 ? i/20 : " ");
			} else {
				rep.append(" ");
			}
		}
		rep.append(" \n");
		
		rep.append(" ");
		for (int i = 0; i < 31; i++) {
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
		for (int i = 0; i < 31; i++) {
			rep.append(i % 2 == 0 ? "-" : "+");
		}
		rep.append("+");
		
		return rep.toString();
	}
	
	public String toString() {
		StringBuilder rep = new StringBuilder();
		
		rep.append(ARPHeader.bitIndices());
		rep.append(ARPHeader.separator()).append("\n");
		rep.append("|").append(Utils.center(Integer.toUnsignedString(Short.toUnsignedInt(this.hardwareType)), 31)).append("|\n");
		rep.append(ARPHeader.separator()).append("\n");
		rep.append("|").append(Utils.center(Integer.toUnsignedString(Short.toUnsignedInt(this.protocolType)), 31)).append("|\n");
		rep.append(ARPHeader.separator()).append("\n");
		rep.append("|")
		   .append(Utils.center(Integer.toUnsignedString(Byte.toUnsignedInt(this.hardwareAddressLength)), 15)).append("|")
		   .append(Utils.center(Integer.toUnsignedString(Byte.toUnsignedInt(this.protocolAddressLength)), 15))
		   .append("|\n");
		rep.append(ARPHeader.separator()).append("\n");
		rep.append("|").append(Utils.center(Integer.toUnsignedString(Short.toUnsignedInt(this.operation)), 31)).append("|\n");
		rep.append(ARPHeader.separator()).append("\n");
		rep.append("|").append(Utils.center(BitString.fromLong(this.senderHardwareAddress, 48).toHexString().toMACAddress(), 31)).append("|\n");
		rep.append(ARPHeader.separator()).append("\n");
		rep.append("|").append(Utils.center(Utils.convertToIPAddress(this.senderProtocolAddress), 31)).append("|\n");
		rep.append(ARPHeader.separator()).append("\n");
		rep.append("|").append(Utils.center(BitString.fromLong(this.targetHardwareAddress, 48).toHexString().toMACAddress(), 31)).append("|\n");
		rep.append(ARPHeader.separator()).append("\n");
		rep.append("|").append(Utils.center(Utils.convertToIPAddress(this.targetProtocolAddress), 31)).append("|\n");
		rep.append(ARPHeader.separator());
		
		return rep.toString();
	}
}
