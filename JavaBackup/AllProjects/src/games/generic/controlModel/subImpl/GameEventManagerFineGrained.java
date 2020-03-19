package games.generic.controlModel.subImpl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import dataStructures.PriorityQueueKey;
import games.generic.controlModel.GEvent;
import games.generic.controlModel.GEventManager;
import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.subImpl.GameEventManagerSimple.EventNotifier;
import tools.Comparators;

public class GameEventManagerFineGrained extends GEventManager {
	/** id observer -> observer */
	public Map<Integer, GEventObserver> genericObservers;
	/** event id -> queue of observer, ordered by their priorities */
	public Map<String, PriorityQueueKey<GEventObserver, Integer>> observersByTypes;
	protected EventNotifier notifierGeneric;
//	protected EventNotifierPQ notifier;
	protected EventNotifierE_PQ_ID notifierPQHelper;

	public GameEventManagerFineGrained(GModality gameModality) {
		super(gameModality);
		observersByTypes = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.STRING_COMPARATOR);
		genericObservers = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
		// used to optimize iterations
		this.notifierGeneric = new EventNotifier(this);
//		this.notifier = new EventNotifierPQ(this);
		this.notifierPQHelper = new EventNotifierE_PQ_ID(this);
	}

	@Override
	public void addEventObserver(GEventObserver geo) {
		Integer idGeo;
		List<String> l;
		idGeo = geo.getObserverID();
		l = geo.getEventsWatching();
		if (l == null || l.isEmpty()) {
			genericObservers.put(idGeo, geo);
		} else {
			l.forEach(idEvent -> {
				PriorityQueueKey<GEventObserver, Integer> pq;
				pq = observersByTypes.get(idEvent);
				if (pq == null) {
					pq = new PriorityQueueKey<>(GEventObserver.COMPARATOR_GameEventObserver,
							Comparators.INTEGER_COMPARATOR, GEventObserver.KEY_EXTRACTOR);
					observersByTypes.put(idEvent, pq);
				}
				pq.put(geo);
			});
		}
	}

	@Override
	public void removeEventObserver(GEventObserver geo) {
		Integer idGeo;
		List<String> l;
		idGeo = geo.getObserverID();
		l = geo.getEventsWatching();
		if (l == null || l.isEmpty()) {
			genericObservers.remove(idGeo);
		} else {
			l.forEach(idEvent -> {
				PriorityQueueKey<GEventObserver, Integer> pq;
				pq = observersByTypes.get(idEvent);
				if (pq != null) {
					pq.remove(geo);
				}
			});
		}
	}

	@Override
	public void removeAllEventObserver() {
		this.observersByTypes.clear();
		this.genericObservers.clear();
	}

	@Override
	public void forEachEventObservers(Consumer<GEventObserver> action) {
		Map<Integer, GEventObserver> alreadySeenObservers;
		alreadySeenObservers = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
		this.observersByTypes.forEach((idEvent, pq) -> {
			pq.forEach(e -> {
				GEventObserver geo;
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
	public void notifyEventObservers(GEvent ge) {
//		Integer idEvent;
		PriorityQueueKey<GEventObserver, Integer> pq;
		pq = observersByTypes.get(ge.getType());
		notifierGeneric.ge = ge;
		if (pq == null) {
			this.genericObservers.forEach(notifierGeneric);
		} else {
//			this.observersByTypes.forEach(notifier);
			pq.forEach(notifierPQHelper);
		}
	}

	//

	//

	/** Do not iterate over ALL observersByTypes */
	protected static class EventNotifierPQ
			implements BiConsumer<Integer, PriorityQueueKey<GEventObserver, Integer>> {
		GEvent ge;
		GameEventManagerFineGrained gem;

		public EventNotifierPQ(GameEventManagerFineGrained gem) {
			super();
			this.gem = gem;
		}

		@Override
		public void accept(Integer t, PriorityQueueKey<GEventObserver, Integer> pq) {
			EventNotifierE_PQ_ID ee;
			ee = gem.notifierPQHelper;
			ee.ge = this.ge;
			pq.forEach(ee);
		}
	}

	protected static class EventNotifierE_PQ_ID implements Consumer<Map.Entry<GEventObserver, Integer>> {
		GEvent ge;
		GameEventManagerFineGrained gem;

		public EventNotifierE_PQ_ID(GameEventManagerFineGrained gem) {
			super();
			this.gem = gem;
		}

		@Override
		public void accept(Entry<GEventObserver, Integer> e) {
			e.getKey().notifyEvent(gem.getGameModality(), ge);
		}
	}
}