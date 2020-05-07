package games.generic.controlModel.subimpl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import dataStructures.MapTreeAVL;
import dataStructures.PriorityQueueKey;
import games.generic.controlModel.GEventManager;
import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.subimpl.GEventManagerSimple.EventNotifier;
import tools.Comparators;
import tools.ObjectWithID;

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

	public GEventManagerFineGrained(GModalityET gameModality) {
		super(gameModality);
		this.observersByTypes = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.STRING_COMPARATOR);
		this.genericObservers = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
		allObsMap = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
		this.observersSet = allObsMap.toSetValue(ObjectWithID.KEY_EXTRACTOR);
		// used to optimize iterations
		this.notifierGeneric = new EventNotifier(this);
//		this.notifier = new EventNotifierPQ(this);
		this.notifierPQHelper = new EventNotifierE_PQ_ID(this);
	}

	protected Set<ObjectWithID> observersSet;
	protected MapTreeAVL<Integer, ObjectWithID> allObsMap;
	/** id observer -> observer */
	protected Map<Integer, GEventObserver> genericObservers;
	/** event id -> queue of observer, ordered by their priorities */
	protected Map<String, PriorityQueueKey<GEventObserver, Integer>> observersByTypes;
	protected EventNotifier notifierGeneric;
//	protected EventNotifierPQ notifier;
	protected EventNotifierE_PQ_ID notifierPQHelper;

	//

	@Override
	public void addEventObserver(GEventObserver geo) {
		Integer idGeo;
		List<String> l;
		idGeo = geo.getObserverID();
		observersSet.add(geo);
		l = geo.getEventsWatching();
		if (l == null || l.isEmpty()) {
			// no specialization found, just add it to the "generic bin"
			genericObservers.put(idGeo, geo);
		} else {
			// some specialization found: add the observer to all queues
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
		observersSet.remove(geo);
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
	public Set<ObjectWithID> getObjects() {
		return observersSet;
	}

	@Override
	public ObjectWithID get(Integer id) {
		return this.allObsMap.get(id);
	}

	@Override
	public boolean contains(ObjectWithID o) {
		return observersSet.contains(o);
	}

	@Override
	public void removeAllEventObserver() {
		this.observersByTypes.clear();
		this.genericObservers.clear();
		this.allObsMap.clear();
	}

	@Override
	public void forEachEventObservers(Consumer<GEventObserver> action) {
//		Map<Integer, GEventObserver> alreadySeenObservers;
//		alreadySeenObservers = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
//		this.observersByTypes.forEach((idEvent, pq) -> {
//			pq.forEach(e -> {
//				GEventObserver geo;
//				geo = e.getKey();
//				alreadySeenObservers.put(geo.getObserverID(), geo);
//				action.accept(geo);
//			});
//		});
//		this.genericObservers.forEach((id, obs) -> {
//			if (!alreadySeenObservers.containsKey(id)) {
//				action.accept(obs);
//			}
//		});
		observersSet.forEach(o -> action.accept((GEventObserver) o));
	}

	@Override
	public void notifyEventObservers(IGEvent ge) {
//		Integer idEvent;
		PriorityQueueKey<GEventObserver, Integer> pq;
		pq = observersByTypes.get(ge.getName());
//		System.out.println("........GEventManagerFineGrained notify event: " + ge);
		if (pq == null) {
			// no adequate observer is registered for this type: broadcast it to "everybody"
//			System.out.println("\t for generic type observers: " + ge.getName());
			this.notifierGeneric.ge = ge;
			this.genericObservers.forEach(notifierGeneric);
		} else {
//			System.out.println("\t for some type(s) observer(s): " + ge.getName());
//			this.observersByTypes.forEach(notifier);
			this.notifierPQHelper.ge = ge;
			pq.forEach(notifierPQHelper);
		}
	}

	//

	//

	/** Do not iterate over ALL observersByTypes */
	protected static class EventNotifierPQ implements BiConsumer<Integer, PriorityQueueKey<GEventObserver, Integer>> {
		IGEvent ge;
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
		IGEvent ge;
		GEventManagerFineGrained gem;

		public EventNotifierE_PQ_ID(GEventManagerFineGrained gem) {
			super();
			this.gem = gem;
		}

		@Override
		public void accept(Entry<GEventObserver, Integer> e) {
			e.getKey().notifyEvent(gem.getGameModality(), ge);
		}
	}
}