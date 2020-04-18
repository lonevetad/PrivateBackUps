package games.theRisingAngel;

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
public enum AttributesTRAr implements AttributeIdentifier {
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

	public static final AttributesTRAr[] VALUES = AttributesTRAr.values();
	private static Map<String, AttributesTRAr> attTRArByName = null;

	public static AttributesTRAr getAttributeTRArByName(String name) {
		AttributesTRAr a;
		Map<String, AttributesTRAr> m;
		if (name == null)
			throw new IllegalArgumentException("Name cannot be null");
		if (attTRArByName == null) {
			m = attTRArByName = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
			for (AttributesTRAr at : VALUES) {
				m.put(at.name(), at); // using name() instead of getName() just to be faster
			}
		}
		a = attTRArByName.get(name);
		if (a == null)
			throw new IllegalArgumentException("Invalid name for AttributesTRAr: " + name);
		return a;
	}

	public static AttributesTRAr getAttributeTRArByIndex(int index) {
		return VALUES[index];
	}

	public static AttributesTRAr damageReductionByType(DamageTypesTRAr dt) {
		return (dt == DamageTypesTRAr.Physical) ? AttributesTRAr.DamageReductionPhysical
				: AttributesTRAr.DamageReductionMagical;
	}

	public static AttributesTRAr damageBonusByType(DamageTypesTRAr dt) {
		return (dt == DamageTypesTRAr.Physical) ? AttributesTRAr.DamageBonusPhysical
				: AttributesTRAr.DamageBonusMagical;
	}
}