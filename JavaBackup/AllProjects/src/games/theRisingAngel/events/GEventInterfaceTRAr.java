package games.theRisingAngel.events;

import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GEventManager;
import games.generic.controlModel.GModality;
import games.generic.controlModel.eventsGame.DestructionObjEvent;
import games.generic.controlModel.eventsGame.EventMoneyChange;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.gameObj.DestructibleObject;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.player.PlayerInGame_Generic;
import games.generic.controlModel.subImpl.GEventManagerFineGrained;

public class GEventInterfaceTRAr implements GEventInterface {

	GEventManager gem;

	public GEventInterfaceTRAr() {
		super();
	}

	@Override
	public void setNewGameEventManager(GModality gameModality) {
		this.gem = new GEventManagerFineGrained(gameModality);
	}

	@Override
	public GEventManager getGameEventManager() {
		return gem;
	}

	//

	// TODO EVENTS

	public void fireDestructionObjectEvent(GModality gaModality, DestructibleObject desObj) {
		DestructionObjEvent doe;
		doe = new DestructionObjEvent(desObj, EventsTRAr.Destroyed.getName());
		this.getGameEventManager().fireEvent(doe);
	}

	@Override
	public void firePlayerEnteringInMap(GModality gameModality, PlayerInGame_Generic p) {
		// semplice "creature entering on the field"
	}

	/***
	 * @param currencyType the index (or an id) of the currency earned-lost
	 */
	public void fireMoneyChangeEvent(GModality gm, int currencyType, int oldValue, int newValue) {
		this.getGameEventManager().fireEvent(new EventMoneyChange(gm.getPlayer(), currencyType, oldValue, newValue));
	}

	public <Source> void fireDamageDealtEvent(GModality gm, Source source, CreatureOfRPGs target,
			DamageGeneric damageInflicted) {
		this.getGameEventManager().fireEvent( //
				new EventDamageTRAr<>(EventsTRAr.DamageInflicted, source, target, damageInflicted));
	}

	public <Source> void fireDamageReceivedEvent(GModality gm, Source source, CreatureOfRPGs target,
			DamageGeneric originalDamage) {
		this.getGameEventManager().fireEvent( //
				new EventDamageTRAr<>(EventsTRAr.DamageReceived, source, target, originalDamage));
	}
}