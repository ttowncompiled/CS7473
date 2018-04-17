package lib.rules;

import java.util.HashMap;

import lib.Showable;

public class Options implements Showable {
	
	public static Options parseOptions(String options) {
		if (options.charAt(0) != '(' && options.charAt(options.length()-1) != ')') {
			return null;
		}
		String[] opts = options.substring(1, options.length()-1).split(";");
		HashMap<String, String> args = new HashMap<>();
		for (int i = 0; i < opts.length; i++) {
			if (! opts[i].contains(":")) {
				continue;
			}
			String[] parts = opts[i].split(":");
			String param = parts[0].trim();
			String arg = parts[1].trim();
			args.put(param, arg);
		}
		return ! args.isEmpty() ? new Options(args) : null;
	}
	
	private HashMap<String, String> options;
	
	public Options(HashMap<String, String> options) {
		this.options = options;
	}
	
	public HashMap<String, String> getOptions() {
		return this.options;
	}

	@Override
	public String toString() {
		StringBuilder rep = new StringBuilder().append("(");
		for (String param : this.options.keySet()) {
			rep.append(param).append(": ").append(this.options.get(param)).append("; ");
		}
		return rep.deleteCharAt(rep.length()-1).append(")").toString();
	}
	
	public void show() {
		System.out.println(this.toString());
	}
}
