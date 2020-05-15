package games.generic.controlModel.subimpl;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GEventManager;
import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.IGEvent;
import tools.Comparators;
import tools.ObjectWithID;

/** Broadcast the event to ALL observers, without any selection. */
public class GEventManagerSimple extends GEventManager {

	public Set<ObjectWithID> observersSet;
	public MapTreeAVL<Integer, ObjectWithID> observers; // previously GEventObserver
	protected EventNotifier notifier;

	public GEventManagerSimple(GModalityET gameModality) {
		super(gameModality);
		this.observers = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.INTEGER_COMPARATOR);
		this.observersSet = this.observers.toSetValue(ObjectWithID.KEY_EXTRACTOR);
		this.notifier = new EventNotifier(this);
	}

	@Override
	public boolean addEventObserver(GEventObserver geo) {
		if (this.observers.containsKey(geo.getID()))
			return false;
		this.observers.put(geo.getObserverID(), geo);
		return true;
	}

	@Override
	public boolean removeEventObserver(GEventObserver geo) {
		if (this.observers.containsKey(geo.getID())) {
			this.observers.remove(geo.getObserverID());
			return true;
		}
		return false;
	}

	@Override
	public void removeAllEventObserver() { this.observers.clear(); }

	@Override
	public void forEachEventObservers(Consumer<GEventObserver> action) {
		this.observers.forEach((id, obs) -> action.accept((GEventObserver) obs));
	}

	@Override
	public void notifyEventObservers(IGEvent ge) {
		notifier.ge = ge;
		this.observers.forEach(notifier);
	}

	@Override
	public Set<ObjectWithID> getObjects() { return this.observersSet; }

	@Override
	public boolean contains(ObjectWithID o) { return this.observers.containsKey(o.getID()); }

	@Override
	public ObjectWithID get(Integer id) { return this.observers.get(id); }

	//

	//

	protected static class EventNotifier implements BiConsumer<Integer, ObjectWithID> {
		IGEvent ge;
		GEventManager gem;

		public EventNotifier(GEventManager gem) {
			super();
			this.gem = gem;
		}

		@Override
		public void accept(Integer t, ObjectWithID o) { ((GEventObserver) o).notifyEvent(gem.getGameModality(), ge); }
	}
}