package games.theRisingAngel;

import java.util.ArrayList;

import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gObj.ExperienceLevelHolder;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.player.BasePlayerRPG;
import games.generic.controlModel.player.ExperienceLevelHolderImpl;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.creatures.BaseCreatureTRAr;
import games.theRisingAngel.events.EventsTRAr;
import games.theRisingAngel.inventory.EquipmentSetTRAr;

public class PlayerTRAr extends BaseCreatureTRAr implements BasePlayerRPG {
	private static final long serialVersionUID = -3336623605789L;

	public PlayerTRAr(GModalityRPG gameModality) {
		super(gameModality, "No name currently provided");
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(EventsTRAr.Destroyed.getName());
		this.experienceLevelHolder = new ExperienceLevelHolderImpl();
		this.setEquipmentSet(new EquipmentSetTRAr());
	}

	protected CurrencySet currencies;
	protected ExperienceLevelHolder experienceLevelHolder;

	@Override
	public ExperienceLevelHolder getExpLevelHolder() {
		return experienceLevelHolder;
	}

	@Override
	public int getExperienceNow() {
		return experienceLevelHolder.getExperienceNow();
	}

	@Override
	public CurrencySet getCurrencies() {
		return currencies;
	}

	//

	@Override
	public void setCurrencies(CurrencySet currencies) {
		this.currencies = currencies;
	}

	@Override
	public void setExpLevelHolder(ExperienceLevelHolder expLevelHolder) {
		this.experienceLevelHolder = expLevelHolder;
	}

	//

	//

	// TODO other methods

	@Override
	public ExperienceLevelHolder reset() {
		experienceLevelHolder.reset();
		return this;
	}

	@Override
	public void recalculateExpToLevelUp() {
		this.experienceLevelHolder.recalculateExpToLevelUp();
	}

//	public void act(GModality modality, int milliseconds) { super.act(modality, milliseconds); }

	@Override
	public void onEnteringInGame(GModality gm) {
		this.setGameModality(gm);
	}

	@Override
	public void onLeavingMap() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDestructionEvent(IGEvent maybeDestructionEvent) {
		return maybeDestructionEvent.getName() == EventsTRAr.Destroyed.getName();
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		super.notifyEvent(modality, ge); // destructible-object
// TODO altro?
	}

	@Override
	public boolean destroy() {
		if (!isDestroyed) {
			isDestroyed = true;
			super.destroy();
			return true;
		}
		return false;
	}

	//
	//

	// TODO FIRE EVENTS

//	public <SourceHealing> void fireLifeHealingReceived(GModality gm, int originalHealing, SourceHealing source) {
//		if (gm == null || (!(gm instanceof GModalityET)))
//			return;
////		GModalityET gmet;
//		GEventInterfaceTRAr gei;
//		if (gm == null || (!(gm instanceof GModalityET)))
//			return;
////		gmet = (GModalityET) gm;
////		gei = (GEventInterfaceTRAr) gmet.getEventInterface();
//		gei = (GEventInterfaceTRAr) gm.getGameObjectsManager().getGEventInterface();
//		gei.fireHealReceivedEvent((GModalityET) gm, source, this,
//				new HealGeneric(originalHealing, HealingTypeExample.Life));
//	}

}