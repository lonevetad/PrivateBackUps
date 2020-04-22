package games.generic.controlModel.misc;

import tools.ObjectNamedID;

/** Provides a set of examples of healing. */
public enum HealingTypeExample implements ObjectNamedID {
	Life, Mana, Shield, Stamina;

	@Override
	public Integer getID() {
		return ordinal();
	}

	@Override
	public String getName() {
		return name();
	}
}