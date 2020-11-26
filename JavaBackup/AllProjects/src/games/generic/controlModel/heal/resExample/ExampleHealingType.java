package games.generic.controlModel.heal.resExample;

import games.generic.controlModel.heal.IHealableResourceType;

/** Provides a set of examples of healing. */
public enum ExampleHealingType implements IHealableResourceType {
	Life, Mana, Shield, Stamina;

	final IDHolderHealableResourceType idHolderHRT;

	ExampleHealingType() { idHolderHRT = IHealableResourceType.newIDHolderHRT(); }

	@Override
	public String getName() { return name(); }

	@Override
	public boolean acceptsNegative() { return false; }

	@Override
	public boolean acceptsZeroAsMaximum() { return true; }

	@Override
	public IDHolderHealableResourceType getIDHolderHRT() { return idHolderHRT; }
}