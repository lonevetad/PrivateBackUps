package games.generic.controlModel.misc;

import games.generic.ObjectNamedID;

/**
 * Describes a generic type of damage (physical, fire, poison, ranged, critical,
 * dark, holy, cold, lightning, etc...), making event deliberation general
 * (lighter in terms of memory but a bit heavier in terms of execution time).
 * Use {@link #getDamageType()} to distinguish the type.
 */
public class DamageGeneric {

	protected int damageAmount;
	protected ObjectNamedID damageType;

	public DamageGeneric(int damageAmount, ObjectNamedID damageType) {
		super();
		this.damageAmount = damageAmount;
		this.damageType = damageType;
	}

	public int getDamageAmount() {
		return damageAmount;
	}

	public ObjectNamedID getDamageType() {
		return damageType;
	}

	public void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}

	public void setDamageType(ObjectNamedID damageType) {
		this.damageType = damageType;
	}

	@Override
	public String toString() {
		return "DamageGeneric [damageAmount=" + damageAmount + ", damageType=" + damageType + "]";
	}
}