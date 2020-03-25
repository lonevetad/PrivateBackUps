package games.generic.controlModel.player;

import games.generic.controlModel.GEvent;
import games.generic.controlModel.GModality;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.inventory.EquipmentSet;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.subImpl.CreatureAttributesModsCaching;
import games.theRisingAngel.AttributesTRAr;
import games.theRisingAngel.CreatureUIDProvider;
import games.theRisingAngel.EquipmentSetTRAr;

public abstract class PlayerRPG extends PlayerInGame_Generic implements CreatureOfRPGs {
	protected boolean isDestroyed;
	protected EquipmentSet equipmentSet;
	protected CreatureAttributes attributes;

	public PlayerRPG(GModality gameModality) {
		super(gameModality);
		this.isDestroyed = false;
		this.ID = CreatureUIDProvider.newID();
		this.equipmentSet = new EquipmentSetTRAr();
		this.attributes = new CreatureAttributesModsCaching(AttributesTRAr.VALUES.length);
	}

	@Override
	protected void initializeID() {
		this.ID = CreatureUIDProvider.newID();
	}

	@Override
	public EquipmentSet getEquipmentSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEquipmentSet(EquipmentSet equips) {
		// TODO Auto-generated method stub

	}

	@Override
	public CreatureAttributes getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttributes(CreatureAttributes attributes) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLife() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLife(int life) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLifeMax() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLifeMax(int lifeMax) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveDamage(GModality gm, int damage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveHealing(GModality gm, int healingAmount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireDamageReceived(GModality gm, int originalDamage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireHealingReceived(GModality gm, int originalHealing) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDestroyed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void fireDestruction(GModality gm) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean destroy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void act(GModality modality, long milliseconds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyEvent(GModality modality, GEvent ge) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLeavingMap() {
		// TODO Auto-generated method stub

	}

}
