package games.generic.controlModel;

import java.util.Set;
import java.util.function.Consumer;

import games.generic.controlModel.misc.uidp.GMapUIDProvider;
import tools.ObjectNamedID;
import tools.ObjectWithID;

/**
 * Holds and manages all objects in the current game, as described by
 * {@link GObjectsHolder} but also helped by the delegate
 * {@link GObjectsInSpaceManager}, obtained by {@link #getGOISMDelegated()}.
 */
public class GMap implements GObjectsHolder, ObjectNamedID {
	private static final long serialVersionUID = -4521201066L;

	public GMap(String mapName) {
		this.mapName = mapName;
		ID = GMapUIDProvider.newID();
	}

	protected Integer ID;
	protected String mapName;
	protected GObjectsInSpaceManager goismDelegated;

	@Override
	public Integer getID() {
		return ID;
	}

	@Override
	public String getName() {
		return mapName;
	}

	/**
	 * That's the delegate for managing the space of this map and the objects to put
	 * them in
	 */
	public GObjectsInSpaceManager getGOISMDelegated() {
		return goismDelegated;
	}

	public String getNameGObjHolder() {
		return goismDelegated.getNameGObjHolder();
	}

	/**
	 * Set the delegator cited in the class's documentation.
	 */
	public void setGOISMDelegated(GObjectsInSpaceManager isomDelegated) {
		this.goismDelegated = isomDelegated;
	}

	@Override
	public Set<ObjectWithID> getObjects() {
		return goismDelegated.getObjects();
	}

	@Override
	public boolean add(ObjectWithID o) {
		return goismDelegated.add(o);
	}

	@Override
	public boolean remove(ObjectWithID o) {
		return goismDelegated.remove(o);
	}

	@Override
	public boolean removeAll() {
		return goismDelegated.removeAll();
	}

	@Override
	public boolean contains(ObjectWithID o) {
		return goismDelegated.contains(o);
	}

	@Override
	public ObjectWithID get(Integer id) {
		return goismDelegated.get(id);
	}

	@Override
	public void forEach(Consumer<ObjectWithID> action) {
		goismDelegated.forEach(action);
	}
}