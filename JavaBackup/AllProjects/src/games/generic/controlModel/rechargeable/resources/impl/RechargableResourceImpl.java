package games.generic.controlModel.rechargeable.resources.impl;

import games.generic.controlModel.holders.ResourceRechargeableHolder;
import games.generic.controlModel.rechargeable.resources.RechargableResource;
import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;

public class RechargableResourceImpl extends RechargableResource {
	private static final long serialVersionUID = -635242000005L;

	public RechargableResourceImpl(ResourceRechargeableHolder resourceHolder, RechargeableResourceType resourceType) {
		super(resourceHolder, resourceType);
		this.amountMax = Math.min(1, resourceType.getUpperBound());
		this.amount = this.amountMax;
		this.rechargeAmount = 0;
	}

	protected int amount, amountMax, rechargeAmount;

	@Override
	public int getAmount() { return amount; }

	@Override
	public int getMaxAmount() { return amountMax; }

	@Override
	public int getRechargeAmount() { return rechargeAmount; }

	//

	@Override
	public void setAmount(int amount) {
		if (amount > this.amountMax) { amount = this.amountMax; }
		this.amount = amount;
	}

	@Override
	public void setAmountMax(int amountMax) {
		int t;
		if (amountMax < (t = this.resourceType.getLowerBound())) { amountMax = t; }
		if (amountMax > (t = this.resourceType.getUpperBound())) { amountMax = t; }
		this.amountMax = amountMax;
		if (amountMax < this.amount) { this.amount = amountMax; }
	}

	@Override
	public void setRechargeAmount(int rechargeAmount) { this.rechargeAmount = rechargeAmount; }
}