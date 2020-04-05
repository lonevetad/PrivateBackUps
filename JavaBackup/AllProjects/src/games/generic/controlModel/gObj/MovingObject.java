package games.generic.controlModel.gObj;

import games.generic.controlModel.GModality;

public interface MovingObject extends TimedObject, ObjectInSpace {

	/** Progress the object's movement by the milliseconds elapsed */
	public void move(int milliseconds);

	@Override
	public default void act(GModality modality, int milliseconds) {
		move(milliseconds);
	}
}