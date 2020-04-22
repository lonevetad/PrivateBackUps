package grammars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import grammars.ContextFreeGrammarCompiled.AbstractTokensIterator;

public class ContextFreeGrammarCompiled implements Predicate<AbstractTokensIterator> {
	static final Comparator<String> STR_COMP = String::compareTo;
	//

	public ContextFreeGrammarCompiled(ContextFreeGrammarSource source) {
		this.source = source;
		neverCompiled = true;
		this.rules = new TreeMap<>(STR_COMP);
	}

	protected boolean neverCompiled;
	protected static int idProg = 0;
	protected final ContextFreeGrammarSource source;
	protected ProductionRuleCompiled root;
	protected final Map<String, ProductionRuleCompiled> rules/* , nonTerminals */;

	public synchronized void compile() {
		boolean canCollect;
		int i, len;
		ProductionRuleCompiled prcOld;
		ProductionRuleGeneric prcNew;
		List<Object[]> nonTerminalProducingRulesSources; // its an object to store alreadi splitted productions
		String[] productionsRaw, symbolsInProduction;
		String antec, temp;
		if (!neverCompiled)
			return;
		// first, collect and add all terminals producing rules and collect all non
		// terminal rules
		nonTerminalProducingRulesSources = new LinkedList<>();
		for (ContextFreeGrammarSource.ProductionRuleSource prs : source.rules) {

			antec = prs.antecedent.trim();
//			productionsRaw = prs.consequent.split("||");
			productionsRaw = new String[] { prs.consequent.trim() };
			canCollect = true;
			System.out.println("antec: " + antec + " -> " + Arrays.toString(productionsRaw));
			{
				int k;
				i = k = -1;
				len = productionsRaw.length;
				while (++i < len) { // trim all productions: avoid blank spaces
					temp = productionsRaw[i].trim();
					if (temp.length() > 0)
						productionsRaw[++k] = temp;
				}
				if (i != k) {
					if (canCollect = k >= 0) {
						symbolsInProduction = new String[++k];
						while (--k >= 0) // array-trimming through array-copy
							symbolsInProduction[k] = productionsRaw[k];
						productionsRaw = symbolsInProduction;
						symbolsInProduction = null;
					}
				}
			}
			if (canCollect) {
				if (isProductionOnlyTerminals(productionsRaw)) {
					PRTerminalsSet prt_s;
					canCollect = true;
					prt_s = null;
					if (rules.containsKey(antec)) {
						System.out.println("cointaining : " + antec);
						prcOld = rules.get(antec);
						// already got, check what is it
						if (prcOld instanceof PRTerminalsSet)
							prt_s = (PRTerminalsSet) prcOld;
						else {
							List<ProductionRuleCompiled> prods;
							// producing tons of terminals on a different kind of PR? collect terminals in
							// it
							if (prcOld instanceof ProductionRuleGeneric) {
								// collect
								ProductionRuleGeneric prg;
								prg = (ProductionRuleGeneric) prcOld;
								i = -1;
								while (++i < len) {
									prods = new LinkedList<>();
									prods.add(new PRSingleTerminal(productionsRaw[i]));
									prg.disjunctiveProductions.add(prods);
								}
								canCollect = false;
							} else if (prcOld instanceof PRSingleTerminal) {
								// collect in a smart way
								prt_s = new PRTerminalsSet(antec);
								rules.remove(antec);
								prt_s.terminals.add(prcOld.name);
								rules.put(antec, prt_s);
							} else {
								// error : cannot manage it
								unManagebleProductionClassException(prcOld);
							}
						}
					} else {
						prt_s = new PRTerminalsSet(antec);
						rules.put(antec, prt_s);
					}
					if (canCollect) {
						i = -1;
						while (++i < len)
							prt_s.terminals.add(productionsRaw[i]);
					}
				} else {
					// mixes of terminals and non-terminals upon productions
					Object[] o;
					o = new Object[] { productionsRaw, antec }; // , prs
					nonTerminalProducingRulesSources.add(o);
					// save the (empty) antecedent: make it an enumeration
					if (!rules.containsKey(antec))
						rules.put(antec, new ProductionRuleGeneric(antec));
				}
			}
		}
		// now produce remaining productions
		// all non-terminal-producing productions are already created on the last "else"
		// if someone new is found -> Exception

		if (!nonTerminalProducingRulesSources.isEmpty()) {
//			ContextFreeGrammarSource.ProductionRuleSource prs;
			System.out.println("nonTerminalProducingRulesSources size: " + nonTerminalProducingRulesSources.size());
			for (Object[] o : nonTerminalProducingRulesSources) {
//				prs = (ProductionRuleSource) o[3];
				productionsRaw = (String[]) o[0];
				antec = (String) o[1];
				o = null;
				System.out.println("antec: " + antec + " -> " + Arrays.toString(productionsRaw));
				// start the real game : create or retrieve the production
				if (rules.containsKey(antec)) {
					prcOld = rules.get(antec);
					if (prcOld instanceof PRSingleTerminal) {
//						PRSingleTerminal prst;
						List<ProductionRuleCompiled> prods;
//						prst = (PRSingleTerminal) prcOld;
						prcNew = new ProductionRuleGeneric(antec);
						// a bit copy-pasted from above
						rules.remove(antec);
						rules.put(antec, prcNew);
						prods = new LinkedList<>();
						prods.add(new PRSingleTerminal(antec)); // == prst.name
						prcNew.disjunctiveProductions.add(prods);
					} else if (prcOld instanceof PRTerminalsSet) {
						PRTerminalsSet prts;
						ProductionRuleGeneric prg;
						prcNew = prg = new ProductionRuleGeneric(antec);
						prts = (PRTerminalsSet) prcOld;
						prts.terminals.forEach(term -> {
							List<ProductionRuleCompiled> prods;
							prods = new LinkedList<>();
							prods.add(new PRSingleTerminal(term));
							prg.disjunctiveProductions.add(prods);
						});
					} else if (prcOld instanceof ProductionRuleGeneric) {
						prcNew = (ProductionRuleGeneric) prcOld;
					} else
						unManagebleProductionClassException(prcOld);
				} else {
					prcNew = new ProductionRuleGeneric(antec);
				}
				// we got the instance, produce all invocations
				for (String singleProductionRaw : productionsRaw) {
					List<ProductionRuleCompiled> prods;
					symbolsInProduction = singleProductionRaw.split("\\s+");
					System.out
							.println("from --" + singleProductionRaw + " got: " + Arrays.toString(symbolsInProduction));
					prods = new LinkedList<>();
					for (String symbol : symbolsInProduction) {
						symbol = symbol.trim();
						if (symbol.length() > 0) {
							if (rules.containsKey(symbol)) {
								prcOld = rules.get(symbol);
//							// what is it? no matter: recycle it
								prods.add(prcOld);
							} else {
								if (isNonTerminal(symbol)) {
									root = null;
									rules.clear();
									throw new IllegalStateException("Unreachable production: " + symbol);
								} else {
									PRSingleTerminal prst;
									// terminal
									prst = new PRSingleTerminal(symbol);
									prods.add(prst);
									rules.put(symbol, prst);
								}
							}
						}
					}
				}
			}
		}
		root = rules.get("S");
		if (root == null) {
			rules.clear();
			throw new IllegalStateException("No starting node defined with the exact name \"S\".");
		}
		neverCompiled = false;
	}

	protected void unManagebleProductionClassException(ProductionRuleCompiled prcOld) {
		rules.clear();
		root = null;
		throw new IllegalStateException(
				"Cannot manage a duplicated production rule of unknown class: " + prcOld.getClass());
	}

	public boolean test(String sentence, Function<String, List<String>> tokenizer) {
		return test(tokenizer.apply(sentence));
	}

	public boolean test(Stream<String> tokens) {
		return test((String[]) tokens.toArray());
	}

	public boolean test(String[] tokens) {
		ArrayList<String> a;
		a = new ArrayList<String>(tokens.length);
		for (String s : tokens)
			a.add(s);
		return test(a);
	}

	public boolean test(Collection<String> tokens) {
		return test(newTokenIterator(tokens));
	}

	@Override
	public boolean test(AbstractTokensIterator t) {
		compile();
		if (neverCompiled)
			return false;
		else
			return root == null ? false : root.test(t);
	}

	protected AbstractTokensIterator newTokenIterator(Collection<String> allTokens) {
		return new TokensIterator_V1(allTokens);
	}

	@Override
	public String toString() {
		StringBuilder sb;
		sb = new StringBuilder(1024);

		return sb.toString();
	}

	//

	public static boolean isTerminal(String s) {
		char c;
		int len;
		len = s.length();
		if (len == 0)
			return true;
		while (--len >= 0 && (
		// skip white characters: they separates symbols
		Character.isWhitespace(c = s.charAt(len)) ||
		// some lower-case -> terminal
				Character.isLowerCase(c)))
			;
		return len < 0;
	}

	public static boolean isNonTerminal(String s) {
		char c;
		int len;
		len = s.length();
		if (len == 0)
			return false;
		while (--len >= 0 && (
		// skip white characters: they separates symbols
		Character.isWhitespace(c = s.charAt(len)) ||
		// some lower-case -> terminal
				Character.isUpperCase(c)))
			;
		return len < 0;
	}

	protected static boolean isProductionOnlyTerminals(String[] productionsRaw) {
		System.out.println("productionsRaw: " + Arrays.toString(productionsRaw));
		for (String pr : productionsRaw)
			if (pr.length() > 0 && isNonTerminal(pr)) {
				System.out.println("non terminal ");
				return false;
			}
		System.out.println("terminal ");
		return true;
	}

	//

	// TODO CLASSES

	public abstract class AbstractTokensIterator implements ListIterator<String> {
		protected int index;
		protected List<String> tokens;
		protected String actualToken;

		AbstractTokensIterator(Collection<String> allTokens) {
			this.tokens = newTokenHolder(allTokens);
			this.index = -1;
			this.actualToken = null;
			this.moveToNext();
		}

		protected abstract List<String> newTokenHolder(Collection<String> allTokens);

		public String getActualToken() {
			return actualToken;
		}

		public void moveToNext() {
			next();
		}

		public void moveToPrevious() {
			previous();
		}

		public void backTrackBy(int n) {
//			while (n-- > 0)
//				moveToPrevious();
			int t;
			if (n <= 0)
				return;
			t = this.index - n;
			this.actualToken = tokens.get(t);
			this.index = t;
		}

		//

		@Override
		public boolean hasNext() {
			return index < (tokens.size());
		}

		@Override
		public String next() {
			String s;
			s = null;
			if (hasNext()) {
				s = actualToken;
				actualToken = tokens.get(++index);
			}
			return s;
//			return (hasNext()) ? tokens.get(index++) : null;
		}

		@Override
		public boolean hasPrevious() {
			return index >= 0;
		}

		@Override
		public String previous() {
			String s;
			s = null;
			if (hasPrevious()) {
				s = actualToken;
				actualToken = tokens.get(--index);
			}
			return s;
//			return (hasPrevious()) ? tokens.get(index--) : null;
		}

		@Override
		public int nextIndex() {
			return index + 1;
		}

		@Override
		public int previousIndex() {
			return index - 1;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(String e) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(String e) {
			throw new UnsupportedOperationException();
		}
	}

	public class TokensIterator_V1 extends AbstractTokensIterator {
		TokensIterator_V1(Collection<String> allTokens) {
			super(allTokens);
		}

		@Override
		protected List<String> newTokenHolder(Collection<String> allTokens) {
			List<String> tl;
			if (allTokens == null)
				throw new IllegalArgumentException("collection of tokens null");
			if (allTokens instanceof ArrayList<?>)
				tl = (ArrayList<String>) allTokens;
			else {
				tl = new ArrayList<>(allTokens.size());
				allTokens.forEach(
						//
						t -> tl.add(t));
			}
			return tl;
		}
	}

	//

	/**
	 * Ambiente di parsing, tiene conto dell'indice del token attuale e della
	 * produzione che si è incaricata di gestirla. Se si cerca di aggiungere una
	 * coppia già presente (ossia la grammatica ha un ciclo infinito di procedure),
	 * spara una eccezione
	 */
	protected static class CyclicGrammarUponTestingDetector {
		static final Comparator<TokenProductionIndexes> COMPARATOR_TPI = (tpi1, tpi2) -> {
			int c;
			c = Integer.compare(tpi1.tokenIndex, tpi2.tokenIndex);
			if (c != 0)
				return c;
			return Integer.compare(tpi1.productionID, tpi2.productionID);
		};
		/**
		 * Associate at each token's index a set of production. If there's a repetition
		 * -> exception
		 */
		protected Map<Integer, Set<TokenProductionIndexes>> productionCalls;// use a map

		protected CyclicGrammarUponTestingDetector() {
			this.productionCalls = new TreeMap<>(Integer::compareTo);
		}

		protected void registerTokenIndexProceduction(int tokenIndex, Integer productionID) {
			Integer i;
			Set<TokenProductionIndexes> calls;
			TokenProductionIndexes tpi;
			i = Integer.valueOf(tokenIndex);
			if (productionCalls.containsKey(i)) {
				calls = productionCalls.get(i);
				tpi = new TokenProductionIndexes(i, productionID);
				if (calls.contains(tpi))
					throw new IllegalStateException("Cycle grammar found");
				else
					calls.add(tpi);
			} else {
				calls = new TreeSet<>(COMPARATOR_TPI);
				calls.add(new TokenProductionIndexes(i, productionID));
				productionCalls.put(i, calls);
			}
		}

		protected static class TokenProductionIndexes {
			public TokenProductionIndexes(Integer tokenIndex, Integer productionID) {
				this.tokenIndex = tokenIndex;
				this.productionID = productionID;
			}

			Integer tokenIndex, productionID;
		}
	}

	//

	// TODO ProductionRuleCompiled

	public abstract class ProductionRuleCompiled implements Predicate<AbstractTokensIterator> {

		protected final Integer id;
		protected final String name; // e' come il nome del simpbolo non terminale

		protected ProductionRuleCompiled(String nonTerminalName) {
			this.id = idProg++;
			this.name = nonTerminalName;
		}

//		String nonTerminalName;
		/**
		 * Verify if the token (read from the sentence to be parsed) is accepted by this
		 * production
		 */
		public abstract boolean acceptToken(String s);

		/**
		 * Test if this production rule can be called and then the given token can be
		 * consumed. <br>
		 * {@inheritDoc}
		 */
		@Override
		public abstract boolean test(AbstractTokensIterator t);

	}

	/**
	 * Built as a list of production, each is a list of symbols (terminals and
	 * non-terminals)
	 */
	public class ProductionRuleGeneric extends ProductionRuleCompiled {
		protected final List<List<ProductionRuleCompiled>> disjunctiveProductions;

		/** All terminals and non terminals */
//		protected final List<List<ProductionRuleCompiled>> productions;

		protected ProductionRuleGeneric(String nonTerminalName) {
			super(nonTerminalName);
			this.disjunctiveProductions = new LinkedList<>();
		}

		@Override
		public boolean acceptToken(String s) {
			System.out.println("WTF - ProductionRuleGeneric");
			return false; // not the way to do this
		}

		@Override
		public boolean test(AbstractTokensIterator t) {
			boolean isAcceptedByProduction, notAccepted;
			int tokenRead, subtokensCount; // i, productionsInOr,
			Iterator<List<ProductionRuleCompiled>> iterProdInOr;
			Iterator<ProductionRuleCompiled> iterProdInAnd;
			List<ProductionRuleCompiled> production;
			ProductionRuleCompiled prodToTest;
//			String token;
			notAccepted = true;
//			i = -1;
//			productionsInOr = this.disjunctiveProductions.size();
//while(++i<productionsInOr) {

			// per ogni produzione
			System.out.println("test on GENERIC " + name + "??");
			iterProdInOr = this.disjunctiveProductions.iterator();
			while (notAccepted && iterProdInOr.hasNext()) {
				isAcceptedByProduction = true;
				tokenRead = 0;
				production = iterProdInOr.next(); // this.disjunctiveProductions
				// per ogni simbolo
				subtokensCount = production.size();
				iterProdInAnd = production.iterator();
				while (isAcceptedByProduction && tokenRead < subtokensCount) {
//					token = t.getActualToken();
					// prendiamo il gestore del simbolo e ...
					prodToTest = iterProdInAnd.next();
					isAcceptedByProduction = prodToTest.test(t);
					// se il test e' positivo, avanziamo
					if (isAcceptedByProduction)
						tokenRead++;
				}
				if (notAccepted = (!isAcceptedByProduction))
					t.backTrackBy(tokenRead);
//				else notAccepted = false;
			}
			System.out.println("test on GENERIC " + name + " : " + (!notAccepted));
			return !notAccepted;
		}

//		protected void buildProduction(String )
	}

	public class PRSingleTerminal extends ProductionRuleCompiled {

		protected PRSingleTerminal(String nonTerminalName) {
			super(nonTerminalName);
		}

		@Override
		public boolean test(AbstractTokensIterator t) {
			return acceptToken(t.getActualToken());
		}

		@Override
		public boolean acceptToken(String s) {
			System.out.println("test on SINGLE " + name + ": " + this.name.equals(s));
			return this.name.equals(s);
		}
	}

	public class PRTerminalsSet extends ProductionRuleCompiled {
		protected Set<String> terminals;

		protected PRTerminalsSet(String nonTerminalName) {
			super(nonTerminalName);
			terminals = new TreeSet<String>(STR_COMP);
		}

		@Override
		public boolean test(AbstractTokensIterator t) {
			return acceptToken(t.getActualToken());
		}

		@Override
		public boolean acceptToken(String s) {
			System.out.println("test on SET " + name + " : " + terminals.contains(s));
			return terminals.contains(s);
		}
	}

	public static void main(String[] args) {
		System.out.println(isNonTerminal("ciao come va"));
	}
}