package games.theRisingAngel.misc;

import games.generic.controlModel.subimpl.CreatureAttributesCaching;

public class CreatureAttributesTRAr extends CreatureAttributesCaching {

	public CreatureAttributesTRAr() {
		super(AttributesTRAn.VALUES.length);
		super.setBonusCalculator(new CreatureAttributesBonusesCalculatorTRAr());
	}

}