package lib;

public class IPPacket extends Packet {
	
	private static String parseData(IPHeader header, String packet) {
		StringBuilder data = new StringBuilder();
		for (int i = 32*header.getIPHeaderLength(); i < 8*header.getTotalLength(); i++) {
			data.append(packet.charAt(i));
		}
		return data.toString();
	}
	
	public static IPPacket parse(String packet) {
		IPHeader header = IPHeader.parse(packet);
		return new IPPacket(header, IPPacket.parseData(header, packet));
	}
	
	public IPPacket(IPHeader header, String data) {
		super(header, data);
	}

	@Override
	public String toString() {
		return "N/A";
	}
	
	public void show() {
		System.out.println(this.toString());
	}
}
