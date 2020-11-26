package games.generic.controlModel.heal;

import tools.ObjectNamedID;

/**
 * A concrete definition of {@link IHealableResourceType} (see
 * {@link #getResourceType()} to check what resource actually wraps) holding the
 * current amount, the maximum amount (the minimum is considered <code>0</code>
 * by default) and the amount of regeneration.<br>
 * The <i>regeneration</i> is meant to be <i>provided after passing a specific
 * amount of time, depending on the game's implementation</i>.
 */
public abstract class AHealableResource implements ObjectNamedID {
	private static final long serialVersionUID = -475214589669875230L;

	public AHealableResource(IHealableResourceType resourceType) {
		super();
		this.resourceType = resourceType;
	}

	protected final IHealableResourceType resourceType;

	/**
	 * The type of resource this instance wraps.
	 * <p>
	 * See {@link IHealableResourceType}.
	 */
	public IHealableResourceType getResourceType() { return resourceType; }

	@Override
	public Integer getID() { return resourceType.getID(); }

	@Override
	public String getName() { return resourceType.getName(); }

	public abstract int getAmount();

	public abstract int getAmountMax();

	public abstract int getRegenerationAmount();

	//

	//

	public abstract void setAmount(int resourceAmount);

	public abstract void setAmountMax(int resourceAmountMax);

	public abstract void setRegenerationAmount(int regenerationAmount);

	public void alterResourceAmount(int delta) { setAmount(this.getAmount() + delta); }
}