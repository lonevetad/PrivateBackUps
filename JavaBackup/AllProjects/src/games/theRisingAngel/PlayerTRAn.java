package games.theRisingAngel;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gObj.ExperienceLevelHolder;
import games.generic.controlModel.inventoryAbil.AbilityGeneric;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.player.BasePlayerRPG;
import games.generic.controlModel.player.ExperienceLevelHolderImpl;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.creatures.BaseCreatureTRAn;
import games.theRisingAngel.events.EventsTRAn;
import games.theRisingAngel.inventory.EquipmentSetTRAn;
import games.theRisingAngel.misc.PlayerCharacterTypesHolder.PlayerCharacterTypes;

public class PlayerTRAn extends BaseCreatureTRAn implements BasePlayerRPG {
	private static final long serialVersionUID = -3336623605789L;

	public PlayerTRAn(GModalityRPG gameModality, PlayerCharacterTypes characterType) {
		super(gameModality, "No name currently provided");
		this.characterType = characterType;
		this.attributePointsLeftToApply = 0;
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(EventsTRAn.Destroyed.getName());
		this.experienceLevelHolder = new ExperienceLevelHolderImpl();
		this.setEquipmentSet(new EquipmentSetTRAn());
	}

	protected int attributePointsLeftToApply;
	protected final PlayerCharacterTypes characterType;
	protected CurrencySet currencies;
	protected ExperienceLevelHolder experienceLevelHolder;

	public PlayerCharacterTypes getCharacterType() { return characterType; }

	public ExperienceLevelHolder getExperienceLevelHolder() { return experienceLevelHolder; }

	@Override
	public ExperienceLevelHolder getExpLevelHolder() { return experienceLevelHolder; }

	@Override
	public int getExperienceNow() { return experienceLevelHolder.getExperienceNow(); }

	@Override
	public CurrencySet getCurrencies() { return currencies; }

	@Override
	public int getAttributePointsLeftToApply() { return attributePointsLeftToApply; }

	//

	@Override
	public void setCurrencies(CurrencySet currencies) {
		this.currencies = currencies;
		if (currencies != null) { currencies.setCanFireCurrencyChangeEvent(true); }
	}

	@Override
	public void setExpLevelHolder(ExperienceLevelHolder expLevelHolder) { this.experienceLevelHolder = expLevelHolder; }

	@Override
	public void setAttributePointsLeftToApply(int attributePointsLeftToApply) {
		this.attributePointsLeftToApply = attributePointsLeftToApply;
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
	public void recalculateExpToLevelUp() { this.experienceLevelHolder.recalculateExpToLevelUp(); }

//	public void act(GModality modality, int milliseconds) { super.act(modality, milliseconds); }

	@Override
	public void onAddedToGame(GModality gm) {
		BiConsumer<String, AbilityGeneric> abilityAdderToGModality;
		super.onAddedToGame(gm);
		// Add equips and abilities on GMod
		abilityAdderToGModality = (n, ab) -> ab.onAddedToGame(gm);
		this.getAbilities().forEach(abilityAdderToGModality);
		this.getEquipmentSet().forEachEquipment((e, i) -> { if (e != null) { e.onAddedToGame(gm); } });
	}

	@Override
	public void onRemovedFromGame(GModality gm) {
		BiConsumer<String, AbilityGeneric> abilityAdderToGModality;
		super.onRemovedFromGame(gm);
		// Add equips and abilities on GMod
		abilityAdderToGModality = (n, ab) -> ab.onRemovedFromGame(gm);
		this.getAbilities().forEach(abilityAdderToGModality);
		this.getEquipmentSet().forEachEquipment((e, i) -> { if (e != null) { e.onRemovedFromGame(gm); } });

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