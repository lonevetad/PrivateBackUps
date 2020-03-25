package tests.tGame.tgEvent1;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.CurrencyHolder;

public class CurrencyHolder_E1 extends CurrencyHolder {

	public CurrencyHolder_E1(GModality gm, int typesAmount) {
		super(gm, typesAmount);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void fireMoneyChangeEvent(GModality gm, int indexType, int oldValue, int newValue) {
		// TODO Auto-generated method stub

	}

}
