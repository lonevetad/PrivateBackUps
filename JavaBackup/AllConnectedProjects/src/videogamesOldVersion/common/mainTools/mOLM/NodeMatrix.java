package common.mainTools.mOLM;

import java.io.Serializable;

import common.abstractCommon.behaviouralObjectsAC.MyComparator;
import common.mainTools.mOLM.MatrixObjectLocationManager.COLORS_VISIT;
import common.mainTools.mOLM.MatrixObjectLocationManager.CoordinatesDeltaForAdjacentNodes;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;

public class NodeMatrix implements Serializable {

	private static final long serialVersionUID = 56108790463603L;
	protected static int progressiveID = 0;

	protected static final MyComparator<NodeMatrix> compNodeMatrix = (NodeMatrix d1, NodeMatrix d2) -> {
		if (d1 == d2) return 0;
		if (d1 == null) return 1;
		if (null == d2) return -1;
		if (d1.getPreviousOnPathfinding() == d2.getPreviousOnPathfinding()) return 0;
		if (d1.getPreviousOnPathfinding() == null) return 1;
		if (d2.getPreviousOnPathfinding() == null) return -1;
		return Double.compare(d1.getFullPathLength(), d2.getFullPathLength());
	};

	public NodeMatrix() {
		this(0, 0);
	}

	protected NodeMatrix(int xOfThisNode, int yOfThisNode) {
		serialNodeID = ++progressiveID;
		reinitializeNode(this);
		x = xOfThisNode;
		y = yOfThisNode;
		lastPathFindSearc = -1;
	}

	// fields of this class
	int x, y, serialNodeID;
	// using this coordinates, i can compute the adjacents for dijikstra path-find algorithm
	ObjectWithID item;

	// fields for pathfind
	transient COLORS_VISIT color;
	/** It's the father, the node-discoverer, the node that discover me */
	transient private NodeMatrix previousOnPathfinding;
	// added by me for my dijkstra algorithm
	transient double fullPathLength;
	// added by me for optimized dijkstra
	transient long lastPathFindSearc;

	//

	// TODO GETTER

	public ObjectWithID getItem() {
		return item;
	}

	public int getSerialNodeID() {
		return serialNodeID;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public COLORS_VISIT getColor() {
		return color;
	}

	public NodeMatrix getpGreek() {
		return getPreviousOnPathfinding();
	}

	public double getFullPathLength() {
		return fullPathLength;
	}

	public long getLastPathFindSearc() {
		return lastPathFindSearc;
	}

	public NodeMatrix getPreviousOnPathfinding() {
		return previousOnPathfinding;
	}

	//

	// TODO SETTER

	public NodeMatrix setItem(ObjectWithID o) {
		item = o;
		return this;
	}

	protected void setSerialNodeID(int serialNodeID) {
		this.serialNodeID = serialNodeID;
	}

	public NodeMatrix setPreviousOnPathfinding(NodeMatrix previousOnPathfinding) {
		this.previousOnPathfinding = previousOnPathfinding;
		return this;
	}

	//

	// TODO OTHER

	public void copyPathFrom(NodeMatrix from) {
		this.fullPathLength = from.fullPathLength;
		this.setPreviousOnPathfinding(from.getPreviousOnPathfinding());
	}

	protected void doOnReinitialization() {
	}

	public static void reinitializeNode(NodeMatrix n) {
		n.fullPathLength = 0;// Double.MAX_VALUE;
		n.color = COLORS_VISIT.WHITE;
		n.setPreviousOnPathfinding(null);
		n.doOnReinitialization();
	}

	public void addArcWeight(NodeMatrix from, CoordinatesDeltaForAdjacentNodes arc) {
		if (arc != null) fullPathLength = from.fullPathLength + arc.weight;
	}

	@Override
	public String toString() {
		return "--NM: (" + x + "," + y + "), fullPathLength: " + fullPathLength + "--";
	}
}