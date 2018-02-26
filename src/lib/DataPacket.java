package lib;

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
		return this.data.toString();
	}
}
