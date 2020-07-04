package games.generic.controlModel.misc;

import games.generic.controlModel.GModality;

public abstract class CurrencySet {
	public static final int BASE_CURRENCY_INDEX = 0;

	public static interface CurrencyIndextypeAmountConsumer {
		public void performWithIndextypeAmount(int indexType, int amount);
	}
//	public static enum BehaviourFiringEvent

	//

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
		this.canFireCurrencyChangeEvent = false;
	}

	protected boolean canFireCurrencyChangeEvent;
	protected int[] values;
	protected GModality gameModality;

	//

	public GModality getGameModality() { return gameModality; }

	public int getCurrencyAmount(int indexType) { return this.values[indexType]; }

	public boolean isFiringEvents() { return canFireCurrencyChangeEvent && gameModality != null; }

	public boolean isCanFireCurrencyChangeEvent() { return canFireCurrencyChangeEvent; }

	//

	public void setCanFireCurrencyChangeEvent(boolean canFireCurrencyChangeEvent) {
		this.canFireCurrencyChangeEvent = canFireCurrencyChangeEvent;
	}

	public void setGameModality(GModality gameModality) { this.gameModality = gameModality; }

	public void setGameModaliy(GModality gameModality) { this.gameModality = gameModality; }

	/***/
	public void setCurrencyAmount(int indexType, int newAmount) {
		int old;
		old = this.values[indexType];
		this.values[indexType] = newAmount;
		if (isFiringEvents())
			fireCurrencyChangeEvent(this.gameModality, indexType, old, newAmount);
	}

	//

	//

	/**
	 * Shorthand to
	 * <code>{@link #setCurrencyAmount(int, int)}( indexType, {@link #getCurrencyAmount(int)}(indexType) + delta)</code>
	 */
	public void alterCurrencyAmount(int indexType, int delta) {
		int old, newAmount;
		old = this.values[indexType];
		this.values[indexType] = newAmount = (old + delta);
		if (isFiringEvents())
			fireCurrencyChangeEvent(this.gameModality, indexType, old, newAmount);
	}

	public void forEachTypeAmount(CurrencyIndextypeAmountConsumer citac) {
		int i, n;
		n = this.values.length;
		i = -1;
		while (++i < n)
			citac.performWithIndextypeAmount(i, this.values[i]);
	}

	public abstract void fireCurrencyChangeEvent(GModality gameModality, int indexType, int oldValue, int newValue);

	@Override
	public String toString() {
		final StringBuilder sb;
		sb = new StringBuilder(16);
		sb.append("CurrencySet [");
		forEachTypeAmount((i, a) -> sb.append(" (").append(i).append(':').append(a).append("), "));
		sb.append(']');
		return sb.toString();
	}

}