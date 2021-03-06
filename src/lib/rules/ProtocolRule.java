package lib.rules;

import lib.Showable;
import lib.packets.Packet;
import util.Config;

public class ProtocolRule implements Showable {
	
	public static ProtocolRule parseProtocol(String protocol) {
		if (! protocol.equals(Config.IP) && ! protocol.equals(Config.ARP) && ! protocol.equals(Config.TCP) && ! protocol.equals(Config.UDP) && ! protocol.equals(Config.ICMP) && ! protocol.equals(Rule.ANY)) {
			return null;
		}
		return new ProtocolRule(protocol);
	}
	
	private String protocol;
	
	public ProtocolRule(String protocol) {
		this.protocol = protocol;
	}
	
	public String getProtocol() {
		return this.protocol;
	}
	
	public boolean isIP() {
		return this.protocol.equals(Config.IP);
	}
	
	public boolean isARP() {
		return this.protocol.equals(Config.ARP);
	}
	
	public boolean isTCP() {
		return this.protocol.equals(Config.TCP);
	}
	
	public boolean isUDP() {
		return this.protocol.equals(Config.UDP);
	}
	
	public boolean isICMP() {
		return this.protocol.equals(Config.ICMP);
	}
	
	public boolean isAny() {
		return this.protocol.equals(Rule.ANY);
	}
	
	public boolean checkPacket(Packet p) {
		if (this.isAny()) {
			return true;
		}
		if (p.getType().equals(Config.ARP)) {
			return this.isARP();
		}
		if (p.getType().equals(Config.IP)) {
			if (this.isIP()) {
				return true;
			}
			return p.getNext().getType().equals(this.getProtocol());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.protocol;
	}
	
	public void show() {
		System.out.println(this.toString());
	}
}
