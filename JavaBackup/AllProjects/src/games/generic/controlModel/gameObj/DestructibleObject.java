package games.generic.controlModel.gameObj;

import games.generic.controlModel.GEventManager;
import games.generic.controlModel.GModality;
import games.generic.controlModel.ObjectWIthID;

/**
 * Denotes an object that could be destroyed and removed from the game(i.e. has
 * a kind of "state", which one of its value is "destroyed") and could fire
 * events accordingly.
 */
public interface DestructibleObject extends ObjectWIthID {

	/**
	 * Flag-like method to check if this has been destroyed (and hopefully removed
	 * from the game). <br>
	 * Could be used to check, for instance, if targets are still valid, or if the
	 * tracked object should be still tracked, of if the object should be
	 * activated/animated/"called", etc (for example: a destroyed trap should not
	 * trigger).
	 */
	public boolean isDestroyed();

	/**
	 * Read-only test, that shouldn't have side effects, to check if this object
	 * should be destroyed and then removed (or meant to evolve its internal state),
	 * optionally firing appropriated events accordingly.
	 */
	public boolean shouldBeDestroyed(); // semplice flag o cosa computata, per esempio verificando se vita<=0

	/**
	 * When this object is being destroyed (that means: "during the
	 * {@link #destroy()} call") this event should be fired, in case of complex
	 * games, to notify all objects that "responds to a damage-received event" that
	 * this kind of event has occurred, also to make the "objects manager" to remove
	 * this object from the game.
	 * <p>
	 * Note: Originally, the parameter was an instance of {@link GEventManager},
	 * now it's generalized to allow simpler event notification systems.
	 */
	public void fireDestruction(GModality gm);

	/**
	 * Apply the destruction, maybe by calling
	 * {@link #fireDestruction(GEventManager)}
	 */
	public boolean destroy();
}