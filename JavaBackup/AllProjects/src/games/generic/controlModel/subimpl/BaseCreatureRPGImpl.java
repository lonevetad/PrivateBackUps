package games.generic.controlModel.subimpl;

import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.inventoryAbil.EquipmentSet;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.misc.GObjMovement;
import games.theRisingAngel.AttributesTRAr;
import games.theRisingAngel.misc.CreatureUIDProvider;
import geometry.AbstractShape2D;
import tools.ObjectWithID;

/**
 * Defines a default (but not mandatory) implementation of a "creature" concept,
 * especially useful for "Rule Play Game" (RPG). <br>
 * A pointer to {@link EquipmentSet} is provided both to define a base for the
 * "player" concept and, maybe, to help spawning "variable" enemies and dropping
 * items (or just <i>define</i> those enemies, recycling equipments's attributes
 * to define their attributes).
 * <p>
 * Useful classes/interfaces used here:
 * <ul>
 * <li>{@link }></li>
 * </ul>
 */
public abstract class BaseCreatureRPGImpl implements BaseCreatureRPG {
	private static final long serialVersionUID = 1L;

	protected boolean isDestroyed;
	protected int life, ticks;
	protected int accumulatedTimeLifeRegen;
	protected Integer ID;
	protected String name;
	protected List<String> eventsWatching;
	protected EquipmentSet equipmentSet;
	protected CreatureAttributes attributes;
	protected AbstractShape2D shape;
	protected GObjMovement movementImplementation;

	protected GModalityRPG gModalityRPG;

	public BaseCreatureRPGImpl(GModalityRPG gModRPG, String name) {
		super();
		this.gModalityRPG = gModRPG;
		this.name = name;
		initializeID();
		this.attributes = newAttributes();
	}

	protected void initializeID() {
		this.ID = CreatureUIDProvider.newID();
	}

	protected CreatureAttributes newAttributes(int attributesAmount) {
		return new CreatureAttributesModsCaching(attributesAmount);
	}

	protected abstract CreatureAttributes newAttributes();

	//

	@Override
	public GModality getGameModality() {
		return gModalityRPG;
	}

	public GModalityRPG getgModalityRPG() {
		return gModalityRPG;
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
	public List<String> getEventsWatching() {
		return eventsWatching;
	}

	@Override
	public CreatureAttributes getAttributes() {
		return attributes;
	}

	@Override
	public AbstractShape2D getShape() {
		return shape;
	}

	@Override
	public EquipmentSet getEquipmentSet() {
		return equipmentSet;
	}

	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}

	@Override
	public int getLife() {
		return life;
	}

	@Override
	public int getLifeMax() {
		return this.getAttributes().getValue(AttributesTRAr.LifeMax.getIndex());
	}

	@Override
	public int getLifeRegenation() {
		return this.getAttributes().getValue(AttributesTRAr.RigenLife.getIndex());
	}

	public GObjMovement getMovementImplementation() {
		return movementImplementation;
	}

	@Override
	public int getTicks() {
		return ticks;
	}

	@Override
	public int getAccumulatedTimeLifeRegen() {
		return accumulatedTimeLifeRegen;
	}

	// SETTER

	public void setgModalityRPG(GModalityRPG gModalityRPG) {
		this.gModalityRPG = gModalityRPG;
	}

	@Override
	public void setGameModality(GModality gameModality) {
		this.gModalityRPG = (GModalityRPG) gameModality;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setEquipmentSet(EquipmentSet equips) {
		this.equipmentSet = equips;
		if (equips != null) {
			equips.setCreatureWearingEquipments(this);
		}
	}

	public void setEventsWatching(List<String> eventsWatching) {
		this.eventsWatching = eventsWatching;
	}

	@Override
	public void setAttributes(CreatureAttributes attributes) {
		this.attributes = attributes;
	}

	@Override
	public void setShape(AbstractShape2D shape) {
		this.shape = shape;
	}

	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	@Override
	public void setLife(int life) {
//	public void setLife(int life) {this.life = life; }
		if (life <= 0)
			this.life = 0;
		else {
			if (life > getLifeMax())
				life = getLifeMax();
			this.life = life;
		}
	}

	public void setMovementImplementation(GObjMovement movementImplementation) {
		this.movementImplementation = movementImplementation;
	}

	@Override
	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	@Override
	public void setAccumulatedTimeLifeRegen(int accumulatedTimeLifeRegen) {
		this.accumulatedTimeLifeRegen = accumulatedTimeLifeRegen;
	}

	/*
	 * public void setLifeMax(int lifeMax) { if (lifeMax > 0) {
	 * this.getAttributes().setOriginalValue(AttributesTRAr.LifeMax.getIndex(),
	 * lifeMax); if (this.getLife() > lifeMax) this.setLife(lifeMax); } }
	 */

	/*
	 * public void setLifeRegenation(int lifeRegenation) { if (lifeRegenation > 0) {
	 * this.getAttributes().setOriginalValue(AttributesTRAr.RigenLife.getIndex(),
	 * lifeRegenation); } }
	 */

	//

	//

	// TODO VERY USEFULL STUFFS

	//

	//

	@Override
	public void move(GModality modality, int timeUnits) {
		movementImplementation.act(modality, timeUnits);
	}

	@Override
	public void fireLifeHealingReceived(GModality gm, int originalHealing) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean destroy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDestructionEvent(IGEvent maybeDestructionEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void fireDestructionEvent(GModality gm) {
		// TODO Auto-generated method stub

	}

	public void equip(EquipmentItem equipment) {
		EquipmentSet es;
		es = this.getEquipmentSet();
		if (es != null) {
			es.addEquipmentItem(getGameModality(), equipment);
		}
	}

	@Override
	public void receiveDamage(GModality gm, DamageGeneric originalDamage, ObjectWithID source) {
		int dr;
		GModalityRPG gmrpg;
		if (originalDamage.getDamageAmount() <= 0)
			return;
		gmrpg = (GModalityRPG) gm;
		// check the type
		gmrpg.getGameObjectsManager().dealsDamageTo(source, this, originalDamage);
		dr = this.getAttributes().getValue(AttributesTRAr.DamageReductionPhysical.getIndex());
		if (dr < 0)
			dr = 0;
		setLife(getLife() - (originalDamage.getDamageAmount() - dr));
		fireDamageReceived(gm, originalDamage, source);
	}

	//

	// TODO FIRE EVENTS

	@Override
	public void fireDamageReceived(GModality gm, DamageGeneric originalDamage, ObjectWithID source) {
		GModalityRPG gmodrpg;
		GEventInterfaceRPG geie1;
//		GameObjectsManager gom;
		if (gm == null || (!(gm instanceof GModalityRPG)))
			return;
		gmodrpg = (GModalityRPG) gm;
//		gom = gmodrpg.getGameObjectsManagerDelegated();
		geie1 = (GEventInterfaceRPG) gmodrpg.getEventInterface();
		geie1.fireDamageReceivedEvent(gmodrpg, source, this, originalDamage);
//		gom.dealsDamageTo(source, this, originalDamage);// cannot "deals" damage because it's already dealt
	}
}