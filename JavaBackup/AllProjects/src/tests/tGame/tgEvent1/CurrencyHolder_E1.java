package tests.tGame.tgEvent1;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.CurrencySet;

public class CurrencyHolder_E1 extends CurrencySet {

	public CurrencyHolder_E1(GModality gm, int typesAmount) {
		super(gm, typesAmount);
	}

	@Override
	public void fireMoneyChangeEvent(GModality gm, int indexType, int oldValue, int newValue) {
		GModality_E1 gme;
		GEventInterface_E1 gei;
		gme = (GModality_E1) gm;
		gei = (GEventInterface_E1) gme.getEventInterface();
		gei.fireMoneyChangeEvent(gm, indexType, oldValue, newValue);
	}
}