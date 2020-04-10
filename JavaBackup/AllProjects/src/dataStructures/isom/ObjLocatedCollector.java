package dataStructures.isom;

import java.awt.Point;
import java.util.Set;
import java.util.function.Predicate;

import geometry.ObjectLocated;
import geometry.pointTools.impl.PointConsumerRestartable;

public interface ObjLocatedCollector extends PointConsumerRestartable {

	public Predicate<ObjectLocated> getTargetsFilter();

	public NodeIsom getNodeAt(Point location);

	public Set<ObjectLocated> getCollectedObjects();

	@Override
	public default void restart() {
		this.getCollectedObjects().clear();
	}

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