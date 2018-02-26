package lib;

public class IPPacket extends Packet {
	
	public IPPacket(IPHeader header, String payload) {
		super(header, payload);
	}

	@Override
	public String toString() {
		return "N/A";
	}
	
	public void show() {
		System.out.println(this.toString());
	}
}
