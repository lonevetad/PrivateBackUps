package games.generic.controlModel.gObj.creature;

import java.util.Set;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.GModalityHolder;
import games.generic.controlModel.gObj.TimedObject;
import games.generic.controlModel.misc.CurableResourceType;
import games.generic.controlModel.misc.HealGeneric;
import games.generic.controlModel.misc.HealingTypeExample;
import games.generic.controlModel.subimpl.GEventInterfaceRPG;
import games.generic.controlModel.subimpl.GModalityET;
import tools.ObjectWithID;

public interface HealingObject extends TimedObject, GModalityHolder { //
	public static final int TICKS_PER_TIME_UNIT = 4, LOG_TICKS_PER_TIME_UNIT = 2;

	/** Helper for this class, just return a field of this class. */
	public CurableResourcesHolders getCurableResourcesHolders();

	/**
	 * "Init-like" method, used to set the Set of {@link #getAllCurableResources()}
	 * by calling {{@link #addCurableResource(CurableResource)}.
	 */
	public void defineAllCurableResources();

	/** Returns all healing types modeled by this class. */
	public default Set<CurableResourceType> getAllCurableResourceTypes() {
		return getCurableResourcesHolders().mcrAsSet;
	}

	/** Returns all healing resources modeled by this class. */
	public default Set<ACurableResource> getAllCurableResources() {
		return getCurableResourcesHolders().curableResources;
	}

	public default void addCurableResourceType(CurableResourceType healType) {
		getCurableResourcesHolders().addCurableResourceType(healType);
	}

	public default void addCurableResource(ACurableResource healType) {
		getCurableResourcesHolders().addCurableResource(healType);
	}

	public default int getCurableResourceMax(CurableResourceType healType) {
		return getCurableResourcesHolders().mapCurableResources.get(healType).getAmountMax();
	}

	public default int getCurableResourceAmount(HealingTypeExample shield) {
		return getCurableResourcesHolders().getCurableResourceAmount(shield);
	}

//	public void removeHealingType(HealingType healType);

	//
	/**
	 * Differently from {@link #getCurableResourceAmount(CurableResourceType)}, this
	 * method (and the setter) relies on the implementor, not the helper class
	 * {@link CurableResourcesHolders}, so no default implementation is provided.
	 * <p>
	 * To be meant "per time unit", like "per second".<br>
	 * For example, those could be life regeneration and mana regeneration.<br>
	 */
	// delegated to the implementor

	public int getCurableResourceRegeneration(CurableResourceType healType);

	//

	/** See {@link #getCurableResourceAmount(CurableResourceType)}. */
	public default void setCurableResourceAmount(CurableResourceType healType, int value) {
		getCurableResourcesHolders().setCurableResourceAmount(healType, value);
	}

	/** See {@link #getCurableResourceRegeneration(CurableResourceType)}. */
	public void setHealingRegenerationAmount(CurableResourceType healType, int value);

	/** See {@link #getCurableResourcesHolders()}. */
	public void setCurableResourcesHolders(CurableResourcesHolders curableResourcesHolders);

	//

	/** Short-hand to getters and setters. */
	public default void alterCurableResourceAmount(CurableResourceType healType, int delta) {
		getCurableResourcesHolders().alterCurableResourceAmount(healType, delta);
	}

	//

	/**
	 * NON mandatory aspect of this class.<br>
	 * If the "time unit" is subdivided into smaller units which the whole system is
	 * based on and the granularity is too little, then the rigeneration process
	 * could be imperfect (also due to the integer amounts, that makes hard to heal
	 * precisely and efficiently). Then macro-subdivisions of "time-units" and
	 * scaling of those smaller units can both be achieved through tickets.
	 */
	public int getTicksHealing();

	/**
	 * Returns {@link #TICKS_PER_TIME_UNIT}, see {@link #getTicksHealing()}.
	 */
	public default int getTicksPerTimeUnit() { return TICKS_PER_TIME_UNIT; }

	/**
	 * Each "time unit" could be composed by smaller units, as described in
	 * {@link #getTicksHealing()}. For instance, a second is composed by
	 * <code>1000</code> milliseconds and a "turn" is composed by just a single
	 * moment. This method returns that amount.
	 */
	public int getTimeSubunitsEachTimeUnits();

	/**
	 * Just calls
	 * <code>{@link #getTimeSubunitsEachTimeUnits()}/{@link #getTicksPerTimeUnit()}</code>.
	 */
	public default int getTimeSubunitsEachTicks() { return getTimeSubunitsEachTimeUnits() / getTicksPerTimeUnit(); }

	/**
	 * Protected method, do NOT override.
	 * <p>
	 * Get the accumulated time between each regeneration instance.
	 */
	public int getAccumulatedTimeRegen();

	//

	/** See {@link #getAccumulatedTimeRegen()}. */
	public void setAccumulatedTimeRegen(int newAccumulated);

	/** See {@link #getTicksHealing()}. */
	public void setTicksHealing(int ticks);

	//

	// core algorithms

	//

	public default void performAllHealings(GModality gm) {
		boolean isLastTick;
		int temp, ticksPerTimeUnits;
		CurableResourcesHolders crh;
		temp = getTicksHealing() + 1;
		ticksPerTimeUnits = getTicksPerTimeUnit();
		isLastTick = (temp >= TICKS_PER_TIME_UNIT);
		if (isLastTick)
			setTicksHealing(0);
		else
			setTicksHealing(temp);
		crh = getCurableResourcesHolders();
		// now apply healing to everybody
		this.getAllCurableResourceTypes().forEach(ht -> {
			int amountHealed, maxAmount, tempTotalResource;
			ACurableResource cr;
			cr = crh.mapCurableResources.get(ht);
			amountHealed = cr.getRegenerationAmount(); // getHealingRegenerationAmount(ht); // how much does I heal
														// myself?
			if (isLastTick) {
				// calculate the last piece of the cake not currently healed
				amountHealed -= (ticksPerTimeUnits - 1) * //
				(amountHealed / ticksPerTimeUnits);
			} else {
				amountHealed /= ticksPerTimeUnits;
			}
			if (amountHealed > 0) {
				maxAmount = cr.getAmountMax(); // getCurableResourceMax(ht);
				tempTotalResource = cr.getAmount() + amountHealed;
				if (tempTotalResource > maxAmount) {
					// check the maximum cap
					amountHealed -= (tempTotalResource - maxAmount);
					tempTotalResource = maxAmount;
				}
				if (amountHealed > 0) { // there is still an healing instance after the cap-check?
					cr.setAmount(tempTotalResource);
//			crh.getCurableResourceAmount(ht);
					fireHealingReceived(gm, this, newHealInstance(ht, amountHealed));
				}
			}
		});
	}

	@Override
	public default void act(GModality modality, int timeUnits) {
		int tfinal, subunitsEachTicks;
		if (timeUnits <= 0)
			return;
		tfinal = getAccumulatedTimeRegen() + timeUnits;
		subunitsEachTicks = getTimeSubunitsEachTicks();
		if (tfinal > subunitsEachTicks) {
			do {
				tfinal -= subunitsEachTicks;
				setAccumulatedTimeRegen(tfinal);
				performAllHealings(modality);
			} while (tfinal > subunitsEachTicks);
		} else {
			setAccumulatedTimeRegen(tfinal);
		}
	}

	//

	// healing stuffs

	//

	public HealGeneric newHealInstance(CurableResourceType healType, int healAmount);

	/**
	 * Make this object receiving a non-negative amount of healing, in a context
	 * expressed by {@link GModality}, which could be used to fire events.
	 */

	public default <SourceHealing extends ObjectWithID> void receiveHealing(GModality gm, SourceHealing source,
			HealGeneric healingInstance) {
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
			HealGeneric healInstance) {
		GEventInterfaceRPG geiRpg;
		geiRpg = (GEventInterfaceRPG) this.getGameModality().getGameObjectsManager().getGEventInterface();
		geiRpg.fireHealReceivedEvent((GModalityET) gm, source, (CreatureSimple) this, healInstance);
	}

	//

	// TODO CLASSES

	public static class CurableResourcesHolders {
//		protected int size;
//		protected int[] curableResources; // ArrayList-like
		protected MapTreeAVL<CurableResourceType, ACurableResource> mapCurableResources;
		protected Set<CurableResourceType> mcrAsSet;
		protected Set<ACurableResource> curableResources;

		public CurableResourcesHolders() {
			this.mapCurableResources = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
					MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, CurableResourceType.COMPARATOR_CURABLE_RES_TYPE);
			this.curableResources = this.mapCurableResources.toSetValue(cr -> cr.getResourceType());
			this.mcrAsSet = this.mapCurableResources.keySet();
		}

		public int getCurableResourceAmount(CurableResourceType healType) {
			return mapCurableResources.get(healType).getAmount();
		}

		/** See {@link #getCurableResourceAmount(CurableResourceType)}. */
		public void setCurableResourceAmount(CurableResourceType healType, int value) {
			mapCurableResources.get(healType).setAmount(value);
		}

		/**
		 * See {@link #getCurableResourceAmount(CurableResourceType)}, but the delta
		 * could be negative.
		 */
		public void alterCurableResourceAmount(CurableResourceType healType, int delta) {
			mapCurableResources.get(healType).alterResourceAmount(delta);
		}

		public void addCurableResourceType(CurableResourceType healType) {
			addCurableResource(new CurableResource(healType));
		}

		public void addCurableResource(ACurableResource cr) { this.mapCurableResources.put(cr.getResourceType(), cr); }

//		public void removeHealingType(HealingType healType) {		}
	}

}