package dataStructures.isom;

import java.awt.Point;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dataStructures.MapTreeAVL;
import dataStructures.SetMapped;
import dataStructures.isom.MultiISOMRetangularMap.MatrixISOMLocatedInSpace;
import dataStructures.isom.MultiISOMRetangularMap.NodeQuadtreeMultiISOMRectangular;
import dataStructures.isom.pathFinders.Heuristic8GridMovement;
import dataStructures.isom.pathFinders.PathFinderIsomAStar;
import geometry.ObjectLocated;
import geometry.ProviderShapeRunner;
import geometry.ProviderShapesIntersectionDetector;
import tools.Comparators;
import tools.NumberManager;
import tools.Stringable;
import tools.UniqueIDProvider;

/**
 * Takes inspiration from {@link MultiISOMRetangularCaching}, but areas could be
 * polygonal, no matter what.
 */
public class MultiISOMPolygonalSubareas<Distance extends Number> extends InSpaceObjectsManagerImpl<Distance>
		implements InSpaceObjectsManager<Distance> {
	private static final long serialVersionUID = 1L;
	protected static final UniqueIDProvider ID_PROV_MULTI_ISOM_POLY = UniqueIDProvider.newBasicIDProvider();

	protected MultiISOMPolygonalSubareas(int maximumSubmapsEachSection) {
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
		misomsHeld = new SetMapped<>(mapsLocatedInSpace.toSetValue(w -> w.ID), w -> w.isomHeld);
	}

	protected final int maximumSubmapsEachSection;
	protected int maxDepth, xLeftTop, yLeftTop, xRightBottom, yRightBottom, width, height;
	protected final Integer ID;
	protected NodeQuadtreeISOM root;
	protected final MapTreeAVL<Integer, ISOMWrapperLocated> mapsLocatedInSpace;
	protected final Set<InSpaceObjectsManager<Distance>> misomsHeld;
	protected final List<ISOMWrapperLocated> mapsAsList;
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

	public ISOMWrapperLocated getCachedMisom() { return cachedIsom == null ? null : cachedIsom /* .misom */; }

	public NodeQuadtreeISOM getRoot() { return root; }

	public int getMaxDepth() { return maxDepth; }

	///

	//

	protected PathFinderIsom<Distance> newPathFinder() {
		return new PathFinderIsomAStar<>(this, new Heuristic8GridMovement<>(getWeightManager()));
	}

	// override because of the NumberManager-dependency of heuristic for AStar
	@Override
	public void setWeightManager(NumberManager<Distance> numberManager) {
		this.weightManager = numberManager;
		this.setPathFinder(newPathFinder());
	}

	//

	//

	/**
	 * Create a new {@link NodeQuadtreeMultiISOMRectangular} based on a given map
	 * (to be held) and a father node.
	 */
	protected NodeQuadtreeISOM newNodeWith(MatrixISOMLocatedInSpace<Distance> map, NodeQuadtreeISOM fatherNode) {
		NodeQuadtreeISOM newNode;
		newNode = new NodeQuadtreeISOM(fatherNode);
		if (this.getMaxDepth() < newNode.depth) { this.maxDepth = newNode.depth; }
		newNode.submaps = new ArrayList<>(4);
		newNode.submaps.add(map);
		return newNode;
	}

	/**
	 * Returns a {@link MatrixISOMLocatedInSpace} that can contains the given point
	 * (those coordinates are absolute, not relative in any way, nor this class's
	 * space represented).
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
			if (x >= n.xMiddle) { // east
				n = (y >= n.yMiddle) ? n.sse : n.sne;
			} else {// west
				n = (y >= n.yMiddle) ? n.ssw : n.snw;
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

	//

	// TODO CLASS

	/** It's still rectangular */
	protected class NodeQuadtreeISOM implements Stringable {
		protected int /* xLeftTop, yLeftTop, width, height, */ xMiddle, yMiddle, depth;
		List<ISOMWrapperLocated> submaps;
		NodeQuadtreeISOM father, snw, sne, ssw, sse;
		RectangularShape shapeRect;

		protected NodeQuadtreeISOM(MultiISOMPolygonalSubareas<Distance>.NodeQuadtreeISOM father) {
			super();
			this.father = father;
			submaps = null;
			depth = (father == null) ? 1 : (father.depth + 1);
		}

		public List<ISOMWrapperLocated> getSubmaps() { return submaps; }

		public boolean isLeaf() { return submaps != null; }

	}

	protected class ISOMWrapperLocated implements ObjectLocated {
		private static final long serialVersionUID = 1L;
//		protected int xLeftTop, yLeftTop, width, height
		public final Integer ID;
		protected final InSpaceObjectsManager<Distance> isomHeld;

		protected ISOMWrapperLocated(InSpaceObjectsManager<Distance> isom) {
			super();
			this.isomHeld = isom;
			this.ID = uidProvider.getNewID();
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
		public Integer getID() { return ID; }

		@Override
		public Point getLocation() { return isomHeld.getLocation(); }

		@Override
		public void setLocation(Point p) { this.setLocation(p.x, p.y); }

		@Override
		public void setLocation(int x, int y) {
//			this.xLeftTop = x;
//			this.yLeftTop = y;
			isomHeld.setLocation(x, y);
		}
	}
}