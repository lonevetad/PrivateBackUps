package games.generic.controlModel.rechargeable.resources;

import games.generic.controlModel.holders.ResourceRechargeableHolder;
import tools.ObjectNamedID;

/**
 * Defines a resource (of the type {@literal IRechargeableResourceType}) that
 * can be recharged <i>and</i> its current amount, its maximum amount and its
 * amount of regeneration.<br>
 * The implementation is free to store the current and maximum values (and every
 * other ones) or delegates the updates to some other class holding the altered
 * fields value.
 */
public abstract class RechargableResource implements ObjectNamedID {
	private static final long serialVersionUID = -475214589669875230L;

	public RechargableResource(ResourceRechargeableHolder resourceHolder, RechargeableResourceType resourceType) {
		super();
		this.resourceHolder = resourceHolder;
		this.resourceType = resourceType;
	}

	protected final RechargeableResourceType resourceType;
	/** The one who owns this resource. */
	protected final ResourceRechargeableHolder resourceHolder;

	@Override
	public Long getID() { return resourceType.getID(); }

	@Override
	public String getName() { return resourceType.getName(); }

	public abstract int getAmount();

	public abstract int getMaxAmount();

	/** Returns the amount of resources that can be recharged each time. */
	public abstract int getRechargeAmount();

	public int getLowerBound() { return resourceType.getLowerBound(); }

	public int getUpperBound() { return resourceType.getUpperBound(); }

	public RechargeableResourceType getResourceType() { return resourceType; }

	//

	public abstract void setAmount(int resourceAmount);

	public abstract void setAmountMax(int resourceAmountMax);

	/** Sets the amount of resources that can be recharged each time. */
	public abstract void setRechargeAmount(int regenerationAmount);

	//

	/**
	 * Alter the current amount ({@link #getAmount()} by the provided integer delta
	 * (could be a negative value).
	 * 
	 * @param delta The amount of resource that will be recharged.
	 */
	public void performRechargeBy(int delta) {
		int a, t;
		a = this.getAmount() + delta;
		if (a < (t = this.resourceType.getLowerBound())) { a = t; }
		if (a > (t = this.resourceType.getUpperBound())) { a = t; }
		this.setAmount(a);
	}
}