package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import lib.rules.Rule;
import util.Config;

public class RuleModuleTest {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(Config.RULES_PATH));
		while (scanner.hasNextLine()) {
			Rule rule = Rule.parseRule(scanner.nextLine());
			rule.show();
		}
		scanner.close();
	}
}
