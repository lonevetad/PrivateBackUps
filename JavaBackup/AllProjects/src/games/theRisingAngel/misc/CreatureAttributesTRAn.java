package games.theRisingAngel.misc;

import games.generic.controlModel.subimpl.CreatureAttributesCaching;

public class CreatureAttributesTRAn extends CreatureAttributesCaching {

	public CreatureAttributesTRAn() {
		super(AttributesTRAn.VALUES.length);
		super.setBonusCalculator(new CreatureAttributesBonusesCalculatorTRAr());
	}

}