package games.generic.controlModel.misc;

public abstract class CreatureAttributesBaseAndDerived extends CreatureAttributes {

	public CreatureAttributesBaseAndDerived(int attributesCount) {
		super(attributesCount);
		this.bonusCalculator = null;
	}

	protected CreatureAttributesBonusesCalculator bonusCalculator;

	//

	public CreatureAttributesBonusesCalculator getBonusCalculator() { return bonusCalculator; }

	public void setBonusCalculator(CreatureAttributesBonusesCalculator bonusCalculator) {
//		if (this.bonusCalculator != null)
//			this.bonusCalculator.setCreatureAttributesSet(null); // CANNOT BE NULL-ED
		this.bonusCalculator = bonusCalculator;
		if (bonusCalculator != null) { bonusCalculator.setCreatureAttributesSet(this); }
	}
}