package games.generic.controlModel.gObj;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.GModalityTRAn;
import geometry.AbstractShape2D;
import geometry.ObjectShaped;
import tools.ObjectWithID;

/**
 * Defines a complex object as a collection of other object(s) orbiting around
 * this instance, acting as a center of rotation (like a Sun and its planets).
 */
public abstract class OrbitingInteractiveObject implements TimedObject, MovingObject {
	private static final long serialVersionUID = -36278285897000123L;

	// public static final int ROTATION_TIME =
	public static final double RHO = 2.0 * Math.PI;

	protected int millisRotation; // max is 5000, ora instance field
	protected double angRad; // radians
	protected List<ObjectShaped> orbitingObjects; // può averne al massimo N, tipo 5
	protected AbstractShape2D shape;
	protected Integer ID;
	protected String name;
	protected ObjectWithID owner;

	protected OrbitingInteractiveObject() {
		this.orbitingObjects = new LinkedList<>();
	}

	//

	/**
	 * Get the amount of milliseconds required to complete a complete rotation.
	 */
	public abstract int getRotationTimeMillis(); // { return ROTATION_TIME; }

	public int getMillisRotation() {
		return millisRotation;
	}

	@Override
	public AbstractShape2D getShape() {
		return shape;
	}

	@Override
	public Integer getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public ObjectWithID getOwner() {
		return owner;
	}

	public double getAngRad() {
		return angRad;
	}

	public List<ObjectShaped> getOrbitingObjects() {
		return orbitingObjects;
	}

	@Override
	public Point getLocation() {
		return shape.getCenter();
	}

	//

	//

//	int tempSpawnBlob;

	public void setID(Integer iD) {
		ID = iD;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwner(ObjectWithID owner) {
		this.owner = owner;
	}

	@Override
	public void setLocation(Point location) {
		shape.setCenter(location);
	}

	@Override
	public void act(GModality modality, int milliseconds) {
		if ((millisRotation += milliseconds) >= getRotationTimeMillis()) {
			millisRotation -= getRotationTimeMillis();
		}
		angRad = (RHO * millisRotation) / getRotationTimeMillis();
		updateCenterPosition(modality);
		move(modality, milliseconds);
		updateAndActOrbitingObjects(modality, milliseconds);
	}

	/**
	 * Override designed.<br>
	 * Could be implemented to spawn those orbiting objects
	 */
	protected void updateAndActOrbitingObjects(GModality modality, int milliseconds) {
		int i, s;
		Iterator<ObjectShaped> iter;
		if (orbitingObjects == null)
			return;
		s = orbitingObjects.size();
		iter = orbitingObjects.iterator();
		i = 0;
		while(iter.hasNext()) {
			updateAndActOrbitingObject(modality, i++, iter.next(), s);
		}
//		this.orbitingObjects.forEach(action);
	}

	/** Update the position of a single orbiting object and perform its ability */
	protected void updateAndActOrbitingObject(GModality modality, int index, ObjectShaped os, int objectsAmount) {
		int r;
		double angleFinal;
		angleFinal = angRad + ((RHO * index)) / objectsAmount;
		r = getOrbitingObjectRadius(index, os);
		os.setLocation(getLocation().x + (int) (r * Math.cos(angleFinal)), //
				getLocation().y + (int) (r * Math.sin(angleFinal)));
		performOrbitingObjAction(modality, index, os);
	}

	/**
	 * This class may require to change its position, like tracking a specific
	 * object, for instance the player.
	 */
	protected abstract void updateCenterPosition(GModality modality);

	/**
	 * Some orbiting objects could have different radius from the center, based on
	 * its index.
	 */
	protected abstract int getOrbitingObjectRadius(int index, ObjectShaped os);

	protected abstract boolean isValidTarget(ObjectShaped possibleTarget);

	/**
	 * After updating the object's position (done in
	 * {@link #updateAndActOrbitingObjects(GModality)}), it should perform some
	 * task, like hitting hobjects, exploding, healing, blocking projectiles, etc.
	 */
	protected void performOrbitingObjAction(GModality modality, int index, ObjectShaped os) {
		GModalityRPG gmrpg;
		GameObjectsManager gom;
		Set<ObjectInSpace> foundObjs;
		gmrpg = (GModalityTRAn) modality;
		gom = gmrpg.getGameObjectsManager();
		foundObjs = gom.findInArea(os.getShape());
		foundObjs.forEach(ois -> {
			if (isValidTarget(ois))
				interactWith(gmrpg, index, os, ois);
		});
	}

	/**
	 * Effectively perform the action, like dealing damage, exploding (and so
	 * removing this object [the third parameter] using its index [second
	 * parameter]), healing, etc
	 */
	protected abstract void interactWith(GModality modality, int index, ObjectShaped os, ObjectShaped target);
}