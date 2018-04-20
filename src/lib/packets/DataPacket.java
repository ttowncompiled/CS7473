package lib.packets;

import util.HexString;
import lib.headers.NullHeader;

public class DataPacket extends Packet {
	
	private HexString data;
	
	public DataPacket(HexString data) {
		super(NullHeader.NULL_HEADER, null);
		this.data = data;
	}
	
	public HexString getData() {
		return this.data;
	}
	
	@Override
	public int size() {
		return this.data.length()/2;
	}
	
	@Override
	public HexString toHexString() {
		return this.data;
	}
	
	@Override
	public int getByteLength() {
		return this.data.length()/2;
	}
	
	@Override
	public String extractPlaintext() {
		return this.data.toBitString().toPlaintext();
	}
	
	private static String separator() {
		StringBuilder rep = new StringBuilder();
		
		rep.append("+");
		for (int i = 0; i < 63; i++) {
			rep.append(i % 2 == 0 ? "-" : "+");
		}
		rep.append("+");
		
		return rep.toString();
	}
	
	@Override
	public String toString() {
		String S = this.data.spaced().toString();
		StringBuilder rep = new StringBuilder();
		rep.append("|");
		for (int i = 0; i < 63 && i < S.length(); i++) {
			rep.append(S.charAt(i));
		}
		if (S.length() > 63) {
			for (int k = 1; k <= S.length()/63-1; k++) {
				rep.append("\n ");
				for (int i = 0; i < 63; i++) {
					rep.append(S.charAt(63*k + i));
				}
			}
			if (S.length() % 63 > 0) {
				rep.append("\n ");
				int offset = S.length() - (S.length() % 63);
				for (int i = 0; i < 63; i++) {
					if (offset + i < S.length()) {
						rep.append(S.charAt(offset + i));
					} else if ((i+1) % 3 == 0) {
						rep.append(" ");
					} else {
						rep.append("x");
					}
					
				}
			}
		} else if (S.length() % 63 > 0) {
			for (int i = S.length(); i < 63; i++) {
				if ((i+1) % 3 == 0) {
					rep.append(" ");
				} else {
					rep.append("x");
				}
			}
		}
		rep.append("|\n");
		rep.append(DataPacket.separator());
		return rep.toString();
	}
}
