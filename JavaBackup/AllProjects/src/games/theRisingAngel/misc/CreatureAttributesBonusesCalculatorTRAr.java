package games.theRisingAngel.misc;

import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CreatureAttributesBonusesCalculator;
import games.theRisingAngel.AttributesTRAr;
import tools.minorTools.RandomWeightedIndexes;

/**
 * Bonuses are calculated this way
 * <ul>
 * <li>Every increments and reductions are integers. Fractions (call those
 * <li>CriticalProbability: it's meant as a "n/10 %" (i.e.: it's not a
 * "percentile" but "per thousand")</li>
 * <li>CriticalMultiplier: it should be divided by 10.0 at least</li> values
 * "f") are to be meant: "add one point of the original value for each 1/f
 * points of this".</li>
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
 * <li>Velocity: "space-unit per time-unit", could be then divided by some
 * factor</li>
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
	}

	@Override
	public int getBonusForValue(int index) {
		int v;
		AttributesTRAr a;
		CreatureAttributes c;
		c = creatureAttributesSet;
		a = AttributesTRAr.VALUES[index];
//		v = super.getValue(index);
		v = 0;
		switch (a) {
		case LifeMax:
			v = c.getValue(AttributesTRAr.Strength.getIndex())
					+ (c.getValue(AttributesTRAr.Constitution.getIndex()) << 1)
					+ (c.getValue(AttributesTRAr.Health.getIndex()) << 2);
			break;
		case RigenLife:
			v = (c.getValue(AttributesTRAr.Constitution.getIndex()) >> 2)
					+ (c.getValue(AttributesTRAr.Health.getIndex()) >> 1)
					+ (c.getValue(AttributesTRAr.Strength.getIndex()) >> 3)
					+ (c.getValue(AttributesTRAr.Wisdom.getIndex()) >> 3)
					+ (c.getValue(AttributesTRAr.Faith.getIndex()) >> 3);
			break;
		case DamageReductionPhysical:
			v = (c.getValue(AttributesTRAr.Strength.getIndex()) >> 2)
					+ (c.getValue(AttributesTRAr.Constitution.getIndex()) >> 1)
					+ (c.getValue(AttributesTRAr.Defense.getIndex()) >> 1)
					+ (c.getValue(AttributesTRAr.Dexterity.getIndex()) / 10);
			break;
		case DamageBonusPhysical:
			v = (c.getValue(AttributesTRAr.Strength.getIndex()) >> 1)
					+ (c.getValue(AttributesTRAr.Constitution.getIndex()) >> 2)
					+ (c.getValue(AttributesTRAr.Precision.getIndex()) / 10)
					+ (c.getValue(AttributesTRAr.Intelligence.getIndex()) / 10)
					+ (c.getValue(AttributesTRAr.Wisdom.getIndex()) >> 4);
			break;
		case ManaMax:
			int wis;
			v = c.getValue(AttributesTRAr.Intelligence.getIndex())//
					+ (wis = c.getValue(AttributesTRAr.Wisdom.getIndex())) + (wis << 1) // == *3
					+ (c.getValue(AttributesTRAr.Faith.getIndex()) << 2);
			break;
		case RigenMana:
			v = (c.getValue(AttributesTRAr.Wisdom.getIndex()) >> 2)
					+ (c.getValue(AttributesTRAr.Faith.getIndex()) >> 1);
			break;
		case DamageBonusMagical:
			v = (c.getValue(AttributesTRAr.Precision.getIndex()) / 10)
					+ (c.getValue(AttributesTRAr.Intelligence.getIndex()) >> 1)
					+ (c.getValue(AttributesTRAr.Wisdom.getIndex()) >> 2)
					+ (c.getValue(AttributesTRAr.Faith.getIndex()) >> 3);
			break;
		case DamageReductionMagical:
			v = (c.getValue(AttributesTRAr.Dexterity.getIndex()) / 10)
					+ (c.getValue(AttributesTRAr.Intelligence.getIndex()) >> 2)
					+ (c.getValue(AttributesTRAr.Wisdom.getIndex()) >> 2)
					+ (c.getValue(AttributesTRAr.Defense.getIndex()) >> 2)
					+ (c.getValue(AttributesTRAr.Faith.getIndex()) >> 3);
			break;
		case CriticalMultiplier:
			v = (c.getValue(AttributesTRAr.Strength.getIndex()) / 5)
					+ (c.getValue(AttributesTRAr.Intelligence.getIndex()) / 10)
					+ (c.getValue(AttributesTRAr.Wisdom.getIndex()) / 10);
//			v /= 10;
			break;
		case CriticalProbability:
			v = (c.getValue(AttributesTRAr.Precision.getIndex()) / 5)
					+ (c.getValue(AttributesTRAr.Dexterity.getIndex()) / 10)
					+ (c.getValue(AttributesTRAr.Intelligence.getIndex()) / 10)
					+ (c.getValue(AttributesTRAr.Wisdom.getIndex()) / 10)
					+ (c.getValue(AttributesTRAr.Faith.getIndex()) / 5);
//			v /= 10;
			break;
		case ProbabilityHit:
			v = (c.getValue(AttributesTRAr.Precision.getIndex()) >> 1)
					+ (c.getValue(AttributesTRAr.Dexterity.getIndex()) >> 2);
			break;
		case ProbabilityAvoid:
			v = (c.getValue(AttributesTRAr.Dexterity.getIndex()) >> 1)
					+ (c.getValue(AttributesTRAr.Precision.getIndex()) >> 2);
			break;
		case Velocity:
			v = c.getValue(AttributesTRAr.Strength.getIndex())
					+ (c.getValue(AttributesTRAr.Constitution.getIndex()) << 1)
					+ (c.getValue(AttributesTRAr.Health.getIndex()) << 2);
			break;
		case Luck:// TODO to be continued
			v = +(c.getValue(AttributesTRAr.Health.getIndex()) >> 3)
					+ (c.getValue(AttributesTRAr.Wisdom.getIndex()) >> 3)
					+ (c.getValue(AttributesTRAr.Faith.getIndex()) >> 3)
					+ (c.getValue(AttributesTRAr.Intelligence.getIndex()) >> 3)
					+ (c.getValue(AttributesTRAr.Dexterity.getIndex()) >> 3);
			break;
		default:
			v = 0;
		}
		return v;
	}
}