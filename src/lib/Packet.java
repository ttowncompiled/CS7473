package lib;

public abstract class Packet implements Showable {

	private Header header;
	private BitString data;
	
	public Packet(Header header, BitString data) {
		this.header = header;
		this.data = data;
	}
	
	public Header getHeader() {
		return this.header;
	}
	
	public BitString getData() {
		return this.data;
	}
	
	public String getType() {
		return this.header.getType();
	}
	
	@Override
	public abstract String toString();
}
