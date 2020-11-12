package games.theRisingAngel.misc;

import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CreatureAttributesBonusesCalculator;
import games.theRisingAngel.GModalityTRAn;
import tools.minorTools.RandomWeightedIndexes;

// TODO : aggiornare la tabella qua sotto
/**
 * Bonuses are calculated this way (and should rely only on "basic attributes"
 * (luck may vary)
 * <ul>
 * <li>Every increments and reductions are integers. Fractions are computed
 * away.</li>
 * <li>EVERY probability and multipliers are considered as "per thousand" (i.e.:
 * "%/10"), meaning that those values must be divided by 1000 or considered in a
 * context where are added or subtracted to values are ranging in
 * [-1000;1000]</li>
 * <li>CriticalProbability: it's meant as a "n/10 %" (i.e.: it's not a "percent"
 * but "per thousand"). Luck helps providing each point half of that
 * "probability unit".</li>
 * <li>CriticalMultiplierPercentage: it should be divided by 10.0, at least (or
 * 100)</li> values "f") are to be meant: "add one point of the original value
 * for each 1/f points of this".</li>
 * <li>Strength:
 * <ul>
 * <li>+2.25 LifeMax</li>
 * <li>+0.25 DamageReductionPhysical</li>
 * <li>+1.25 DamageBonusPhysical</li>
 * <li>+0.25 CriticalMultiplierPercentage</li>
 * <li>+0.25 RegenLife</li>
 * <li>+0.25 ProbabilityPerThousandHitPhysical
 * </ul>
 * </li>
 * <li>Constitution:
 * <ul>
 * <li>+10.5 LifeMax</li>
 * <li>+0.75 DamageReductionPhysical</li>
 * <li>+1 RegenLife</li>
 * <li>+0.25 DamageBonusPhysical</li>
 * <li>+0.5 DamageReductionMagical</li>
 * <li>+0.25 CriticalProbabilityPerThousandAvoid
 * </ul>
 * </li>
 * <li>Health:
 * <ul>
 * <li>+4.5 LifeMax</li>
 * <li>+ 2 RegenLife</li>
 * <li>+0.03125 Luck</li>
 * </ul>
 * </li>
 * <li>Defense:
 * <ul>
 * <li>+1 LifeMax</li>
 * <li>+1 DamageReductionPhysical</li>
 * <li>+1 DamageReductionMagical</li>
 * <li>+0.5 CriticalProbabilityPerThousandAvoid</li>
 * </ul>
 * </li>
 * <li>Dexterity:
 * <ul>
 * <li>+1 ProbabilityPerThousandAvoidPhysical.
 * <li>+0.5 ProbabilityPerThousandHitPhysical</li>
 * <li>+0.25 DamageReductionPhysical</li>
 * <li>+0.25 DamageReductionMagical</li>
 * <li>+0.25 CriticalProbabilityPerThousand</li>
 * <li>+0.03125 Luck</li>
 * <li>+1 CriticalProbabilityPerThousandAvoid</li>
 * </ul>
 * </li>
 * <li>Precision:
 * <ul>
 * <li>+1 ProbabilityPerThousandHitPhysical</li>
 * <li>+0.5 ProbabilityPerThousandAvoidPhysical</li>
 * <li>+0.25 DamageBonusPhysical</li>
 * <li>+0.125 DamageBonusMagical</li>
 * <li>+1 CriticalProbabilityPerThousand</li>
 * <li>+0.5 CriticalMultiplierPercentage</li>
 * <li>+0.5 CriticalProbabilityPerThousandAvoid</li>
 * </ul>
 * </li>
 * <li>Intelligence:
 * <ul>
 * <li>+1 ManaMax</li>
 * <li>+1 DamageBonusMagical</li>
 * <li>+0.25 DamageReductionMagical</li>
 * <li>+0.25 CriticalProbabilityPerThousand</li>
 * <li>+0.25 CriticalMultiplierPercentage</li>
 * <li>+0.0625 Luck</li>
 * <li>+0.5 CriticalProbabilityPerThousandAvoid</li>
 * <li>+0.5 ProbabilityPerThousandHitPhysical</li>
 * </ul>
 * </li>
 * <li>Wisdom:
 * <ul>
 * <li>+2 ManaMax</li>
 * <li>+0.25 RegenMana</li>
 * <li>+0.5 DamageBonusMagical</li>
 * <li>+0.5 DamageReductionMagical</li>
 * <li>+0.125 CriticalProbabilityPerThousand</li>
 * <li>+0.125 CriticalMultiplierPercentage</li>
 * <li>+0.25 LifeMax</li>
 * <li>+0.0625 Luck</li>
 * <li>+0.125 RegenLife</li>
 * <li>+0.125 CriticalProbabilityPerThousandAvoid</li>
 * <li>+0.25 ProbabilityPerThousandHitPhysical</li>
 * </ul>
 * </li>
 * <li>Faith:
 * <ul>
 * <li>+4.25 ManaMax</li>
 * <li>+1 RegenMana</li>
 * <li>+0.125 Luck
 * </ul>
 * </li>
 * <li>Velocity: "space-unit per time-unit", corresponds to the numbers of
 * sub-unit expressed in
 * {@link GModalityTRAn#SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE_TRAN}</li>
 * <li>Luck: as for CriticalProbability, it's not meant as a percentage, but
 * "per 10 thousand", so it should divided by 100. Used to alter
 * {@link RandomWeightedIndexes} in some way.<br>
 * It receives the following bonuses (fraction for each point):
 * <ul>
 * <li>Faith: 1/8</li>
 * <li>Intelligence and Wisdom: 1/16</li>
 * <li>Health and Dexterity: 1/32</li>
 * </ul>
 * </li>
 * </ul>
 */
//* <li></li>
public class CreatureAttributesBonusesCalculatorTRAn implements CreatureAttributesBonusesCalculator {

	protected CreatureAttributes creatureAttributesSet;

	@Override
	public CreatureAttributes getCreatureAttributesSet() { return creatureAttributesSet; }

	@Override
	public void setCreatureAttributesSet(CreatureAttributes creatureAttributesSet) {
		this.creatureAttributesSet = creatureAttributesSet;
	}

	@Override
	public void markCacheAsDirty() {}

	@Override
	public int getBonusFor(int index) {
		int v;
		AttributesTRAn a;
		CreatureAttributes c;
		a = AttributesTRAn.VALUES[index];
		c = creatureAttributesSet;
		switch (a) {
		case Luck: {
			v = (+c.getValue(AttributesTRAn.Faith)// start 1/8
					+ (( // start 1/16
					+(( // start 1/32
					+c.getValue(AttributesTRAn.Dexterity) + c.getValue(AttributesTRAn.Health))//
							>> 1) // end 1/32
							+ c.getValue(AttributesTRAn.Wisdom) + c.getValue(AttributesTRAn.Intelligence)) //
							>> 1)) // end 1/16
					>> 3; // end 1/8
			break;
		}
		case LifeMax: {
			int cost, str, h;
			cost = c.getValue(AttributesTRAn.Constitution);
			str = c.getValue(AttributesTRAn.Strength);
			h = c.getValue(AttributesTRAn.Health);
			v = +c.getValue(AttributesTRAn.Defense) //
					+ (( // *2
					+(h << 1) //
							+ (cost + (cost << 2)) // = *5
							+ str) //
							<< 1)
					+ //
					(( //
					+((c.getValue(AttributesTRAn.Wisdom) + str) >> 1) //
							+ cost //
					) >> 1);
			break;
		}
		case RegenLife: {
			v = +((// 0.25
			+(c.getValue(AttributesTRAn.Wisdom) >> 1) + c.getValue(AttributesTRAn.Strength)) >> 2)//
					+ c.getValue(AttributesTRAn.Constitution) //
					+ (c.getValue(AttributesTRAn.Health) << 1);
			break;
//					+((c.getValue(AttributesTRAn.Health) + (c.getValue(AttributesTRAn.Constitution) >> 1)
//					+ (c.getValue(AttributesTRAn.Strength) / 3) // >> 2)
//					+ ((c.getValue(AttributesTRAn.Wisdom) + c.getValue(AttributesTRAn.Faith)) >> 3)) >> 3);
//			v >>= 1; // to highbreak;
		}
		case DamageReductionPhysical: {
			int cons;
			cons = c.getValue(AttributesTRAn.Constitution);
			v = +c.getValue(AttributesTRAn.Defense) //
					+ (( //
					+(cons + (cons << 1)) // *3
							+ c.getValue(AttributesTRAn.Strength) //
							+ c.getValue(AttributesTRAn.Dexterity) //
					) >> 2);
//					( //
//			+c.getValue(AttributesTRAn.Defense) //
//					+ (((c.getValue(AttributesTRAn.Strength) >> 1) + c.getValue(AttributesTRAn.Constitution)
//							+ (c.getValue(AttributesTRAn.Health) >> 2)) >> 1)
//					+ (c.getValue(AttributesTRAn.Dexterity) / 5)//
//			) >> 1;
			break;
		}
		case DamageBonusPhysical: {
			int str;
			str = c.getValue(AttributesTRAn.Strength);
			v = str //
					+ ((str + c.getValue(AttributesTRAn.Constitution) + c.getValue(AttributesTRAn.Precision))//
							>> 2);
//			( //
//			+((c.getValue(AttributesTRAn.Strength) + c.getValue(AttributesTRAn.Constitution)) >> 1)
//					+ ((c.getValue(AttributesTRAn.Precision) + c.getValue(AttributesTRAn.Intelligence)) / 5)
//					+ (c.getValue(AttributesTRAn.Wisdom) >> 3)//
//			) >> 1;
			break;
		}
		case ManaMax: {
			int faith;
			faith = c.getValue(AttributesTRAn.Faith);
			v = c.getValue(AttributesTRAn.Intelligence) //
					+ ((faith << 1) + (c.getValue(AttributesTRAn.Wisdom)) << 1) //
					+ (faith >> 2);
			break;
		}
		case RegenMana: {
			v = +c.getValue(AttributesTRAn.Faith) + //
					+((c.getValue(AttributesTRAn.Wisdom) >> 2)) //
			;
			break;
//		;}case RegenMana : {v= ((c.getValue(AttributesTRAn.Faith))
//				+ (c.getValue(AttributesTRAn.Wisdom) >> 1)
//				+ (c.getValue(AttributesTRAn.Intelligence) >> 2)
//				+ (c.getValue(AttributesTRAn.Health) >> 3)) / 10;break;
		}
		case DamageBonusMagical: {
			v = c.getValue(AttributesTRAn.Intelligence) //
					+ (( //
					+(c.getValue(AttributesTRAn.Precision) >> 2) + c.getValue(AttributesTRAn.Wisdom)) >> 1);
//					( //
//			(c.getValue(AttributesTRAn.Precision) / 5) + c.getValue(AttributesTRAn.Intelligence)
//					+ ((c.getValue(AttributesTRAn.Wisdom) + (c.getValue(AttributesTRAn.Faith) >> 1)) >> 1)//
//			) >> 1;
			break;
		}
		case DamageReductionMagical: {
			v = +c.getValue(AttributesTRAn.Defense) //
					+ (( //
					+c.getValue(AttributesTRAn.Constitution) + c.getValue(AttributesTRAn.Wisdom) + ((//
					+c.getValue(AttributesTRAn.Intelligence) + c.getValue(AttributesTRAn.Dexterity)) >> 1) //
					) >> 1);
//					( //
//			+(c.getValue(AttributesTRAn.Dexterity) / 5) + c.getValue(AttributesTRAn.Intelligence)
//					+ c.getValue(AttributesTRAn.Wisdom) //
//					+ ((c.getValue(AttributesTRAn.Defense) >> 2) + (c.getValue(AttributesTRAn.Faith)) >> 1)//
//			) >> 1;
			break;
		}
		case CriticalMultiplierPercentage: {
			v = +((//
			+(c.getValue(AttributesTRAn.Wisdom) >> 1) //
					+ c.getValue(AttributesTRAn.Strength) //
					+ c.getValue(AttributesTRAn.Precision) //
					+ c.getValue(AttributesTRAn.Intelligence) //
			) >> 2) //
			;
//					(c.getValue(AttributesTRAn.Strength) << 1) + c.getValue(AttributesTRAn.Intelligence)
//					+ c.getValue(AttributesTRAn.Wisdom) + c.getValue(AttributesTRAn.Luck);
			break;
		}
		case CriticalProbabilityPerThousand: {
			v = +c.getValue(AttributesTRAn.Precision) //
					+ ((// 1/2
					+((// 1/4
					+((c.getValue(AttributesTRAn.Wisdom)) >> 1) // 1/8
							+ c.getValue(AttributesTRAn.Dexterity) //
							+ c.getValue(AttributesTRAn.Intelligence) //
					) >> 1) //
							+ c.getValue(AttributesTRAn.Luck)) >> 1);
			//
//					((c.getValue(AttributesTRAn.Precision) << 1) //
//							+ c.getValue(AttributesTRAn.Dexterity) //
//							+ ((+c.getValue(AttributesTRAn.Intelligence) //
//									+ c.getValue(AttributesTRAn.Wisdom)) >> 1)//
//					) + c.getValue(AttributesTRAn.Luck);
			break;
		}
		case CriticalMultiplierPercentageReduction: {
			v = +c.getValue(AttributesTRAn.Defense) //
					+ ((//
					+c.getValue(AttributesTRAn.Constitution) //
							+ c.getValue(AttributesTRAn.Intelligence) //
							+ c.getValue(AttributesTRAn.Luck) //
					) >> 1);
//					( //
//			+c.getValue(AttributesTRAn.Defense) + c.getValue(AttributesTRAn.Constitution) //
//					+ ((+(c.getValue(AttributesTRAn.Strength) >> 1) + (c.getValue(AttributesTRAn.Dexterity) >> 2)) >> 1)//
//			) >> 3;
			break;
		}
		case CriticalProbabilityPerThousandAvoid: {
			v = +c.getValue(AttributesTRAn.Dexterity) //
					+ ((// 1/2
					+c.getValue(AttributesTRAn.Defense) //
							+ c.getValue(AttributesTRAn.Intelligence) //
							+ c.getValue(AttributesTRAn.Luck) //
							+ c.getValue(AttributesTRAn.Precision) //
							+ ((// 1/4
							+c.getValue(AttributesTRAn.Constitution) //
									+ ((+c.getValue(AttributesTRAn.Wisdom)) >> 1) // 1/8
							) >> 1)) //
							>> 1);
			;
//					(//
//			+c.getValue(AttributesTRAn.Constitution) //
//					+ ((//
//					+c.getValue(AttributesTRAn.Dexterity)//
//							+ ((c.getValue(AttributesTRAn.Defense) //
//									+ ((c.getValue(AttributesTRAn.Precision) + c.getValue(AttributesTRAn.Intelligence)) //
//											>> 1))//
//									>> 1))//
//							>> 1)//
//			);
			break;
		}
		case ProbabilityPerThousandHitPhysical: {
			v = +c.getValue(AttributesTRAn.Precision) //
					+ ((// 1/2
					+c.getValue(AttributesTRAn.Dexterity) //
							+ c.getValue(AttributesTRAn.Luck) //
							+ c.getValue(AttributesTRAn.Intelligence)//
							+ ((// 1/4
							+c.getValue(AttributesTRAn.Strength) //
									+ c.getValue(AttributesTRAn.Wisdom)) >> 1) // 1/8
					) >> 1);
//					+c.getValue(AttributesTRAn.Precision)
//					+ c.getValue(AttributesTRAn.Luck)
//							+ ((c.getValue(AttributesTRAn.Dexterity) + (c.getValue(AttributesTRAn.Strength) >> 1)
//									+ (c.getValue(AttributesTRAn.Intelligence) >> 2)) >> 1);
			break;
		}
		case ProbabilityPerThousandAvoidPhysical: {
			v = +c.getValue(AttributesTRAn.Dexterity) //
					+ ((// 1/2
					+c.getValue(AttributesTRAn.Precision) //
							+ c.getValue(AttributesTRAn.Luck) //
							+ ((// 1/4
							+c.getValue(AttributesTRAn.Strength) //
									+ c.getValue(AttributesTRAn.Wisdom) //
									+ c.getValue(AttributesTRAn.Intelligence) //
							) >> 1))//
							>> 1);
//					+c.getValue(AttributesTRAn.Dexterity) + c.getValue(AttributesTRAn.Luck)
//							+ ((c.getValue(AttributesTRAn.Precision) + (c.getValue(AttributesTRAn.Strength) >> 1)
//									+ (c.getValue(AttributesTRAn.Intelligence) >> 2)) >> 1);
			break;
		}
		case ProbabilityPerThousandHitMagical: {
			v = +c.getValue(AttributesTRAn.Wisdom) //
					+ ((// 1/2
					+c.getValue(AttributesTRAn.Intelligence) //
							+ c.getValue(AttributesTRAn.Luck) //
							+ c.getValue(AttributesTRAn.Dexterity)//
							+ ((// 1/4
							+c.getValue(AttributesTRAn.Strength) //
									+ c.getValue(AttributesTRAn.Precision)) >> 1) // 1/8
					) >> 1);
//					+c.getValue(AttributesTRAn.Wisdom) + c.getValue(AttributesTRAn.Luck)
//							+ ((c.getValue(AttributesTRAn.Intelligence) + (c.getValue(AttributesTRAn.Faith) >> 1)
//									+ (c.getValue(AttributesTRAn.Precision) >> 2)) >> 1);
			break;
		}
		case ProbabilityPerThousandAvoidMagical: {
			v = +c.getValue(AttributesTRAn.Wisdom) //
					+ ((// 1/2
					+c.getValue(AttributesTRAn.Intelligence) //
							+ c.getValue(AttributesTRAn.Luck) //
							+ ((// 1/4
							+c.getValue(AttributesTRAn.Strength) //
									+ c.getValue(AttributesTRAn.Dexterity) //
									+ c.getValue(AttributesTRAn.Precision) //
							) >> 1))//
							>> 1);
//					+c.getValue(AttributesTRAn.Luck) + c.getValue(AttributesTRAn.Intelligence)
//					+ ((c.getValue(AttributesTRAn.Wisdom) + (c.getValue(AttributesTRAn.Faith) >> 1)
//							+ (c.getValue(AttributesTRAn.Dexterity) >> 3)) >> 1);
			break;
		}
		case Velocity: {
			v = ((c.getValue(AttributesTRAn.Dexterity) << 1) / 5) //
					+ (((c.getValue(AttributesTRAn.Constitution) + c.getValue(AttributesTRAn.Strength)) << 1) / 15);
			break;
		}
		default:
			v = 0;
		}
		return v;
	}
}