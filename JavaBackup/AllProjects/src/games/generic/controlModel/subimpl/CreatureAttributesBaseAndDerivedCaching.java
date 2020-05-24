package games.generic.controlModel.subimpl;

import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributesBaseAndDerived;
import games.generic.controlModel.misc.CreatureAttributesBonusesCalculator;

public class CreatureAttributesBaseAndDerivedCaching extends CreatureAttributesBaseAndDerived {

	public CreatureAttributesBaseAndDerivedCaching(int attributesCount) {
		super(attributesCount);
		this.attributesModificationsApplied = new int[attributesCount];
		this.cacheValues = new int[attributesCount];
		this.isCacheAvailable = false;
	}

	protected transient boolean isCacheAvailable = false;
//protected transient int attributesCountLeftToUpdate;
//protected final boolean[] attributesUpdated;
	protected int[] attributesModificationsApplied, cacheValues;

	@Override
	public int getValue(int index) {
		throw new UnsupportedOperationException(
				"Cannot invoke on integer index because it cannot perform \"AttributeIdentifier#isStrictlyPositive()\" check");
	}

	@Override
	public int getValue(AttributeIdentifier identifier) {
		int v, index;
		index = identifier.getIndex();
		if (!isCacheAvailable) { recalculateCache(); }
		v = this.cacheValues[index];
		return (identifier.isStrictlyPositive() && v < 0) ? 0 : v;
	}

	protected void recalculateCache() {
		boolean bcNotNull;
		int i, ac;
		final int[] cv, ov, ama;
		CreatureAttributesBonusesCalculator bc;
		isCacheAvailable = true;
		cv = this.cacheValues;
		ov = super.originalValues;
		ama = this.attributesModificationsApplied;
		ac = attributesCount;
		// then others
		if (bcNotNull = (bc = this.bonusCalculator) != null) { bc.markCacheAsDirty(); }
		i = ac;
		if (bcNotNull) {
			while (--i >= 0) {// update the values
				cv[i] = ov[i] + ama[i] + bc.getBonusFor(i);
			}
		} else { // update others
			while (--i >= 0) {// update the values
				cv[i] = ov[i] + ama[i];
			}
		}
	}

//public int[] getComputedAttributesModifications() {
//	return attributesModifications;
//}
	@Override
	public void setBonusCalculator(CreatureAttributesBonusesCalculator bonusCalculator) {
		this.isCacheAvailable = false;
		super.setBonusCalculator(bonusCalculator);
	}

	/**
	 * Set the attribute's (identified by the index: first parameter) value (second
	 * parameter).
	 */
	@Override
	public void setOriginalValue(int index, int value) {
		this.isCacheAvailable = false;
		this.originalValues[index] = value;
	}

	@Override
	public void applyAttributeModifier(AttributeModification eam) {
		this.isCacheAvailable = false;
		this.attributesModificationsApplied[eam.getAttributeModified().getIndex()] += eam.getValue();
	}

	@Override
	public void removeAttributeModifier(AttributeModification eam) {
		this.isCacheAvailable = false;
		this.attributesModificationsApplied[eam.getAttributeModified().getIndex()] -= eam.getValue();
	}
}