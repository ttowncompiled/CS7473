package lib.options;

import lib.headers.IPHeader;
import lib.packets.Packet;
import util.Config;

public class SameIPOption {

	public static final String SAMEIP = "sameip";
	
	private boolean sameip;
	
	public SameIPOption(boolean sameip) {
		this.sameip = sameip;
	}
	
	public boolean checkSameIP(int src, int dest) {
		return this.sameip && src == dest;
	}
	
	public boolean checkPacket(Packet p) {
		if (p.getType().equals(Config.IP)) {
			IPHeader header = (IPHeader) p.getHeader();
			return this.checkSameIP(header.getSourceIPAddress(), header.getDestinationIPAddress());
		}
		return false;
	}
}
