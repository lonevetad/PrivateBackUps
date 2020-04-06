package games.generic.controlModel.gObj;

import games.generic.controlModel.GModality;
import tools.ObjectWithID;

/**
 * Mark an object as having a "status" (or at least a non empty set of actions)
 * that depends on the concept of time or, in particular, <i>evolves</i> as the
 * time goes on.<br>
 * The "time unit" could be everything that's needed: seconds, milliseconds,
 * "turns" (i.e., a single unit amount of time), etc.<br>
 * It differs to the concept expressed in {@link #getTimeUnitSuperscale()}, see
 * it for further details.
 * <p>
 * Used by {@link GModality}es that are using some concept of "time" (real time
 * or turn-based) to describe objects performing actions over time.
 * <p>
 * This interface could have been a special case of {@link GEventObserverb}, but
 * it would have been heavily inefficient. It's a "hard-code / embedded"
 * solution for listening the time progression, but it's enough fast.
 */
public interface TimedObject extends ObjectWithID {

	/**
	 * Evolve the status of this object depending on the elapsed time denoted by the
	 * second parameter.<br>
	 * As yet said, the "time unit" could be everything that's needed: seconds,
	 * milliseconds, "turns" (i.e., a single unit amount of time), etc. The second
	 * parameter just express the quantity, not the "measurement unit".
	 * <p>
	 * Probably, this method's name is not the best clear and fitting one.
	 **/
	public void act(GModality modality, int timeUnits);

	/**
	 * As said in this class ({@link TimedObject}) and in this method
	 * {@link #act(GModality, int)}, the "time" unit could be everything. There
	 * still could be a "greater order unit":
	 * <ul>
	 * <li>In case of real time games, the "unit" is "milliseconds" and the
	 * "superscale unit" is "a whole second", so 1000 milliseconds.</li>
	 * <li>For turn based games, both the time unit and the "superscale unit" are
	 * jus <i>one single turn</i></li>
	 * </ul>
	 * By default, it returns <i>1000 milliseconds</i>.
	 */
	// {@link}
	public default int getTimeUnitSuperscale() {
		return 1000;
	}
}