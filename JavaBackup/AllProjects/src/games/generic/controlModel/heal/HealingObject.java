package games.generic.controlModel.heal;

import java.util.Set;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.GModalityHolder;
import games.generic.controlModel.gObj.TimedObject;
import games.generic.controlModel.heal.resExample.ExampleHealingType;
import games.generic.controlModel.subimpl.GEventInterfaceRPG;
import games.generic.controlModel.subimpl.GModalityET;
import tools.ObjectWithID;

public interface HealingObject extends TimedObject, GModalityHolder { //

	/** Helper to realize the way to heal stuffs over time */
	public IHealingResourcesOverTimeStrategy getHealingStrategyHelper();

	/** Helper for this class, just return a field of this class. */
	public IHealableResourcesHolder getHealableResourcesHolder();

	/** See {@link #getHealingStrategyHelper()}. */
	public void setHealingStrategyHelper(IHealingResourcesOverTimeStrategy healingStrategyHelper);

	/** See {@link #getHealableResourcesHolder()}. */
	public void setHealableResourcesHolder(IHealableResourcesHolder curableResourcesHolder);

	/**
	 * "Init-like" method, used to set the Set of {@link #getAllHealableResources()}
	 * by calling {{@link #addHealableResource(HealableResource)}.
	 */
	public void defineAllHealableResources();

	//

	// delegators

	//

	/** Returns all healing types modeled by this class. */
	public default Set<IHealableResourceType> getAllHealableResourceTypes() {
		return this.getHealableResourcesHolder().getHealableResourceTypes();
	}

	/** Returns all healing resources modeled by this class. */
	public default Set<AHealableResource> getAllHealableResources() {
		return this.getHealableResourcesHolder().getHealableResources();
	}

	/** Retrives the resource associated with the given type. */
	public default AHealableResource getHealableResourceFor(IHealableResourceType healType) {
		return this.getHealableResourcesHolder().getHealableResourceFor(healType);
	}

	public default void addHealableResourceType(IHealableResourceType healType) {
		this.getHealableResourcesHolder().addHealableResourceType(healType);
	}

	public default void addHealableResource(AHealableResource healType) {
		this.getHealableResourcesHolder().addHealableResource(healType);
	}

	public default void removeHealableResourceType(IHealableResourceType healType) {
		this.getHealableResourcesHolder().removeHealableResourceType(healType);
	}

	public default void removeHealableResource(AHealableResource cr) {
		this.getHealableResourcesHolder().removeHealableResource(cr);
	}

	public default boolean containsHealableResourceType(IHealableResourceType healType) {
		return this.getHealableResourcesHolder().containsHealableResourceType(healType);
	}

	public default boolean containsHealableResource(AHealableResource cr) {
		return this.getHealableResourcesHolder().containsHealableResource(cr);
	}

	//

	public default int getHealableResourceAmount(ExampleHealingType shield) {
		return this.getHealableResourcesHolder().getHealableResourceAmount(shield);
	}

	public default int getHealableResourceAmountMax(IHealableResourceType healType) {
		return this.getHealableResourcesHolder().getHealableResourceAmountMax(healType);
	}

	public default int getHealableResourceRegeneration(IHealableResourceType healType) {
		return this.getHealableResourcesHolder().getHealableResourceRegeneration(healType);
	}

	//

	/** See {@link #getHealableResourceAmount(IHealableResourceType)}. */
	public default void setHealableResourceAmount(IHealableResourceType healType, int value) {
		this.getHealableResourcesHolder().setHealableResourceAmount(healType, value);
	}

	/** See {@link #getHealableResourceAmountMax(IHealableResourceType)}. */
	public default void setHealableResourceAmountMax(IHealableResourceType healType, int value) {
		this.getHealableResourcesHolder().setHealableResourceAmountMax(healType, value);
	}

	/** See {@link #getHealableResourceRegeneration(IHealableResourceType)}. */
	public default void setHealableRegenerationAmount(IHealableResourceType healType, int value) {
		this.getHealableResourcesHolder().setHealableRegenerationAmount(healType, value);
	}

	//

	/** Short-hand to getters and setters. */
	public default void alterCurableResourceAmount(IHealableResourceType healType, int delta) {
		getHealableResourcesHolder().alterHealableResourceAmount(healType, delta);
	}

	// end delegators

	//

	// core algorithms

	//

	@Override
	public default void act(GModality modality, int timeUnits) { getHealingStrategyHelper().act(modality, timeUnits); }

	///

	public abstract HealAmountInstance newHealInstance(IHealableResourceType healType, int healAmount);

	/**
	 * See {@link #receiveHealing(GModality, ObjectWithID, HealAmountInstance)},
	 * passing this held instances.
	 */
	public default void healMyself(HealAmountInstance healingInstance) {
		receiveHealing(getGameModality(), this, healingInstance);
	}

	/**
	 * Make this object receiving a non-negative amount of healing, in a context
	 * expressed by {@link GModality}, which could be used to fire events.
	 */
	public default <SourceHealing extends ObjectWithID> void receiveHealing(GModality gm, SourceHealing source,
			HealAmountInstance healingInstance) {
		int healingAmount;
		healingAmount = healingInstance.getHealAmount();
		if (healingAmount > 0) {
			this.alterCurableResourceAmount(healingInstance.getHealType(), healingAmount);
			fireHealingReceived(gm, source, healingInstance);
		}
	}

	/**
	 * Similar to {@link #fireDamageReceived( GModality, int, int)}, but about
	 * healing.
	 */
	public default <SourceHealing extends ObjectWithID> void fireHealingReceived(GModality gm, SourceHealing source,
			HealAmountInstance healInstance) {
		GEventInterfaceRPG geiRpg;
		geiRpg = (GEventInterfaceRPG) this.getGameModality().getGameObjectsManager().getGEventInterface();
		geiRpg.fireHealReceivedEvent((GModalityET) gm, source, (CreatureSimple) this, healInstance);
	}
}