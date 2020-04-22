package videogamesOldVersion.games.theRisingArmy.abilities;

import videogamesOldVersion.games.theRisingArmy.abstractTRAr.AbstractAbilityTRAr;

public interface AbstractAbilityParserTRAr {

	/**
	 * Returns the grammar this parser is following.<br>
	 * Must be expressed as a list of items HTML's UL, one LI for each
	 * production.<br>
	 * Common uses : "number" is self explaining; "e" usually stands for
	 * "epsilon-production", i.e. "nothing"; the first production should be "start"
	 */
	public String getGrammarParser();

	public String encode(AbstractAbilityTRAr ability);

	public AbstractAbilityTRAr decode(String ability);

	//

	public static String composeGrammarParser(String[][] productions) {
		StringBuilder sb;
		sb = new StringBuilder(productions.length << 4);
		sb.append("<UL>");
		for (String[] production : productions) {
			sb.append("<LI>");
			sb.append(production[0]);
			sb.append(" -> ");
			sb.append(production[1]);
			sb.append("</LI>");
		}
		sb.append("</UL>");
		return sb.toString();
	}
}