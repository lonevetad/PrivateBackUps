package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.CreatureAttributes;
import games.theRisingAngel.misc.AttributesTRAn;

public class AAttrSingleBonusMalusRandomFixed extends AAttrSingleBonusMalusRandom {
	private static final long serialVersionUID = 2154186081264778L;
	public static final int MALUS_AMOUNT = -10, BONUS_AMOUNT = (-MALUS_AMOUNT) << 1;
	public static final String NAME = AAttrSingleBonusMalusRandom.NAME + " absolute";

	public AAttrSingleBonusMalusRandomFixed() {
		super(NAME);
	}

	@Override
	public int getAttributesBonusValue(GModality gm, CreatureAttributes ca, AttributesTRAn attr) {
		return BONUS_AMOUNT;
	}

	@Override
	public int getAttributesMalusValue(GModality gm, CreatureAttributes ca, AttributesTRAn attr) {
		return MALUS_AMOUNT;
	}
}