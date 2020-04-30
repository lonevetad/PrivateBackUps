package games.theRisingAngel.events;

import java.awt.Point;

import games.generic.controlModel.GEventManager;
import games.generic.controlModel.gEvents.DestructionObjEvent;
import games.generic.controlModel.gEvents.EventDamage;
import games.generic.controlModel.gEvents.EventMoneyChange;
import games.generic.controlModel.gObj.DamageDealerGeneric;
import games.generic.controlModel.gObj.DestructibleObject;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.misc.HealGeneric;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.subimpl.GEventInterfaceRPG;
import games.generic.controlModel.subimpl.GEventManagerFineGrained;
import games.generic.controlModel.subimpl.GModalityET;
import geometry.ObjectLocated;
import tools.ObjectWithID;

public class GEventInterfaceTRAn implements GEventInterfaceRPG {

	public GEventInterfaceTRAn() {
		super();
	}

	protected GEventManager gem;

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
		doe = new DestructionObjEvent(desObj, EventsTRAn.Destroyed.getName());
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
	public EventDamage fireDamageDealtEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric damage) {
		EventDamage ed;
		ed = new EventDamageTRAn(EventsTRAn.DamageInflicted, source, target, damage);
		this.getGameEventManager().fireEvent(ed);
		return ed;
	}

	public void fireDamageAvoidedEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric damage) {
		EventDamage ed;
		ed = new EventDamageTRAn(EventsTRAn.DamageAvoided, source, target, damage);
		this.getGameEventManager().fireEvent(ed);
	}

	public void fireDamageMissedEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric damage) {
		EventDamage ed;
		ed = new EventDamageTRAn(EventsTRAn.DamageMissed, source, target, damage);
		this.getGameEventManager().fireEvent(ed);
	}

	@Override
	public EventDamage fireCriticalDamageDealtEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric damage) {
		EventDamage ed;
		ed = new EventDamageTRAn(EventsTRAn.DamageCriticalInflicted, source, target, damage);
		this.getGameEventManager().fireEvent(ed);
		return ed;
	}

	@Override
	public EventDamage fireDamageReceivedEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric originalDamage, int damageAmountToBeApplied) {
		EventDamageTRAn ed;
		ed = new EventDamageTRAn(EventsTRAn.DamageReceived, source, target, originalDamage, damageAmountToBeApplied);
		this.getGameEventManager().fireEvent(ed);
		return ed;
	}

	@Override
	public EventDamage fireDamageCriticalReceivedEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric originalDamage, int damageAmountToBeApplied) {
		EventDamageTRAn ed;
		ed = new EventDamageTRAn(EventsTRAn.DamageCriticalReceived, source, target, originalDamage,
				damageAmountToBeApplied);
		this.getGameEventManager().fireEvent(ed);
		return ed;
	}

	@Override
	public <SourceHealing extends ObjectWithID> void fireHealGivenEvent(GModalityET gaModality, LivingObject receiver,
			HealGeneric heal, SourceHealing source) {
		this.getGameEventManager().fireEvent(new EventHealTRAr<>(EventsTRAn.HealGiven, source, receiver, heal));
	}

	@Override
	public <SourceHealing extends ObjectWithID> void fireHealReceivedEvent(GModalityET gaModality, SourceHealing source,
			LivingObject receiver, HealGeneric heal) {
		this.getGameEventManager().fireEvent(new EventHealTRAr<>(EventsTRAn.HealReceived, source, receiver, heal));
	}

	@Override
	public void fireExpGainedEvent(GModalityET gm, int expGained) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireGameObjectAdded(GModalityET gameModality, ObjectLocated o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireGameObjectRemoved(GModalityET gameModality, ObjectLocated o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireGameObjectMoved(GModalityET gameModality, Point previousLocation, ObjectLocated o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireLevelGainedEvent(GModalityET gm, int levelGained) {
		// TODO Auto-generated method stub

	}
}