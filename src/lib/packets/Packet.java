package lib.packets;

import lib.headers.Header;
import lib.Showable;

public class Packet implements Showable {

	private Header header;
	private Packet next;
	
	public Packet(Header header, Packet next) {
		this.header = header;
		this.next = next;
	}
	
	public Header getHeader() {
		return this.header;
	}
	
	public Packet getNext() {
		return this.next;
	}
	
	public String getType() {
		return this.header.getType();
	}
	
	@Override
	public String toString() {
		if (this.next == null) {
			return new StringBuilder().append(this.header.toString()).toString();
		}
		return new StringBuilder().append(this.header.toString()).append("\n").append(this.next).toString();
	}
	
	public void show() {
		System.out.println(this.toString());
	}
}
