package games.generic.controlModel.gEvents;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.DamageDealerGeneric;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.misc.DamageTypeGeneric;
import tools.ObjectNamedID;

/**
 * Marker interface for an entity capable of receiving damage, analog and
 * "opposite" to {@link DamageDealerGeneric}.<br>
 * It's enriched with info useful for damage computation.
 */
public interface DamageReceiverGeneric extends ObjectNamedID {

	public int getLife();

	public int getLifeMax();

	/**
	 * Raw setting, like assigning a variable's value.
	 */
	public void setLife(int life);

	/**
	 * Raw setting, like assigning a variable's value, like {@link #setLife(int).
	 * Could invoke this last method if the actual life (returned by
	 * {@link #getLife()} is greater than the given parameter).
	 */
	public void setLifeMax(int lifeMax);

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
}