package games.theRisingAngel.misc;

import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributesBonusesCalculator;
import games.generic.controlModel.subimpl.CreatureAttributesCaching;

public class CreatureAttributesTRAn extends CreatureAttributesCaching {

	public CreatureAttributesTRAn() {
		super(AttributesTRAn.VALUES.length);
		this.cacheValues = null;
		super.setBonusCalculator(new CreatureAttributesBonusesCalculatorTRAn());
	}

	@Override
	public int getValue(AttributeIdentifier identifier) {
		int v, i;
		CreatureAttributesBonusesCalculator bc;
		i = identifier.getIndex();
		v = super.getOriginalValue(i) + super.attributesModificationsApplied[i];
		if ((bc = this.bonusCalculator) != null) { v += bc.getBonusFor(i); }
		return (identifier == AttributesTRAn.Velocity && v <= 0) ? 1 : v; // no less than 1 for velocity
	}

	@Override
	public int getValue(int index) { return this.getValue(AttributesTRAn.VALUES[index]); }

	@Override
	protected void recalculateCache() {}

	@Override
	public String toString() {
		StringBuilder sb;
		sb = new StringBuilder(256);
		System.out.println("TO STRIIIIING");
		sb.append("CreatureAttributesTRAn [\n");
		for (int i = 0, n = getAttributesCount(); i < n; i++)
//			sb.append(getOriginalValue(i)).append(", ");
			sb.append('\t').append(AttributesTRAn.VALUES[i].getName()).append(':').append(this.getValue(i))
					.append('\n');
		return sb.append(']').toString();
	}
}