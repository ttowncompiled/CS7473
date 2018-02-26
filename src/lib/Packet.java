package lib;

public abstract class Packet implements Showable {

	private Header header;
	private String payload;
	
	public Packet(Header header, String payload) {
		this.header = header;
		this.payload = payload;
	}
	
	public Header getHeader() {
		return this.header;
	}
	
	public String getPayload() {
		return this.payload;
	}
	
	public String getType() {
		return this.header.getType();
	}
	
	@Override
	public abstract String toString();
}
