package games.theRisingAngel.events;

import games.generic.controlModel.GEventManager;
import games.generic.controlModel.gEvents.DestructionObjEvent;
import games.generic.controlModel.gEvents.EventMoneyChange;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.DestructibleObject;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.subImpl.GEventInterfaceRPG;
import games.generic.controlModel.subImpl.GEventManagerFineGrained;
import games.generic.controlModel.subImpl.GModalityET;

public class GEventInterfaceTRAr implements GEventInterfaceRPG {

	GEventManager gem;

	public GEventInterfaceTRAr() {
		super();
	}

	@Override
	public void setNewGameEventManager(GModalityET gameModality) {
		this.gem = new GEventManagerFineGrained(gameModality);
	}

	@Override
	public GEventManager getGameEventManager() {
		return gem;
	}

	//

	// TODO EVENTS

	@Override
	public void fireDestructionObjectEvent(GModalityET gaModality, DestructibleObject desObj) {
		DestructionObjEvent doe;
		doe = new DestructionObjEvent(desObj, EventsTRAr.Destroyed.getName());
		this.getGameEventManager().fireEvent(doe);
	}

// TODOOOOOOOOO dc'è da spostare molti eventi in un luogo più idoneo

	@Override
	public void firePlayerEnteringInMap(GModalityET gameModality, PlayerGeneric p) {
		// semplice "creature entering on the field"
	}

	/***
	 * @param currencyType the index (or an id) of the currency earned-lost
	 */
	@Override
	public void fireMoneyChangeEvent(GModalityET gm, int currencyType, int oldValue, int newValue) {
		this.getGameEventManager().fireEvent(new EventMoneyChange(gm.getPlayer(), currencyType, oldValue, newValue));
	}

	@Override
	public <SourceDamage> void fireDamageDealtEvent(GModalityET gm, SourceDamage source, CreatureSimple target,
			DamageGeneric damage) {
		this.getGameEventManager().fireEvent( //
				new EventDamageTRAr<SourceDamage>(EventsTRAr.DamageInflicted, source, target, damage));
	}

	@Override
	public <SourceDamage> void fireDamageReceivedEvent(GModalityET gm, SourceDamage source, CreatureSimple target,
			DamageGeneric originalDamage) {
		this.getGameEventManager().fireEvent( //
				new EventDamageTRAr<SourceDamage>(EventsTRAr.DamageReceived, source, target, originalDamage));
	}

	@Override
	public void fireExpGainedEvent(GModalityET gm, int expGained) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireLevelGainedEvent(GModalityET gm, int levelGained) {
		// TODO Auto-generated method stub

	}
}