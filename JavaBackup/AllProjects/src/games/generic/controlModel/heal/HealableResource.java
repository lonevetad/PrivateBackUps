package games.generic.controlModel.heal;

import games.theRisingAngel.creatures.BaseCreatureTRAn.HealableResourceTRAn;

/**
 * An implementation of {@link AHealableResource}, check it for an example.
 * <p>
 * An example: {@link HealableResourceTRAn}
 */
public class HealableResource extends AHealableResource {
	private static final long serialVersionUID = 5850480L;

	public HealableResource(IHealableResourceType ht) { super(ht); }

	protected int amount, amountMax, regenerationAmount;

	@Override
	public int getAmount() { return amount; }

	@Override
	public int getAmountMax() { return amountMax; }

	@Override
	public int getRegenerationAmount() { return regenerationAmount; }

	//

	//

	@Override
	public void setAmount(int resourceAmount) {
		if (resourceAmount < 0 && (!this.resourceType.acceptsNegative())) { resourceAmount = 0; }
		if ((resourceAmount > 0 && resourceAmount > amountMax) || (resourceAmount < 0 && resourceAmount < amountMax)) {
			resourceAmount = amountMax;
		}
		this.amount = resourceAmount;
	}

	@Override
	public void setAmountMax(int resourceAmountMax) {
		if (resourceAmountMax < 0 && (!this.resourceType.acceptsNegative())) {
			resourceAmountMax = this.resourceType.acceptsZeroAsMaximum() ? 0 : 1;
		} else if (resourceAmountMax == 0 && (!this.resourceType.acceptsZeroAsMaximum())) { resourceAmountMax = 1; }
		this.amountMax = resourceAmountMax;
		// set bounds
		if ((resourceAmountMax > 0 && resourceAmountMax < amount)
				|| (resourceAmountMax < 0 && resourceAmountMax > amount)) {
			this.amount = resourceAmountMax;
		}
	}

	@Override
	public void setRegenerationAmount(int regenerationAmount) { this.regenerationAmount = regenerationAmount; }
}