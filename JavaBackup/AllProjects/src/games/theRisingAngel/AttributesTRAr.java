package games.theRisingAngel;

import games.generic.controlModel.misc.AttributeIdentifier;

public enum AttributesTRAr implements AttributeIdentifier {
	Life, LifeMax, Mana, ManaMax, RigenLife, RigenMana, //
	Luck, Velocity,
	//
	DamageMin, DamageMax, MagicDamage,
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
}