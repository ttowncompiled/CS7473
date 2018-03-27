package lib.headers;

import util.BitString;
import util.Config;
import util.HexString;

public class IPHeader extends Header {
	
	public static final int MIN_BITS = 160;
	public static final int MAX_BITS = 480;
	public static final int MAX_HEX = IPHeader.MAX_BITS/4;
	
	private byte version;
	private byte IPHeaderLength;
	private byte typeOfService;
	private short totalLength;
	private short identification;
	private boolean[] flags;
	private short fragmentationOffset;
	private byte timeToLive;
	private byte protocol;
	private short checksum;
	private int sourceIPAddress;
	private int destinationIPAddress;
	private int[] optionsPadding;
	
	public static IPHeader parse(BitString packet) {
		if (packet.length() < IPHeader.MIN_BITS) {
			return null;
		}
		byte version = packet.substring(0, 4).toByte();
		byte IHL = packet.substring(4, 8).toByte();
		byte typeOfService = packet.substring(8, 16).toByte();
		short totalLength = packet.substring(16, 32).toShort();
		short identification = packet.substring(32, 48).toShort();
		boolean[] flags = packet.substring(48, 51).toBits();
		short fragOffset = packet.substring(51, 64).toShort();
		byte timeToLive = packet.substring(64, 72).toByte();
		byte protocol = packet.substring(72, 80).toByte();
		short checksum = packet.substring(80, 96).toShort();
		int source = packet.substring(96, 128).toInt();
		int dest = packet.substring(128, 160).toInt();
		int[] options = null;
		if (IHL > 5) {
			options = new int[IHL-5];
			for (int i = 0; i < IHL-5; i++) {
				options[i] = packet.substring(160+32*i, 192+32*i).toInt();
			}
		}
		return new IPHeader(version, IHL, typeOfService, totalLength, identification, flags, fragOffset, timeToLive, protocol, checksum, source, dest, options);
	}

	public IPHeader(byte version, byte IHL, byte typeOfService, short totalLength, short identification, boolean[] flags, short fragOffset, byte timeToLive, byte protocol, short checksum, int source, int dest, int[] optionsPadding) {
		super(Config.IP);
		this.version = version;
		this.IPHeaderLength = IHL;
		this.typeOfService = typeOfService;
		this.totalLength = totalLength;
		this.identification = identification;
		this.flags = flags;
		this.fragmentationOffset = fragOffset;
		this.timeToLive = timeToLive;
		this.protocol = protocol;
		this.checksum = checksum;
		this.sourceIPAddress = source;
		this.destinationIPAddress = dest;
		this.optionsPadding = optionsPadding;
	}
	
	public byte getVersion() {
		return this.version;
	}
	
	public byte getIPHeaderLength() {
		return this.IPHeaderLength;
	}
	
	public byte getTypeOfService() {
		return this.typeOfService;
	}
	
	public short getTotalLength() {
		return this.totalLength;
	}
	
	public short getIdentification() {
		return this.identification;
	}
	
	public boolean[] getFlags() {
		return this.flags;
	}
	
	public short getFragmentationOffset() {
		return this.fragmentationOffset;
	}
	
	public int getByteFragmentationOffset() {
		return 8*this.fragmentationOffset;
	}
	
	public byte getTimeToLive() {
		return this.timeToLive;
	}
	
	public byte getProtocol() {
		return this.protocol;
	}
	
	public short getChecksum() {
		return this.checksum;
	}
	
	public int getSourceIPAddress() {
		return this.sourceIPAddress;
	}
	
	public int getDestinationIPAddress() {
		return this.destinationIPAddress;
	}
	
	public int[] getOptionsPadding() {
		return this.optionsPadding;
	}
	
	public int getHeaderBitLength() {
		return 32*this.IPHeaderLength;
	}
	
	public int getHeaderByteLength() {
		return 4*this.IPHeaderLength;
	}
	
	public int getHeaderHexLength() {
		return 8*this.IPHeaderLength;
	}
	
	public boolean isFragment() {
		return this.flags[2] == true || this.fragmentationOffset > 0;
	}
	
	public boolean isFirstFragment() {
		return this.flags[2] == true && this.fragmentationOffset == 0;
	}
	
	public boolean isLastFragment() {
		return this.flags[2] == false && this.fragmentationOffset > 0;
	}
	
	public int getLengthNoHeader() {
		return this.totalLength - this.getHeaderByteLength();
	}
	
	public int getLengthWithOffset() {
		return this.getByteFragmentationOffset() + this.getLengthNoHeader();
	}
	
	public HexString toHexString() {
		BitString bits = BitString.fromByte(this.version, 4)
								  .concat(BitString.fromByte(this.IPHeaderLength, 4))
								  .concat(BitString.fromByte(this.typeOfService))
								  .concat(BitString.fromShort(this.totalLength))
								  .concat(BitString.fromShort(this.identification))
								  .concat(BitString.fromBits(this.flags))
								  .concat(BitString.fromShort(this.fragmentationOffset, 13))
								  .concat(BitString.fromByte(this.timeToLive))
								  .concat(BitString.fromByte(this.protocol))
								  .concat(BitString.fromShort(this.checksum))
								  .concat(BitString.fromInt(this.sourceIPAddress))
								  .concat(BitString.fromInt(this.destinationIPAddress));
		if (this.optionsPadding != null && this.optionsPadding.length > 0) {
			for (int i = 0; i < this.optionsPadding.length; i++) {
				bits = bits.concat(BitString.fromInt(this.optionsPadding[i]));
			}
		}
		return bits.toHexString();
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
		
		rep.append(IPHeader.bitIndices());
		rep.append(IPHeader.separator()).append("\n");
		rep.append("|").append(BitString.fromByte(this.version, 4).spaced()).append("|").append(BitString.fromByte(this.IPHeaderLength, 4).spaced()).append("|").append(BitString.fromByte(this.typeOfService).spaced()).append("|").append(BitString.fromShort(this.totalLength).spaced()).append("|\n");
		rep.append(IPHeader.separator()).append("\n");
		rep.append("|").append(BitString.fromShort(this.identification).spaced()).append("|").append(BitString.fromBits(this.flags).spaced()).append("|").append(BitString.fromShort(this.fragmentationOffset, 13).spaced()).append("|\n");
		rep.append(IPHeader.separator()).append("\n");
		rep.append("|").append(BitString.fromByte(this.timeToLive).spaced()).append("|").append(BitString.fromByte(this.protocol).spaced()).append("|").append(BitString.fromShort(this.checksum).spaced()).append("|\n");
		rep.append(IPHeader.separator()).append("\n");
		rep.append("|").append(BitString.fromInt(this.sourceIPAddress).spaced()).append("|\n");
		rep.append(IPHeader.separator()).append("\n");
		rep.append("|").append(BitString.fromInt(this.destinationIPAddress).spaced()).append("|\n");
		rep.append(IPHeader.separator()).append("\n");
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
		rep.append(IPHeader.separator());
		
		return rep.toString();
	}
}
