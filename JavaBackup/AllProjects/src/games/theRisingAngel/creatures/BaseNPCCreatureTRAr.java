package games.theRisingAngel.creatures;

import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.inventoryAbil.EquipmentSet;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.subImpl.CreatureAttributesModsCaching;
import games.theRisingAngel.AttributesTRAr;
import games.theRisingAngel.CreatureUIDProvider;
import games.theRisingAngel.inventory.EquipmentSetTRAr;
import geometry.AbstractShape2D;

/**
 * This is NOT a {@link PlayerGeneric}, even if it's similar (but there's
 * no multiple inheritance, so ... interfaces and redundancy).
 */
public class BaseNPCCreatureTRAr implements BaseCreatureTRAr {
	private static final long serialVersionUID = 4478514563690L;
	protected boolean isDestroyed;
	protected int life, ticks;
	protected long accumulatedTimeLifeRegen;
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
		this.equipmentSet.setCreatureWearingEquipments(this);
		this.attributes = new CreatureAttributesModsCaching(AttributesTRAr.VALUES.length);
		this.life = 1;
		ticks = 0;
		accumulatedTimeLifeRegen = 0;
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
	public int getLife() {
		return life;
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

	@Override
	public List<String> getEventsWatching() {
		return eventsWatching;
	}

	@Override
	public int getTicks() {
		return ticks;
	}

	@Override
	public long getAccumulatedTimeLifeRegen() {
		return accumulatedTimeLifeRegen;
	}

	//

	@Override
	public void setAccumulatedTimeLifeRegen(long newAccumulated) {
		this.accumulatedTimeLifeRegen = newAccumulated;
	}

	@Override
	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	@Override
	public void setGameModality(GModality gameModality) {
		this.gameModality = gameModality;
	}

	@Override
	public void setLife(int life) {
		if (life >= 0)
			this.life = life;
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

	//

	@Override
	public void receiveLifeHealing(GModality gm, int healingAmount) {

	}

	@Override
	public void fireLifeHealingReceived(GModality gm, int originalHealing) {

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
	public void move(long milliseconds) {
		// TODO Auto-generated method stub

	}

	@Override
	public AbstractShape2D getShape() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setShape(AbstractShape2D shape) {
		// TODO Auto-generated method stub

	}

}