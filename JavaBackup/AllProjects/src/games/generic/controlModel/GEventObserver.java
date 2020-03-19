package games.generic.controlModel;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import games.generic.UniqueIDProvider;
import tools.Comparators;

/**
 * Object able to react upon {@link GEvent}s.<br>
 * Some subclasses (and sublasses of {@link GEventManager}) could implement
 * some more fine-grained notification system, refers to
 * {@link GEventManager} for more details, providing a list of specific
 * Events to watch (so, filtering the non required ones).
 */
public interface GEventObserver {
	public static final Comparator<GEventObserver> COMPARATOR_GameEventObserver = (o1, o2) -> {
		if (o1 == o2)
			return 0;
		if (o1 == null)
			return -1;
		if (o2 == null)
			return 1;
		return Comparators.INTEGER_COMPARATOR.compare(o1.getObserverID(), o2.getObserverID());
	};
	public static final Function<GEventObserver, Integer> KEY_EXTRACTOR = geo -> geo.getObserverPriority();

	//

	/**
	 * Notify this observer about the happening of an {@link GEvent}, referring
	 * to the current {@link GModality}.
	 * <p>
	 * The given {@link GModality} could be usefull, for instance:
	 * <ul>
	 * <li>if the object is an automatic turret, reacting to the movement of an
	 * object could trigger it to shoot</li>
	 * <li>if the object is a trap, the movement could trigger it</li>
	 * <li>if the object is an amulet and it reacts every time the character does
	 * something (receive damage, heals itself, pick money, etc), then could
	 * trigger</li>
	 * <li>if the object is a "saving from death tool", it could sacrifice itself
	 * invalidating a "DeathEvent" (a particular subclass of GameEvent, probably)
	 * and saving the related character</li>
	 * </ul>
	 */
	public void notifyEvent(GModality modality, GEvent ge);

	/**
	 * Some observers could requires different priorities, as described on
	 * {@link GEventManager}. The greater the value, the greater the priority
	 * (usually).<br>
	 * The default value is 0.
	 */
	public default int getObserverPriority() {
		return 0;
	}

	/**
	 * Optional method, it's strongly recommended to return a unique ID and it's
	 * left unimplemented to be more clear to subclasses, used by
	 * {@link GEventManager} along with {@link #getEventsWatching()}. See this
	 * last method for further informations.
	 * <p>
	 * Use {@link UniqueIDProvider} to help on creating IDs.
	 */
	public Integer getObserverID();

	/**
	 * Returns the list of {@link GEvent} (identified by its
	 * {@link GEvent#getType()}) to watch, meaning that "everything else" is
	 * ignored.<br>
	 * If empty or <code>null</code>, it should be intended as "watching everything"
	 * and {@link #getObserverID()} could be useful to distinguish from one observer
	 * to another.
	 * <p>
	 * NOTE: this is an optional method, it's not required by
	 * {@link GEventManager} to implement it (but it's usefull for fine-grained
	 * systems).
	 */
	public default List<String> getEventsWatching() {
		return null;
	}
}