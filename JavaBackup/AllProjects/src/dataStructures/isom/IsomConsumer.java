package dataStructures.isom;

import geometry.pointTools.PointConsumer;

public interface IsomConsumer<Distance extends Number> extends PointConsumer {
	/**
	 * perform an action to the given {@link InSpaceObjectsManagerImpl}, referring to a
	 * given coordinates of the type (row, column) and the object hold by related
	 * node, if any.
	 */
//	public void accept(GridObjectManager gom, ObjectWithID node, int row, int column);

	public InSpaceObjectsManagerImpl<Distance> getInSpaceObjectsManager();

	public void setInSpaceObjectsManager(InSpaceObjectsManagerImpl<Distance> isom);
}