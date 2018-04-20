package lib.rules;

import java.util.ArrayList;

import lib.packets.Packet;

public class RuleModule {
	
	private ArrayList<Rule> rules;
	
	public RuleModule() {
		this.rules = new ArrayList<>();
	}
	
	public ArrayList<Rule> getRules() {
		return this.rules;
	}
	
	public RuleModule addRule(Rule r) {
		this.rules.add(r);
		return this;
	}
	
	public ArrayList<Rule> checkPacket(Packet p) {
		ArrayList<Rule> violations = new ArrayList<>();
		for (Rule r : this.rules) {
			if (r.checkPacket(p)) {
				violations.add(this.handleRule(r, p));
			}
		}
		return violations;
	}
	
	private Rule handleRule(Rule r, Packet p) {
		return r;
	}
}
