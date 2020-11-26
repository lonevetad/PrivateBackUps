package games.generic.controlModel.damage;

import games.generic.controlModel.gObj.CreatureSimple;
import tools.ObjectNamedID;

/**
 * Marker interface for an entity capable of dealing damage, analog and
 * "opposite" to {@link DamageReceiverGeneric}.<br>
 * It's enriched with info useful for damage computation.
 * <p>
 * Could be everything: a {@link CreatureSimple}, a skill, a fireball, a weapon,
 * a bullet, a meteor, an avalanche, a cat, a thunderbolt, an arrow, etc.
 */
public interface DamageDealerGeneric extends ObjectNamedID {
	/**
	 * NOTE: this is NOT a percentage (i.e. "%"), but a "per thousand" probability!!
	 * <p>
	 * Related to
	 * {@link DamageReceiverGeneric#getProbabilityPerThousandAvoid(DamageTypeGeneric)}.
	 * <br>
	 * Gets the probability of this source to deals a damage. (Could be calculated
	 * as the composition [summation] of the caster [an object firing a bullet or
	 * attack] and this particular bullet/skill base value).
	 */
	public int getProbabilityPerThousandHit(DamageTypeGeneric damageType);

	/**
	 * Similar to {@link #getProbabilityPerThousandHit(DamageTypeGeneric)}, it's a
	 * percentage to calculate if a critical strike will occour.
	 */
	public int getProbabilityPerThousandCriticalStrike(DamageTypeGeneric damageType);

	/** Percentage amount of multiplication of the damage. Should be positive. */
	public int getPercentageCriticalStrikeMultiplier(DamageTypeGeneric damageType);

	// TODO altro?
}