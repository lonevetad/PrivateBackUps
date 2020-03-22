package games.generic.controlModel.subImpl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import dataStructures.MapTreeAVL;
import dataStructures.PriorityQueueKey;
import games.generic.controlModel.GEvent;
import games.generic.controlModel.GEventManager;
import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.subImpl.GameEventManagerSimple.EventNotifier;
import tools.Comparators;

/**
 * Explanation of priorities:
 * <ul>
 * <li>"n > 0": alta priorità: esempio, come la "bambola Voodoo" del gioco
 * Castlevenia, "se stai per morire, allora mi distruggo io e tu
 * rinasci/sopravvivi".</li>
 * <li>"h == 0": "importante ma non troppo". Esempio: "se muori tu, io, tua
 * fatina spirituale, muoio con te" o "questo buff/malus dura fino alla
 * morte"</li>
 * <li>"h < 0": low-priority</li>
 * </ul>
 */
public class GEventManagerFineGrained extends GEventManager {
	/**
	 * The priority is negated because the {@link MapTreeAVL} orders its keys in
	 * growing order, giving priority to lower values.
	 */
	public static final Function<GEventObserver, Integer> KEY_EXTRACTOR_Embedded = geo -> (-geo.getObserverPriority());

	/** id observer -> observer */
	public Map<Integer, GEventObserver> genericObservers;
	/** event id -> queue of observer, ordered by their priorities */
	public Map<String, PriorityQueueKey<GEventObserver, Integer>> observersByTypes;
	protected EventNotifier notifierGeneric;
//	protected EventNotifierPQ notifier;
	protected EventNotifierE_PQ_ID notifierPQHelper;

	public GEventManagerFineGrained(GModality gameModality) {
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
							Comparators.INTEGER_COMPARATOR, KEY_EXTRACTOR_Embedded);
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
		System.out.println("........GEventManagerFineGrained notify event: " + ge);
		if (pq == null) {
			// no adequate observer is registered for this type: broadcast it to "everybody"
			System.out.println("\t for generic type observers: " + ge.getType());
			this.notifierGeneric.ge = ge;
			this.genericObservers.forEach(notifierGeneric);
		} else {
			System.out.println("\t for some type(s) observer(s): " + ge.getType());
//			this.observersByTypes.forEach(notifier);
			this.notifierPQHelper.ge = ge;
			pq.forEach(notifierPQHelper);
		}
	}

	//

	//

	/** Do not iterate over ALL observersByTypes */
	protected static class EventNotifierPQ implements BiConsumer<Integer, PriorityQueueKey<GEventObserver, Integer>> {
		GEvent ge;
		GEventManagerFineGrained gem;

		public EventNotifierPQ(GEventManagerFineGrained gem) {
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
		GEventManagerFineGrained gem;

		public EventNotifierE_PQ_ID(GEventManagerFineGrained gem) {
			super();
			this.gem = gem;
		}

		@Override
		public void accept(Entry<GEventObserver, Integer> e) {
			System.out.println("_____notifying event : " + ge);
			e.getKey().notifyEvent(gem.getGameModality(), ge);
		}
	}
}