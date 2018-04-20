package lib.packets;

import lib.headers.Header;
import util.HexString;
import lib.Showable;

public class Packet implements Showable {

	protected Header header;
	protected Packet next;
	
	public static Packet build(HexString data, Header... headers) {
		Packet p = ! data.isEmpty() ? new DataPacket(data) : null;
		if (headers.length > 0) {
			for (int i = headers.length-1; i >= 0; i--) {
				p = new Packet(headers[i], p);
			}
		}
		return p; 
	}
	
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
	
	public int size() {
		if (this.next == null) {
			return 0;
		}
		return this.next.size();
	}
	
	public int getByteLength() {
		if (this.getNext() == null) {
			return this.header.getHeaderByteLength();
		}
		return this.header.getHeaderByteLength() + this.getNext().getByteLength();
	}
	
	public HexString toHexString() {
		if (this.next == null) {
			return this.header.toHexString();
		}
		return this.header.toHexString().concat(this.next.toHexString());
	}
	
	public String extractPlaintext() {
		if (this.next != null) {
			return this.next.extractPlaintext();
		}
		return "";
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
