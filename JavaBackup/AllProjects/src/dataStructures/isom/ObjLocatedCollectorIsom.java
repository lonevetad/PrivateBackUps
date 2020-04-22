package dataStructures.isom;

import java.awt.Point;

import geometry.ObjectLocated;
import geometry.pointTools.impl.ObjCollector;

public interface ObjLocatedCollectorIsom extends ObjCollector<ObjectLocated> {

	@Override
	public default ObjectLocated getAt(Point location) {
		return getNodeAt(location).getObject(0); // jus a randonm object
	}

	public NodeIsom getNodeAt(Point location);

	@Override
	public default void accept(Point location) {
		NodeIsom n;
		n = this.getNodeAt(location);
		n.forEachAcceptableObject(getTargetsFilter(), o -> {
			if (o == null)
				return;
			getCollectedObjects().add(o);
		});
	}
}