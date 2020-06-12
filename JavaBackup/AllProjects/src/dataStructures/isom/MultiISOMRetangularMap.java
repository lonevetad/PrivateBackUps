package dataStructures.isom;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.SetMapped;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager.CoordinatesDeltaForAdjacentNodes;
import dataStructures.isom.matrixBased.pathFinders.PathFinderDijkstra_Matrix;
import geometry.AbstractMultiOISMRectangular;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.PathOptimizer;
import tests.tDataStruct.Test_MultiISOMRetangularMap_V1;
import tools.Comparators;
import tools.LoggerMessages;
import tools.MathUtilities;
import tools.NumberManager;
import tools.Stringable;

/**
 * TODO of 10/06/2020
 * <p>
 * Take inspiration from {@link MatrixInSpaceObjectsManager} to implement
 * {@link InSpaceObjectsManager} and {@link Test_MultiISOMRetangularMap_V1} to
 * implement {@link AbstractMultiOISMRectangular}.
 * <p>
 * To implement {@link NodeIsomProvider#getNodeAt(Point)}, use the
 * {@link Test_MultiISOMRetangularMap_V1#getMapContaining(Point)} code.
 */
public class MultiISOMRetangularMap<Distance extends Number> extends AbstractMultiOISMRectangular
		implements InSpaceObjectsManager<Distance> {
	private static final long serialVersionUID = -879653210489L;
	public static final int MAXIMUM_SUBMAPS_EACH_SECTION = 4, MINIMUM_DIMENSION_MAP = 4;

	public MultiISOMRetangularMap(int maximumSubmapsEachSection) {
		if (maximumSubmapsEachSection < 1)
			throw new IllegalArgumentException(
					"Incorrect number of maximum submaps on each section: " + maximumSubmapsEachSection);
		this.maximumSubmapsEachSection = maximumSubmapsEachSection;
		maps = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.INTEGER_COMPARATOR);
		mapsAsList = maps.toListValue(r -> r.IDInteger);
		setObjectsAdded(MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR));
		clear();
		pathFinder = new PathFinderDijkstra_Matrix<Distance>(this);
	}

	protected int idProg = 0;
	protected LoggerMessages log;
//	protected PathFinderIsomAdapter<NodeIsom, D> pathFinder;
	protected PathFinderIsom<Point, ObjectLocated, Distance> pathFinder;
	protected NumberManager<Distance> numberManager;
	protected PathOptimizer<Point> pathOptimizer;
	// from isom
	protected Map<Integer, ObjectLocated> objectsAdded;
	protected Set<ObjectLocated> objectsAddedSet;
	// multi-misom stuffs
	protected boolean neverBuilt;
	protected int maximumSubmapsEachSection;
	protected int maxDepth, xLeftTop, yLeftTop, xRightBottom, yRightBottom, width, height;
	protected NodeMultiISOMRectangular root;
	protected final MapTreeAVL<Integer, MISOMWrapper> maps;
	protected final List<MISOMWrapper> mapsAsList;

	@Override
	public LoggerMessages getLog() { return log; }

	@Override
	public PathFinderIsom<Point, ObjectLocated, Distance> getPathFinder() { return pathFinder; }

	@Override
	public NumberManager<Distance> getNumberManager() { return numberManager; }

	@Override
	public PathOptimizer<Point> getPathOptimizer() { return pathOptimizer; }

	@Override
	public void setLog(LoggerMessages log) { this.log = log; }

	@Override
	public void setPathFinder(PathFinderIsom<Point, ObjectLocated, Distance> pathFinder) {
		this.pathFinder = pathFinder;
	}

	@Override
	public void setNumberManager(NumberManager<Distance> numberManager) { this.numberManager = numberManager; }

	@Override
	public void setPathOptimizer(PathOptimizer<Point> pathOptimizer) { this.pathOptimizer = pathOptimizer; }

	/** Sets the map holding all objects in this space. */
	protected void setObjectsAdded(Map<Integer, ObjectLocated> objectsAdded) {
		this.objectsAdded = objectsAdded;
		if (objectsAdded == null)
			this.objectsAddedSet = null;
		else {
			if (objectsAdded instanceof MapTreeAVL<?, ?>)
				this.objectsAddedSet = ((MapTreeAVL<Integer, ObjectLocated>) objectsAdded).toSetValue(ol -> ol.getID());
			else
				this.objectsAddedSet = new SetMapped<>(objectsAdded.entrySet(), e -> e.getValue());
		}
	}

	//

	// TODO OTHER METHODS

	//

	@Override
	public NodeIsom getNodeAt(Point location) {
		MatrixInSpaceObjectsManager<Distance> misom;
		misom = getMISOMContaining(location);
		return (misom == null) ? null : misom.getNodeAt(location);
//	return getNodeAt(location.x,location. y); 
	}

	@Override
	public NodeIsom getNodeAt(int x, int y) { return getNodeAt(new Point(x, y)); }

	@Override
	public void forEachNode(Consumer<NodeIsom> action) { this.maps.forEach((i, mw) -> mw.misom.forEachNode(action)); }

	@Override
	public Set<ObjectLocated> getAllObjectLocated() { return this.objectsAddedSet; }

	@Override
	public ObjectLocated getObjectLocated(Integer ID) { return this.objectsAdded.get(ID); }

	@Override
	public void forEachAdjacents(NodeIsom node, BiConsumer<NodeIsom, Distance> adjacentDistanceConsumer) {
		MatrixInSpaceObjectsManager<Distance> misom;
		Point p;
		p = new Point();
		for (CoordinatesDeltaForAdjacentNodes c : MatrixInSpaceObjectsManager.VALUES_CoordinatesDeltaForAdjacentNodes) {
			p.x = c.dx;
			p.y = c.dy;
			misom = getMISOMContaining(p);
			if (misom != null) {
				adjacentDistanceConsumer.accept(misom.getNodeAt(p), misom.getNumberManager().fromDouble(c.weight));
			}
		}
	}

	@Override
	public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter,
			List<Point> path) {
		// TODO Auto-generated method stub
		return null;
	}

	//

	// TODO multi-isom's methods

	//

	public void removeAllMaps() {
		maps.clear();
		root = null;
		clearDimensionCache();
	}

	protected void clearDimensionCache() {
		neverBuilt = true;
		maxDepth = 0;
		xLeftTop = yLeftTop = xRightBottom = yRightBottom = width = height = 0;
		this.objectsAdded.clear();
	}

	public void clear() { removeAllMaps(); }

	public MatrixInSpaceObjectsManager<Distance> getMISOMContaining(Point p) {
		NodeMultiISOMRectangular n, prev;
		List<MISOMWrapper> submaps;
		if (root == null)
			return null;
		n = prev = root;
		// traverse the tree
		while (!(n == null || n.isLeaf())) {
			prev = n;
			if (p.x >= n.xm) { // east
				n = (p.y >= n.ym) ? n.sse : n.sne;
			} else {// west
				n = (p.y >= n.ym) ? n.ssw : n.snw;
			}
		}
		// get the collection of submaps
		submaps = (n != null) ? n.submaps : prev.submaps;
		if (submaps == null)
			return null;
		// if any holds that point, then return it
		for (MISOMWrapper r : submaps) {
			if (r.contains(p)) // MathUtilities.isInside(r, p))//
				return r.misom;
		}
		return null; // Error 404
	}

	public void addMap(MISOMWrapper r) {
		int c;
		if (r == null)
			return;
		c = updateBoundingBox(r);
		if (c < 0)
			return;
		maps.put(r.ID, r);
//		if (c > 0)
		rebuild();
//		else
//			addNotRebuilding(r);
	}

	public void addMaps(Collection<MISOMWrapper> mapsList) {
		int[] cc = { -1 };
		mapsList.forEach(r -> {
			int c;
			if (r != null) {
				c = updateBoundingBox(r);
				if (c >= 0) {
					if (cc[0] < c)
						cc[0] = c;
					maps.put(r.ID, r);
				}
			}
		});
//		if (cc[0] > 0)
		rebuild();
//		else
//			mapsList.forEach(this::addNotRebuilding);
	}

	/**
	 * EMPTY METHOD
	 * <P>
	 * Should be a method to add a map without a full rebuild, but I don't know how
	 * to do it in a smart way.
	 */
	protected void addNotRebuilding(MISOMWrapper r) {
		// TODO
		throw new UnsupportedOperationException("Too lazy to think a so-variable scenario");
	}

	public void removeMap(MISOMWrapper r) {
		if (maps.containsKey(r.ID)) {
			maps.remove(r.ID);
			recalculateBoundingBox();
			rebuild();
		}
	}

	public void removeMap(Collection<MISOMWrapper> mapsList) {
		boolean[] cc = { false };
		mapsList.forEach(r -> {
			if (maps.containsKey(r.ID)) {
				cc[0] = true;
				maps.remove(r.ID);
			}
		});
		if (cc[0]) {
			recalculateBoundingBox();
			rebuild();
		}
	}

	protected void recalculateBoundingBox() {
		clearDimensionCache();
		mapsAsList.forEach(this::updateBoundingBox);
	}

	protected int updateBoundingBox(MISOMWrapper r) {
		boolean changed;
		int t;
		if (r.width < 1 || r.height < 1) { return -1; }
		if (neverBuilt) {
			neverBuilt = false;
			xLeftTop = r.x;
			yLeftTop = r.y;
			width = r.width;
			height = r.height;
			xRightBottom = (r.x + r.width) - 1;
			yRightBottom = (r.y + r.height) - 1;
			return 1;
		}
		changed = false;
		if (r.x < xLeftTop) {
			changed = true;
			xLeftTop = r.x;
		}
		if (r.y < yLeftTop) {
			changed = true;
			yLeftTop = r.y;
		}
		t = r.x + r.width;
		if (t > xRightBottom) {
			changed = true;
			xRightBottom = t;
		}
		t = r.y + r.height;
		if (t > yRightBottom) {
			changed = true;
			yRightBottom = t;
		}
		this.width = xRightBottom - xLeftTop;
		this.height = yRightBottom - yLeftTop;
		return (changed || root == null) ? 1 : 0;
	}

	protected void rebuild() {
		this.maxDepth = 0;
		this.root = rebuild(null, mapsAsList, //
				xLeftTop, yLeftTop, xRightBottom, yRightBottom, width, height//
				, xLeftTop + (width >> 1)//
				, yLeftTop + (height >> 1)); // clear all
	}

	protected NodeMultiISOMRectangular rebuild(NodeMultiISOMRectangular father, List<MISOMWrapper> submaps, //
			int xLeftTop, int yLeftTop, int xRightBottom, int yRightBottom, int width, int height, //
			int xm, int ym) {
		final int mxw, mxe, myn, mys, widthWest, widthEst, heightNorth, heightSouth, ymmmmm, xmmmmm;
		NodeMultiISOMRectangular n;
		List<MISOMWrapper> snw, sne, ssw, sse;
		n = new NodeMultiISOMRectangular(father);
		if (this.maxDepth < n.depth) { this.maxDepth = n.depth; }
		n.x = xLeftTop;
		n.y = yLeftTop;
		n.w = width;
		n.h = height;
		n.xm = xm;
		n.ym = ym;
		if (submaps.size() <= maximumSubmapsEachSection //
				|| width <= MINIMUM_DIMENSION_MAP || height <= MINIMUM_DIMENSION_MAP) {
			n.submaps = submaps;
			return n;
		}
		snw = new ArrayList<>(maximumSubmapsEachSection);
		sne = new ArrayList<>(maximumSubmapsEachSection);
		ssw = new ArrayList<>(maximumSubmapsEachSection);
		sse = new ArrayList<>(maximumSubmapsEachSection);
		ymmmmm = ym;
		xmmmmm = xm;
		mxw = xLeftTop + ((widthWest = (xm - xLeftTop)) >> 1);
		myn = yLeftTop + ((heightNorth = (ym - yLeftTop)) >> 1);
		mxe = ++xm + ((widthEst = 1 + (xRightBottom - xm)) >> 1);
		mys = ++ym + ((heightSouth = 1 + (yRightBottom - ym)) >> 1);
		xm = xmmmmm;
		ym = ymmmmm;
		submaps.forEach(r -> {
			if (MathUtilities.intersects(xLeftTop, yLeftTop, widthWest, heightNorth, r.x, r.y, r.width, r.height)) {
				snw.add(r);
			}
			if (MathUtilities.intersects(xmmmmm + 1, yLeftTop, widthEst, heightNorth, r.x, r.y, r.width, r.height)) {
				sne.add(r);
			}
			if (MathUtilities.intersects(xmmmmm + 1, ymmmmm + 1, widthEst, heightSouth, r.x, r.y, r.width, r.height)) {
				sse.add(r);
			}
			if (MathUtilities.intersects(xLeftTop, ymmmmm + 1, widthWest, heightSouth, r.x, r.y, r.width, r.height)) {
				ssw.add(r);
			}
		});
		if (snw.size() > 0)
			n.snw = rebuild(n, snw, //
					xLeftTop, yLeftTop, xmmmmm, ymmmmm, // corner points
					widthWest, heightNorth, //
					mxw, myn); // middle point
		if (sne.size() > 0)
			n.sne = rebuild(n, sne, //
					(xmmmmm + 1), yLeftTop, xRightBottom, ymmmmm, // corner points
					widthEst, heightNorth, // dimensions
					mxe, myn); // middle point
		if (ssw.size() > 0)
			n.ssw = rebuild(n, ssw, //
					xLeftTop, ymmmmm + 1, xmmmmm, yRightBottom, // corner points
					widthWest, heightSouth, // dimensions
					mxw, mys); // middle point
		if (sse.size() > 0)
			n.sse = rebuild(n, sse, //
					xmmmmm + 1, ymmmmm + 1, xRightBottom, yRightBottom, // corner points
					widthEst, heightSouth, // dimensions
					mxe, mys); // middle point
		return n;
	}

	//

	//

	// TODO CLASSES

	//

	//

	protected class MISOMWrapper extends Rectangle {
		private static final long serialVersionUID = 560804524L;

		public MISOMWrapper(MatrixInSpaceObjectsManager<Distance> misom, int x, int y, int width, int height) {
			super(x, y, width, height);
			this.misom = misom;
			this.IDInteger = (this.ID = idProg++);
		}

		public MISOMWrapper(MatrixInSpaceObjectsManager<Distance> misom, Point p, Dimension d) {
			super(p, d);
			this.misom = misom;
			this.IDInteger = (this.ID = idProg++);
		}

		public final int ID;
		public final Integer IDInteger;
		protected final MatrixInSpaceObjectsManager<Distance> misom;
	}

	protected class NodeMultiISOMRectangular implements Stringable {
		private static final long serialVersionUID = 1L;
		protected int x, y, w, h, xm, ym, depth;
		List<MISOMWrapper> submaps;
		NodeMultiISOMRectangular father, snw, sne, ssw, sse;

		NodeMultiISOMRectangular(NodeMultiISOMRectangular father) {
			this.father = father;
			submaps = null;
			depth = (father == null) ? 1 : (father.depth + 1);
		}

		public boolean isLeaf() { return submaps != null; }

		@Override
		public String toString() { return "Node--[x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + "]"; }

		@Override
		public void toString(StringBuilder sb, int tabLevel) {
			sb.append('\n');
			addTab(sb, tabLevel);
			sb.append("Node at level ").append(tabLevel).append(", is:");
			addTab(sb, tabLevel + 1);
			sb.append(toString());
			addTab(sb, tabLevel);
			sb.append("and has subparts:");
			if (isLeaf()) {
				int t;
				sb.append(" is leaf:n");
				t = 1 + tabLevel;
				this.submaps.forEach(r -> {
//					sb.append('\n');
					addTab(sb, t);
					sb.append(r);
				});
//				sb.append(t);
			} else {
				tabLevel++;
				if (snw != null) { snw.toString(sb, tabLevel); }
				if (sne != null) { sne.toString(sb, tabLevel); }
				if (ssw != null) { ssw.toString(sb, tabLevel); }
				if (sse != null) { sse.toString(sb, tabLevel); }
			}
		}
	}
}