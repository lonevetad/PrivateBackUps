package games.generic.controlModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import tools.Comparators;

/**
 * Manager of {@link GEvent}s. <br>
 * As described in {@link GEvent}, maybe applying all
 * <p>
 * NOTE:
 * <p>
 * Ogni GameEvent, come vedremo, ha un ID. Questo Manager ha un insieme di
 * GameEventHandler, plausibilmente suddisi per ciascun GameEvent. Ciascuna
 * suddivisione, qualora il GameEventManager implementasse effettivamente la
 * suddivisione, potrebbe essere una coda di priorità:
 * <ul>
 * <li>"n > 0": alta priorità: esempio, come la "bambola Voodoo" del gioco
 * Castlevenia, "se stai per morire, allora mi distruggo io e tu
 * rinasci/sopravvivi".</li>
 * <li>"h == 0": "importante ma non troppo". Esempio: "se muori tu, io, tua
 * fatina spirituale, muoio con te" o "questo buff/malus dura fino alla
 * morte"</li>
 * <li>"h < 0": low-priority</li>
 * </ul>
 * Questo manager dovrebbe gestire ogni singolo evento come se avvenissero in
 * contemporanea
 */
public abstract class GEventManager {
	GModality gameModality; // back reference
	Map<Integer, List<GEvent>> eventQueued;

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
	public Map<Integer, List<GEvent>> getEventQueued() {
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

	public abstract void notifyEventObservers(GEvent ge);

	public abstract void forEachEventObservers(Consumer<GEventObserver> action);

	//

	//

	public void addEvent(GEvent ge) {
		Integer id;
		List<GEvent> l; ///
		id = ge.getID();
		l = this.eventQueued.get(id);
		if (l == null) {
			l = new LinkedList<>();
			this.eventQueued.put(id, l);
		}
		l.add(ge);
	}

	public void performAllEvents() {
		final GModality gm;
		gm = this.gameModality;
		this.eventQueued.forEach((id, l) -> {
			while(!l.isEmpty()) {
				while(gm.checkIsRunningElseSleep()) { // make me sleep and waiting the game to be resumed
//					l.remove(0).performEvent(gm);
					notifyEventObservers(l.remove(0));
				}
			}
		});
	}
}