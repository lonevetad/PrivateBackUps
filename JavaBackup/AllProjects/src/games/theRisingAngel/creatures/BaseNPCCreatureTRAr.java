package games.theRisingAngel.creatures;

import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.inventory.EquipmentSet;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.player.PlayerInGame_Generic;
import games.generic.controlModel.subImpl.CreatureAttributesModsCaching;
import games.theRisingAngel.AttributesTRAr;
import games.theRisingAngel.CreatureUIDProvider;
import games.theRisingAngel.inventory.EquipmentSetTRAr;

/**
 * This is NOT a {@link PlayerInGame_Generic}, even if it's similar (but there's
 * no multiple inheritance, so ... interfaces and redundancy).
 */
public class BaseNPCCreatureTRAr implements CreatureOfRPGs {
	protected boolean isDestroyed;
	protected Integer ID;
	protected String name;
	protected List<String> eventsWatching;
	protected EquipmentSet equipmentSet;
	protected CreatureAttributes attributes;
	protected GModality gameModality;

	public BaseNPCCreatureTRAr() {
		this.isDestroyed = false;
		this.ID = CreatureUIDProvider.newID();
		this.equipmentSet = new EquipmentSetTRAr();
		this.attributes = new CreatureAttributesModsCaching(AttributesTRAr.VALUES.length);
	}

	@Override
	public boolean isDestroyed() {
		return false;
	}

	@Override
	public Integer getID() {
		return ID;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getLifeMax() {
		return 0;
	}

	@Override
	public int getLife() {

		return 0;
	}

	@Override
	public CreatureAttributes getAttributes() {
		return attributes;
	}

	@Override
	public EquipmentSet getEquipmentSet() {
		return equipmentSet;
	}

	@Override
	public GModality getGameModality() {
		return gameModality;
	}

	//

	@Override
	public void setGameModality(GModality gameModality) {
		this.gameModality = gameModality;
	}

	@Override
	public void setEquipmentSet(EquipmentSet equips) {
		this.equipmentSet = equips;
	}

	@Override
	public void setAttributes(CreatureAttributes attributes) {
		this.attributes = attributes;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setLife(int life) {

	}

	@Override
	public void setLifeMax(int lifeMax) {

	}

	//

	@Override
	public void receiveDamage(GModality gm, int damage) {

	}

	@Override
	public void receiveHealing(GModality gm, int healingAmount) {

	}

	@Override
	public void fireDamageReceived(GModality gm, int originalDamage) {

	}

	@Override
	public void fireHealingReceived(GModality gm, int originalHealing) {

	}

	@Override
	public void fireDestructionEvent(GModality gm) {

	}

	@Override
	public boolean destroy() {

		return false;
	}

	@Override
	public void act(GModality modality, long milliseconds) {
		// TODO make progress ALL abilities .. or maybe not, jus hardcoded abilities
	}

	@Override
	public boolean isDestructionEvent(IGEvent maybeDestructionEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getEventsWatching() {
		return eventsWatching;
	}
}