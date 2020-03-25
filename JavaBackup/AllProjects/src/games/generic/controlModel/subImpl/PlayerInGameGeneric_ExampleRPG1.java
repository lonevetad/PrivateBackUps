package games.generic.controlModel.subImpl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.inventory.EquipmentSet;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CurrencyHolder;
import games.generic.controlModel.player.PlayerIG_WithExperience;
import games.theRisingAngel.AttributesTRAr;
import games.theRisingAngel.CreatureUIDProvider;
import games.theRisingAngel.inventory.EquipmentSetTRAr;

/** Designed for Role Play Game. */
public abstract class PlayerInGameGeneric_ExampleRPG1 extends PlayerIG_WithExperience implements CreatureOfRPGs {
//	private static final long serialVersionUID = -777564684007L;

//	GModality gameModality;
	protected boolean isDestroyed;
	protected EquipmentSet equipmentSet;
	protected CreatureAttributes attributes;
	protected CurrencyHolder moneys;

	public PlayerInGameGeneric_ExampleRPG1(GModality gameModality) {
		super(gameModality);
		this.isDestroyed = false;
//		this.ID = CreatureUIDProvider.newID();
		this.equipmentSet = new EquipmentSetTRAr();
		this.attributes = new CreatureAttributesModsCaching(AttributesTRAr.VALUES.length);
	}

	@Override
	protected void initializeID() {
		this.ID = CreatureUIDProvider.newID();
	}

	//

	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}

	public CurrencyHolder getMoneys() {
		return moneys;
	}

	@Override
	public EquipmentSet getEquipmentSet() {
		return equipmentSet;
	}

	@Override
	public CreatureAttributes getAttributes() {
		return attributes;
	}

	//

	//

	@Override
	public void setEquipmentSet(EquipmentSet equips) {
		this.equipmentSet = equips;
	}

	@Override
	public void setAttributes(CreatureAttributes attributes) {
		this.attributes = attributes;
	}

	public void setMoneys(CurrencyHolder moneys) {
		this.moneys = moneys;
	}

	//

	//

	/**
	 * Override designed. <br>
	 * When the game actually starts and the player "drops into the game", some
	 * actions could be performed.
	 */
	public abstract void onStartingGame(GModality mg);

	//

	// TODO EVENTS FIRING

}