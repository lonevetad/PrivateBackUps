package games.generic.controlModel.subimpl.movements;

import java.awt.Point;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.MovingObject;
import games.generic.controlModel.misc.GObjMovement;
import tools.MathUtilities;

public class GObjLinearMovement extends GObjMovement {
	private static final long serialVersionUID = 1L;

	public GObjLinearMovement() {
		distanceTraveled = 0;
		startingPoint = null;
	}

	protected int tempTimeUnit, distanceTraveled, distanceToDestination, velocity, previousVelocity;
	protected double cos, sin;
	protected Point startingPoint, destination;

	@Override
	public int getDistanceTraveled() {
		return distanceTraveled;
	}

	@Override
	public Integer getID() {
		return null;
	}

	@Override
	public int getVelocity() {
		return velocity;
	}

	public int getDistanceToDestination() {
		return distanceToDestination;
	}

	public Point getDestination() {
		return destination;
	}

	public Point getStartingPoint() {
		return startingPoint;
	}

	//

	public void setDestination(Point destination) {
		this.destination = destination;
		// apply the distance done until now, then zig zag
		updateObjToMovePosition(distanceTraveled);
		distanceTraveled = 0;
		tempTimeUnit = 0;
		updateDest();
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity > 0 ? velocity : 0;
	}

	@Override
	public void setObjectToMove(MovingObject objectToMove) {
		super.setObjectToMove(objectToMove);
		if (objectToMove != null)
			this.startingPoint = objectToMove.getLocation();
	}

	///

	public void resetStartingPoint() {
		Point otml;
		otml = this.objectToMove.getLocation();
		if (this.startingPoint == null) {
			this.startingPoint = otml;
		}
		this.startingPoint.x = otml.x;
		this.startingPoint.y = otml.y;
		this.tempTimeUnit = 0;
		this.distanceTraveled = 0;
		updateDest();
	}

	protected void updateDest() {
		int dx, dy;
		double angRad;
		if (startingPoint == null) {
			distanceToDestination = 0;
			sin = 0.0;
			cos = 1.0;
			return;
		}
		dx = startingPoint.x - destination.x;
		dy = startingPoint.y - destination.y;
		distanceToDestination = (int) Math.hypot(dx, dy);
		angRad = MathUtilities.angleRadiants(dx, dy);
		sin = Math.sin(angRad);
//		cos=Math.cos(angRad);
		cos = 1 - sin * sin;// from the fundamental equality (or Phytagorean Theorem)
	}

	protected void updateObjToMovePosition(int totalDistTraveled) {
		this.objectToMove.setLocation(//
				((int) (cos * totalDistTraveled)) + startingPoint.x,
				((int) (sin * totalDistTraveled)) + startingPoint.y);
	}

	//

	@Override
	public void act(GModality modality, int timeUnits) {
		int distFromLastVelcityChange, totalDistTraveled;
		if (velocity == 0)
			return;
		/**
		 * Singe velocity could change from time to time, let's update the total
		 * distance traveled to make it more precise
		 */
		if (previousVelocity != velocity) {
			// update velocity, distance traveled progression, etc
			distanceTraveled += (tempTimeUnit * previousVelocity) / getTimeUnitSuperscale();
			previousVelocity = velocity;
			tempTimeUnit = 0;
		}
		distFromLastVelcityChange = ((this.tempTimeUnit += timeUnits) * velocity) / getTimeUnitSuperscale();
		totalDistTraveled = distanceTraveled + distFromLastVelcityChange;
		if (totalDistTraveled >= distanceToDestination) {
			// ARRIVED
			this.objectToMove.setLocation(destination);
			this.distanceTraveled = 0;
			this.velocity = 0;
			this.tempTimeUnit = 0;
			this.startingPoint = null;
		} else {
			updateObjToMovePosition(totalDistTraveled);
		}
	}
}