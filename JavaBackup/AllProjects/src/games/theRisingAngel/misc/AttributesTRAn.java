package games.theRisingAngel.misc;

import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.misc.AttributeIdentifier;
import tools.Comparators;

/**
 * All attributes for creatures and player.
 * <p>
 * Important notes:
 * 
 */
public enum AttributesTRAn implements AttributeIdentifier {
	LifeMax, ManaMax, RigenLife, RigenMana, //
	Luck, Velocity,
	//
	Strength, Constitution, Health, //
	Defense, Dexterity, Precision, //
	Intelligence, Wisdom, Faith,
	//
	ProbabilityHit, ProbabilityAvoid,
	//
	DamageBonusPhysical, DamageBonusMagical, DamageReductionPhysical, DamageReductionMagical, //
	CriticalProbability, CriticalMultiplier //
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

	public static AttributesTRAn damageReductionByType(DamageTypesTRAr dt) {
		return (dt == DamageTypesTRAr.Physical) ? AttributesTRAn.DamageReductionPhysical
				: AttributesTRAn.DamageReductionMagical;
	}

	public static AttributesTRAn damageBonusByType(DamageTypesTRAr dt) {
		return (dt == DamageTypesTRAr.Physical) ? AttributesTRAn.DamageBonusPhysical
				: AttributesTRAn.DamageBonusMagical;
	}
}