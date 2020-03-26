package games.theRisingAngel;

import games.generic.controlModel.misc.AttributeIdentifier;

public enum AttributesTRAr implements AttributeIdentifier {
	LifeCurrent, LifeMax, ManaCurrent, ManaMax, RigenLife, RigenMana, //
	Luck, Velocity,
	//
	DamagePhysicalMin, DamagePhysicalMax, DamageMagic, DamageReductionPhysical, DamageReductionMagical,
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
}