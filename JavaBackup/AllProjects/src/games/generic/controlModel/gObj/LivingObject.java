package games.generic.controlModel.gObj;

import games.generic.ObjectWithID;
import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.DamageGeneric;

public interface LivingObject extends DestructibleObject {

	public int getLife();

	public int getLifeMax();

	/**
	 * Shorthand to get the life regeneration.<br>
	 * Could be intended as "amount per second.
	 */
	public int getLifeRegenation();

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

	/** See {@link #getLifeRegenation()}. */
	public void setLifeRegenation(int lifeRegenation);

	/**
	 * Make this object receiving a non-negative amount of damage, in a context
	 * expressed by {@link GModality}, which could be used to fire events.
	 */
	public void receiveDamage(GModality gm, DamageGeneric damage, ObjectWithID source);

	/**
	 * Make this object receiving a non-negative amount of damage, in a context
	 * expressed by {@link GModality}, which could be used to fire events.
	 */
	public default void receiveLifeHealing(GModality gm, int healingAmount) {
		if (healingAmount > 0) {
			setLife(getLife() + healingAmount);
			fireLifeHealingReceived(gm, healingAmount);
		}
	}

	/**
	 * Similar to {@link #fireDestructionEvent(GModality)}, upon receiving damage
	 * (that means: "during the {@link #receiveDamage(GModality, int)} call") this
	 * event should be fired, in case of complex games, to notify all objects that
	 * "responds to a damage-received event" that this kind of event has
	 * occurred.<br>
	 * A reply/reaction to the "raw damage received" could be a damage reduction.
	 */
	public void fireDamageReceived(GModality gm, DamageGeneric originalDamage, ObjectWithID source); // , int
	// actualDamageReceived);

	/**
	 * Similar to {@link #fireDamageReceived( GModality, int, int)}, but about
	 * healing.
	 */
	public void fireLifeHealingReceived(GModality gm, int originalHealing); // , int actualHealingReceived);

	//

	//

	@Override
	public default boolean shouldBeDestroyed() {
		return this.getLife() <= 0;
	}
}