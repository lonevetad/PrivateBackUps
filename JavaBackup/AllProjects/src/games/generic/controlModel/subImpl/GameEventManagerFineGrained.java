package games.generic.controlModel.subImpl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import dataStructures.PriorityQueueKey;
import games.generic.controlModel.GameEvent;
import games.generic.controlModel.GameEventManager;
import games.generic.controlModel.GameEventObserver;
import games.generic.controlModel.GameModality;
import games.generic.controlModel.subImpl.GameEventManagerSimple.EventNotifier;
import tools.Comparators;

public class GameEventManagerFineGrained extends GameEventManager {
	/** id observer -> observer */
	public Map<Integer, GameEventObserver> genericObservers;
	/** event id -> queue of observer, ordered by their priorities */
	public Map<Integer, PriorityQueueKey<GameEventObserver, Integer>> observers;
	protected EventNotifier notifierGeneric;
//	protected EventNotifierPQ notifier;
	protected EventNotifierE_PQ_ID notifierPQHelper;

	public GameEventManagerFineGrained(GameModality gameModality) {
		super(gameModality);
		observers = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.INTEGER_COMPARATOR);
		genericObservers = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
		// used to optimize iterations
		this.notifierGeneric = new EventNotifier(this);
//		this.notifier = new EventNotifierPQ(this);
		this.notifierPQHelper = new EventNotifierE_PQ_ID(this);
	}

	@Override
	public void addEventObserver(GameEventObserver geo) {
		Integer idGeo;
		List<Integer> l;
		idGeo = geo.getObserverID();
		l = geo.getEventsWatching();
		if (l == null || l.isEmpty()) {
			genericObservers.put(idGeo, geo);
		} else {
			l.forEach(idEvent -> {
				PriorityQueueKey<GameEventObserver, Integer> pq;
				pq = observers.get(idEvent);
				if (pq == null) {
					pq = new PriorityQueueKey<>(GameEventObserver.COMPARATOR_GameEventObserver,
							Comparators.INTEGER_COMPARATOR, GameEventObserver.KEY_EXTRACTOR);
					observers.put(idEvent, pq);
				}
				pq.put(geo);
			});
		}
	}

	@Override
	public void removeEventObserver(GameEventObserver geo) {
		Integer idGeo;
		List<Integer> l;
		idGeo = geo.getObserverID();
		l = geo.getEventsWatching();
		if (l == null || l.isEmpty()) {
			genericObservers.remove(idGeo);
		} else {
			l.forEach(idEvent -> {
				PriorityQueueKey<GameEventObserver, Integer> pq;
				pq = observers.get(idEvent);
				if (pq != null) {
					pq.remove(geo);
				}
			});
		}
	}

	@Override
	public void removeAllEventObserver() {
		this.observers.clear();
		this.genericObservers.clear();
	}

	@Override
	public void forEachEventObservers(Consumer<GameEventObserver> action) {
		Map<Integer, GameEventObserver> alreadySeenObservers;
		alreadySeenObservers = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
		this.observers.forEach((idEvent, pq) -> {
			pq.forEach(e -> {
				GameEventObserver geo;
				geo = e.getKey();
				alreadySeenObservers.put(geo.getObserverID(), geo);
				action.accept(geo);
			});
		});
		this.genericObservers.forEach((id, obs) -> {
			if (!alreadySeenObservers.containsKey(id)) {
				action.accept(obs);
			}
		});
	}

	@Override
	public void notifyEventObservers(GameEvent ge) {
//		Integer idEvent;
		PriorityQueueKey<GameEventObserver, Integer> pq;
		pq = observers.get(ge.getID());
		notifierGeneric.ge = ge;
		if (pq == null) {
			this.genericObservers.forEach(notifierGeneric);
		} else {
//			this.observers.forEach(notifier);
			pq.forEach(notifierPQHelper);
		}
	}

	//

	//

	/** Do not iterate over ALL observers */
	protected static class EventNotifierPQ
			implements BiConsumer<Integer, PriorityQueueKey<GameEventObserver, Integer>> {
		GameEvent ge;
		GameEventManagerFineGrained gem;

		public EventNotifierPQ(GameEventManagerFineGrained gem) {
			super();
			this.gem = gem;
		}

		@Override
		public void accept(Integer t, PriorityQueueKey<GameEventObserver, Integer> pq) {
			EventNotifierE_PQ_ID ee;
			ee = gem.notifierPQHelper;
			ee.ge = this.ge;
			pq.forEach(ee);
		}
	}

	protected static class EventNotifierE_PQ_ID implements Consumer<Map.Entry<GameEventObserver, Integer>> {
		GameEvent ge;
		GameEventManagerFineGrained gem;

		public EventNotifierE_PQ_ID(GameEventManagerFineGrained gem) {
			super();
			this.gem = gem;
		}

		@Override
		public void accept(Entry<GameEventObserver, Integer> e) {
			e.getKey().notifyEvent(ge, gem.getGameModality());
		}
	}
}