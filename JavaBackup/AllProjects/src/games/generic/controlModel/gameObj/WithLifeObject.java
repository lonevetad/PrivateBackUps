package games.generic.controlModel.gameObj;

import games.generic.controlModel.GameModality;

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
	 * expressed by {@link GameModality}, which could be used to fire events.
	 */
	public void receiveDamage(int damage, GameModality gm);

	/**
	 * Make this object receiving a non-negative amount of damage, in a context
	 * expressed by {@link GameModality}, which could be used to fire events.
	 */
	public void receiveHealing(int healingAmount, GameModality gm);

	/**
	 * Similar to {@link #notifyDestruction(GameModality)}, upon receiving damage
	 * (that means: "during the {@link #receiveDamage(int, GameModality)} call")
	 * this event should be fired, in case of complex games, to notify all objects
	 * that "responds to a damage-received event" that this kind of event has
	 * occurred
	 */
	public void notifyDamageReceived(int originalDamage, int actualDamageReceived, GameModality gm);

	/**
	 * Similar to {@link #notifyDamageReceived(int, int, GameModality)}, but about
	 * healing.
	 */
	public void notifyHealingReceived(int originalHealing, int actualHealingReceived, GameModality gm);

	//

	//

	@Override
	public default boolean shouldBeDestroyed() {
		return this.getLife() <= 0;
	}
}