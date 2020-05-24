package games.theRisingAngel.misc;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.CurrencySet;
import games.theRisingAngel.GModalityTRAn;
import games.theRisingAngel.events.GEventInterfaceTRAn;

public class CurrencySetTRAn extends CurrencySet {

	public CurrencySetTRAn(GModality gm, int typesAmount) { super(gm, typesAmount); }

	@Override
	public void fireCurrencyChangeEvent(GModality gm, int indexType, int oldValue, int newValue) {
		GModalityTRAn gmt;
		GEventInterfaceTRAn gei;
		gmt = (GModalityTRAn) gm;
		gei = (GEventInterfaceTRAn) gmt.getEventInterface();
		gei.fireMoneyChangeEvent(gmt, indexType, oldValue, newValue);
	}
}