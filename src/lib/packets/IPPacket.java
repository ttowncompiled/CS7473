package lib.packets;

import lib.headers.IPHeader;

public class IPPacket extends Packet {

	public IPPacket(IPHeader header, Packet next) {
		super(header, next);
	}
	
	public IPHeader getHeader() {
		return (IPHeader) this.header;
	}
}
