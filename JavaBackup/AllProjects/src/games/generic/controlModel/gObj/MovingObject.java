package games.generic.controlModel.gObj;

import games.generic.controlModel.GModality;

public interface MovingObject extends TimedObject, ObjectInSpace {

	/** Progress the object's movement by the milliseconds elapsed */
	public void move(long milliseconds);

	@Override
	public default void act(GModality modality, long milliseconds) {
		move(milliseconds);
	}
}