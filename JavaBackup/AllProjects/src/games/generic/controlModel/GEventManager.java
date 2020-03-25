package games.generic.controlModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.subImpl.GEvent;
import tools.Comparators;

/**
 * Manager of {@link GEvent}s. <br>
 * All {@link GEvent}s' effects should be applied "at the same time" because all
 * of them happens in the same "moment" (i.e. in the same cycle, implemented in
 * {@link GModality#doOnEachCycle(long)}) but it's obviously hard to implement.
 */
public abstract class GEventManager {
	GModality gameModality; // back reference
	MapTreeAVL<Integer, List<IGEvent>> eventQueued;

	public GEventManager(GModality gameModality) {
		this.gameModality = gameModality;
		this.eventQueued = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.INTEGER_COMPARATOR);
		// <Integer, List<GameEvent>>
	}

	//

	//

	public GModality getGameModality() {
		return gameModality;
	}

	/** Use with caution. */
	public Map<Integer, List<IGEvent>> getEventQueued() {
		return eventQueued;
	}

	public void setGameModality(GModality gameModality) {
		this.gameModality = gameModality;
	}

	//

	// TODO ABSTRACT

	public abstract void addEventObserver(GEventObserver geo);

	public abstract void removeEventObserver(GEventObserver geo);

	public abstract void removeAllEventObserver();

	public abstract void notifyEventObservers(IGEvent ge);

	public abstract void forEachEventObservers(Consumer<GEventObserver> action);

	//

	// TODO CONCRETE METHODS

	public void addEvent(IGEvent ge) {
		Integer id;
		List<IGEvent> l; ///
		if (ge == null)
			return;
		id = ge.getID();
		l = this.eventQueued.get(id);
		if (l == null) {
			l = new LinkedList<>();
			this.eventQueued.put(id, l);
		}
		System.out.println("--- GEventManager adding printer event : " + ge);
		l.add(ge);
	}

	/**
	 * Notify all observers and remove them from queue (Events cannot stay in the
	 * queue for more than one cycle because they are broadcasted at EACH game
	 * cycle, i.e. each {@link GModality#doOnEachCycle(long)})
	 */
	public void performAllEvents() {
		final GModality gm;
		gm = this.gameModality;
		this.eventQueued.forEach((id, l) -> {
			while(!l.isEmpty()) {

				// make me sleep and waiting the game to be resumed
				/**
				 * This pausing-on-fail-check has been added because the game thread should be
				 * paused as soon as possible if the user requested to pause the game, so the
				 * highest-grained checks should be performed.<br>
				 * If the {@link GModality} splits the "real game" and the "event management" in
				 * two different threads, then this check is way more required, to synchronize
				 * both threads upon sleeping and awakening.
				 */
				while((!l.isEmpty()) && gm.isRunningOrSleep()) {
//					l.remove(0).performEvent(gm);
					notifyEventObservers(l.remove(0));
				}
			}
		});
	}
}