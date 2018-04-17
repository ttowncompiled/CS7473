package lib.rules;

import java.util.ArrayList;

public class RuleModule {

	private ArrayList<Rule> rules;
	
	public RuleModule() {
		this.rules = new ArrayList<>();
	}
	
	public RuleModule addRule(Rule r) {
		this.rules.add(r);
		return this;
	}
}
