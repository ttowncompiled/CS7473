package lib.options;

import org.apache.commons.lang3.StringUtils;

import lib.headers.TCPHeader;
import lib.packets.Packet;
import util.Config;

public class AckOption {

	public static final String ACK = "ack";
	
	public static AckOption parseOption(String option) {
		if (! StringUtils.isNumeric(option.trim())) {
			return null;
		}
		return new AckOption(Integer.parseInt(option.trim()));
	}
	
	private int ack;
	
	public AckOption(int ack) {
		this.ack = ack;
	}
	
	public boolean checkAck(int ack) {
		return ack == this.ack;
	}
	
	public boolean checkPacket(Packet p) {
		if (p.getType().equals(Config.IP)) {
			p = p.getNext();
		}
		if (p.getType().equals(Config.TCP)) {
			TCPHeader header = (TCPHeader) p.getHeader();
			return this.checkAck(header.getAcknowledgementNumber());
		}
		return false;
	}
}
