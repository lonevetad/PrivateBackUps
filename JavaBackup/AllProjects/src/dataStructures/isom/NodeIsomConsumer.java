package dataStructures.isom;

import java.awt.Point;

import geometry.pointTools.PointConsumer;

public interface NodeIsomConsumer extends PointConsumer {

	public NodeIsomProvider getNodeIsomProvider();

	public void setNodeIsomProvider(NodeIsomProvider nodeIsomProvider);

	//

	public default NodeIsom getNodeAt(Point location) {
		NodeIsomProvider nip;
		nip = getNodeIsomProvider();
		return (nip == null) ? null : nip.getNodeAt(location);
	}

	public default NodeIsom getNodeAt(int x, int y) {
		NodeIsomProvider nip;
		nip = getNodeIsomProvider();
		return (nip == null) ? null : nip.getNodeAt(x, y);
	}

	public void consume(NodeIsom n);

	@Override
	public default void accept(Point location) { consume(getNodeAt(location)); }
}