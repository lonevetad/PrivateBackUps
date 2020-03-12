package games.generic.controlModel;

/**
 * interfaccia per definire un qualcosa che modifica il suo stato nel tempo, che
 * progredisce può definire metodi come "act", "move", etc può far innescare
 * eventi, come l'essersi mosso (utile per le "trappole a pressione", per
 * controllare le collisioni tra oggetti, ascoltatori di eventi come
 * "ogniqualvolta entra una creatura in campo, fai XYZ", etc...)
 */
public interface TimeProgressingObject{
	public void act(long milliseconds);
}