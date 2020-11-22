package games.generic.controlModel.gObj.creature;

import games.generic.controlModel.misc.CurableResourceType;
import tools.ObjectNamedID;

public abstract class ACurableResource implements ObjectNamedID {
	private static final long serialVersionUID = -475214589669875230L;

	public ACurableResource(CurableResourceType resourceType) {
		super();
		this.resourceType = resourceType;
	}

	protected final CurableResourceType resourceType;

	public CurableResourceType getResourceType() { return resourceType; }

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