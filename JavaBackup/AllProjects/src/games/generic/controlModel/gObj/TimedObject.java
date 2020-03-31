package games.generic.controlModel.gObj;

import games.generic.controlModel.GModality;
import tools.ObjectWithID;

/**
 * Mark an object as having a "status" (or at least a non empty set of actions)
 * that depends on the concept of time or, in particular, <i>evolves</i> as the
 * time goes on.
 * <p>
 * Used by {@link GModality}es that <re using some concept of "time" (real time
 * or turn-based) to describe objects performing actions over time.
 * 
 * <p>
 * This interaface could have been a special case of {@link GEventObserverb},
 * but it would have been heavily inefficient. It's a "hard-code / embedded"
 * solution for listening the time progression, but it's enought fast.
 * <p>
 * interfaccia per definire un qualcosa che modifica il suo stato nel tempo, che
 * progredisce può definire metodi come "act", "move", etc può far innescare
 * eventi, come l'essersi mosso (utile per le "trappole a pressione", per
 * controllare le collisioni tra oggetti, ascoltatori di eventi come
 * "ogniqualvolta entra una creatura in campo, fai XYZ", etc...)
 */
public interface TimedObject extends ObjectWithID {

	/**
	 * Evolve the status of this object depending on the elapsed time denoted by the
	 * second parameter.<br>
	 * Probably, this method's name is not the best clear and fitting one.
	 **/
	public void act(GModality modality, int milliseconds);
}