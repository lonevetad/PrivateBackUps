package games.theRisingAngel;

import java.util.ArrayList;

import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.subImpl.GModalityET;
import games.generic.controlModel.subImpl.PlayerInGameGeneric_ExampleRPG1;
import games.theRisingAngel.creatures.BaseCreatureTRAr;
import games.theRisingAngel.events.EventsTRAr;
import games.theRisingAngel.events.GEventInterfaceTRAr;
import geometry.AbstractShape2D;

public class PlayerTRAr extends PlayerInGameGeneric_ExampleRPG1 implements BaseCreatureTRAr {
	private static final long serialVersionUID = -3336623605789L;

	public PlayerTRAr(GModality gameModality) {
		super(gameModality);
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(EventsTRAr.Destroyed.getName());
	}

	//

	//

	// TODO other methods

	@Override
	public void receiveDamage(GModality gm, int damage) {
	}

	@Override
	public void receiveLifeHealing(GModality gm, int healingAmount) {
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
	public void fireLifeHealingReceived(GModality gm, int originalHealing) {
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

	@Override
	public int getHealingsTicksPerSeconds() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getAccumulatedTimeLifeRegen() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAccumulatedTimeLifeRegen(long newAccumulated) {
		// TODO Auto-generated method stub

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