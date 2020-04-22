package games.theRisingArmy.main;

import java.io.Serializable;
import java.util.List;

import games.theRisingArmy.abilities.GameObjectTrarFilter;
import games.theRisingArmy.cards.permanentsAndCreatures.PermanentTRAR;

public class AbilitySpellCastCost implements Serializable {
	private static final long serialVersionUID = -8748052904621233L;

	public AbilitySpellCastCost() {
	}

	int life;// to be spent
	ManaGroup mana;
	java.util.List<PermanentTRAR> toBeSacrificed, toBeTapped;
	List<GameObjectTrarFilter> cardsToBeDiscarded;
}