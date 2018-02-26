package lib;

import util.Config;
import util.Utils;

public class IPHeader extends Header {
	
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
	private int sourceAddress;
	private int destinationAddress;
	private int optionsPadding;

	public IPHeader(byte version, byte IHL, byte typeOfService, short totalLength, short identification, boolean[] flags, short fragOffset, byte timeToLive, byte protocol, short checksum, int source, int dest, int optionsPadding) {
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
		this.sourceAddress = source;
		this.destinationAddress = dest;
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
	
	public byte getTimeToLive() {
		return this.timeToLive;
	}
	
	public byte getProtocol() {
		return this.protocol;
	}
	
	public short getChecksum() {
		return this.checksum;
	}
	
	public int getSourceAddress() {
		return this.sourceAddress;
	}
	
	public int getDestinationAddress() {
		return this.destinationAddress;
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
		
		rep.append(IPHeader.bitIndices());
		rep.append(IPHeader.separator());
		rep.append("|").append(Utils.spaced(Utils.bitString(this.version, 4))).append("|").append(Utils.spaced(Utils.bitString(this.IPHeaderLength, 4))).append("|").append(Utils.spaced(Utils.bitString(this.typeOfService))).append("|").append(Utils.spaced(Utils.bitString(this.totalLength))).append("|\n");
		rep.append(IPHeader.separator());
		rep.append("|").append(Utils.spaced(Utils.bitString(this.identification))).append("|").append(Utils.bitString(this.flags[0])).append(" ").append(Utils.bitString(this.flags[1])).append(" ").append(Utils.bitString(this.flags[2])).append("|").append(Utils.spaced(Utils.bitString(this.fragmentationOffset, 13))).append("|\n");
		rep.append(IPHeader.separator());
		rep.append("|").append(Utils.spaced(Utils.bitString(this.timeToLive))).append("|").append(Utils.spaced(Utils.bitString(this.protocol))).append("|").append(Utils.spaced(Utils.bitString(this.checksum))).append("|\n");
		rep.append(IPHeader.separator());
		rep.append("|").append(Utils.spaced(Utils.bitString(this.sourceAddress))).append("|\n");
		rep.append(IPHeader.separator());
		rep.append("|").append(Utils.spaced(Utils.bitString(this.destinationAddress))).append("|\n");
		rep.append(IPHeader.separator());
		rep.append("|").append(Utils.spaced(Utils.bitString(this.optionsPadding))).append("|\n");
		rep.append(IPHeader.separator());
		
		return rep.toString();
	}
	
	public void show() {
		System.out.println(this.toString());
	}
}
