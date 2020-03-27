package games.theRisingAngel;

import java.util.ArrayList;

import games.generic.ObjectWithID;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.subImpl.GModalityET;
import games.generic.controlModel.subImpl.PlayerInGameGeneric_ExampleRPG1;
import games.theRisingAngel.creatures.BaseCreatureTRAr;
import games.theRisingAngel.events.EventsTRAr;
import games.theRisingAngel.events.GEventInterfaceTRAr;
import games.theRisingAngel.inventory.EquipmentSetTRAr;
import geometry.AbstractShape2D;

public class PlayerTRAr extends PlayerInGameGeneric_ExampleRPG1 implements BaseCreatureTRAr {
	private static final long serialVersionUID = -3336623605789L;

	public PlayerTRAr(GModality gameModality) {
		super(gameModality);
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(EventsTRAr.Destroyed.getName());
		this.setEquipmentSet(new EquipmentSetTRAr());
	}

	//

	//

	// TODO other methods

	@Override
	public void receiveDamage(GModality gm, DamageGeneric originalDamage, ObjectWithID source) {
		if (originalDamage.getDamageAmount() <= 0)
			return;
		BaseCreatureTRAr.super.receiveDamage(gm, originalDamage, source);
	}

	@Override
	public boolean destroy() {
		if (!isDestroyed) {
			isDestroyed = true;
			return true;
		}
		return false;
	}

//	public void act(GModality modality, long milliseconds) { super.act(modality, milliseconds); }

	@Override
	public void onStartingGame(GModality mg) {
		this.setGameModality(mg);
	}

	@Override
	public void onLeavingMap() {
	}

	@Override
	public void onEnteringInGame(GModality gm) {
		this.setGameModality(gm);
	}

	//

	// TODO FIRE EVENTS

	@Override
	public void fireLifeHealingReceived(GModality gm, int originalHealing) {
		if (gm == null || (!(gm instanceof GModalityET)))
			return;
		GModalityET gmet;
		GEventInterfaceTRAr gei;
		if (gm == null || (!(gm instanceof GModalityET)))
			return;
		gmet = (GModalityET) gm;
		gei = (GEventInterfaceTRAr) gmet.getEventInterface();
//		gei.fire .... TODO
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

	@Override
	public void move(long milliseconds) {
		// TODO Auto-generated method stub

	}

	@Override
	public AbstractShape2D getAbstractShape() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAbstractShape(AbstractShape2D shape) {
		// TODO Auto-generated method stub

	}
}