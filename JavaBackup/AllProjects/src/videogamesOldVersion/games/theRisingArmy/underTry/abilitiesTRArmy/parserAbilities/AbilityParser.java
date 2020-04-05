package videogamesOldVersion.games.theRisingArmy.underTry.abilitiesTRArmy.parserAbilities;

import videogamesOldVersion.games.theRisingArmy.abilities.AbilityParserTRAr_1.AbilityParserTokenHolder;

public interface AbilityParser {

	public static final String[] GRAMMAR = new String[] { "S -> Costs \":\" EffectList", //
			"Costs -> SingleCost (epsilon | Costs)", //
			"SingleCost -> Manacost | SacrificeCost | TapCost | ...and so on", //
			"EffectList -> SingleEffect (epsilon | EffectList)", //
			"SingleEffect -> Effect | EffectFilter Effect", //
			"EffectFilter -> some expression like \"X greater than N , X is count of life\" or \"X equals to 5, X is count of colors among Filter, Filter is Permanent and Controlled and Creature\", \"for each Targets\"", //
			"Effect -> \"TAP target\", \"draw N cards\", \"discard N cards\", \"gain X life\", \"deals X damage\", \"destroy\", \"sacrifice\", \"mill\", etc etc" //
	};

	//

	public static enum AbilityTypes implements AbilityParserTokenHolder {
		Static("S"), Activated("A"), Triggered("T"); // , ManaSource("M");

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
		Everytime("E"), If("I"), Then("T"), Or("O"), And("A"), AtLeast("Al"), AtMost("Am"), MoreThan("M"),
		LessThen("L");

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
}
