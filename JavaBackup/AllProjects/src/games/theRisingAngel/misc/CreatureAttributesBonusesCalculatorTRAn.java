package games.theRisingAngel.misc;

import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CreatureAttributesBonusesCalculator;
import games.theRisingAngel.GModalityTRAn;
import tools.minorTools.RandomWeightedIndexes;

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
public class CreatureAttributesBonusesCalculatorTRAn implements CreatureAttributesBonusesCalculator {

	protected boolean isCacheDirty;
	protected int[] cache;
	protected CreatureAttributes creatureAttributesSet;

	@Override
	public CreatureAttributes getCreatureAttributesSet() {
		return creatureAttributesSet;
	}

	@Override
	public void setCreatureAttributesSet(CreatureAttributes creatureAttributesSet) {
		int n, c[];
		this.creatureAttributesSet = creatureAttributesSet;
		if (creatureAttributesSet == null)
			throw new IllegalArgumentException("Creaure Attribute Set is null");
		c = cache = new int[n = creatureAttributesSet.getAttributesCount()];
		while (n-- > 0)// clean memory
			c[n] = 0;
	}

	@Override
	public void markCacheAsDirty() {
		isCacheDirty = true;
	}

	@Override
	public int getBonusForValue(int index) {
		if (isCacheDirty)
			recalculateCache();
		return cache[index];
	}

	public void recalculateCache() {
		CreatureAttributes c;
		c = getCreatureAttributesSet();
		if (c == null)
			throw new IllegalStateException("Creaure Attribute Set is null");
		isCacheDirty = false;
		cache[AttributesTRAn.Luck.ordinal()] = // TODO to be continued
//				+(c.getValue(AttributesTRAn.Health.getIndex()) >> 3)
//						+ (c.getValue(AttributesTRAn.Wisdom.getIndex()) >> 3)
//						+ (c.getValue(AttributesTRAn.Faith.getIndex()) >> 3)
//						+ (c.getValue(AttributesTRAn.Intelligence.getIndex()) >> 3)
//						+ (c.getValue(AttributesTRAn.Dexterity) >> 3);
				(c.getValue(AttributesTRAn.Health) + c.getValue(AttributesTRAn.Wisdom)
						+ c.getValue(AttributesTRAn.Faith) + c.getValue(AttributesTRAn.Dexterity)
						+ c.getValue(AttributesTRAn.Intelligence)) >> 3;

		cache[AttributesTRAn.LifeMax.ordinal()] = c.getValue(AttributesTRAn.Constitution)
				+ (c.getValue(AttributesTRAn.Strength) >> 1) + (c.getValue(AttributesTRAn.Health) << 1);

		cache[AttributesTRAn.RegenLife.ordinal()] = //
				+((c.getValue(AttributesTRAn.Health) + (c.getValue(AttributesTRAn.Constitution) >> 1)
						+ (c.getValue(AttributesTRAn.Strength) / 3) // >> 2)
				) >> 2)//
						+ ((c.getValue(AttributesTRAn.Wisdom) + c.getValue(AttributesTRAn.Faith)) >> 5);
//			v >>= 1; // to high

		cache[AttributesTRAn.DamageReductionPhysical.ordinal()] = ( //
		+c.getValue(AttributesTRAn.Defense) //
				+ (((c.getValue(AttributesTRAn.Strength) >> 1) + c.getValue(AttributesTRAn.Constitution)
						+ (c.getValue(AttributesTRAn.Health) >> 2)) >> 1)
				+ (c.getValue(AttributesTRAn.Dexterity) / 5)//
		) >> 1;

		//

		cache[AttributesTRAn.DamageBonusPhysical.ordinal()] = ( //
		+((c.getValue(AttributesTRAn.Strength) + c.getValue(AttributesTRAn.Constitution)) >> 1)
				+ ((c.getValue(AttributesTRAn.Precision) + c.getValue(AttributesTRAn.Intelligence)) / 5)
				+ (c.getValue(AttributesTRAn.Wisdom) >> 3)//
		) >> 1;

		cache[AttributesTRAn.ManaMax.ordinal()] = (c.getValue(AttributesTRAn.Intelligence) >> 1) //
				+ (c.getValue(AttributesTRAn.Wisdom)) + (c.getValue(AttributesTRAn.Faith) << 1);
//			v >>= 1; // too high, make it half

		cache[AttributesTRAn.RegenMana.ordinal()] = //
				+(c.getValue(AttributesTRAn.Faith) + (c.getValue(AttributesTRAn.Intelligence) / 3)// >>
																									// 2)
						+ ((c.getValue(AttributesTRAn.Wisdom) + (c.getValue(AttributesTRAn.Health) >> 2)) >> 1)//
				) >> 2;
//		cache[AttributesTRAn.RigenMana.ordinal()] = ((c.getValue(AttributesTRAn.Faith))
//				+ (c.getValue(AttributesTRAn.Wisdom) >> 1)
//				+ (c.getValue(AttributesTRAn.Intelligence) >> 2)
//				+ (c.getValue(AttributesTRAn.Health) >> 3)) / 10;

		cache[AttributesTRAn.DamageBonusMagical.ordinal()] = ( //
		(c.getValue(AttributesTRAn.Precision) / 5) + c.getValue(AttributesTRAn.Intelligence)
				+ ((c.getValue(AttributesTRAn.Wisdom) + (c.getValue(AttributesTRAn.Faith) >> 1)) >> 1)//
		) >> 1;

		cache[AttributesTRAn.DamageReductionMagical.ordinal()] = ( //
		+(c.getValue(AttributesTRAn.Dexterity) / 5) + c.getValue(AttributesTRAn.Intelligence)
				+ c.getValue(AttributesTRAn.Wisdom) //
				+ ((c.getValue(AttributesTRAn.Defense) >> 2) + (c.getValue(AttributesTRAn.Faith)) >> 1)//
		) >> 1;

		cache[AttributesTRAn.CriticalMultiplier.ordinal()] = (c.getValue(AttributesTRAn.Strength) << 1)
				+ c.getValue(AttributesTRAn.Intelligence) + c.getValue(AttributesTRAn.Wisdom)
				+ c.getValue(AttributesTRAn.Luck);

		cache[AttributesTRAn.CriticalProbability.ordinal()] = //
				((c.getValue(AttributesTRAn.Precision) << 1) + c.getValue(AttributesTRAn.Dexterity)
						+ c.getValue(AttributesTRAn.Faith)//
						+ ((+c.getValue(AttributesTRAn.Intelligence) + c.getValue(AttributesTRAn.Wisdom)) >> 1)//
				) + c.getValue(AttributesTRAn.Luck);

		cache[AttributesTRAn.CriticalMultiplierReduction.ordinal()] = ( //
		+c.getValue(AttributesTRAn.Defense) + c.getValue(AttributesTRAn.Constitution) //
				+ ((+(c.getValue(AttributesTRAn.Strength) >> 1) + (c.getValue(AttributesTRAn.Dexterity) >> 2)) >> 1)//
		) >> 3;

		cache[AttributesTRAn.CriticalProbabilityAvoid.ordinal()] = (+c.getValue(AttributesTRAn.Constitution) //
				+ ((+c.getValue(AttributesTRAn.Dexterity)//
						+ ((c.getValue(AttributesTRAn.Defense) + ((c.getValue(AttributesTRAn.Precision)
								+ c.getValue(AttributesTRAn.Intelligence)) >> 1)) >> 1)) >> 1)//
		);

		cache[AttributesTRAn.ProbabilityHitPhysical.ordinal()] = //
				+c.getValue(AttributesTRAn.Precision) + c.getValue(AttributesTRAn.Luck)
						+ ((c.getValue(AttributesTRAn.Dexterity) + (c.getValue(AttributesTRAn.Strength) >> 1)
								+ (c.getValue(AttributesTRAn.Intelligence) >> 2)) >> 1);

		cache[AttributesTRAn.ProbabilityAvoidPhysical.ordinal()] = //
				+c.getValue(AttributesTRAn.Dexterity) + c.getValue(AttributesTRAn.Luck)
						+ ((c.getValue(AttributesTRAn.Precision) + (c.getValue(AttributesTRAn.Strength) >> 1)
								+ (c.getValue(AttributesTRAn.Intelligence) >> 2)) >> 1);

		cache[AttributesTRAn.ProbabilityHitMagical.ordinal()] = //
				+c.getValue(AttributesTRAn.Wisdom) + c.getValue(AttributesTRAn.Luck)
						+ ((c.getValue(AttributesTRAn.Intelligence) + (c.getValue(AttributesTRAn.Faith) >> 1)
								+ (c.getValue(AttributesTRAn.Precision) >> 2)) >> 1);

		cache[AttributesTRAn.ProbabilityAvoidMagical.ordinal()] = +c.getValue(AttributesTRAn.Luck)
				+ c.getValue(AttributesTRAn.Intelligence) + ((c.getValue(AttributesTRAn.Wisdom)
						+ (c.getValue(AttributesTRAn.Faith) >> 1) + (c.getValue(AttributesTRAn.Dexterity) >> 3)) >> 1);

		cache[AttributesTRAn.Velocity.ordinal()] = ((c.getValue(AttributesTRAn.Dexterity) << 1) / 5) //
				+ (((c.getValue(AttributesTRAn.Constitution) + c.getValue(AttributesTRAn.Strength)) << 1) / 15);
	}
}