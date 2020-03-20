package tests.tGame.tgEvent1;

import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GEventManager;
import games.generic.controlModel.GModality;
import games.generic.controlModel.eventsGame.EventInfo_SourceToTarget;
import games.generic.controlModel.eventsGame.EventMoneyChange;
import games.generic.controlModel.eventsGame.ExampleGameEvents;
import games.generic.controlModel.player.PlayerInGame_Generic;
import games.generic.controlModel.subImpl.GEventManagerFineGrained;

public class GEventInterface_E1 implements GEventInterface {
	GEventManager gem;

	@Override
	public void setNewGameEventManager(GModality gameModality) {
		this.gem = new GEventManagerFineGrained(gameModality);
	}

	@Override
	public GEventManager getGameEventManager() {
		return gem;
	}

	@Override
	public void firePlayerEnteringInMap(GModality gameModality, PlayerInGame_Generic p) {
		// TODO Auto-generated method stub
	}

	/***
	 * @param currencyType the index (or an id) of the currency earned-lost
	 */
	public void fireMoneyChangeEvent(GModality gm, int currencyType, int oldValue, int newValue) {
		this.getGameEventManager().addEvent(new EventMoneyChange(gm.getPlayer(), currencyType, oldValue, newValue));
	}

	public <Source, Target> void fireDamageDealtEvent(GModality gm, Source source, Target target, int damageInflicted) {
		this.getGameEventManager().addEvent(new EventInfo_SourceToTarget<Source, Target>(
				ExampleGameEvents.DamageInflicted.getType(), source, target, damageInflicted));
	}

}