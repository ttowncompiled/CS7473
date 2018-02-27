package lib.headers;

import util.BitString;
import util.Config;

public class TCPHeader extends Header {
	
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
	private int optionsPadding;

	public TCPHeader(short source, short dest, int sequence, int ack, byte offset, byte reserved, boolean[] flags, short windowSize, short checksum, short urgent, int optionsPadding) {
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
	
	public int getOptionsPadding() {
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
		rep.append("|").append(BitString.fromInt(this.optionsPadding).spaced()).append("|\n");
		rep.append(TCPHeader.separator());
		
		return rep.toString();
	}
}
