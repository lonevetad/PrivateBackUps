package games.theRisingAngel;

import java.util.ArrayList;

import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.player.BasePlayerRPG;
import games.generic.controlModel.player.ExperienceLevelHolderImpl;
import games.generic.controlModel.subImpl.GModalityET;
import games.generic.controlModel.subImpl.GModalityRPG;
import games.theRisingAngel.creatures.BaseCreatureTRAr;
import games.theRisingAngel.events.EventsTRAr;
import games.theRisingAngel.events.GEventInterfaceTRAr;
import games.theRisingAngel.inventory.EquipmentSetTRAr;

public class PlayerTRAr extends BaseCreatureTRAr implements BasePlayerRPG {
	private static final long serialVersionUID = -3336623605789L;

	public PlayerTRAr(GModalityRPG gameModality) {
		super(gameModality, "No name currently provided");
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(EventsTRAr.Destroyed.getName());
		this.setEquipmentSet(new EquipmentSetTRAr());
	}

	//

	//

	// TODO other methods

	@Override
	public boolean destroy() {
		if (!isDestroyed) {
			isDestroyed = true;
			super.destroy();
			return true;
		}
		return false;
	}

//	public void act(GModality modality, long milliseconds) { super.act(modality, milliseconds); }

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
	public boolean isDestructionEvent(IGEvent maybeDestructionEvent) {
		return maybeDestructionEvent.getName() == EventsTRAr.Destroyed.getName();
	}

	@Override
	public void move(long milliseconds) {
		// TODO Auto-generated method stub

	}

	@Override
	public ExperienceLevelHolderImpl getExpLevelHolder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setExpLevelHolder(ExperienceLevelHolderImpl expLevelHolder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLeavingMap() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getExperienceNow() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ExperienceLevelHolderImpl reset() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void recalculateExpToLevelUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public CurrencySet getCurrencies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCurrencies(CurrencySet currencies) {
		// TODO Auto-generated method stub

	}

}