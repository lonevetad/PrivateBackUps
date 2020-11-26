package games.generic.controlModel.heal.resExample;

import tools.ObjectNamedID;

public interface ManaHavingObject extends ObjectNamedID {

	public int getMana();

	public int getManaMax();

	/**
	 * Raw setting, like assigning a variable's value.
	 */
	public void setMana(int mana);

	/**
	 * Raw setting, like assigning a variable's value, like {@link #setMana(int).
	 * Could invoke this last method if the actual mana (returned by
	 * {@link #getMana()} is greater than the given parameter).
	 */
	public void setManaMax(int manaMax);

	/**
	 * Shorthand to get the mana regeneration.<br>
	 * Could be intended as "amount per second".
	 */
	public int getManaRegenation();

	/** See {@link #getManaRegenation()}. */
	public void setManaRegenation(int manaRegenation);

	public default void spendMana(int amount) {
		amount = getMana() - amount;
		setMana(amount > 0 ? amount : 0);
	}
}