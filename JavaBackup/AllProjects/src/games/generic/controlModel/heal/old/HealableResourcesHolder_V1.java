package games.generic.controlModel.heal.old;

import java.util.Set;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.heal.AHealableResource;
import games.generic.controlModel.heal.HealableResource;
import games.generic.controlModel.heal.HealableResourcesHolder;
import games.generic.controlModel.heal.IHealableResourceType;
import games.generic.controlModel.heal.IHealableResourcesHolder;

/**
 * @deprecated replaced by {@link HealableResourcesHolder}
 */
@Deprecated
public class HealableResourcesHolder_V1 implements IHealableResourcesHolder {
	private static final long serialVersionUID = -201257L;

	public HealableResourcesHolder_V1() {
		this.mapHealableResources = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, IHealableResourceType.COMPARATOR_CURABLE_RES_TYPE);
		this.healableResources = this.mapHealableResources.toSetValue(cr -> cr.getResourceType());
		this.mhrAsSet = this.mapHealableResources.keySet();
	}

	protected MapTreeAVL<IHealableResourceType, AHealableResource> mapHealableResources;
	protected Set<IHealableResourceType> mhrAsSet;
	protected Set<AHealableResource> healableResources;

	@Override
	public int getHealableResourceAmount(IHealableResourceType healType) {
		return mapHealableResources.get(healType).getAmount();
	}

	@Override
	public void setHealableResourceAmount(IHealableResourceType healType, int value) {
		mapHealableResources.get(healType).setAmount(value);
	}

	@Override
	public void alterHealableResourceAmount(IHealableResourceType healType, int delta) {
		mapHealableResources.get(healType).alterResourceAmount(delta);
	}

	@Override
	public void addHealableResourceType(IHealableResourceType healType) {
		addHealableResource(new HealableResource(healType));
	}

	@Override
	public void addHealableResource(AHealableResource cr) { this.mapHealableResources.put(cr.getResourceType(), cr); }

	@Override
	public Set<IHealableResourceType> getHealableResourceTypes() { return mhrAsSet; }

	@Override
	public Set<AHealableResource> getHealableResources() { return healableResources; }

	@Override
	public AHealableResource getHealableResourceFor(IHealableResourceType healType) {
		return mapHealableResources.get(healType);
	}
}