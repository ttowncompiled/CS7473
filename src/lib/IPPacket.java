package lib;

public class IPPacket extends Packet {
	
	private static BitString extractData(IPHeader header, BitString packet) {
		return packet.substring(32*header.getIPHeaderLength(), 8*header.getTotalLength());
	}
	
	public static IPPacket parse(BitString packet) {
		IPHeader header = IPHeader.parse(packet);
		return new IPPacket(header, IPPacket.extractData(header, packet));
	}
	
	public IPPacket(IPHeader header, BitString data) {
		super(header, data);
	}

	@Override
	public String toString() {
		return new StringBuilder().append(this.getHeader().toString()).append(this.getData().toString()).append("\n").toString();
	}
	
	public void show() {
		System.out.println(this.toString());
	}
}
