package dataStructures.isom;

import java.awt.Point;
import java.io.Serializable;
import java.util.Set;

import geometry.ObjectLocated;
import geometry.ObjectLocated.PointWrapper;

public abstract class NodeIsom extends PointWrapper implements Serializable {
	private static final long serialVersionUID = 4052487990441743L;

	public NodeIsom() {
		x = y = 0;
	}

	/**
	 * Add the given object to this node. This node could store just a single object
	 * or a {@link Set }, it depends on implementation.
	 */
	public abstract boolean addObject(ObjectLocated o);

	@Override
	public Point getLocation() {
		return new Point(x, y);
	}

//	public void setLocation(Point location) {
//		this.x = location.x;
//		this.y = location.y;
//	}
//	public void setLocation(int x, int y) {
//		this.x = x;
//		this.y = y;
//	}
}