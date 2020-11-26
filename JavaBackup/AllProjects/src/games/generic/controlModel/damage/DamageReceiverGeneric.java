package games.generic.controlModel.damage;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.GameObjectGeneric;
import games.generic.controlModel.heal.resExample.LifeHavingObject;

/**
 * Marker interface for an entity capable of receiving damage, analog and
 * "opposite" to {@link DamageDealerGeneric}.<br>
 * It's enriched with info useful for damage computation.
 */
public interface DamageReceiverGeneric extends LifeHavingObject, GameObjectGeneric {

	/***
	 * NOTE: this is NOT a percentage (i.e. "%"), but a "per thousand" probability!!
	 * <p>
	 * Related to
	 * {@link DamageDealerGeneric#getProbabilityPerThousandHit(DamageTypeGeneric)}
	 */
	public int getProbabilityPerThousandAvoid(DamageTypeGeneric damageType);

	/**
	 * Make this object receiving a non-negative amount of damage, in a context
	 * expressed by {@link GModality}, which could be used to fire events.
	 */
	public void receiveDamage(GModality gm, DamageGeneric damage, DamageDealerGeneric source);

	/**
	 * Similar to
	 * {@link DamageDealerGeneric#getProbabilityPerThousandHit(DamageTypeGeneric)},
	 * see it for further informations.
	 */
	public int getProbabilityPerThousandAvoidCritical(DamageTypeGeneric damageType);

	/** Percentage amount of multiplication of the damage. Should be positive. */
	public int getPercentageCriticalStrikeReduction(DamageTypeGeneric damageType);
}