package games.theRisingAngel;

import games.generic.controlModel.misc.AttributeIdentifier;

public enum AttributesTRAr implements AttributeIdentifier {
	LifeMax, ManaMax, RigenLife, RigenMana, //
	Luck, Velocity,
	//
	DamageBonusPhysical, DamageBonusMagical, DamageReductionPhysical, DamageReductionMagical,
//
	Strength, Constitution, Health, //
	Dexterity, Precision, Defence, //
	Intelligence, Wisdom, Faith;

	public static final AttributesTRAr[] VALUES = AttributesTRAr.values();

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

	public static AttributesTRAr damageReductionByType(DamageTypesTRAr dt) {
		return (dt == DamageTypesTRAr.Physical) ? AttributesTRAr.DamageReductionPhysical
				: AttributesTRAr.DamageReductionMagical;
	}

	public static AttributesTRAr damageBonusByType(DamageTypesTRAr dt) {
		return (dt == DamageTypesTRAr.Physical) ? AttributesTRAr.DamageBonusPhysical
				: AttributesTRAr.DamageBonusMagical;
	}
}