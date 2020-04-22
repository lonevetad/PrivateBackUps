package common.abstractCommon.behaviouralObjectsAC;

import common.GameObjectInMap;

/**
 * This is the class that should be implemented by all of {@link GameObjectInMap} and its subclasses
 * to perform their typical behavior on time passing by.
 */
public interface ObjectActing extends // ObjectMoving,
		ObjectActingOnPassingTime {

	/** Usually, an act is timed with exactly one second (1000 milliseconds). */
	public static final int DEFAULT_MILLIS_ACT_CONSUMING = 1000;

	//

	// TODO GETTER

	/**
	 * An {@link ObjectActing} tracks the time passing by storing it in a variable. This {@code int}
	 * should track how many milliseconds has been passed since the last "action".<br>
	 * This method should return that variable.
	 */
	public int getTimeActCounter();

	/**
	 * See {@link ObjectActing#DEFAULT_MILLIS_ACT_CONSUMING}.
	 * <p>
	 * This function MUST returns a positive number (greater than zero). If not, the default
	 * implementations of this interface will crash or creates bugs.
	 */
	public default int getTimeMillisecondsBetweenActs() {
		return DEFAULT_MILLIS_ACT_CONSUMING;
	}

	/** Should mind the {@link #getTimeActCounter()} !! */
	public default boolean canAct() {
		return getTimeActCounter() >= getTimeMillisecondsBetweenActs();
	}

	//

	// TODO SETTER

	public ObjectActing setCanAct(boolean canAct);

	/** Should set the variable described in {@link #getTimeActCounter()}. */
	public ObjectActing setTimeActCounter(int timeActCounter);

	//

	// TODO ABSTRACT

	public void performAct();

	//

	// TODO DEFAULT METHODS

	/**
	 * Avert this instance that an amount of time (specified by the parameter) has passed by.
	 */
	@Override
	public default void act(int millisecond) {
		// int tmba;
		if (millisecond > 0 && canAct()) {
			increaseTimeActCounter(millisecond);

			// move(millisecond);
			// if ((tmba = getTimeMillisecondsBetweenActs()) < 0) tmba = -tmba;
			while (canAct()) {
				decreaseTimeActCounter(getTimeMillisecondsBetweenActs());
				performAct();
			}
		}
	}

	public default void resetActTimers() {
		setTimeActCounter(0);
	}

	//

	public default void increaseTimeActCounter(int delta) {
		if (delta > 0)
			setTimeActCounter(getTimeActCounter() + delta);
		else
			decreaseTimeActCounter(-delta);
	}

	public default void decreaseTimeActCounter(int delta) {
		if (delta > 0)
			setTimeActCounter(getTimeActCounter() - delta);
		else
			increaseTimeActCounter(-delta);
	}
}