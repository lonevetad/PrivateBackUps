package games.generic.controlModel.subImpl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.TimedObject;

/**
 * Mark a {@link GModality} as a "time-based" game, maybe "real-time" based
 * game.<br>
 * For this purpose, implements the method {@link GModality#doOnEachCycle(long)}
 * so that it invokes this interface's method
 * {@link #progressElapsedTime(long)}.
 */
public interface IGameModalityTimeBased {

	/**
	 * Override designed.
	 * <p>
	 * The game is "time based" so tons of objects (the majority or totality,
	 * probably) are implementing {@link TimedObject} (see that interface's
	 * documentation for more informations about what does this means) and this
	 * method is designed to "progress / evolve" those objects' status depending on
	 * elapsed time.
	 * <p>
	 * Method that should be invoked by {@link GModality#doOnEachCycle(long)}.
	 */
	public void progressElapsedTime(long millisecToElapse);
}