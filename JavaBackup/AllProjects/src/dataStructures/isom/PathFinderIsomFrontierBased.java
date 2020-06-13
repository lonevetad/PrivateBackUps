package dataStructures.isom;

import java.awt.Point;

import geometry.ObjectLocated;

public interface PathFinderIsomFrontierBased<Distance extends Number>
		extends PathFinderIsom<Point, ObjectLocated, Distance> {

	public static enum NodePositionInFrontier {
		NeverAdded, InFrontier, Closed;
	}

	public static class NodeInfoFrontierBased<D extends Number> extends NodeInfo<D> {

		public NodePositionInFrontier color;

		public NodeInfoFrontierBased(NodeIsom thisNode) {
			super(thisNode);
			this.color = NodePositionInFrontier.NeverAdded;
		}

	}
}