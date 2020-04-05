package games.theRisingAngel;

import games.generic.ObjectNamedID;

public enum DamageTypesTRAr implements ObjectNamedID {
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