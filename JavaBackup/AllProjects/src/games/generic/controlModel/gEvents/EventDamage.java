package games.generic.controlModel.gEvents;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gObj.DamageDealerGeneric;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.inventoryAbil.AbilityGeneric;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AbilityModifyingAttributesRealTime;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AbilityVanishingOverTime;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.misc.DamageTypeGeneric;

public class EventDamage extends EventInfo_SourceToTarget<DamageDealerGeneric, LivingObject> {
	private static final long serialVersionUID = 1L;

	public EventDamage(IGEvent eventIdentifier, DamageDealerGeneric source, LivingObject target, DamageGeneric damage) {
		this(eventIdentifier, source, target, damage, damage.getDamageAmount());
	}

	public EventDamage(IGEvent eventIdentifier, DamageDealerGeneric source, LivingObject target, DamageGeneric damage,
			int damageReductedByArmour) {
		super(eventIdentifier, source, target);
		this.damage = damage;
		this.damageAmountToBeApplied = this.damageReducedByTargetArmors = damageReductedByArmour;
	}

	protected DamageGeneric damage;
	protected int damageAmountToBeApplied, damageReducedByTargetArmors;

	/**
	 * DO NOT USE IT, LEFT FOR SHORTCUTS.
	 * <p>
	 * Returns the original damage with related informations. See
	 * {@link DamageGeneric}.
	 */
	public DamageGeneric getDamage() { return damage; }

	/**
	 * Proxy for calling <code>{@link #getDamage()}.getDamageType()</code>.
	 */
	public DamageTypeGeneric getDamageType() { return damage.getDamageType(); }

	/**
	 * Proxy for calling <code>{@link #getDamage()}.getDamageAmount()</code>.
	 */
	public int getDamageAmountOriginal() { return damage.getDamageAmount(); }

	/**
	 * The amount of damage that will be dealt to the target, computed considering
	 * as a starting valuethe value returned by
	 * {@link #getDamageReducedByTargetArmors()} (which is calculated applying some
	 * kind of "armor/reduction" to {@link #getDamageAmountOriginal()}, see that
	 * first method's documentation for further informations ) and also considering
	 * the effects of attributes ({@link CreatureAttributes}), equipments
	 * ({@link EquipmentItem}) or abilities' effects ({@link AbilityGeneric}, like
	 * {@link AbilityModifyingAttributesRealTime} and/or
	 * {@link AbilityVanishingOverTime}).
	 * <p>
	 * Each ability (in general, an {@link GEventObserver} able to react to damage,
	 * i.e.: this event) should modify this value.
	 */
	public int getDamageAmountToBeApplied() { return damageAmountToBeApplied; }

	/**
	 * Upon receiving some damage, a {@link LivingObject} may apply some reductions
	 * or some malus, maybe based on some kind of "armor/reduction", which could be
	 * calculated considering that object's attributes ({@link CreatureAttributes}).
	 */
	public int getDamageReducedByTargetArmors() { return damageReducedByTargetArmors; }

	public void setDamage(DamageGeneric damage) { this.damage = damage; }

	/** See {@link #getDamageAmountToBeApplied()}. */
	public void setDamageAmountToBeApplied(int damageAmountToBeApplied) {
		this.damageAmountToBeApplied = damageAmountToBeApplied;
	}

	@Override
	public boolean isRequirigImmediateProcessing() { return true; }

	public boolean isSource(DamageDealerGeneric ddg) { return super.source == ddg; }

	public boolean isTarget(LivingObject ddg) { return super.target == ddg; }

	@Override
	public String toString() {
		return "EventDamage [damage=" + damage + ", damageAmountToBeApplied=" + damageAmountToBeApplied + "]";
	}
}