package lib.rules;

import java.util.ArrayList;

import lib.packets.Packet;

public class RuleModule {
	
	private ArrayList<Rule> rules;
	
	public RuleModule() {
		this.rules = new ArrayList<>();
	}
	
	public RuleModule addRule(Rule r) {
		this.rules.add(r);
		return this;
	}
	
	public void checkPacket(Packet p) {
		for (Rule r : this.rules) {
			if (r.checkPacket(p)) {
				this.handleRule(r, p);
			}
		}
	}
	
	private void handleRule(Rule r, Packet p) {
		
	}
}
