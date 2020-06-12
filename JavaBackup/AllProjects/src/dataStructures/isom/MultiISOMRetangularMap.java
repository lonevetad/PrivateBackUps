package dataStructures.isom;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.SetMapped;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager.CoordinatesDeltaForAdjacentNodes;
import dataStructures.isom.matrixBased.ObjLocatedCollectorMultimatrix;
import dataStructures.isom.matrixBased.pathFinders.PathFinderDijkstra_Matrix;
import geometry.AbstractMultiOISMRectangular;
import geometry.AbstractShape2D;
import geometry.AbstractShapeRunner;
import geometry.ObjectLocated;
import geometry.ObjectShaped;
import geometry.PathOptimizer;
import geometry.ProviderShapeRunner;
import geometry.ProviderShapesIntersectionDetector;
import geometry.implementations.shapes.ShapeRectangle;
import geometry.pointTools.PointConsumer;
import geometry.pointTools.impl.ObjCollector;
import tests.tDataStruct.Test_MultiISOMRetangularMap_V1;
import tools.Comparators;
import tools.LoggerMessages;
import tools.MathUtilities;
import tools.NumberManager;
import tools.PathFinder;
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
		log = LoggerMessages.LOGGER_DEFAULT;
		maps = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.INTEGER_COMPARATOR);
		mapsAsList = maps.toListValue(r -> r.IDInteger);
		setObjectsAdded(MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR));
		shapeRect = null;// new ShapeRectangle()
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
	// protected boolean neverBuilt;
	protected int maximumSubmapsEachSection;
	protected int maxDepth, xLeftTop, yLeftTop, xRightBottom, yRightBottom, width, height;
	protected NodeMultiISOMRectangular root;
	protected final MapTreeAVL<Integer, MISOMWrapper<Distance>> maps;
	protected final List<MISOMWrapper<Distance>> mapsAsList;
	protected ShapeRectangle shapeRect;

	@Override
	public LoggerMessages getLog() { return log; }

	@Override
	public AbstractShape2D getBoundingShape() { return shapeRect; }

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
		if (objectsAdded == null) // here and below, update the set
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

	/**
	 * Return an instance of {@link NodeIsomProviderCachingMISOM}, which is an
	 * instance of {@link NodeIsomProvider} with a optimization (see its
	 * documentation).
	 */
	public NodeIsomProviderCachingMISOM<Distance> newNodeIsomProviderCaching() {
		return new NodeIsomProviderCachingMISOM<Distance>(this);
	}

	/**
	 * Returns a wrapper of this class optimized as described in
	 * {@link NodeIsomProviderCachingMISOM}
	 */
	public ISOMCachingMISOM<Distance> newIsomCachingMISOM() { return new ISOMCachingMISOM<Distance>(this); }

	@Override
	public NodeIsom getNodeAt(Point location) {
		MatrixInSpaceObjectsManager<Distance> misom;
		MISOMWrapper<Distance> mw;
		mw = getMISOMWrapperContaining(location);
		misom = mw.misom;
//				getMISOMContaining(location);
		if (misom == null)
			return null;
		// consider the offset
		return misom.getNodeAt(location.x - mw.x, location.y - mw.y); // .getNodeAt(location);
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
	public ObjLocatedCollectorIsom newObjLocatedCollector(Predicate<ObjectLocated> objectFilter) {
		return new ObjLocatedCollectorMultimatrix<Distance>(this.newNodeIsomProviderCaching(), objectFilter);
	}

	@Override
	public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter,
			List<Point> path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(ObjectLocated o) {
		MatrixInSpaceObjectsManager<Distance> m;
		if (o == null)
			return false;
		m = getMISOMContaining(o.getLocation());
		if (m == null)
			return false;
		m.add(o);
		return true;
	}

	@Override
	public boolean contains(ObjectLocated o) {
		MatrixInSpaceObjectsManager<Distance> m;
		if (o == null)
			return false;
		m = getMISOMContaining(o.getLocation());
		if (m == null)
			return false;
		return m.contains(o);
	}

	@Override
	public boolean remove(ObjectLocated o) {
		MatrixInSpaceObjectsManager<Distance> m;
		if (o == null)
			return false;
		m = getMISOMContaining(o.getLocation());
		if (m == null)
			return false;
		return m.remove(o);
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
		// neverBuilt = true;
		shapeRect = null;
		maxDepth = 0;
		xLeftTop = yLeftTop = xRightBottom = yRightBottom = width = height = 0;
		this.objectsAdded.clear();
	}

	public void clear() { removeAllMaps(); }

	public MatrixInSpaceObjectsManager<Distance> getMISOMContaining(Point p) {
		MISOMWrapper<Distance> mw;
		mw = getMISOMWrapperContaining(p);
		return (mw == null) ? null : mw.misom;
	}

	protected MISOMWrapper<Distance> getMISOMWrapperContaining(Point p) { return getMISOMWrapperContaining(p.x, p.y); }

	protected MISOMWrapper<Distance> getMISOMWrapperContaining(int x, int y) {
		NodeMultiISOMRectangular n, prev;
		List<MISOMWrapper<Distance>> submaps;
		if (root == null)
			return null;
		n = prev = root;
		// traverse the tree
		while (!(n == null || n.isLeaf())) {
			prev = n;
			if (x >= n.xm) { // east
				n = (y >= n.ym) ? n.sse : n.sne;
			} else {// west
				n = (y >= n.ym) ? n.ssw : n.snw;
			}
		}
		// get the collection of submaps
		submaps = (n != null) ? n.submaps : prev.submaps;
		if (submaps == null)
			return null;
		// if any holds that point, then return it
		for (MISOMWrapper<Distance> r : submaps) {
			if (r.contains(x, y)) // MathUtilities.isInside(r, p))//
				return r;
		}
		return null; // Error 404
	}

	public void addMap(MISOMWrapper<Distance> r) {
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

	public void addMaps(Collection<MISOMWrapper<Distance>> mapsList) {
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
	protected void addNotRebuilding(MISOMWrapper<Distance> r) {
		// TODO
		throw new UnsupportedOperationException("Too lazy to think a so-variable scenario");
	}

	public void removeMap(MISOMWrapper<Distance> r) {
		if (maps.containsKey(r.ID)) {
			maps.remove(r.ID);
			recalculateBoundingBox();
			rebuild();
		}
	}

	public void removeMap(Collection<MISOMWrapper<Distance>> mapsList) {
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

	protected void resetShape(boolean mustReallocate) {
		if (mustReallocate)
			shapeRect = new ShapeRectangle(0.0, 0, 0, true, 0, 0);
		shapeRect.setWidth(width);
		shapeRect.setHeight(height);
		shapeRect.setLeftTopCorner(xLeftTop, yLeftTop);
	}

	protected void recalculateBoundingBox() {
		clearDimensionCache();
		mapsAsList.forEach(this::updateBoundingBox);
	}

	protected int updateBoundingBox(MISOMWrapper<Distance> r) {
		boolean changed;
		int t;
		if (r.width < 1 || r.height < 1) { return -1; }
		if (shapeRect == null) {
			xLeftTop = r.x;
			yLeftTop = r.y;
			width = r.width;
			height = r.height;
			xRightBottom = (r.x + r.width) - 1;
			yRightBottom = (r.y + r.height) - 1;
			resetShape(true);
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
		if (changed || root == null) {
			resetShape(false);
			return 1;
		}
		return 0;
	}

	protected void rebuild() {
		this.maxDepth = 0;
		this.root = rebuild(null, mapsAsList, //
				xLeftTop, yLeftTop, xRightBottom, yRightBottom, width, height//
				, xLeftTop + (width >> 1)//
				, yLeftTop + (height >> 1)); // clear all
	}

	protected NodeMultiISOMRectangular rebuild(NodeMultiISOMRectangular father, List<MISOMWrapper<Distance>> submaps, //
			int xLeftTop, int yLeftTop, int xRightBottom, int yRightBottom, int width, int height, //
			int xm, int ym) {
		final int mxw, mxe, myn, mys, widthWest, widthEst, heightNorth, heightSouth, ymmmmm, xmmmmm;
		NodeMultiISOMRectangular n;
		List<MISOMWrapper<Distance>> snw, sne, ssw, sse;
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

	/**
	 * Wrapper of a {@link MatrixInSpaceObjectsManager} that specify where that
	 * matrix-map is located in space.<br>
	 * Mainly used for internal stuffs and to {@link NodeIsomProviderCachingMISOM}
	 * <p>
	 * NOTE: each matrix's coordinates are relative to them, so a point like
	 * <code>{x:5, y:10}</code> of a map located at <code>{x:1, y:3}</code> is
	 * translated (using a notation abuse) to
	 * <code>{x:5, y:10} - {x:1, y:3} = {x:4, y:7}</code>, so the {@link NodeIsom}
	 * located at <code>{x:4, y:7}</code> will be taken into account.
	 */
	public static class MISOMWrapper<Dd extends Number> extends Rectangle {
		private static final long serialVersionUID = 560804524L;

		public MISOMWrapper(MultiISOMRetangularMap<Dd> multi, MatrixInSpaceObjectsManager<Dd> misom, int x, int y,
				int width, int height) {
			super(x, y, width, height);
			this.misom = misom;
			this.multi = multi;
			this.IDInteger = (this.ID = this.multi.idProg++);
		}

		public MISOMWrapper(MultiISOMRetangularMap<Dd> multi, MatrixInSpaceObjectsManager<Dd> misom, Point p,
				Dimension d) {
			super(p, d);
			this.misom = misom;
			this.multi = multi;
			this.IDInteger = (this.ID = this.multi.idProg++);
		}

		public final int ID;
		public final Integer IDInteger;
		protected final MultiISOMRetangularMap<Dd> multi;
		protected final MatrixInSpaceObjectsManager<Dd> misom;
	}

	protected class NodeMultiISOMRectangular implements Stringable {
		private static final long serialVersionUID = 1L;
		protected int x, y, w, h, xm, ym, depth;
		List<MISOMWrapper<Distance>> submaps;
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

	//

	// TODO NodeIsomProviderCachingMISOM

	//

	/**
	 * Implementation of {@link NodeIsomProvider} optimized for iterating (as
	 * {@link AbstractShapeRunner} subclasses does) over sets of {@link Point} (used
	 * to obtain {@link NodeIsom}).<br>
	 * It's optimized because it caches the instances of
	 * {@link MatrixInSpaceObjectsManager}
	 */
	/*
	 * TODO 12/06/2020 questa classe andrebbe astratta (previa astrazione di
	 * "multi misom dotata di getMisomWrapperContaining")
	 */
	public static class NodeIsomProviderCachingMISOM<Dd extends Number> implements NodeIsomProvider {
		private static final long serialVersionUID = 1L;

		public NodeIsomProviderCachingMISOM(MultiISOMRetangularMap<Dd> delegator) {
			super();
			this.delegator = delegator;
//			this.cachedMisom = null;
			this.cachedMW = null;
			misomLocCache = null;
		}

		protected final MultiISOMRetangularMap<Dd> delegator;
//		protected MatrixInSpaceObjectsManager<Dd> cachedMisom;
		protected MISOMWrapper<Dd> cachedMW;
		protected MISOMAndLocation<Dd> misomLocCache;

		public MatrixInSpaceObjectsManager<Dd> getCachedMisom() { return cachedMW == null ? null : cachedMW.misom; }

		///

		/** Calls {@link #getMisomAt(int, int)} */
		public MISOMAndLocation<Dd> getMisomAt(Point location) {
			return getMisomAt(location.x, location.y);
		}

		/**
		 * Returns a pair holding the desired {@link MatrixInSpaceObjectsManager} and a
		 * {@link Point} representing its location in space (in
		 * {@link MultiISOMRetangularMap}, to be precise).
		 */
		public MISOMAndLocation<Dd> getMisomAt(int x, int y) {
			MISOMWrapper<Dd> mw;
			if (cachedMW == null || (!cachedMW.contains(x, y))) {
				mw = cachedMW = delegator.getMISOMWrapperContaining(x, y);
				misomLocCache = (mw == null) ? null : new MISOMAndLocation<Dd>(mw.misom, mw.x, mw.y);
			} else {
			}
			return misomLocCache;
		}

		@Override
		public NodeIsom getNodeAt(Point location) { return this.getNodeAt(location.x, location.y); }

		@Override
		public NodeIsom getNodeAt(int x, int y) {
//			MISOMWrapper<Dd> mw;
//			MatrixInSpaceObjectsManager<Dd> misom;
			MISOMAndLocation<Dd> ml;
			ml = getMisomAt(x, y);
			if (ml == null)
				return null;
//			mw = cachedMW;
			// consider the offset
			return ml.misom.getNodeAt(x - ml.locationMisom.x, y - ml.locationMisom.y);
		}

		@Override
		public void forEachNode(Consumer<NodeIsom> action) { delegator.forEachNode(action); }

		/** Produced by {@link NodeIsomProviderCachingMISOM#getMisomAt(Point)} */
		public static class MISOMAndLocation<D extends Number> {
			public final MatrixInSpaceObjectsManager<D> misom;
			public final Point locationMisom;

			protected MISOMAndLocation(MatrixInSpaceObjectsManager<D> misom, int x, int y) {
				this(misom, new Point(x, y));
			}

			protected MISOMAndLocation(MatrixInSpaceObjectsManager<D> misom, Point locationMisom) {
				super();
				this.misom = misom;
				this.locationMisom = locationMisom;
			}
		}
	}

	//

	//

	protected static class ISOMCachingMISOM<Dd extends Number> extends NodeIsomProviderCachingMISOM<Dd>
			implements InSpaceObjectsManager<Dd> {
		private static final long serialVersionUID = 1L;

		public ISOMCachingMISOM(MultiISOMRetangularMap<Dd> delegator) { super(delegator); }

		//

		//

		@Override
		public void clearAllNodes() { delegator.clearAllNodes(); }

		@Override
		public Iterator<ObjectLocated> iterator() { return delegator.iterator(); }

		@Override
		public ProviderShapesIntersectionDetector getProviderShapesIntersectionDetector() {
			return delegator.getProviderShapesIntersectionDetector();
		}

		@Override
		public void forEach(Consumer<? super ObjectLocated> action) { delegator.forEach(action); }

		@Override
		public ProviderShapeRunner getProviderShapeRunner() { return delegator.getProviderShapeRunner(); }

		@Override
		public void setProviderShapesIntersectionDetector(
				ProviderShapesIntersectionDetector providerShapesIntersectionDetector) {
			delegator.setProviderShapesIntersectionDetector(providerShapesIntersectionDetector);
		}

		@Override
		public void setProviderShapeRunner(ProviderShapeRunner providerShapeRunner) {
			delegator.setProviderShapeRunner(providerShapeRunner);
		}

		@Override
		public boolean removeAllObjects() { return delegator.removeAllObjects(); }

		@Override
		public int hashCode() { return delegator.hashCode(); }

		@Override
		public boolean clearArea(AbstractShape2D areaToClear) { return delegator.clearArea(areaToClear); }

		@Override
		public boolean remove(AbstractShape2D areaToClear, Predicate<ObjectLocated> objectFilter) {
			return delegator.remove(areaToClear, objectFilter);
		}

		@Override
		public Spliterator<ObjectLocated> spliterator() { return delegator.spliterator(); }

		@Override
		public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, ObjCollector<ObjectLocated> collector,
				List<Point> path) {
			return delegator.findInPath(areaToLookInto, collector, path);
		}

		@Override
		public List<Point> getPath(Point start, Point destination, Predicate<ObjectLocated> isWalkableTester) {
			return delegator.getPath(start, destination, isWalkableTester);
		}

		@Override
		public Set<ObjectLocated> fetch(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter) {
			return delegator.fetch(areaToLookInto, objectFilter);
		}

		@Override
		public List<Point> getPath(Point start, Point destination) { return delegator.getPath(start, destination); }

		@Override
		public List<Point> getPath(ObjectShaped objRequiringTo, Point destination,
				Predicate<ObjectLocated> isWalkableTester) {
			return delegator.getPath(objRequiringTo, destination, isWalkableTester);
		}

		@Override
		public Set<ObjectLocated> fetch(AbstractShape2D areaToLookInto) { return delegator.fetch(areaToLookInto); }

		@Override
		public LoggerMessages getLog() { return delegator.getLog(); }

		@Override
		public AbstractShape2D getBoundingShape() { return delegator.getBoundingShape(); }

		@Override
		public boolean equals(Object obj) { return delegator.equals(obj); }

		@Override
		public PathFinderIsom<Point, ObjectLocated, Dd> getPathFinder() { return delegator.getPathFinder(); }

		@Override
		public NumberManager<Dd> getNumberManager() { return delegator.getNumberManager(); }

		@Override
		public PathOptimizer<Point> getPathOptimizer() { return delegator.getPathOptimizer(); }

		@Override
		public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, List<Point> path) {
			return delegator.findInPath(areaToLookInto, path);
		}

		@Override
		public void setLog(LoggerMessages log) { delegator.setLog(log); }

		@Override
		public List<Point> getPath(ObjectShaped objRequiringTo, Point destination) {
			return delegator.getPath(objRequiringTo, destination);
		}

		@Override
		public void setPathFinder(PathFinderIsom<Point, ObjectLocated, Dd> pathFinder) {
			delegator.setPathFinder(pathFinder);
		}

		@Override
		public void setNumberManager(NumberManager<Dd> numberManager) { delegator.setNumberManager(numberManager); }

		@Override
		public void setPathOptimizer(PathOptimizer<Point> pathOptimizer) { delegator.setPathOptimizer(pathOptimizer); }

		@Override
		public void runOnWholeMap(PointConsumer action) { delegator.runOnWholeMap(action); }

		@Override
		public void runOnShape(AbstractShape2D shape, PointConsumer action) { delegator.runOnShape(shape, action); }

		@Override
		public <NodeType extends Point, NodeContent, D extends Number> List<NodeType> getPath(NodeType start,
				NodeType destination, PathFinder<NodeType, NodeContent, D> pathFinder, NumberManager<D> numManager,
				Predicate<NodeContent> isWalkableTester) {
			return delegator.getPath(start, destination, pathFinder, numManager, isWalkableTester);
		}

		@Override
		public <NodeType extends Point, NodeContent, D extends Number> List<NodeType> getPath(NodeType start,
				NodeType destination, PathFinder<NodeType, NodeContent, D> pathFinder, NumberManager<D> numManager) {
			return delegator.getPath(start, destination, pathFinder, numManager);
		}

		@Override
		public Set<ObjectLocated> getAllObjectLocated() { return delegator.getAllObjectLocated(); }

		@Override
		public ObjectLocated getObjectLocated(Integer ID) { return delegator.getObjectLocated(ID); }

		@Override
		public void forEachAdjacents(NodeIsom node, BiConsumer<NodeIsom, Dd> adjacentDistanceConsumer) {
			delegator.forEachAdjacents(node, adjacentDistanceConsumer);
		}

		@Override
		public <NodeType extends Point, NodeContent, D extends Number> List<NodeType> getPath(
				ObjectShaped objRequiringToMove, NodeType destination, PathFinder<NodeType, NodeContent, D> pathFinder,
				NumberManager<D> numManager, Predicate<NodeContent> isWalkableTester) {
			return delegator.getPath(objRequiringToMove, destination, pathFinder, numManager, isWalkableTester);
		}

		@Override
		public ObjLocatedCollectorIsom newObjLocatedCollector(Predicate<ObjectLocated> objectFilter) {
			return delegator.newObjLocatedCollector(objectFilter);
		}

		@Override
		public <NodeType extends Point, NodeContent, D extends Number> List<NodeType> getPath(
				ObjectShaped objRequiringToMove, NodeType destination, PathFinder<NodeType, NodeContent, D> pathFinder,
				NumberManager<D> numManager) {
			return delegator.getPath(objRequiringToMove, destination, pathFinder, numManager);
		}

		@Override
		public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter,
				List<Point> path) {
			return delegator.findInPath(areaToLookInto, objectFilter, path);
		}

		@Override
		public boolean add(ObjectLocated o) { return delegator.add(o); }

		@Override
		public boolean contains(ObjectLocated o) { return delegator.contains(o); }

		@Override
		public boolean remove(ObjectLocated o) { return delegator.remove(o); }

		public void removeAllMaps() { delegator.removeAllMaps(); }

		public void clear() { delegator.clear(); }

		public MatrixInSpaceObjectsManager<Dd> getMISOMContaining(Point p) { return delegator.getMISOMContaining(p); }

		public void addMap(MISOMWrapper<Dd> r) { delegator.addMap(r); }

		public void addMaps(Collection<MISOMWrapper<Dd>> mapsList) { delegator.addMaps(mapsList); }

		@Override
		public String toString() { return delegator.toString(); }

		public void removeMap(MISOMWrapper<Dd> r) { delegator.removeMap(r); }

		public void removeMap(Collection<MISOMWrapper<Dd>> mapsList) { delegator.removeMap(mapsList); }

	}
}