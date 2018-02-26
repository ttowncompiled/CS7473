package lib;

public class PacketBuilder implements Showable {

	private Header header;
	private PacketBuilder next;
	private BitString data;
	
	public PacketBuilder() {
		this.header = null;
		this.next = null;
		this.data = null;
	}
	
	public PacketBuilder setHeader(Header header) {
		this.header = header;
		return this;
	}
	
	public PacketBuilder setNext(PacketBuilder next) {
		this.next = next;
		return this;
	}
	
	public PacketBuilder setData(BitString data) {
		this.data = data;
		return this;
	}
	
	public Packet build() {
		if (this.next == null) {
			return new DataPacket(this.data);
		}
		return new Packet(this.header, this.next.build());
	}
	
	@Override
	public String toString() {
		if (this.data != null) {
			return new StringBuilder().append(this.data).append("\n").toString();
		}
		return new StringBuilder().append(this.header != null ? this.header : "").append(this.next != null ? this.next : "").append("\n").toString();
	}
	
	public void show() {
		System.out.println(this.toString());
	}
}
