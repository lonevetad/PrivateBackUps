package games.theRisingAngel.misc;

import games.generic.controlModel.damage.DamageTypeGeneric;

public enum DamageTypesTRAn implements DamageTypeGeneric {
	Physical, Magical;

	@Override
	public Integer getID() {
		return ordinal();
	}

	@Override
	public String getName() {
		return name();
	}
}