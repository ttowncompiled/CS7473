package lib.headers;

import util.BitString;
import util.Config;

public class ARPHeader extends Header {

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
		rep.append("+\n");
		
		return rep.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder rep = new StringBuilder();
		
		rep.append(ARPHeader.bitIndices());
		rep.append(ARPHeader.separator());
		rep.append("|").append(BitString.fromShort(this.hardwareType).spaced()).append("|\n");
		rep.append(ARPHeader.separator());
		rep.append("|").append(BitString.fromShort(this.protocolType).spaced()).append("|\n");
		rep.append(ARPHeader.separator());
		rep.append("|").append(BitString.fromByte(this.hardwareAddressLength).spaced()).append("|").append(BitString.fromByte(this.protocolAddressLength).spaced()).append("|\n");
		rep.append(ARPHeader.separator());
		rep.append("|").append(BitString.fromShort(this.operation).spaced()).append("|\n");
		rep.append(ARPHeader.separator());
		BitString SHA = BitString.fromLong(this.senderHardwareAddress, 48).spaced();
		rep.append("|").append(SHA.substring(0, 16)).append("\n");
		rep.append(" ").append(SHA.substring(16, 32)).append("\n");
		rep.append(" ").append(SHA.substring(32, 48)).append("|\n");
		rep.append(ARPHeader.separator());
		BitString SPA = BitString.fromInt(this.senderProtocolAddress).spaced();
		rep.append("|").append(SPA.substring(0, 16)).append("\n");
		rep.append(" ").append(SPA.substring(16, 32)).append("|\n");
		rep.append(ARPHeader.separator());
		BitString THA = BitString.fromLong(this.targetHardwareAddress, 48).spaced();
		rep.append("|").append(THA.substring(0, 16)).append("\n");
		rep.append(" ").append(THA.substring(16, 32)).append("\n");
		rep.append(" ").append(THA.substring(32, 48)).append("|\n");
		rep.append(ARPHeader.separator());
		BitString TPA = BitString.fromInt(this.targetProtocolAddress).spaced();
		rep.append("|").append(TPA.substring(0, 16)).append("\n");
		rep.append(" ").append(TPA.substring(16, 32)).append("|\n");
		rep.append(ARPHeader.separator());
		
		return rep.toString();
	}
}
