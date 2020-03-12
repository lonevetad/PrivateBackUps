package games.generic.controlModel;

/**
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
public abstract class GameEventManager {
}
