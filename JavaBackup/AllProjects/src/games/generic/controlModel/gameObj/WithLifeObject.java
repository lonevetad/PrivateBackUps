package games.generic.controlModel.gameObj;

import games.generic.controlModel.GModality;

public interface WithLifeObject extends DestructibleObject {

	public int getLife();

	/** Raw setting, like assigning a variable's value. */
	public void setLife(int life);

	public int getLifeMax();

	/**
	 * Raw setting, like assigning a variable's value, like {@link #setLife(int).
	 * Could invoke this last method if the actual life (returned by
	 * {@link #getLife()} is greater than the given parameter).
	 */
	public void setLifeMax(int lifeMax);

	/**
	 * Make this object receiving a non-negative amount of damage, in a context
	 * expressed by {@link GModality}, which could be used to fire events.
	 */
	public void receiveDamage(GModality gm, int damage);

	/**
	 * Make this object receiving a non-negative amount of damage, in a context
	 * expressed by {@link GModality}, which could be used to fire events.
	 */
	public void receiveHealing(GModality gm, int healingAmount);

	/**
	 * Similar to {@link #fireDestructionEvent(GModality)}, upon receiving damage (that
	 * means: "during the {@link #receiveDamage(GModality, int)} call") this event
	 * should be fired, in case of complex games, to notify all objects that
	 * "responds to a damage-received event" that this kind of event has
	 * occurred.<br>
	 * A reply/reaction to the "raw damage received" could be a damage reduction.
	 */
	public void fireDamageReceived(GModality gm, int originalDamage); // , int actualDamageReceived);

	/**
	 * Similar to {@link #fireDamageReceived( GModality, int, int)}, but about
	 * healing.
	 */
	public void fireHealingReceived(GModality gm, int originalHealing); // , int actualHealingReceived);

	//

	//

	@Override
	public default boolean shouldBeDestroyed() {
		return this.getLife() <= 0;
	}
}