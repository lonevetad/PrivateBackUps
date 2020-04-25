package games.generic.controlModel.misc;

import games.generic.controlModel.GModality;

public abstract class CurrencySet {
	public static final int BASE_CURRENCY_INDEX = 0;

	int[] values;
	GModality gameModality;

	/**
	 * The {@link GModality} is not mandatory: i's used to compute, in the default
	 * implementation, the value of {@link #isFiringEvents()}:
	 * <code>return gameModality != null</code><br>
	 * The "typeAmount" parameter could be an enumeration's size.
	 */
	public CurrencySet(GModality gameModality, int typesAmount) {
		this.gameModality = gameModality;
		if (typesAmount < 1)
			throw new IllegalArgumentException("Cannot exist no currency types, just don't create me instead!");
		this.values = new int[typesAmount];
	}

	//

	public GModality getGameModality() {
		return gameModality;
	}

	public int getMoneyAmount(int indexType) {
		return this.values[indexType];
	}

	public boolean isFiringEvents() {
		return gameModality != null;
	}

	//

	public void setGameModaliy(GModality gameModality) {
		this.gameModality = gameModality;
	}

	public void setMoneyAmount(int indexType, int newAmount) {
		int old;
		old = this.values[indexType];
		this.values[indexType] = newAmount;
		if (isFiringEvents())
			fireMoneyChangeEvent(this.gameModality, indexType, old, newAmount);
	}

	//

	public abstract void fireMoneyChangeEvent(GModality gameModality, int indexType, int oldValue, int newValue);
}