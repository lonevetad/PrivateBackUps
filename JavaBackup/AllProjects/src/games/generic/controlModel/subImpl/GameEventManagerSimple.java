package games.generic.controlModel.subImpl;

import java.util.Map;
import java.util.function.BiConsumer;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GameEvent;
import games.generic.controlModel.GameEventManager;
import games.generic.controlModel.GameEventObserver;
import games.generic.controlModel.GameModality;
import tools.Comparators;

/** Broadcast the event to ALL observers, without any selection. */
public class GameEventManagerSimple extends GameEventManager {

	public Map<Integer, GameEventObserver> observers;
	protected EventNotifier notifier;

	public GameEventManagerSimple(GameModality gameModality) {
		super(gameModality);
		observers = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.INTEGER_COMPARATOR);
		this.notifier = new EventNotifier(this);
	}

	@Override
	public void addEventObserver(GameEventObserver geo) {
		this.observers.put(geo.getObserverID(), geo);
	}

	@Override
	public void removeEventObserver(GameEventObserver geo) {
		this.observers.remove(geo.getObserverID());
	}

	@Override
	public void removeAllEventObserver() {
		this.observers.clear();
	}

	@Override
	public void notifyEventObservers(GameEvent ge) {
		notifier.ge = ge;
		this.observers.forEach(notifier);
	}

	//

	//

	protected static class EventNotifier implements BiConsumer<Integer, GameEventObserver> {
		GameEvent ge;
		GameEventManager gem;

		public EventNotifier(GameEventManager gem) {
			super();
			this.gem = gem;
		}

		@Override
		public void accept(Integer t, GameEventObserver o) {
			o.notifyEvent(ge, gem.getGameModality());
		}
	}

}