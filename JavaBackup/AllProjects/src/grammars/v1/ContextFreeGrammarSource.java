package grammars.v1;

import java.util.LinkedList;
import java.util.List;

/**
 * NOTE: the consequent of a rule is a list of production separated by a
 * <code>|<code> and each terminal or non-terminal is separated by a white space
 */
public class ContextFreeGrammarSource {

	public ContextFreeGrammarSource() {
		rules = new LinkedList<>();
	}

	protected List<ProductionRuleSource> rules;

	public ContextFreeGrammarSource addProductionRule(String antecedent, String consequent) {
		this.rules.add(new ProductionRuleSource(antecedent, consequent));
		return this;
	}

	public ContextFreeGrammarCompiled toCompiled() {
		return new ContextFreeGrammarCompiled(this);
	}

	public ContextFreeGrammarCompiled compile() {
		ContextFreeGrammarCompiled c;
		c = toCompiled();
		c.compile();
		return c;
	}

	public static class ProductionRuleSource {
		public ProductionRuleSource(String antecedent, String consequent) {
			super();
			this.antecedent = antecedent;
			this.consequent = consequent;
		}

		public final String antecedent, consequent;
	}
}