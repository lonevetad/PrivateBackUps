package games.generic.controlModel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

import games.generic.controlModel.subimpl.GEvent;
import games.generic.controlModel.subimpl.GModalityET;
import tools.ObjectWithID;

/**
 * One of the core classes.
 * <p>
 * Manager of {@link GEvent}s and their {@link GEventObserver}. In particular,
 * it's a {@link GObjectsHolder} of {@link GEventObserver}<br>
 * All {@link GEvent}s' effects should be applied "at the same time" because all
 * of them happens in the same "moment" (i.e. in the same cycle, implemented in
 * {@link GModality#doOnEachCycle(long)}) but it's obviously hard to implement.
 */
public abstract class GEventManager implements GObjectsHolder {
	protected GModalityET gameModality; // back reference
//	protected MapTreeAVL<Integer, Queue<IGEvent>> eventsQueued;
	protected Queue<IGEvent> eventsQueued;

	public GEventManager(GModalityET gameModality) {
		this.gameModality = gameModality;
		/*
		 * this.eventsQueued =
		 * MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
		 * Comparators.INTEGER_COMPARATOR);
		 */
		eventsQueued = new LinkedList<>();
	}

	//

	//

	public GModality getGameModality() { return gameModality; }

	/**
	 * Use with caution.
	 * <p>
	 * 15/04/2020:<br>
	 * I honestly forgot why the events was grouped by their ID (and not their
	 * name?) while a simple queue would have been much simpler.
	 */
//	public Map<Integer, Queue<IGEvent>> getEventQueued() { return eventsQueued;}

	//

	/***/
	public void setGameModality(GModalityET gameModality) { this.gameModality = gameModality; }

	//

	// TODO ABSTRACT

	public abstract void addEventObserver(GEventObserver geo);

	public abstract void removeEventObserver(GEventObserver geo);

	public abstract void removeAllEventObserver();

	public abstract void notifyEventObservers(IGEvent ge);

	public abstract void forEachEventObservers(Consumer<GEventObserver> action);

	//

	// TODO CONCRETE METHODS

	/** Similar to {@link Collection#clear()}. */
	@Override
	public boolean removeAll() {
		removeAllEventObserver();
		return true;
	}

	@Override
	public void forEach(Consumer<ObjectWithID> action) { forEachEventObservers(geo -> action.accept(geo)); }

	@Override
	public boolean add(ObjectWithID o) {
		if (o == null || (!(o instanceof GEventObserver)))
			return false;
		addEventObserver((GEventObserver) o);
		return true;
	}

	@Override
	public boolean remove(ObjectWithID o) {
		if (o == null || (!(o instanceof GEventObserver)))
			return false;
		removeEventObserver((GEventObserver) o);
		return true;
	}

	/**
	 * "Fire", "post", "add", call it as You want, but put the given {@link IGEvent}
	 * in some kind of event queue and (lately? separately? in a parallel way?)
	 * notify the {@link GEventObserver}s that the event has been occurred.
	 */
	public void fireEvent(IGEvent ge) {
		/**
		 * Integer id; Queue<IGEvent> l; // <br>
		 * if (ge == null) return; // <br>
		 * id = ge.getID(); // <br>
		 * l = this.eventsQueued.get(id); // <br>
		 * if (l == null) { // <br>
		 * l = new LinkedList<>(); // <br>
		 * this.eventsQueued.put(id, l); // <br>
		 * }
		 */
		if (ge.isRequirigImmediateProcessing()) {
			this.notifyEventObservers(ge);
		} else {
			eventsQueued.add(ge); // like offer
		}
	}

	/**
	 * Notify all observers and remove them from queue (Events cannot stay in the
	 * queue for more than one cycle because they are broadcasted at EACH game
	 * cycle, i.e. each {@link GModality#doOnEachCycle(long)})
	 */
	public void performAllEvents() {
		final GModalityET gm;
		Queue<IGEvent> q;
		IGEvent event;
		gm = this.gameModality;
		q = this.eventsQueued;
//		this.eventsQueued.forEach((id, q) -> {
		while (!q.isEmpty()) {

			// make me sleep and waiting the game to be resumed
			/**
			 * This pausing-on-fail-check has been added because the game thread should be
			 * paused as soon as possible if the user requested to pause the game, so the
			 * highest-grained checks should be performed.<br>
			 * If the {@link GModality} splits the "real game" and the "event management" in
			 * two different threads, then this check is way more required, to synchronize
			 * both threads upon sleeping and awakening.
			 */
			while ((!q.isEmpty()) && gm.isRunningOrSleep()) {
//					l.remove(0).performEvent(gm);
				notifyEventObservers(event = q.poll()); // remove the first event
				event.onProcessingEnded();
			}
		}
//		});
	}
}