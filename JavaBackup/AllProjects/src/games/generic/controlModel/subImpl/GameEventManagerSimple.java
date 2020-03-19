package games.generic.controlModel.subImpl;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GEvent;
import games.generic.controlModel.GEventManager;
import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import tools.Comparators;

/** Broadcast the event to ALL observers, without any selection. */
public class GameEventManagerSimple extends GEventManager {

	public Map<Integer, GEventObserver> observers;
	protected EventNotifier notifier;

	public GameEventManagerSimple(GModality gameModality) {
		super(gameModality);
		observers = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.INTEGER_COMPARATOR);
		this.notifier = new EventNotifier(this);
	}

	@Override
	public void addEventObserver(GEventObserver geo) {
		this.observers.put(geo.getObserverID(), geo);
	}

	@Override
	public void removeEventObserver(GEventObserver geo) {
		this.observers.remove(geo.getObserverID());
	}

	@Override
	public void removeAllEventObserver() {
		this.observers.clear();
	}

	@Override
	public void forEachEventObservers(Consumer<GEventObserver> action) {
		this.observers.forEach((id, obs) -> action.accept(obs));
	}

	@Override
	public void notifyEventObservers(GEvent ge) {
		notifier.ge = ge;
		this.observers.forEach(notifier);
	}

	//

	//

	protected static class EventNotifier implements BiConsumer<Integer, GEventObserver> {
		GEvent ge;
		GEventManager gem;

		public EventNotifier(GEventManager gem) {
			super();
			this.gem = gem;
		}

		@Override
		public void accept(Integer t, GEventObserver o) {
			o.notifyEvent(gem.getGameModality(), ge);
		}
	}

}