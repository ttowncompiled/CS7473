package lib.packets;

import util.BitString;
import lib.headers.NullHeader;

public class DataPacket extends Packet {
	
	private BitString data;
	
	public DataPacket(BitString data) {
		super(NullHeader.NULL_HEADER, null);
		this.data = data;
	}
	
	public BitString getData() {
		return this.data;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append(this.data).append("\n").toString();
	}
}
