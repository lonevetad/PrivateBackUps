package games.generic.controlModel.heal.resExample;

import tools.ObjectNamedID;

public interface ShieldHavingObject extends ObjectNamedID {

	public int getShield();

	public int getShieldMax();

	/**
	 * Raw setting, like assigning a variable's value.
	 */
	public void setShield(int shield);

	/**
	 * Raw setting, like assigning a variable's value, like {@link #setShield(int).
	 * Could invoke this last method if the actual shield (returned by
	 * {@link #getShield()} is greater than the given parameter).
	 */
	public void setShieldMax(int shieldMax);

	/**
	 * Shorthand to get the shield regeneration.<br>
	 * Could be intended as "amount per second".
	 */
	public int getShieldRegenation();

	/** See {@link #getShieldRegenation()}. */
	public void setShieldRegenation(int shieldRegenation);

}