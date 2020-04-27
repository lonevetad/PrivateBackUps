package games.theRisingAngel.misc;

import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CreatureAttributesBonusesCalculator;
import games.theRisingAngel.GModalityTRAn;
import tools.minorTools.RandomWeightedIndexes;

/**
 * Bonuses are calculated this way
 * <ul>
 * <li>Every increments and reductions are integers. Fractions (call those
 * <li>CriticalProbability: it's meant as a "n/10 %" (i.e.: it's not a
 * "percentile" but "per thousand")</li>
 * <li>CriticalMultiplier: it should be divided by 10.0, at least (or 100)</li>
 * values "f") are to be meant: "add one point of the original value for each
 * 1/f points of this".</li>
 * <li>Strength: each point gives +1 LifeMax, +0.25 DamageReductionPhysical,
 * +0.5 DamageBonusPhysical, +0.2 CriticalMultiplier</li>
 * <li>Constitution: +2 LifeMax, +0.5 DamageReductionPhysical, +0.25 RegenLife,
 * +0.25 DamageBonusPhysical</li>
 * <li>Health: +4 LifeMax, + 0.5 RigenLife</li>
 * <li>Defense: +0.5 DamageReductionPhysical, +0.25 DamageReductionMagical</li>
 * <li>Dexterity: +0.5 ProbabilityAvoid. +0.25 ProbabilityHit, +0.1
 * DamageReductionPhysical, +0.1 DamageReductionMagical, +0.1
 * CriticalProbability</li>
 * <li>Precision: +0.5 ProbabilityHit. +0.25 ProbabilityAvoid, +0.1
 * DamageBonusPhysical, +0.1 DamageBonusMagical, +0.2 CriticalProbability</li>
 * <li>Intelligence: +1 ManaMax, +0.5 DamageBonusMagical, +0.25
 * DamageReductionMagical, +0.1 CriticalProbability, +0.1
 * CriticalMultiplier</li>
 * <li>Wisdom: +2 ManaMax, +0.25 RigenMana, +0.25 DamageBonusMagical, +0.25
 * DamageReductionMagical, +0.1 CriticalProbabilityl, +0.1
 * CriticalMultiplier</li>
 * <li>Faith: +3 ManaMax, +0.5 RigenMana, +0.125 DamageBonusMagical, +0.125
 * DamageReductionMagical, +0.2 CriticalProbability</li>
 * <li>Velocity: "space-unit per time-unit", corresponds to the numbers of
 * sub-unit expressed in
 * {@link GModalityTRAn#SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE_TRAN}</li>
 * <li>Luck: as for CriticalProbability, it's not meant as a percentage, but
 * "per 10 thousand", so it should divided by 100. Used to alter
 * {@link RandomWeightedIndexes} in some way</li>
 * <li></li>
 * </ul>
 */
public class CreatureAttributesBonusesCalculatorTRAr implements CreatureAttributesBonusesCalculator {

	protected CreatureAttributes creatureAttributesSet;

	@Override
	public CreatureAttributes getCreatureAttributesSet() {
		return creatureAttributesSet;
	}

	@Override
	public void setCreatureAttributesSet(CreatureAttributes creatureAttributesSet) {
		this.creatureAttributesSet = creatureAttributesSet;
		if (creatureAttributesSet == null)
			throw new IllegalArgumentException("Creaure Attribute Set is null");
	}

	@Override
	public int getBonusForValue(int index) {
		int v;
		AttributesTRAn a;
		CreatureAttributes c;
		c = getCreatureAttributesSet();
		if (c == null)
			throw new IllegalStateException("Creaure Attribute Set is null");
		a = AttributesTRAn.VALUES[index];
//		v = super.getValue(index);
		v = 0;
		switch (a) {
		case LifeMax:
			v = c.getValue(AttributesTRAn.Constitution.getIndex())
					+ (c.getValue(AttributesTRAn.Strength.getIndex()) >> 1)
					+ (c.getValue(AttributesTRAn.Health.getIndex()) << 1);
			break;
		case RigenLife:
			v = (c.getValue(AttributesTRAn.Constitution.getIndex()) >> 3)
					+ ((c.getValue(AttributesTRAn.Health.getIndex())) >> 2)
					+ (c.getValue(AttributesTRAn.Strength.getIndex()) >> 4)
					+ (c.getValue(AttributesTRAn.Wisdom.getIndex()) >> 5)
					+ (c.getValue(AttributesTRAn.Faith.getIndex()) >> 5);
			v >>= 1; // to high
			break;
		case DamageReductionPhysical:
			v = (c.getValue(AttributesTRAn.Strength.getIndex()) >> 2)
					+ (c.getValue(AttributesTRAn.Constitution.getIndex()) >> 1)
					+ (c.getValue(AttributesTRAn.Defense.getIndex()) >> 1)
					+ (c.getValue(AttributesTRAn.Dexterity.getIndex()) / 10);
			break;
		case DamageBonusPhysical:
			v = (c.getValue(AttributesTRAn.Strength.getIndex()) >> 1)
					+ (c.getValue(AttributesTRAn.Constitution.getIndex()) >> 2)
					+ (c.getValue(AttributesTRAn.Precision.getIndex()) / 10)
					+ (c.getValue(AttributesTRAn.Intelligence.getIndex()) / 10)
					+ (c.getValue(AttributesTRAn.Wisdom.getIndex()) >> 4);
			break;
		case ManaMax:
			v = (c.getValue(AttributesTRAn.Intelligence.getIndex()) >> 1) //
					+ (c.getValue(AttributesTRAn.Wisdom.getIndex()))
					+ (c.getValue(AttributesTRAn.Faith.getIndex()) << 1);
			v >>= 1; // too high, make it half
			break;
		case RigenMana:
			v = (c.getValue(AttributesTRAn.Wisdom.getIndex()) >> 3)
					+ (c.getValue(AttributesTRAn.Faith.getIndex()) >> 2);
			break;
		case DamageBonusMagical:
			v = (c.getValue(AttributesTRAn.Precision.getIndex()) / 10)
					+ (c.getValue(AttributesTRAn.Intelligence.getIndex()) >> 1)
					+ (c.getValue(AttributesTRAn.Wisdom.getIndex()) >> 2)
					+ (c.getValue(AttributesTRAn.Faith.getIndex()) >> 3);
			break;
		case DamageReductionMagical:
			v = (c.getValue(AttributesTRAn.Dexterity.getIndex()) / 10)
					+ (c.getValue(AttributesTRAn.Intelligence.getIndex()) >> 1)
					+ (c.getValue(AttributesTRAn.Wisdom.getIndex()) >> 1)
					+ (c.getValue(AttributesTRAn.Defense.getIndex()) >> 2)
					+ (c.getValue(AttributesTRAn.Faith.getIndex()) >> 2);
			break;
		case CriticalMultiplier:
			v = (c//
					.getValue(AttributesTRAn.Strength.getIndex()) / 5)
					+ (c.getValue(AttributesTRAn.Intelligence.getIndex()) / 10)
					+ (c.getValue(AttributesTRAn.Wisdom.getIndex()) / 10);
//			v /= 10;
			break;
		case CriticalProbability:
			v = (c.getValue(AttributesTRAn.Precision.getIndex()) / 5)
					+ (c.getValue(AttributesTRAn.Dexterity.getIndex()) / 10)
					+ (c.getValue(AttributesTRAn.Intelligence.getIndex()) / 10)
					+ (c.getValue(AttributesTRAn.Wisdom.getIndex()) / 10)
					+ (c.getValue(AttributesTRAn.Faith.getIndex()) / 5);
//			v /= 10;
			break;
		case ProbabilityHitPhysical:
			v = (c.getValue(AttributesTRAn.Precision.getIndex()) >> 1)
					+ (c.getValue(AttributesTRAn.Dexterity.getIndex()) >> 2);
			break;
		case ProbabilityAvoidPhysical:
			v = (c.getValue(AttributesTRAn.Dexterity.getIndex()) >> 1)
					+ (c.getValue(AttributesTRAn.Precision.getIndex()) >> 2)
					+ (c.getValue(AttributesTRAn.Intelligence.getIndex()) >> 3);
			break;
		case Velocity:
			v = ((c.getValue(AttributesTRAn.Dexterity.getIndex()) << 1) / 5) //
					+ ((c.getValue(AttributesTRAn.Constitution.getIndex()) << 1) / 15)
					+ ((c.getValue(AttributesTRAn.Strength.getIndex()) << 1) / 15);
			break;
		case Luck:// TODO to be continued
			v = +(c.getValue(AttributesTRAn.Health.getIndex()) >> 3)
					+ (c.getValue(AttributesTRAn.Wisdom.getIndex()) >> 3)
					+ (c.getValue(AttributesTRAn.Faith.getIndex()) >> 3)
					+ (c.getValue(AttributesTRAn.Intelligence.getIndex()) >> 3)
					+ (c.getValue(AttributesTRAn.Dexterity.getIndex()) >> 3);
			break;
		default:
			v = 0;
		}
		return v;
	}
}