package games.generic.controlModel.gObj;

import java.util.Set;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gEvents.EventEnteringOnMap;
import games.generic.controlModel.gEvents.EventLeavingMap;
import games.generic.controlModel.gEvents.EventOnMap;
import games.generic.controlModel.inventoryAbil.AbilitiesProvider;
import games.generic.controlModel.inventoryAbil.AbilityGeneric;
import games.generic.controlModel.subimpl.GameObjectsProvidersHolderRPG;

/**
 * Marks a "class" of creatures which all of them shares all abilities.<br>
 * To do so, upon entering into the game map, shares all of those abilities.<br>
 * The same is true for the "removing" event, to updae that set.
 * <p>
 * IMPLEMENTATION NOTE:
 * <ul>
 * <li>Each "class" MUST define its own
 * {@link #getClassCreaturesSharingAbilities()} to distinguish each of them.
 * </li>
 * <li>The list returned by {@link GEventObserver#getEventsWatching()} MUST
 * include the concept "creature entering"</li>
 * <li></li>
 * </ul>
 */
public interface CreatureSharingAbilities extends BaseCreatureRPG, GEventObserver {
	public String getClassCreaturesSharingAbilities();

	/** Override designed. */
	public default boolean isEventCreatureEnteringOnMap(IGEvent event) {
		return event instanceof EventEnteringOnMap;
	}

	public default boolean isEventCreatureLeavingMap(IGEvent event) {
		return event instanceof EventLeavingMap;
	}

	public String getAbilityToShare();

	public Set<String> getSharedAbilities();

	public void setSharedAbilities(Set<String> abilities);

	@Override
	public default void notifyEvent(GModality modality, IGEvent ge) {
		boolean isEntering, isLeaving;
		EventOnMap eom;
		String abilSharedByObj;
		CreatureSharingAbilities csa;
		Set<String> sharedAbl;
		AbilityGeneric ability;
		GameObjectsProvidersHolderRPG providersHolder;
		AbilitiesProvider aProv;
		isEntering = isEventCreatureEnteringOnMap(ge);
		isLeaving = isEventCreatureLeavingMap(ge);
		if (!(isEntering || isLeaving))
			return;
		eom = (EventOnMap) ge;
		if (!(eom.getObjectInvolved() instanceof CreatureSharingAbilities))
			return;
		csa = (CreatureSharingAbilities) eom.getObjectInvolved();
		if (this.getClassCreaturesSharingAbilities() != csa.getClassCreaturesSharingAbilities())
			return;
		sharedAbl = csa.getSharedAbilities();
		abilSharedByObj = csa.getAbilityToShare();
		if (isEntering) {
			setSharedAbilities(sharedAbl);
			sharedAbl.add(abilSharedByObj);
			providersHolder = (GameObjectsProvidersHolderRPG) modality.getGameObjectsProvider();
			aProv = providersHolder.getAbilitiesProvider();
			ability = aProv.getAbilityByName(modality, abilSharedByObj);
			if (ability != null) {
				this.getAbilities().put(abilSharedByObj, ability);
				ability.setOwner(this);
				ability.onAddingToOwner(modality);
			}
		} else { // isLeaving
			sharedAbl.remove(abilSharedByObj);
			ability = this.getAbilities().get(abilSharedByObj);
			if (ability != null) {
				ability.onRemoving(modality);
				// then remove it
				this.getAbilities().remove(abilSharedByObj);
			}
		}
	}

//	public default void forEachAbilities(Consumer<AbilityGeneric> action) {
//		getAbilities().forEach(action);
//getSharedAbilities().forEach(action);
//	}
}