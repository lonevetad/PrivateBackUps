package games.generic.controlModel.subimpl;

import dataStructures.isom.InSpaceObjectsManager;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.controlModel.gObj.ObjectInSpace;

public class GObjectsInSpaceManagerImpl implements GObjectsInSpaceManager {

	public GObjectsInSpaceManagerImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public InSpaceObjectsManager getOIMManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(ObjectInSpace o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveObject(ObjectInSpace o, Object from, Object to) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addObject(boolean fireEvent, ObjectInSpace o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeObject(boolean fireEvent, ObjectInSpace o) {
		// TODO Auto-generated method stub

	}

}
