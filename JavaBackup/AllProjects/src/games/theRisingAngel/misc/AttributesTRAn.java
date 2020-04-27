package games.theRisingAngel.misc;

import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.misc.AttributeIdentifier;
import tools.Comparators;

/**
 * All attributes for creatures and player.
 * <p>
 * Important notes:
 * <ul>
 * <li>Probabilities and multipliers are meant o be a percentage</li>
 * <li>Luck should add up to the above concepts and to the drop (what kinds of
 * drops, rarity, amount of modifiers and abilities and their rarities).</li>
 * <li>Velocity is free to interpretation (usually, "10-th of a unit each
 * second").</li>
 * <li>Probabilities to hit and avoid are competitive: upon dealing receiving an
 * attack, the attacker:</li>
 * </ul>
 */
public enum AttributesTRAn implements AttributeIdentifier {
	Strength, Constitution, Health, //
	Defense, Dexterity, Precision, //
	Intelligence, Wisdom, Faith,
	//
	LifeMax, ManaMax, RigenLife, RigenMana, //
	Luck, Velocity,
	//
	DamageBonusPhysical, DamageReductionPhysical, //
	ProbabilityHitPhysical, ProbabilityAvoidPhysical, //
	DamageBonusMagical, DamageReductionMagical, //
	ProbabilityHitMagical, ProbabilityAvoidMagical, //
	//
	CriticalProbability, CriticalMultiplier //
	, LifeLeechPercentage, ManaLeechPercentage//
	;

	@Override
	public int getIndex() {
		return ordinal();
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public Integer getID() {
		return ordinal();
	}

	//

	public static final int ATTRIBUTES_UPGRADABLE_COUNT = 9, FIRST_INDEX_ATTRIBUTE_UPGRADABLE = Strength.getIndex();
	public static final AttributesTRAn[] VALUES = AttributesTRAn.values();
	private static Map<String, AttributesTRAn> attTRArByName = null;

	public static AttributesTRAn getAttributeTRArByName(String name) {
		AttributesTRAn a;
		Map<String, AttributesTRAn> m;
		if (name == null)
			throw new IllegalArgumentException("Name cannot be null");
		if (attTRArByName == null) {
			m = attTRArByName = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
			for (AttributesTRAn at : VALUES) {
				m.put(at.name(), at); // using name() instead of getName() just to be faster
			}
		}
		a = attTRArByName.get(name);
		if (a == null)
			throw new IllegalArgumentException("Invalid name for AttributesTRAr: " + name);
		return a;
	}

	public static AttributesTRAn getAttributeTRArByIndex(int index) {
		return VALUES[index];
	}

	public static AttributesTRAn damageReductionByType(DamageTypesTRAn dt) {
		return (dt == DamageTypesTRAn.Physical) ? AttributesTRAn.DamageReductionPhysical
				: AttributesTRAn.DamageReductionMagical;
	}

	public static AttributesTRAn damageBonusByType(DamageTypesTRAn dt) {
		return (dt == DamageTypesTRAn.Physical) ? AttributesTRAn.DamageBonusPhysical
				: AttributesTRAn.DamageBonusMagical;
	}
}