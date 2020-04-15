package games.theRisingAngel.misc;

import games.generic.controlModel.subimpl.CreatureAttributesCaching;
import games.theRisingAngel.AttributesTRAr;

public class CreatureAttributesTRAr extends CreatureAttributesCaching {

	public CreatureAttributesTRAr() {
		super(AttributesTRAr.VALUES.length);
		super.setBonusCalculator(new CreatureAttributesBonusesCalculatorTRAr());
	}

}