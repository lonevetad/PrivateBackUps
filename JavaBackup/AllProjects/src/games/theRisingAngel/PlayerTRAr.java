package games.theRisingAngel;

import java.util.ArrayList;

import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.subImpl.GModalityET;
import games.generic.controlModel.subImpl.PlayerInGameGeneric_ExampleRPG1;
import games.theRisingAngel.events.EventsTRAr;
import games.theRisingAngel.events.GEventInterfaceTRAr;

public class PlayerTRAr extends PlayerInGameGeneric_ExampleRPG1 implements CreatureOfRPGs {

	public PlayerTRAr(GModality gameModality) {
		super(gameModality);
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(EventsTRAr.Destroyed.getName());

	}

	//

	@Override
	public int getLifeMax() {
		return this.attributes.getValue(AttributesTRAr.LifeMax.getIndex());
	}

	@Override
	public int getLife() {
		return this.attributes.getValue(AttributesTRAr.LifeCurrent.getIndex());
	}

	//

	@Override
	public void setLife(int life) {
		this.attributes.setOriginalValue(AttributesTRAr.LifeCurrent.getIndex(), life);
	}

	@Override
	public void setLifeMax(int lifeMax) {
		if (lifeMax > 0) {
			this.attributes.setOriginalValue(AttributesTRAr.LifeMax.getIndex(), lifeMax);
			if (this.getLife() > lifeMax)
				this.setLife(lifeMax);
		}
	}

	//

	// TODO other methods

	@Override
	public void receiveDamage(GModality gm, int damage) {
	}

	@Override
	public void receiveHealing(GModality gm, int healingAmount) {
	}

	@Override
	public boolean destroy() {
		if (!isDestroyed) {
			isDestroyed = true;
			return true;
		}
		return false;
	}

	@Override
	public void act(GModality modality, long milliseconds) {
	}

	@Override
	public void onStartingGame(GModality mg) {
	}

	@Override
	public void onLeavingMap() {
	}

	@Override
	public void onEnteringInGame(GModality gm) {
	}

	//

	// TODO FIRE EVENTS

	@Override
	public void fireDamageReceived(GModality gm, int originalDamage) {
		if (gm == null || (!(gm instanceof GModalityET)))
			return;
	}

	@Override
	public void fireHealingReceived(GModality gm, int originalHealing) {
		if (gm == null || (!(gm instanceof GModalityET)))
			return;
	}

	@Override
	public void fireDestructionEvent(GModality gm) {
		GModalityET gmet;
		GEventInterfaceTRAr gei;
		if (gm == null || (!(gm instanceof GModalityET)))
			return;
		gmet = (GModalityET) gm;
		gei = (GEventInterfaceTRAr) gmet.getEventInterface();
		gei.fireDestructionObjectEvent(gm, this);
	}

	@Override
	public void fireExpGainedEvent(GModality gm, int expGained) {
		if (gm == null || (!(gm instanceof GModalityET)))
			return;
	}

	@Override
	public void fireLevelGainedEvent(GModality gm, int newLevel) {
		if (gm == null || (!(gm instanceof GModalityET)))
			return;
	}

	@Override
	public boolean isDestructionEvent(IGEvent maybeDestructionEvent) {
		return maybeDestructionEvent.getName() == EventsTRAr.Destroyed.getName();
	}
}