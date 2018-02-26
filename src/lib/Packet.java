package lib;

public abstract class Packet implements Showable {

	private Header header;
	private String data;
	
	public Packet(Header header, String data) {
		this.header = header;
		this.data = data;
	}
	
	public Header getHeader() {
		return this.header;
	}
	
	public String getData() {
		return this.data;
	}
	
	public String getType() {
		return this.header.getType();
	}
	
	@Override
	public abstract String toString();
}
