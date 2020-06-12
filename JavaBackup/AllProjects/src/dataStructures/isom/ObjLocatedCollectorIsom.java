package dataStructures.isom;

import java.awt.Point;
import java.util.Set;

import geometry.ObjectLocated;
import geometry.pointTools.impl.ObjCollector;

public interface ObjLocatedCollectorIsom extends ObjCollector<ObjectLocated>, NodeIsomConsumer {

	@Override
	public default ObjectLocated getAt(Point location) {
		return getNodeAt(location).getObject(0); // jus a randonm object
	}

//	public NodeIsom getNodeAt(Point location);

	@Override
	public default void consume(NodeIsom n) {
		final Set<ObjectLocated> co;
		co = getCollectedObjects();
		n.forEachAcceptableObject(getTargetsFilter(), o -> {
			if (o == null)
				return;
			co.add(o);
		});
	}

	@Override
	default void accept(Point location) { // TODO Auto-generated method stub
		NodeIsomConsumer.super.accept(location);
	}
}