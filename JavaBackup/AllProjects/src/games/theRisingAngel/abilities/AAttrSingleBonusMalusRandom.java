package games.theRisingAngel.abilities;

import java.util.Random;

import games.generic.controlModel.GModality;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AbilAttributesBonusMalusChanging;
import games.generic.controlModel.misc.CreatureAttributes;
import games.theRisingAngel.misc.AttributesTRAn;

public abstract class AAttrSingleBonusMalusRandom extends AbilAttributesBonusMalusChanging {
	private static final long serialVersionUID = 305617900000L;
	public static final String NAME = "Amoprhous";
	public static final int RARITY = 2;
	public static final long TIME_TRESHOLD_CHANGING = 4000;

	public AAttrSingleBonusMalusRandom(String name) {
		super(name);
		bonus = new AttributeModification(AttributesTRAn.Luck, 0); // Luck will never be changed
		malus = new AttributeModification(AttributesTRAn.Luck, 0); // Luck will never be changed
	}

//	protected AttributesTRAn bonus, malus;
	protected AttributeModification bonus, malus;

	@Override
	public long getTimeThreshold() {
		return TIME_TRESHOLD_CHANGING;
	}

	@Override
	public void removeAttributesBonusesMaluses(GModality gm, CreatureAttributes ca) {
		ca.removeAttributeModifier(bonus);
		ca.removeAttributeModifier(malus);
	}

	@Override
	public void applyAttributesBonusesMaluses(GModality gm, CreatureAttributes ca) {
		ca.applyAttributeModifier(bonus);
		ca.applyAttributeModifier(malus);
	}

	public abstract int getAttributesBonusValue(GModality gm, CreatureAttributes ca, AttributesTRAn attr);

	public abstract int getAttributesMalusValue(GModality gm, CreatureAttributes ca, AttributesTRAn attr);

	@Override
	public void updateAttributesBonuses(GModality gm, CreatureAttributes ca) {
		int indexRange, newIndex;
		Random r;
		AttributesTRAn newBonus;
		r = gm.getRandom();
		indexRange = AttributesTRAn.ATTRIBUTES_UPGRADABLE_COUNT;
		do {
			newIndex = r.nextInt(indexRange) + AttributesTRAn.LAST_INDEX_ATTRIBUTE_UPGRADABLE;
			newBonus = AttributesTRAn.VALUES[newIndex];
		} while (newBonus == this.bonus.getAttributeModified());
		this.bonus.setType(newBonus);
		this.bonus.setValue(getAttributesBonusValue(gm, ca, newBonus));
	}

	@Override
	public void updateAttributesMaluses(GModality gm, CreatureAttributes ca) {
		int indexRange, newIndex;
		Random r;
		AttributesTRAn newMalus;
		r = gm.getRandom();
		indexRange = AttributesTRAn.ATTRIBUTES_UPGRADABLE_COUNT;
		do {
			newIndex = r.nextInt(indexRange) + AttributesTRAn.LAST_INDEX_ATTRIBUTE_UPGRADABLE;
			newMalus = AttributesTRAn.VALUES[newIndex];
		} while (newMalus == this.malus.getAttributeModified());
		this.malus.setType(newMalus);
		this.malus.setValue(getAttributesMalusValue(gm, ca, newMalus));
	}

}