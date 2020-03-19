package games.generic.controlModel.gameObj;

import games.generic.controlModel.GModality;
import games.generic.controlModel.ObjectWIthID;

/**
 * Used for {@link GModality}es using some concept of "time" (real time or
 * turn-based) to describe objects performing actions over time.
 * <p>
 * interfaccia per definire un qualcosa che modifica il suo stato nel tempo, che
 * progredisce può definire metodi come "act", "move", etc può far innescare
 * eventi, come l'essersi mosso (utile per le "trappole a pressione", per
 * controllare le collisioni tra oggetti, ascoltatori di eventi come
 * "ogniqualvolta entra una creatura in campo, fai XYZ", etc...)
 */
public interface TimedObject extends ObjectWIthID {
	public void act(GModality modality, long milliseconds);
}