package games.theRisingAngel.misc;

import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.subimpl.CreatureAttributesCaching;

public class CreatureAttributesTRAn extends CreatureAttributesCaching {

	public CreatureAttributesTRAn() {
		super(AttributesTRAn.VALUES.length);
		super.setBonusCalculator(new CreatureAttributesBonusesCalculatorTRAn());
	}

	@Override
	public int getValue(AttributeIdentifier identifier) {
		int v;
		v = super.getValue(identifier);
		return (identifier == AttributesTRAn.Velocity && v <= 0) ? 1 : v; // no less than 1 for velocity
	}
}