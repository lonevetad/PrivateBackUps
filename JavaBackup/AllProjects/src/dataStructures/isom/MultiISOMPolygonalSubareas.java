package dataStructures.isom;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.SetMapped;
import dataStructures.isom.MultiISOMRetangularMap.NodeQuadtreeMultiISOMRectangular;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager.CoordinatesDeltaForAdjacentNodes;
import dataStructures.isom.pathFinders.Heuristic8GridMovement;
import dataStructures.isom.pathFinders.PathFinderIsomAStar;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.PointInt;
import geometry.ProviderShapeRunner;
import geometry.ProviderShapesIntersectionDetector;
import geometry.ShapesIntersectionDetector;
import geometry.implementations.shapes.ShapeRectangle;
import tools.Comparators;
import tools.NumberManager;
import tools.Stringable;
import tools.UniqueIDProvider;

// TODO nella "addMap", considerare	la Shape dell'ISOM da aggiungere E

/**
 * Takes inspiration from {@link MultiISOMRetangularCaching}, but areas could be
 * polygonal, no matter what.
 */
public class MultiISOMPolygonalSubareas<Distance extends Number> extends InSpaceObjectsManagerImpl<Distance>
		implements InSpaceObjectsManager<Distance> {
	private static final long serialVersionUID = 1L;
	protected static final UniqueIDProvider ID_PROV_MULTI_ISOM_POLY = UniqueIDProvider.newBasicIDProvider();

	public MultiISOMPolygonalSubareas(int maximumSubmapsEachSection) {
		super();
		if (maximumSubmapsEachSection < 1) {
			log.log("Incorrect number of maximum submaps on each section: " + maximumSubmapsEachSection);
			throw new IllegalArgumentException(
					"Incorrect number of maximum submaps on each section: " + maximumSubmapsEachSection);
		}
		this.ID = ID_PROV_MULTI_ISOM_POLY.getNewID();
		this.maximumSubmapsEachSection = maximumSubmapsEachSection;
		this.uidProvider = UniqueIDProvider.newBasicIDProvider();
		mapsLocatedInSpace = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.INTEGER_COMPARATOR);
		mapsAsList = mapsLocatedInSpace.toListValue(r -> r.ID);
		isomsHeldCenterLocated = new SetMapped<>(mapsLocatedInSpace.toSetValue(w -> w.ID), w -> w.isomAndLocation);
		setObjectsAddedMap(MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR));
	}

	protected final int maximumSubmapsEachSection;
	protected int maxDepth, xLeftTop, yLeftTop, xRightBottom, yRightBottom, width, height;
	protected final Integer ID;
	protected NodeQuadtreeISOM root;
	protected final MapTreeAVL<Integer, ISOMWrapperLocated> mapsLocatedInSpace;
	protected final Set<Entry<InSpaceObjectsManager<Distance>, PointInt>> isomsHeldCenterLocated;
	protected final List<ISOMWrapperLocated> mapsAsList;
	protected ShapeRectangle shapeBoundingBox;
	protected Map<Integer, ObjectLocated> objectsAddedMap;
	protected Set<ObjectLocated> objectsAddedSet;
	//
	protected final UniqueIDProvider uidProvider;
	protected ProviderShapeRunner providerShapeRunner;
	/** Used on adding algorithm */
	protected ProviderShapesIntersectionDetector providerShapesIntersectionDetector;
	//
	protected ISOMWrapperLocated cachedIsom;

	///

	//

	//

	@Override
	public Integer getID() { return ID; }

	public ISOMWrapperLocated getCachedMisom() { return cachedIsom == null ? null : cachedIsom /* .misom */; }

	public NodeQuadtreeISOM getRoot() { return root; }

	public int getMaxDepth() { return maxDepth; }

	public int getxLeftTop() { return xLeftTop; }

	public int getyLeftTop() { return yLeftTop; }

	public int getxRightBottom() { return xRightBottom; }

	public int getyRightBottom() { return yRightBottom; }

	@Override
	public int getWidth() { return width; }

	@Override
	public int getHeight() { return height; }

	@Override
	public AbstractShape2D getBoundingShape() { return this.shapeBoundingBox; }

	@Override
	public ProviderShapesIntersectionDetector getProviderShapesIntersectionDetector() {
		return this.providerShapesIntersectionDetector;
	}

	@Override
	public ProviderShapeRunner getProviderShapeRunner() { return this.providerShapeRunner; }

	@Override
	public Set<ObjectLocated> getAllObjectLocated() { return this.objectsAddedSet; }

	@Override
	public ObjectLocated getObjectLocated(Integer ID) { return this.objectsAddedMap.get(ID); }

	/**
	 * Returns a set of pairs of {@link InSpaceObjectsManager} and its absolute
	 * location of its top-left corner (assuming a null rotation, i.e.
	 * {@link NodeQuadtreeISOM#get}
	 */

	public Set<Entry<InSpaceObjectsManager<Distance>, PointInt>> getIsomsHeldCenterLocated() {
		return isomsHeldCenterLocated;
	}

	///

	//

	@Override
	public void setProviderShapesIntersectionDetector(
			ProviderShapesIntersectionDetector providerShapesIntersectionDetector) {
		this.providerShapesIntersectionDetector = providerShapesIntersectionDetector;
	}

	@Override
	public void setProviderShapeRunner(ProviderShapeRunner providerShapeRunner) { // TODO Auto-generated method stub
		this.providerShapeRunner = providerShapeRunner;
	}

	@Override
	public void setShape(AbstractShape2D shape) {}

	protected PathFinderIsom<Distance> newPathFinder() {
		return new PathFinderIsomAStar<>(this, new Heuristic8GridMovement<>(getWeightManager()));
	}

	@Override
	public void setWeightManager(NumberManager<Distance> numberManager) {
		this.weightManager = numberManager;
		this.setPathFinder(newPathFinder());
	}

	/** Sets the map holding all objects in this space. */
	protected void setObjectsAddedMap(Map<Integer, ObjectLocated> objectsAdded) {
		this.objectsAddedMap = objectsAdded;
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

	// TODO other public methods

	//

	// but this following method is fundamental as public ones

	/**
	 * Create a new {@link NodeQuadtreeMultiISOMRectangular} based on a given map
	 * (to be held) and a father node.
	 */
	protected NodeQuadtreeISOM newNodeWith(ISOMWrapperLocated map, NodeQuadtreeISOM fatherNode) {
		NodeQuadtreeISOM newNode;
		newNode = new NodeQuadtreeISOM(fatherNode);
		if (this.getMaxDepth() < newNode.depth) { this.maxDepth = newNode.depth; }
		newNode.submaps = new ArrayList<>(4);
		newNode.submaps.add(map);
		return newNode;
	}

	public void clear() { removeAllMaps(); }

	public void removeAllMaps() {
		mapsLocatedInSpace.clear();
		root = null;
		clearDimensionCache();
	}

	@Override
	public NodeIsom<Distance> getNodeAt(Point location) { return getNodeAt(location.x, location.y); }

	@Override
	public NodeIsom<Distance> getNodeAt(int x, int y) {
		ISOMWrapperLocated ml;
		ml = getMapLocatedContaining(x, y);
		if (ml == null)
			return null;
		return ml.getNodeAt(x, y); // offset is implemented in this delegated function
	}

	@Override
	public void forEachNode(BiConsumer<NodeIsom<Distance>, Point> action) {
		this.mapsLocatedInSpace.forEach((i, mw) -> mw.forEachNode(action));
	}

	/**
	 * See {@link MultiISOMRetangularMap#getMapLocatedContaining(Point)}, since it's
	 * a similar function.
	 */
	public ISOMWrapperLocated getMapLocatedContaining(Point p) { return getMapLocatedContaining(p.x, p.y); }

	/**
	 * See {@link MultiISOMRetangularMap#getMapLocatedContaining(int, int)}, since
	 * it's a similar function.
	 */
	public ISOMWrapperLocated getMapLocatedContaining(int x, int y) {
		NodeQuadtreeISOM n, prev;
		List<ISOMWrapperLocated> submaps;

		n = prev = getRoot();
		if (n == null)
			return null;
		// check cache
//		providerShapesIntersectionDetector.getShapesIntersectionDetector(si1, si2)
		if (cachedIsom != null && cachedIsom.getShape().contains(x, y)) { return cachedIsom; }

		// traverse the tree
		while (!(n == null || n.isLeaf())) {
			prev = n;
			if (x >= n.getXMiddle()) { // east
				n = (y >= n.getYMiddle()) ? n.sse : n.sne;
			} else {// west
				n = (y >= n.getYMiddle()) ? n.ssw : n.snw;
			}
		}
		// get the collection of submaps
		submaps = (n != null) ? n.submaps : prev.submaps;
		if (submaps == null)
			return null;
		// if any holds that point, then return it
		for (ISOMWrapperLocated r : submaps) {
//			if (r.contains(x, y)) // MathUtilities.isInside(r, p))//
			if (r.isomHeld.getShape().contains(x, y))
				return r;
		}
		return null; // Error 404
	}

	// TODO addMap

	/**
	 * See
	 * {@link MultiISOMRetangularMap#addMap(MatrixInSpaceObjectsManager, int, int)},
	 * since it's a similar function.<br>
	 * REMEMBER: the given coordinates are the top-left corner.
	 */
	public ISOMWrapperLocated addMap(InSpaceObjectsManager<Distance> map, int x, int y) {
		return addMap(map, x, y, 0.0);
	}

	/**
	 * See
	 * {@link MultiISOMRetangularMap#addMap(MatrixInSpaceObjectsManager, int, int,double)},
	 * since it's a similar function.
	 */
	public ISOMWrapperLocated addMap(InSpaceObjectsManager<Distance> map, int x, int y, double angleRotationDegrees) {
		int c;
		ISOMWrapperLocated r;
		if (map == null || map.getWidth() < 1 || map.getWidth() < 1)
			return null;
		map.setTopLeftCorner(x, y);
		System.out.println(
				"setting top-left corner in (x:" + x + ";y:" + y + ") , but map gives: " + map.getTopLetCorner());
		System.out.println("center point: " + map.getCenter() + ", w: " + map.getWidth() + ", h: " + map.getHeight());
		r = new ISOMWrapperLocated(map);
		r.setAngleRotationDegrees(angleRotationDegrees);
		c = updateBoundingBox(r);
		if (c >= 0) {
			mapsLocatedInSpace.put(r.ID, r);
			if (c > 0)
				rebuild();
			else
				addNotRebuilding(r);
		}
		return r;
	}

	/** See {@link #addMap(MatrixInSpaceObjectsManager, int, int)}. */
	public ISOMWrapperLocated addMap(InSpaceObjectsManager<Distance> map, Point locationLeftTop) {
		return addMap(map, locationLeftTop, 0.0);
	}

	public ISOMWrapperLocated addMap(InSpaceObjectsManager<Distance> map, Point locationLeftTop,
			double angleRotationDegrees) {
		if (map == null || map.getWidth() < 1 || map.getWidth() < 1)
			return null;
		return addMap(map, locationLeftTop.x, locationLeftTop.y, angleRotationDegrees);
	}

	public void rebuild() {
		this.maxDepth = 0;
		this.root = rebuild(null, mapsAsList, //
				this.getxLeftTop(), this.getyLeftTop(), this.xRightBottom, this.yRightBottom, this.width, this.height//
				, this.getxLeftTop() + (this.width >> 1)//
				, this.getyLeftTop() + (this.height >> 1)); // clear all
	}

//	public void addMaps(Collection<InSpaceObjectsManager<Distance>> mapsList) {
//		int[] cc = { -1 };
//		mapsList.forEach(r -> {
//			int c;
//			if (r != null) {
//				c = updateBoundingBox(r);
//				if (c >= 0) {
//					if (cc[0] < c)
//						cc[0] = c;
//					mapsLocatedInSpace.put(r.ID, r);
//				}
//			}
//		});
//		if (cc[0] > 0)
//			rebuild();
//		else
//			mapsList.forEach(this::addNotRebuilding);
//	}

	public void removeMap(ISOMWrapperLocated r) {
		if (mapsLocatedInSpace.containsKey(r.ID)) {
			mapsLocatedInSpace.remove(r.ID);
			recalculateBoundingBox();
			/*
			 * TODO should perform a more fine action like adding maps
			 * ("if the map change the bounding box or not")
			 */
			rebuild();
		}
	}

	public void removeMapLocatedIn(InSpaceObjectsManager<Distance> map, Point location) {
		this.removeMapLocatedIn(map, location.x, location.y);
	}

	public void removeMapLocatedIn(InSpaceObjectsManager<Distance> map, int x, int y) {
		ISOMWrapperLocated r;
		r = getMapLocatedContaining(x, y);
		if (r != null) { removeMap(r); }
	}

	public void removeMaps(Collection<ISOMWrapperLocated> mapsList) {
		boolean[] cc = { false };
		mapsList.forEach(r -> {
			if (mapsLocatedInSpace.containsKey(r.ID)) {
				cc[0] = true;
				mapsLocatedInSpace.remove(r.ID);
			}
		});
		if (cc[0]) {
			recalculateBoundingBox();
			rebuild();
		}
	}

	@Override
	public void forEachAdjacents(NodeIsom<Distance> node,
			BiConsumer<NodeIsom<Distance>, Distance> adjacentDistanceConsumer) {
//		ISOMWrapperLocated misom;
		ISOMWrapperLocated mlis;
		Point p, absoluteNodeLocation;
		NodeIsom<Distance> adj;
		p = new Point();
		absoluteNodeLocation = node.getLocationAbsolute();
		for (CoordinatesDeltaForAdjacentNodes c : MatrixInSpaceObjectsManager.VALUES_CoordinatesDeltaForAdjacentNodes) {
			p.x = absoluteNodeLocation.x + c.dx;
			p.y = absoluteNodeLocation.y + c.dy;
			adj = null;
//			misom = getMapLocatedContaining(p);
			mlis = getMapLocatedContaining(p);
//			if (misom != null) {
			if (mlis != null) {
				adj = this.getNodeAt(p); // changed to "this" to let considering the misom's offset
				if (adj != null && mlis.getShape().contains(p))
					adjacentDistanceConsumer.accept(adj, getWeightManager().fromDouble(c.weight));
			}
		}
	}

	@Override
	public boolean add(ObjectLocated o) {
		ISOMWrapperLocated m;
		if (o == null)
			return false;
		m = getMapLocatedContaining(o.getLocation());
		if (m == null)
			System.out.println("---not found D:");
		if (m == null)
			return false;
		System.out.println("--- found isom wrapper at: " + o.getLocation());
		return m.add(o);
	}

	@Override
	public boolean contains(ObjectLocated o) {
		ISOMWrapperLocated m;
		if (o == null)
			return false;
		m = getMapLocatedContaining(o.getLocation());
		if (m == null)
			return false;
		return m.contains(o);
	}

	@Override
	public boolean remove(ObjectLocated o) {
		ISOMWrapperLocated m;
		if (o == null)
			return false;
		m = getMapLocatedContaining(o.getLocation());
		if (m == null)
			return false;
		return m.remove(o);
	}

	// TODO protected

	/**
	 * See
	 * {@link MultiISOMRetangularMap#updateBoundingBox(dataStructures.isom.MultiISOMRetangularMap.MatrixISOMLocatedInSpace)},
	 * since it's a similar function.
	 */
	protected int updateBoundingBox(ISOMWrapperLocated isom) {
		boolean changed;
		int temp;
		AbstractShape2D sh;
		Rectangle r;
		sh = isom.getShape();
		r = sh.getBoundingBox();
		if (r.width < 1 || r.height < 1) { return -1; }
		if (shapeBoundingBox == null) {
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
		if (r.x < getxLeftTop()) {
			changed = true;
			xLeftTop = r.x;
		}
		if (r.y < getyLeftTop()) {
			changed = true;
			yLeftTop = r.y;
		}
		temp = r.x + r.width;
		if (temp > xRightBottom) {
			changed = true;
			xRightBottom = temp;
		}
		temp = r.y + r.height;
		if (temp > yRightBottom) {
			changed = true;
			yRightBottom = temp;
		}
		this.width = xRightBottom - getxLeftTop();
		this.height = yRightBottom - getyLeftTop();
		if (changed || getRoot() == null) {
			resetShape(false);
			return 1;
		}
		//
		return 0;
	}

	protected NodeQuadtreeISOM rebuild(NodeQuadtreeISOM father, List<ISOMWrapperLocated> submaps, //
			int xLeftTop, int yLeftTop, int xRightBottom, int yRightBottom, int width, int height, //
			int xMiddle, int yMiddle) {
		final int mxw, mxe, myn, mys, widthWest, widthEst, heightNorth, heightSouth, yMiddlemmmm, xMiddlemmmm;
		NodeQuadtreeISOM n;
		List<ISOMWrapperLocated> snw, sne, ssw, sse;
		final ShapeRectangle shapeTempSubarea;
		n = new NodeQuadtreeISOM(father);
		if (this.getMaxDepth() < n.depth) { this.maxDepth = n.depth; }
//		n.x = xLeftTop;
//		n.y = yLeftTop;
//		n.width = width;
//		n.height = height;
		n.shapeBBSubarea.setRectangle(xLeftTop, yLeftTop, width, height);
//		n.xMiddle = xMiddle;
//		n.yMiddle = yMiddle;
		if (submaps.size() <= maximumSubmapsEachSection //
				|| width <= MultiISOMRetangularMap.MINIMUM_DIMENSION_MAP
				|| height <= MultiISOMRetangularMap.MINIMUM_DIMENSION_MAP) {
			n.submaps = submaps;
			return n;
		}
		snw = new ArrayList<>(maximumSubmapsEachSection);
		sne = new ArrayList<>(maximumSubmapsEachSection);
		ssw = new ArrayList<>(maximumSubmapsEachSection);
		sse = new ArrayList<>(maximumSubmapsEachSection);
		yMiddlemmmm = yMiddle;
		xMiddlemmmm = xMiddle;
		mxw = xLeftTop + ((widthWest = (xMiddle - xLeftTop)) >> 1);
		myn = yLeftTop + ((heightNorth = (yMiddle - yLeftTop)) >> 1);
		mxe = ++xMiddle + ((widthEst = 1 + (xRightBottom - xMiddle)) >> 1);
		mys = ++yMiddle + ((heightSouth = 1 + (yRightBottom - yMiddle)) >> 1);
		xMiddle = xMiddlemmmm;
		yMiddle = yMiddlemmmm;
		shapeTempSubarea = new ShapeRectangle(true);
		shapeTempSubarea.setAngleRotation(0);

		submaps.forEach(r -> {
			final ShapesIntersectionDetector intersectionDetector;
			final AbstractShape2D shapeMap;

			shapeMap = r.isomHeld.getShape();
			intersectionDetector = providerShapesIntersectionDetector.getShapesIntersectionDetector(shapeTempSubarea,
					shapeMap);
			shapeTempSubarea.setRectangle(xLeftTop, yLeftTop, widthWest, heightNorth);
			if (intersectionDetector.areIntersecting(shapeTempSubarea, shapeMap)) { snw.add(r); }
			shapeTempSubarea.setRectangle(xMiddlemmmm + 1, yLeftTop, widthEst, heightNorth);
			if (intersectionDetector.areIntersecting(shapeTempSubarea, shapeMap)) { sne.add(r); }
			shapeTempSubarea.setRectangle(xMiddlemmmm + 1, yMiddlemmmm + 1, widthEst, heightSouth);
			if (intersectionDetector.areIntersecting(shapeTempSubarea, shapeMap)) { sse.add(r); }
			shapeTempSubarea.setRectangle(xLeftTop, yMiddlemmmm + 1, widthWest, heightSouth);
			if (intersectionDetector.areIntersecting(shapeTempSubarea, shapeMap)) { ssw.add(r); }
		});
		if (snw.size() > 0)
			n.snw = rebuild(n, snw, //
					xLeftTop, yLeftTop, xMiddlemmmm, yMiddlemmmm, // corner points
					widthWest, heightNorth, //
					mxw, myn); // middle point
		if (sne.size() > 0)
			n.sne = rebuild(n, sne, //
					(xMiddlemmmm + 1), yLeftTop, xRightBottom, yMiddlemmmm, // corner points
					widthEst, heightNorth, // dimensions
					mxe, myn); // middle point
		if (ssw.size() > 0)
			n.ssw = rebuild(n, ssw, //
					xLeftTop, yMiddlemmmm + 1, xMiddlemmmm, yRightBottom, // corner points
					widthWest, heightSouth, // dimensions
					mxw, mys); // middle point
		if (sse.size() > 0)
			n.sse = rebuild(n, sse, //
					xMiddlemmmm + 1, yMiddlemmmm + 1, xRightBottom, yRightBottom, // corner points
					widthEst, heightSouth, // dimensions
					mxe, mys); // middle point
		return n;
		// TODO to-do convert from MultiISOMREctangular to M..Poly
	}

	protected void addNotRebuilding(ISOMWrapperLocated map) {
		if (getRoot() == null)
			rebuild();
		else
			root = addNotRebuilding(map, getRoot());
	}

	// TODO to-do convert from MultiISOMREctangular to M..Poly
	protected NodeQuadtreeISOM addNotRebuilding(ISOMWrapperLocated map, NodeQuadtreeISOM currentNode) {
		int xcn, ycn, wcn, hcn;
		xcn = currentNode.getXTopLeft();
		ycn = currentNode.getYTopLeft();
		wcn = currentNode.getWidthSubmap();
		hcn = currentNode.getHeightSubmap();
		if (currentNode.isLeaf()) {
			if (currentNode.submaps.size() < this.maximumSubmapsEachSection) {
				// just add and nothing more
				currentNode.submaps.add(map);
			} else {
				// else, build ricoursively
//				currentNode.submaps.add(map);
				List<ISOMWrapperLocated> submaps;
				submaps = currentNode.submaps;
				currentNode.submaps = null;
				submaps.add(map);
				currentNode = rebuild(currentNode.father, /* currentNode. */submaps, xcn, ycn, xcn + wcn, ycn + wcn,
						wcn, hcn, currentNode.getXMiddle(), currentNode.getYMiddle());
				if (this.getMaxDepth() < currentNode.depth) { this.maxDepth = currentNode.depth; }
			}

		} else {
			int hw, hh, x_w_1, y_h_1;
			AbstractShape2D shapeMap;
			ShapeRectangle shapeNodeSubsection;
			ShapesIntersectionDetector intersectionDetector;
			shapeMap = map.getShape();
			hw = wcn >> 1;
			hh = hcn >> 1;
			x_w_1 = xcn + hw + 1;
			y_h_1 = ycn + hh + 1;
			// for each subsection ..
			shapeNodeSubsection = new ShapeRectangle(0.0, xcn, ycn, true, hw, hh);
			intersectionDetector = providerShapesIntersectionDetector.getShapesIntersectionDetector(shapeNodeSubsection,
					shapeMap);
			if ((currentNode.snw != null && currentNode.snw.intersectsWithMap(map)) //
					|| // or that area does NOT exists BUT could hold the new map
					(currentNode.snw == null && //
//							MathUtilities.intersects(xcn, ycn, hw, hh, shapeMap.getXTopLeft(), shapeMap.getYTopLeft(),
//									shapeMap.getWidth(), shapeMap.getHeight())
							intersectionDetector.areIntersecting(shapeNodeSubsection, shapeMap)//
					)) {
				currentNode.snw = (currentNode.snw == null) ? newNodeWith(map, currentNode)
						: addNotRebuilding(map, currentNode.snw);
			}
			shapeNodeSubsection.setRectangle(x_w_1, ycn, wcn - hw, hh);
			if ((currentNode.sne != null && currentNode.sne.intersectsWithMap(map)) //
					|| // or that area does NOT exists BUT could hold the new map
					(currentNode.sne == null && //
//							MathUtilities.intersects(x_w_1, ycn, wcn - hw, hh, shapeMap.getXTopLeft(),
//									shapeMap.getYTopLeft(), shapeMap.getWidth(), shapeMap.getHeight())
							intersectionDetector.areIntersecting(shapeNodeSubsection, shapeMap)//
					)) {
				currentNode.sne = (currentNode.sne == null) ? newNodeWith(map, currentNode)
						: addNotRebuilding(map, currentNode.sne);
			}
			shapeNodeSubsection.setRectangle(xcn, y_h_1, hw, hcn - hh);
			if ((currentNode.ssw != null && currentNode.ssw.intersectsWithMap(map)) //
					|| // or that area does NOT exists BUT could hold the new map
					(currentNode.ssw == null && //
//							MathUtilities.intersects(xcn, y_h_1, hw, hcn - hh, shapeMap.getXTopLeft(),
//									shapeMap.getYTopLeft(), shapeMap.getWidth(), shapeMap.getHeight())
							intersectionDetector.areIntersecting(shapeNodeSubsection, shapeMap)//
					)) {
				currentNode.ssw = (currentNode.ssw == null) ? newNodeWith(map, currentNode)
						: addNotRebuilding(map, currentNode.ssw);
			}
			shapeNodeSubsection.setRectangle(x_w_1, y_h_1, wcn - hw, hcn - hh);
			if ((currentNode.sse != null && currentNode.sse.intersectsWithMap(map)) //
					|| // or that area does NOT exists BUT could hold the new map
					(currentNode.sse == null && //
//							MathUtilities.intersects(x_w_1, y_h_1, wcn - hw, hcn - hh, shapeMap.getXTopLeft(),
//									shapeMap.getYTopLeft(), shapeMap.getWidth(), shapeMap.getHeight())
							intersectionDetector.areIntersecting(shapeNodeSubsection, shapeMap)//
					)) {
				currentNode.sse = (currentNode.sse == null) ? newNodeWith(map, currentNode)
						: addNotRebuilding(map, currentNode.sse);
			}
		}
		return currentNode;
	}

	//

	protected void resetShape(boolean mustReallocate) {
		if (mustReallocate)
			shapeBoundingBox = new ShapeRectangle(0.0, 0, 0, true, 0, 0);
		shapeBoundingBox.setWidth(width);
		shapeBoundingBox.setHeight(height);
		shapeBoundingBox.setLeftTopCorner(getxLeftTop(), getyLeftTop());
	}

	protected void recalculateBoundingBox() {
		clearDimensionCache();
		mapsAsList.forEach(this::updateBoundingBox);
	}

	protected void clearDimensionCache() {
		// neverBuilt = true;
		shapeBoundingBox = null;
		maxDepth = 0;
		xLeftTop = yLeftTop = xRightBottom = yRightBottom = width = height = 0;
//		this.objectsAddedMap.clear();
	}

	//

	// TODO CLASS

	/** It's still rectangular */
	protected class NodeQuadtreeISOM implements Stringable {
		private static final long serialVersionUID = 530125748L;
		protected int /* xLeftTop, yLeftTop, width, height, xMiddle, yMiddle, */
		depth;
		List<ISOMWrapperLocated> submaps;
		NodeQuadtreeISOM father, snw, sne, ssw, sse;
		ShapeRectangle shapeBBSubarea;

		protected NodeQuadtreeISOM(MultiISOMPolygonalSubareas<Distance>.NodeQuadtreeISOM father) {
			super();
			this.father = father;
			submaps = null;
			depth = (father == null) ? 1 : (father.depth + 1);
			shapeBBSubarea = new ShapeRectangle(true);
		}

		public List<ISOMWrapperLocated> getSubmaps() { return submaps; }

		public boolean isLeaf() { return submaps != null; }

		public int getXTopLeft() { return shapeBBSubarea.getXTopLeft(); }

		public int getYTopLeft() { return shapeBBSubarea.getYTopLeft(); }

		public int getWidthSubmap() { return shapeBBSubarea.getWidth(); }

		public int getHeightSubmap() { return shapeBBSubarea.getHeight(); }

		/** Similar (or same) to the concept of "centre". */
		public int getXMiddle() {
			return // this.xMiddle;
			shapeBBSubarea.getCenter().x;
		}

		/** See {@link #getXMiddle()}. */
		public int getYMiddle() {
			return // this.yMiddle;
			shapeBBSubarea.getCenter().y;
		}

		public int getDepth() { return this.depth; }

		/** Beware of <code>null</code>s! */
		public void forEachSubsection(Consumer<NodeQuadtreeISOM> action) {
			action.accept(snw);
			action.accept(sne);
			action.accept(ssw);
			action.accept(sse);
		}

		public void recalculateEachSubsection(Function<NodeQuadtreeISOM, NodeQuadtreeISOM> action) {
			snw = action.apply(snw);
			sne = action.apply(sne);
			ssw = action.apply(ssw);
			sse = action.apply(sse);
		}

		public boolean intersectsWithMap(ISOMWrapperLocated map) {
//			return MathUtilities.intersects(getXTopLeft(), getYTopLeft(), getWidthSubmap(), getHeightSubmap(), map.x, map.y, map.width,
//					map.height);
			return false;
		}
	}

	protected class ISOMWrapperLocated implements ObjectLocated {
		private static final long serialVersionUID = 1L;
//		protected int xLeftTop, yLeftTop, width, height
		/** In Degreed */
		protected double angleRotationDegrees, sinCache, cosCache, sinInverseCache, cosInverseCache;
		public final Integer ID;
		protected final InSpaceObjectsManager<Distance> isomHeld;
		protected Entry<InSpaceObjectsManager<Distance>, PointInt> isomAndLocation;

		protected ISOMWrapperLocated(InSpaceObjectsManager<Distance> isom) {
			super();
			this.isomHeld = isom;
			this.ID = uidProvider.getNewID();
			// sinCache =
			sinInverseCache = 0;
			// cosCache =
			cosInverseCache = 1;
			isomAndLocation = new IsomAndTopLefCornerAbs(isom, isom.getTopLetCorner());
		}

		public AbstractShape2D getShape() { return isomHeld.getShape(); }

		@Override
		public Integer getID() { return ID; }

		@Override
		public Point getLocation() { return isomHeld.getLocation(); }

		public InSpaceObjectsManager<Distance> getIsomHeld() { return isomHeld; }

		public double getAngleRotationDegrees() { return angleRotationDegrees; }

		public Entry<InSpaceObjectsManager<Distance>, PointInt> getIsomAndLocation() {
			Point isomLoc;
			isomLoc = this.isomHeld.getLocation(); // it is absolute and about center
			isomAndLocation.setValue(PointInt.fromPoint2D(isomLoc));
			return isomAndLocation;
		}

		public void setAngleRotationDegrees(double angleRotationDegrees) {
			double rad;
			this.angleRotationDegrees = angleRotationDegrees % 360.0;
			if (this.angleRotationDegrees < 0.0)
				this.angleRotationDegrees += 360.0;
			rad = Math.toRadians(this.angleRotationDegrees);
			sinCache = Math.sin(rad);
			cosCache = Math.cos(rad);
			sinInverseCache = Math.sin(-rad);
			cosInverseCache = Math.cos(-rad);
		}

		/** See {@link #setLocationAbsolute(int, int)}. */
		public void setLocationAbsolute(Point locationAbsolute) {
			setLocation(locationAbsolute);
		}

		/** To be meant as absolute location of the centre. */
		public void setLocationAbsolute(int x, int y) {
			setLocation(x, y);
		}

		@Override
		public void setLocation(Point p) { this.setLocation(p.x, p.y); }

		@Override
		public void setLocation(int x, int y) {
//			this.xLeftTop = x;
//			this.yLeftTop = y;
			isomHeld.setTopLeftCorner(x, y);
		}

		/**
		 * Change the provided {@link Point}'s internal values equal to the relative
		 * top-left corner of the {@link InSpaceObjectsManager} held by this class.
		 */
		public Point makePointRelativeToTopLeftCorner(Point p) {
			PointInt location;
			location = this.isomHeld.getTopLetCorner();
			p.x -= location.getX();
			p.y -= location.getY();
			return p;
		}

		/**
		 * Calls {@link #makePointRelativeToTopLeftCorner(Point)} providing a newly
		 * created {@link Point}. The given point is to be meant as <b>absolute</b>,
		 * i.e. NOT relative to this {@link InSpaceObjectsManager}.
		 */
		public Point makePointRelativeToTopLeftCorner(int x, int y) {
			return makePointRelativeToTopLeftCorner(new Point(x, y));
		}

		public Point makePointAbsoluteToTopLeftCorner(Point p) {
			PointInt location;
			location = this.isomHeld.getTopLetCorner();
			p.x += location.getX();
			p.y += location.getY();
			return p;
		}

		/** See {@link #makePointRelativeToTopLeftCorner(int, int)}. */
		public Point makePointRelativeToCenter(Point p) {
			Point location;
//			location = this.isomHeld.getTopLetCorner();
//			p.x += (this.isomHeld.getWidth() >> 1) - location.x;
//			p.y += (this.isomHeld.getHeight() >> 1) - location.y;
//			return p;
			location = this.isomHeld.getLocation();
			p.x -= location.x;
			p.y -= location.y;
			return p;
		}

		public Point makePointAbsoluteToCenter(int x, int y) { return makePointAbsoluteToCenter(new Point(x, y)); }

		public Point makePointAbsoluteToCenter(Point p) {
			Point location;
//			location = this.isomHeld.getTopLetCorner();
//			p.x += location.x - (this.isomHeld.getWidth() >> 1);
//			p.y += location.y - (this.isomHeld.getHeight() >> 1);
			location = this.isomHeld.getLocation();
			p.x += location.x;
			p.y += location.y;
			return p;
		}

		/**
		 * The given {@link InSpaceObjectsManager} has a rotation in degrees (i.e.:
		 * {@link #getAngleRotationDegrees()}), so the <b>relative</b> given point is
		 * rotated relatively to the center so that it could be used as a
		 * <i>2D-index</i>.
		 */
		public Point applyIsomsRotation(Point p) {
			int x, y;
			x = p.x;
			y = p.y;
			p.x = (int) (x * cosInverseCache + y * sinInverseCache);
			p.y = (int) ((y * cosInverseCache) - (x * sinInverseCache));
			return p;
		}

		/**
		 * Calls {@link #makePointRelativeToTopLeftCorner(Point)} providing a newly
		 * created {@link Point}.
		 */
		public Point applyIsomsRotation(int x, int y) { return applyIsomsRotation(new Point(x, y)); }

		/**
		 * Apply both {@link #makePointRelativeToTopLeftCorner(Point)} and then
		 * {@link #applyIsomsRotation(Point)} to the given <b>absolute</b> point.
		 */
		public Point makeRelativeToCenterAndRotate(Point p) {
			return applyIsomsRotation(makePointRelativeToCenter(p));
		}

		public Point makeRelativeAndRotate(int x, int y) { return makeRelativeToCenterAndRotate(new Point(x, y)); }

		protected void relativeFromCentreToLefttop(Point p) {
			p.x += this.isomHeld.getWidth() >> 1;
			p.y += this.isomHeld.getHeight() >> 1;
		}

		/** Absolute coordinates. */
		public NodeIsom<Distance> getNodeAt(int x, int y) {
//			double xtemp, ytemp;
			Point location;
			if (this.angleRotationDegrees != 0.0) {
				// NOTE: actions in "makeRelativeAndRotate" are make here just to make them fast
				location = this.isomHeld.getLocation();
				// make coordinates relative to the centre
//				x += location.x - (this.isomHeld.getWidth() >> 1);
//				y += location.y - (this.isomHeld.getHeight() >> 1);
				x -= location.x;
				y -= location.y;
//			s = Math.sin(a); // a is in radians
//			c = Math.cos(a);
//			rotationMatrix = [
//				[c, -s],
//				[s, c]
//			];
				// [x,y] = dotProduct( [x,y], rotationMatrix);
//			xtemp = x * cosInverseCache;
//			ytemp = y * sinInverseCache;
				x = (int) (x * cosInverseCache + y * sinInverseCache);
				y = (int) ((y * cosInverseCache) - (x * sinInverseCache));
			}
			return isomHeld.getNodeAt(x, y);
		}

		public boolean add(ObjectLocated o) {
			boolean c;
			int xo, yo;// , x, y;
			Point oldLocation;
			if (o == null)
				return false;
			oldLocation = o.getLocation(); // it's the center of the object
			xo = oldLocation.x;
			yo = oldLocation.y;
//			o.setLocation(xo - misomLocation.x, yo - misomLocation.y);
			makeRelativeToCenterAndRotate(oldLocation);
			relativeFromCentreToLefttop(oldLocation);
			c = isomHeld.add(o);
			o.setLocation(xo, yo);
			return c;
		}

		public boolean contains(ObjectLocated o) {
			boolean c;
			int xo, yo;// , x, y;
			Point oldLocation;
			if (o == null)
				return false;
			oldLocation = o.getLocation();
			xo = oldLocation.x;
			yo = oldLocation.y;
//			o.setLocation(xo - misomLocation.x, yo - misomLocation.y);
			makeRelativeToCenterAndRotate(oldLocation);
			c = isomHeld.contains(o);
			o.setLocation(xo, yo);
			return c;
		}

		public boolean remove(ObjectLocated o) {
			boolean c;
			int xo, yo;// , x, y;
			Point oldLocation;
//			misomLocation = this.isomHeld.getLocation();
			if (o == null)
				return false;
			oldLocation = o.getLocation();
			xo = oldLocation.x;
			yo = oldLocation.y;
//			o.setLocation(xo - misomLocation.x, yo - misomLocation.y);
			makeRelativeToCenterAndRotate(oldLocation);
			c = isomHeld.remove(o);
			o.setLocation(xo, yo);
			return c;
		}

		public void forEachNode(BiConsumer<NodeIsom<Distance>, Point> action) {
			int hw, hh;
//			Point isomLocationCentre;
//			isomLocationCentre = this.isomHeld.getLocation();
			PointInt isomTopLeftCorner;
			isomTopLeftCorner = this.isomHeld.getTopLetCorner();
			hw = (this.isomHeld.getWidth() >> 1);
			hh = (this.isomHeld.getHeight() >> 1);
//			System.out.println("\n\n\n for eaching NODE and location");
			this.isomHeld.forEachNode((n, p) -> {
				int xRotated, yRotated;
				// p is relative right now
				if (this.angleRotationDegrees == 0.0) {
					// must be made absolute ..
//					System.out.print("p was .. " + p);
					p.x += isomTopLeftCorner.getX();
					p.y += isomTopLeftCorner.getY();
//					System.out.println("and now p is: " + p);
				} else {
					// ..and rotated
					// 1) make it from relative to top-left to relative to center
					p.x -= hw;
					p.y -= hh;
					// 2) apply the rotation
					xRotated = (int) (p.x * cosCache + p.y * sinCache);
					yRotated = (int) ((p.y * cosCache) - (p.x * sinCache));
					p.x = xRotated;
					p.y = yRotated;
					// 3) make it back relative to top-left corner and 4) absolute
//					p.x += isomLocationCentre.x - hw;
//					p.y += isomLocationCentre.y - hh;
					p.x += hw + isomTopLeftCorner.getX();
					p.y += hh + isomTopLeftCorner.getY();
					// 5) DONE
				}
				action.accept(n, p);
			});
		}

		//

		protected class IsomAndTopLefCornerAbs implements Entry<InSpaceObjectsManager<Distance>, PointInt> {
			protected InSpaceObjectsManager<Distance> key;
			protected PointInt value;

			protected IsomAndTopLefCornerAbs(InSpaceObjectsManager<Distance> key, PointInt value) {
				super();
				this.key = key;
				this.value = value;
			}

			@Override
			public InSpaceObjectsManager<Distance> getKey() { return key; }

			@Override
			public PointInt getValue() { return value; }

			@Override
			public PointInt setValue(PointInt value) {
				PointInt oldValue;
				oldValue = this.value;
				this.value = value;
				return oldValue;
			}
		}

	}

	//

	//

	//

	//

	//

	@Override
	public ObjLocatedCollectorIsom<Distance> newObjLocatedCollector(Predicate<ObjectLocated> objectFilter) { // TODO
																												// Auto-generated
																												// method
																												// stub
		return null;
	}

	@Override
	public Iterator<ObjectLocated> iterator() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter,
			List<Point> path) {
		// TODO Auto-generated method stub
		return null;
	}
}