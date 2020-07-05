package games.generic.controlModel.misc;

/** Provides a set of examples of healing. */
public enum HealingTypeExample implements CurableResourceType {
	Life, Mana, Shield, Stamina;

	@Override
	public Integer getID() { return ordinal(); }

	@Override
	public String getName() { return name(); }

	@Override
	public boolean acceptsNegative() { return false; }

	@Override
	public boolean acceptsZeroAsMaximum() { return true; }
}