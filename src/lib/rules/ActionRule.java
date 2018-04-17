package lib.rules;

public class ActionRule {

	public static final String ALERT = "alert";
	public static final String PASS = "pass";
	
	public static ActionRule parseAction(String action) {
		if (! action.equals(ActionRule.ALERT) && ! action.equals(ActionRule.PASS)) {
			return null;
		}
		return new ActionRule(action);
	}
	
	private String action;
	
	public ActionRule(String action) {
		this.action = action;
	}
	
	public String getAction() {
		return this.action;
	}
	
	public boolean isAlert() {
		return this.action.equals(ActionRule.ALERT);
	}
	
	public boolean isPass() {
		return this.action.equals(ActionRule.PASS);
	}
}
