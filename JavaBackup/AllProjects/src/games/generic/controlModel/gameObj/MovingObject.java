package games.generic.controlModel.gameObj;

public interface MovingObject extends TimedObject, ObjectInSpace {

	/** Progress the object's movement by the milliseconds elapsed */
	public void move(long milliseconds);
}