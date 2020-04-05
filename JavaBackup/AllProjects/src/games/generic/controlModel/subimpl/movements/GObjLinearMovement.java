package games.generic.controlModel.subimpl.movements;

import java.awt.Point;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GObjMovement;
import games.generic.controlModel.gObj.MovingObject;

public class GObjLinearMovement extends GObjMovement {
	private static final long serialVersionUID = 1L;

	public GObjLinearMovement() {
		distanceTraveled = 0;
		startingPoint = null;
	}

	protected int tempTimeUnit, distanceTraveled, distanceToDestination;
	protected Point startingPoint, destination;

	@Override
	public int getDistanceTraveled() {
		return distanceTraveled;
	}

	public void resetStartingPoint() {
		if (this.startingPoint == null)
			this.startingPoint = this.objectToMove.getLocation();
		else {
			Point otml;
			otml = this.objectToMove.getLocation();
			this.startingPoint.x = otml.x;
			this.startingPoint.y = otml.y;
			tempTimeUnit = 0;
			distanceTraveled = 0;

		}
	}

	@Override
	public void setObjectToMove(MovingObject objectToMove) {
		super.setObjectToMove(objectToMove);
		if (objectToMove != null)
			this.startingPoint = objectToMove.getLocation();
	}

	protected void updateDistToDest() {
		if (startingPoint == null)
			distanceToDestination = 0;
		distanceToDestination = (int) Math.hypot(startingPoint.x - destination.x, startingPoint.y - destination.y);
	}

	@Override
	public void act(GModality modality, int timeUnits) {
		// TODO Auto-generated method stub

	}

}