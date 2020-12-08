package games.generic.controlModel.heal;

import java.io.Serializable;
import java.util.Set;

import games.generic.controlModel.heal.resExample.ExampleHealingType;

/**
 * Defines a way to hold some types of {@link AHealableResource}s.<br>
 * See {@link ExampleHealingType}.
 */
public interface IHealableResourcesHolder extends Serializable {
	public Set<IHealableResourceType> getHealableResourceTypes();

	public Set<AHealableResource> getHealableResources();

	//

	/**
	 * See {@link #getHealableResourceAmount(IHealableResourceType)}, but the delta
	 * could be negative. It's <b>added</b> to the current value.<br>
	 * This should be used in generic, intermediate implementations and should
	 * delegate the optional "event firing" to the end methods, like "healing
	 * gaining" or "resource loss".
	 */
	public void alterHealableResourceAmount(IHealableResourceType healType, int delta);

	public AHealableResource getHealableResourceFor(IHealableResourceType healType);

	//

	public default int getHealableResourceAmount(IHealableResourceType healType) {
		return this.getHealableResourceFor(healType).getAmount();
	}

	public default int getHealableResourceAmountMax(IHealableResourceType healType) {
		return this.getHealableResourceFor(healType).getAmountMax();
	}

	public default int getHealableResourceRegeneration(IHealableResourceType healType) {
		return this.getHealableResourceFor(healType).getRegenerationAmount();
	}

	/** See {@link #getHealableResourceAmount(IHealableResourceType)}. */
	public default void setHealableResourceAmount(IHealableResourceType healType, int value) {
		this.getHealableResourceFor(healType).setAmount(value);
	}

	/** See {@link #getHealableResourceAmountMax(IHealableResourceType)}. */
	public default void setHealableResourceAmountMax(IHealableResourceType healType, int value) {
		this.getHealableResourceFor(healType).setAmountMax(value);
	}

	/** See {@link #getHealableResourceRegeneration(IHealableResourceType)}. */
	public default void setHealableRegenerationAmount(IHealableResourceType healType, int value) {
		this.getHealableResourceFor(healType).setRegenerationAmount(value);
	}

	//

	public default void addHealableResourceType(IHealableResourceType healType) {
		addHealableResource(new HealableResourceImpl(healType));
	}

	public default void addHealableResource(AHealableResource cr) { getHealableResources().add(cr); }

	public default void removeHealableResourceType(IHealableResourceType healType) {
		getHealableResourceTypes().remove(healType);
	}

	public default void removeHealableResource(AHealableResource cr) { getHealableResources().remove(cr); }

	public default boolean containsHealableResourceType(IHealableResourceType healType) {
		return getHealableResourceTypes().contains(healType);
	}

	public default boolean containsHealableResource(AHealableResource cr) {
		return getHealableResources().contains(cr);
	}
}