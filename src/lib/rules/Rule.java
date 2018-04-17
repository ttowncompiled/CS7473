package lib.rules;

import lib.Showable;
import lib.packets.Packet;

public class Rule implements Showable {
	
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
		Options options = null;
		if (args.length > 7) {
			StringBuilder rep = new StringBuilder();
			rep.append(args[7]);
			for (int i = 8; i < args.length; i++) {
				rep.append(" ").append(args[i]);
			}
			options = Options.parseOptions(rep.toString());
		}
		return new Rule(action, protocol, srcIPMask, srcPort, direction, destIPMask, destPort, options);
	}
	
	private ActionRule action;
	private ProtocolRule protocol;
	private IPMaskRule srcIPMask;
	private PortRule srcPort;
	private DirectionRule direction;
	private IPMaskRule destIPMask;
	private PortRule destPort;
	private Options options;
	
	public Rule(ActionRule action, ProtocolRule protocol, IPMaskRule srcIPMask, PortRule srcPort, DirectionRule direction, IPMaskRule destIPMask, PortRule destPort, Options options) {
		this.action = action;
		this.protocol = protocol;
		this.srcIPMask = srcIPMask;
		this.srcPort = srcPort;
		this.direction = direction;
		this.destIPMask = destIPMask;
		this.destPort = destPort;
		this.options = options;
	}
	
	public boolean checkPacket(Packet p) {
		return false;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append(this.action.toString()).append(" ")
								  .append(this.protocol.toString()).append(" ")
								  .append(this.srcIPMask.toString()).append(" ")
								  .append(this.srcPort.toString()).append(" ")
								  .append(this.direction.toString()).append(" ")
								  .append(this.destIPMask.toString()).append(" ")
								  .append(this.destPort.toString()).append(" ")
								  .append(this.options != null ? this.options.toString() : "")
								  .toString();
	}
	
	public void show() {
		System.out.println(this.toString());
	}
}
