package games.generic.controlModel;

/** Handler for objects in game */
public interface GameObjectsInSpaceManager {

	public boolean contains(ObjectInSpace o);

	/**
	 * To be intended as "teleport". <br>
	 * <code>
		if(! contains(o)){ addObject(true, o); }
		else if(to==null)removeObject(false, o);
		// else then TELEPORT
	 * </code>
	 */
	public boolean moveObject(ObjectInSpace o, Object from, Object to);

	public default void addObject(ObjectInSpace o) {
		addObject(true, o);
	}

	/**
	 * Set the object's location before adding it.
	 * 
	 * @param fireEvent specify if a "add" event should be fired
	 */
	public void addObject(boolean fireEvent, ObjectInSpace o);

	public default void removeObject(ObjectInSpace o) {
		removeObject(true, o);
	}

	/**
	 * @param fireEvent specify if a "remove" event should be fired
	 */
	public void removeObject(boolean fireEvent, ObjectInSpace o);
}