package games.theRisingAngel.misc;

import tools.ObjectNamedID;

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