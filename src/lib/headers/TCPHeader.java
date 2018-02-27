package lib.headers;

import util.BitString;
import util.Config;

public class TCPHeader extends Header {
	
	public static final int MAX_BITS = 480;
	public static final int MAX_HEX = TCPHeader.MAX_BITS/4;
	
	private short sourcePortAddress;
	private short destinationPortAddress;
	private int sequenceNumber;
	private int acknowledgementNumber;
	private byte dataOffset;
	private byte reserved;
	private boolean[] flags;
	private short windowSize;
	private short checksum;
	private short urgentPointer;
	private int[] optionsPadding;
	
	public static TCPHeader parse(BitString packet) {
		short source = packet.substring(0, 16).toShort();
		short dest = packet.substring(16, 32).toShort();
		int sequence = packet.substring(32, 64).toInt();
		int ack = packet.substring(64, 96).toInt();
		byte offset = packet.substring(96, 100).toByte();
		byte reserved = packet.substring(100, 103).toByte();
		boolean[] flags = packet.substring(103, 112).toBits();
		short windowSize = packet.substring(112, 128).toShort();
		short checksum = packet.substring(128, 144).toShort();
		short urgent = packet.substring(144, 160).toShort();
		int[] options = null;
		if (offset > 5) {
			options = new int[offset-5];
			for (int i = 0; i < offset-5; i++) {
				options[i] = packet.substring(160+32*i, 192+32*i).toInt();
			}
		}
		return new TCPHeader(source, dest, sequence, ack, offset, reserved, flags, windowSize, checksum, urgent, options);
	}

	public TCPHeader(short source, short dest, int sequence, int ack, byte offset, byte reserved, boolean[] flags, short windowSize, short checksum, short urgent, int[] optionsPadding) {
		super(Config.TCP);
		this.sourcePortAddress = source;
		this.destinationPortAddress = dest;
		this.sequenceNumber = sequence;
		this.acknowledgementNumber = ack;
		this.dataOffset = offset;
		this.reserved = reserved;
		this.flags = flags;
		this.windowSize = windowSize;
		this.checksum = checksum;
		this.urgentPointer = urgent;
		this.optionsPadding = optionsPadding;
	}
	
	public short getSourcePortAddress() {
		return this.sourcePortAddress;
	}
	
	public short getDestinationPortAddress() {
		return this.destinationPortAddress;
	}
	
	public int getSequenceNumber() {
		return this.sequenceNumber;
	}
	
	public int getAcknowledgementNumber() {
		return this.acknowledgementNumber;
	}
	
	public byte getDataOffset() {
		return this.dataOffset;
	}
	
	public byte getReserved() {
		return this.reserved;
	}
	
	public boolean[] getFlags() {
		return this.flags;
	}
	
	public short getWindowSize() {
		return this.windowSize;
	}
	
	public short getChecksum() {
		return this.checksum;
	}
	
	public short getUrgentPointer() {
		return this.urgentPointer;
	}
	
	public int[] getOptionsPadding() {
		return this.optionsPadding;
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
		
		rep.append(TCPHeader.bitIndices());
		rep.append(TCPHeader.separator());
		rep.append("|").append(BitString.fromShort(this.sourcePortAddress).spaced()).append("|").append(BitString.fromShort(this.destinationPortAddress).spaced()).append("|\n");
		rep.append(TCPHeader.separator());
		rep.append("|").append(BitString.fromInt(this.sequenceNumber).spaced()).append("|\n");
		rep.append(TCPHeader.separator());
		rep.append("|").append(BitString.fromInt(this.acknowledgementNumber).spaced()).append("|\n");
		rep.append(TCPHeader.separator());
		rep.append("|").append(BitString.fromByte(this.dataOffset, 4).spaced()).append("|").append(BitString.fromByte(this.reserved, 3).spaced()).append("|").append(BitString.fromBits(this.flags).spaced()).append("|").append(BitString.fromShort(this.windowSize).spaced()).append("|\n");
		rep.append(TCPHeader.separator());
		rep.append("|").append(BitString.fromShort(this.checksum).spaced()).append("|").append(BitString.fromShort(this.urgentPointer).spaced()).append("|\n");
		rep.append(TCPHeader.separator());
		rep.append("|");
		if (this.optionsPadding == null || this.optionsPadding.length == 0) {
			rep.append(BitString.fromInt(0).spaced()).append("|\n");
		} else {
			rep.append(BitString.fromInt(this.optionsPadding[1]).spaced()).append("\n");
			for (int i = 1; i < this.optionsPadding.length-1; i++) {
				rep.append(" ").append(BitString.fromInt(this.optionsPadding[i]).spaced()).append("\n");
			}
			rep.append(" ").append(BitString.fromInt(this.optionsPadding[this.optionsPadding.length-1]).spaced()).append("|\n");
		}
		rep.append(TCPHeader.separator());
		
		return rep.toString();
	}
}