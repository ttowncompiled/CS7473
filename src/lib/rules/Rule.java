package lib.rules;

import lib.packets.Packet;

public class Rule {
	
	public static final String ANY = "any";
	
	public static Rule parseRule(String rule) {
		String[] args = rule.split(" ");
		ActionRule action = ActionRule.parseAction(args[0]);
		ProtocolRule protocol = ProtocolRule.parseProtocol(args[1]);
		IPMaskRule srcIPMask = IPMaskRule.parseIPMask(args[2]);
		PortRule srcPort = PortRule.parsePort(args[3]);
		DirectionRule direction = DirectionRule.parseDirection(args[4]);
		IPMaskRule destIPMask = IPMaskRule.parseIPMask(args[5]);
		PortRule destPort = PortRule.parsePort(args[6]);
		if (action == null || protocol == null || srcIPMask == null || srcPort == null || direction == null || destIPMask == null || destPort == null) {
			return null;
		}
		return new Rule(action, protocol, srcIPMask, srcPort, direction, destIPMask, destPort);
	}
	
	private ActionRule action;
	private ProtocolRule protocol;
	private IPMaskRule srcIPMask;
	private PortRule srcPort;
	private DirectionRule direction;
	private IPMaskRule destIPMask;
	private PortRule destPort;
	
	public Rule(ActionRule action, ProtocolRule protocol, IPMaskRule srcIPMask, PortRule srcPort, DirectionRule direction, IPMaskRule destIPMask, PortRule destPort) {
		this.action = action;
		this.protocol = protocol;
		this.srcIPMask = srcIPMask;
		this.srcPort = srcPort;
		this.direction = direction;
		this.destIPMask = destIPMask;
		this.destPort = destPort;
	}
	
	public boolean checkPacket(Packet p) {
		
	}
}
