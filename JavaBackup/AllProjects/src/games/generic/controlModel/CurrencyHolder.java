package games.generic.controlModel;

public abstract class CurrencyHolder {

	int[] values;
	GameModality gm;

	/** The "typeAmount" parameter could be an enumeration's size. */
	public CurrencyHolder(GameModality gm, int typesAmount) {
		this.gm = gm;
		this.values = new int[typesAmount];
	}

	//

	public GameModality getGm() {
		return gm;
	}

	public int getMoneyAmount(int indexType) {
		return this.values[indexType];
	}

	//

	public void setGm(GameModality gm) {
		this.gm = gm;
	}

	public void setMoneyAmount(int indexType, int newAmount) {
		int old;
		old = this.values[indexType];
		this.values[indexType] = newAmount;
		fireMoneyChangeEvent(indexType, old, newAmount, this.gm);
	}

	//

	public abstract void fireMoneyChangeEvent(int indexType, int oldValue, int newValue, GameModality gm);
}