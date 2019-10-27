package cfg;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class TestCFG1 {

	public TestCFG1() {
	}

	public static void main2(String[] args) {
		args = new String[] { " A B", "C || D ", "F||D", "G|E", "E || T||U", "I|O|P" };
		for (String s : args) {
			System.out.println("\n\n--------");
			System.out.println(s);
			System.out.println(Arrays.toString(s.split(" ")));
			System.out.println(Arrays.toString(s.split("|")));
			System.out.println(Arrays.toString(s.split("||")));
			System.out.println(Arrays.toString(s.split("\\s+")));
			System.out.println(Arrays.toString(s.split("\\|")));
		}
	}

	public static void main(String[] args) {
		ContextFreeGrammarSource cfgs;
		ContextFreeGrammarCompiled cfgc;
		String sentence;
		Function<String, List<String>> tokenizer;

		cfgs = new ContextFreeGrammarSource();
		cfgs.addProductionRule("S", "A");
		cfgs.addProductionRule("S", "B");
		cfgs.addProductionRule("S", "A S");
		cfgs.addProductionRule("S", "S B");
		cfgs.addProductionRule("A", "a A");
		cfgs.addProductionRule("A", "a");
		cfgs.addProductionRule("B", "b B");
		cfgs.addProductionRule("B", "b");

		System.out.println("compile");
		tokenizer = (sent) -> {
			String[] sp;
			LinkedList<String> l;
			l = new LinkedList<>();
			sp = sent.split(" ");
			for (String s : sp) {
				s = s.trim();
				if (s.length() > 0)
					l.add(s);
			}
			return l;
		};

		cfgc = cfgs.compile();
		System.out.println("compiled, testit");

		sentence = "a a a b b b";
		System.out.println(cfgc.test(sentence, tokenizer));

//		sentence = "a a a b b";
	}

}
