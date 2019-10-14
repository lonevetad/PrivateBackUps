package videogamesOldVersion.games.theRisingArmy.abilities;

import videogamesOldVersion.games.theRisingArmy.abstractTRAr.AbstractAbilityTRAr;

public class AbilityParserTRAr_1 implements AbstractAbilityParserTRAr {
	public static final String grammarParser = AbstractAbilityParserTRAr.composeGrammarParser(new String[][] { //
			/* that number is the index / ordinal of the ability in a hypotetic set */
			new String[] { "Start", "number | typeDefinition" } //
			, new String[] { "typeDefinition", "type : definition" } //
			// , new String[] { "type", "S | A | M" } //
			, new String[] { "Start", "\'A\' cost : effect | \'S\'" } //
			, new String[] { "Start", "number | typeDefinition" } //
			, new String[] { "Start", "number | typeDefinition" } //
			, new String[] { "Start", "number | typeDefinition" } //
			, new String[] { "Start", "number | typeDefinition" } //
			, new String[] { "number", "[0-9]+ .. x € N" } //
			, });

	public static interface AbilityParserTokenHolder {
		public String getName();

		public String getToken();
	}

	public static enum AbilityTypes implements AbilityParserTokenHolder {
		Untagged, Static("S"), Activated("A"), Triggered("T"), ManaSource("M");

		AbilityTypes() {
			this(null);
		}

		AbilityTypes(String s) {
			this.token = s;
		}

		final String token;

		@Override
		public String getToken() {
			return token;
		}

		@Override
		public String getName() {
			return name();
		}
	}

	public static enum AbilityTokens implements AbilityParserTokenHolder {
		Everytime("E"), If("I"), Then("T");

		AbilityTokens() {
			this(null);
		}

		AbilityTokens(String s) {
			this.token = s;
		}

		final String token;

		@Override
		public String getToken() {
			return token;
		}

		@Override
		public String getName() {
			return name();
		}
	}

	@Override
	public String getGrammarParser() {
		return grammarParser;
	}

	public AbilityParserTRAr_1() {
	}

	@Override
	public String encode(AbstractAbilityTRAr ability) {
		return null;
	}

	@Override
	public AbstractAbilityTRAr decode(String ability) {
		return null;
	}

	@Override
	public String encode(videogamesOldVersion.games.theRisingArmy.abilities.AbstractAbilityTRAr ability) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public videogamesOldVersion.games.theRisingArmy.abilities.AbstractAbilityTRAr decode(String ability) {
		// TODO Auto-generated method stub
		return null;
	}

}
