package games.theRisingAngel.misc;

import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributesBonusesCalculator;
import games.generic.controlModel.subimpl.CreatureAttributesBaseAndDerivedCaching;
import games.theRisingAngel.enums.AttributesTRAn;

public class CreatureAttributesTRAn extends CreatureAttributesBaseAndDerivedCaching {

	public CreatureAttributesTRAn() {
		super(AttributesTRAn.ALL_ATTRIBUTES.length, AttributesTRAn.INDEX_TO_ATTRIBUTE_TRAn);
		this.cacheValues = null; // not needed
		super.setBonusCalculator(new CreatureAttributesBonusesCalculatorTRAn());
	}

	@Override
	public int getValue(AttributeIdentifier identifier) {
		int v, i;
		CreatureAttributesBonusesCalculator bc;
		i = identifier.getIndex();
		v = super.getOriginalValue(i) + super.attributesModificationsApplied[i];
		if ((bc = this.bonusCalculator) != null) { v += bc.getBonusFor(i); }
		if (v > identifier.upperBound()) {
			v = identifier.upperBound();
		} else if (v < identifier.lowerBound()) { v = identifier.lowerBound(); }
		return v;
	}

	@Override
	public int getValue(int index) { return this.getValue(AttributesTRAn.ALL_ATTRIBUTES[index]); }

	@Override
	protected void recalculateCache() {}

	@Override
	public String toString() {
		StringBuilder sb;
		sb = new StringBuilder(256);
		sb.append("CreatureAttributesTRAn [\n");
		for (int i = 0, n = getAttributesCount(); i < n; i++)
//			sb.append(getOriginalValue(i)).append(", ");
			sb.append('\t').append(AttributesTRAn.ALL_ATTRIBUTES[i].getName()).append(':').append(this.getValue(i))
					.append('\n');
		return sb.append(']').toString();
	}
}