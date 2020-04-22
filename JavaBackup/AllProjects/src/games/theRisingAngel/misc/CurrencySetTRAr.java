package games.theRisingAngel.misc;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.CurrencySet;
import games.theRisingAngel.GModalityTRAr;
import games.theRisingAngel.events.GEventInterfaceTRAr;

public class CurrencySetTRAr extends CurrencySet {

	public CurrencySetTRAr(GModality gm, int typesAmount) {
		super(gm, typesAmount);
	}

	@Override
	public void fireMoneyChangeEvent(GModality gm, int indexType, int oldValue, int newValue) {
		GModalityTRAr gmt;
		GEventInterfaceTRAr gei;
		gmt = (GModalityTRAr) gm;
		gei = (GEventInterfaceTRAr) gmt.getEventInterface();
		gei.fireMoneyChangeEvent(gmt, indexType, oldValue, newValue);
	}
}