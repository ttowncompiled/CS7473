package lib.rules;

import org.apache.commons.lang3.StringUtils;

public class PortRule {

	public static PortRule parsePort(String port) {
		if (port.equals(Rule.ANY)) {
			return new PortRule();
		}
		if (! port.contains(":")) {
			if (! StringUtils.isNumeric(port)) {
				return null;
			}
			return new PortRule(Short.parseShort(port), true);
		} else {
			String[] ports = port.split(":");
			if (ports.length != 1 && ports.length != 2) {
				return null;
			}
			if (! StringUtils.isNoneBlank(ports[0])) {
				return null;
			}
			if (ports.length == 1) {
				return new PortRule(Short.parseShort(ports[0]), false);
			} else {
				if (! StringUtils.isNumeric(ports[1])) {
					return null;
				}
				return new PortRule(Short.parseShort(ports[0]), Short.parseShort(ports[1]));
			}
		}
	}
	
	private short port1;
	private short port2;
	private boolean onlyPort1;
	private boolean lessThanPort2;
	private boolean anyPort;
	
	public PortRule(short port1, short port2) {
		this.port1 = port1;
		this.port2 = port2;
	}
	
	public PortRule(short port, boolean isPort1) {
		if (isPort1) {
			this.port1 = port;
			this.onlyPort1 = true;
		} else {
			this.port2 = port;
			this.lessThanPort2 = true;
		}
	}
	
	public PortRule() {
		this.anyPort = true;
	}
	
	public short getPort1() {
		return this.port1;
	}
	
	public short getPort2() {
		return this.port2;
	}
	
	public boolean isAny() {
		return this.anyPort;
	}
	
	public boolean checkPort(short port) {
		if (this.anyPort) {
			return true;
		} else if (this.onlyPort1) {
			return this.port1 == port;
		} else if (this.lessThanPort2) {
			return this.port2 >= port;
		}
		return this.port1 <= port && port <= this.port2;
	}
}
