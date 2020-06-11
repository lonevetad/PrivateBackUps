package dataStructures.isom.matrixBased;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.SetMapped;
import dataStructures.isom.InSpaceObjectsManagerImpl;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.ObjLocatedCollectorIsom;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ObjectShaped;
import geometry.ProviderShapeRunner;
import geometry.ProviderShapesIntersectionDetector;
import geometry.implementations.shapes.ShapeRectangle;
import geometry.pointTools.PointConsumer;
import tools.Comparators;
import tools.LoggerMessages;
import tools.NumberManager;

/** Rectangular matrix-based implementation */
public abstract class MatrixInSpaceObjectsManager<Distance extends Number> extends InSpaceObjectsManagerImpl<Distance> {
	private static final long serialVersionUID = 6663104159265L;
	protected static final Double justOne = 1.0, sqrtTwo = /* Math.max(justOne + 0.5, */Math.sqrt(2);
//	public static enum OperationOnShape {Add, Remove, Replace, Collect;}

	public static enum CoordinatesDeltaForAdjacentNodes {
		// TOP(0, -1), RIGHT(+1, 0), BOTTOM(0, 1), LEFT(-1, 0);
		NORD(0, -1, justOne), SUD(0, 1, justOne), OVEST(-1, 0, justOne), EST(1, 0, justOne)//
		, NORD_EST(1, -1, sqrtTwo), SUD_EST(1, 1, sqrtTwo), SUD_OVEST(-1, 1, sqrtTwo), NORD_OVEST(-1, -1, sqrtTwo)//
		;

		final int dx, dy;
		final Double weight;

		CoordinatesDeltaForAdjacentNodes(int dxx, int dyy, double w) {
			dx = dxx;
			dy = dyy;
			this.weight = w;
		}
	}

	public MatrixInSpaceObjectsManager(boolean isLazyNodeInstancing, int width, int height,
			NumberManager<Distance> weightManager) {
		this.isLazyNodeInstancing = isLazyNodeInstancing;
		this.height = height;
		this.width = width;
		this.weightManager = weightManager;
		onCreate();
		// TODO create intersectors and runners
//		ProviderShapesIntersectionDetector shapeIntersectionDetectorProvider;
//		ProviderShapeRunner shapeRunnerProvider = new ProviderShapeRunnerImpl() ???? e quello che fa cache dei cerchi?
	}

//	protected Comparator IDOwidComparator;
	protected final boolean isLazyNodeInstancing;
	protected int width, height;
	protected NodeIsom[][] matrix;
	protected final NumberManager<Distance> weightManager;
	protected AbstractShape2D shape;
	protected ProviderShapesIntersectionDetector shapeIntersectionDetectorProvider;
	protected ProviderShapeRunner shapeRunnerProvider;
	protected Map<Integer, ObjectLocated> objectsAdded;
	protected Set<ObjectLocated> objectsAddedSet;

	//

	protected void onCreate() {
		this.shape = new ShapeRectangle(0, width >> 1, height >> 1, true, width, height);
		setObjectsAdded(MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR));
		reinstanceMatrix();
	}

	//

	@Override
	public AbstractShape2D getBoundingShape() {
		return shape;
	}

	@Override
	public ProviderShapesIntersectionDetector getProviderShapesIntersectionDetector() {
		return shapeIntersectionDetectorProvider;
	}

	@Override
	public ProviderShapeRunner getProviderShapeRunner() {
		return shapeRunnerProvider;
	}

	public boolean isLazyNodeInstancing() {
		return isLazyNodeInstancing;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ProviderShapeRunner getShapeRunnerProvider() {
		return shapeRunnerProvider;
	}

	/** Use with caution. */
	public Map<Integer, ObjectLocated> getObjectsAdded() {
		return objectsAdded;
	}

	@Override
	public Set<ObjectLocated> getAllObjectLocated() {
		return objectsAddedSet;
	}

	public NodeIsom[] getRow(int y) {
		return matrix[y];
	}

	//

	// TODO SETTER

	@Override
	public void setProviderShapesIntersectionDetector(
			ProviderShapesIntersectionDetector providerShapesIntersectionDetector) {
		this.shapeIntersectionDetectorProvider = providerShapesIntersectionDetector;
	}

	public void setShapeRunnerProvider(ProviderShapeRunner shapeRunnerProvider) {
		this.shapeRunnerProvider = shapeRunnerProvider;
	}

	/** Sets the map holding all objects in this space. */
	public void setObjectsAdded(Map<Integer, ObjectLocated> objectsAdded) {
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

	@Override
	public void setLog(LoggerMessages log) {
		this.log = LoggerMessages.loggerOrDefault(log);
	}

	@Override
	public void setProviderShapeRunner(ProviderShapeRunner providerShapeRunner) {
		this.shapeRunnerProvider = providerShapeRunner;
	}

	//

	//

	public abstract NodeIsom newNodeMatrix(int x, int y);

	public void reinstanceMatrix() {
		int r, c, w, h;
		NodeIsom[] row, m[];
		m = this.matrix = new NodeIsom[h = this.height][w = this.width];
		if (isLazyNodeInstancing)
			return;
		r = -1;
		while(++r < h) {
			row = m[r];
			c = -1;
			while(++c < w) {
				row[c] = newNodeMatrix(c, r);
			}
		}
	}

	@Override
	public NodeIsom getNodeAt(Point p) {
		int x, y;
		NodeIsom n;
		n = matrix[y = (int) p.getY()][x = (int) p.getX()];
		if (isLazyNodeInstancing && n == null) {
			n = matrix[y][x] = newNodeMatrix(x, y);
		}
		return n;
	}

	public NodeIsom getNodeAt(int x, int y) {
		NodeIsom n;
		n = matrix[y][x];
		if (isLazyNodeInstancing && n == null) {
			n = matrix[y][x] = newNodeMatrix(x, y);
		}
		return n;
	}

	@Override
	public void runOnWholeMap(PointConsumer action) {
		Point p;
		p = new Point();
		int r, c, w, h;
		NodeIsom[] row, m[];
//		NodeIsom node;
		m = this.matrix;
		h = this.height;
		w = this.width;
		r = -1;
		while(++r < h) {
			row = m[r];
			c = -1;
			p.y = r;
			while(++c < w) {
				if (row[c] != null) {
//				if ((node = row[c]) != null) {
					p.x = c;
					action.accept(p);
				}
			}
		}
	}

	@Override
	public ObjLocatedCollectorIsom newObjLocatedCollector(Predicate<ObjectLocated> objectFilter) {
		return new ObjLocatedCollectorMatrix<>(this, objectFilter);
	}

	// TODO add

	@Override
	public boolean add(ObjectLocated o) {
		AdderObjLocated<Distance> a;
		if (o == null)
			return false;
		a = new AdderObjLocated<>(this, o);
		if (a instanceof ObjectShaped) {
			runOnShape(((ObjectShaped) o).getShape(), a);
		} else {
			a.accept(o.getLocation());
		}
		this.getObjectsAdded().put(o.getID(), o);
		return true;
	}

	@Override
	public ObjectLocated getObjectLocated(Integer ID) {
		return this.objectsAdded.get(ID);
	}

	@Override
	public boolean contains(ObjectLocated o) {
		return o != null && this.getObjectsAdded().containsKey(o.getID());
	}

	@Override
	public boolean remove(ObjectLocated o) {
		RemoverObjLocated<Distance> r;
		if (o == null)
			return false;
		if (this.getObjectsAdded().remove(o.getID()) != null)
			return false;
		r = new RemoverObjLocated<>(this, o);
		if (r instanceof ObjectShaped) {
			runOnShape(((ObjectShaped) o).getShape(), r);
		} else {
			r.accept(o.getLocation());
		}
		return true;
	}

	@Override
	public boolean removeAllObjects() {
		Thread tClearer;
		if (this.getObjectsAdded().isEmpty())
			return false;
		this.getObjectsAdded().clear();
		tClearer = new Thread(() -> {
			runOnWholeMap(p -> {
				NodeIsom n;
				n = getNodeAt(p);
				if (n != null)
					n.removeAllObjects();
			});
		});
		tClearer.start();
		return true;
	}

	@Override
	public void forEachAdjacents(NodeIsom node, BiConsumer<NodeIsom, Distance> adjacentDistanceConsumer) {
		boolean xNotTooOnRight, xStrictlyPositive;
		int x;
		NodeIsom row[];
		x = node.x;
		xStrictlyPositive = x > 0;
		xNotTooOnRight = x < (matrix[0].length - 1);
		if (node.y > 0) {
			row = matrix[node.y - 1];
			if (xStrictlyPositive)
				adjacentDistanceConsumer.accept(row[x - 1], weightManager.fromDouble(sqrtTwo));
			adjacentDistanceConsumer.accept(row[x], weightManager.fromDouble(justOne));
			if (xNotTooOnRight)
				adjacentDistanceConsumer.accept(row[x + 1], weightManager.fromDouble(sqrtTwo));
		}
		if (xStrictlyPositive)
			adjacentDistanceConsumer.accept(matrix[node.y][x - 1], weightManager.fromDouble(justOne));
		if (xNotTooOnRight)
			adjacentDistanceConsumer.accept(matrix[node.y][x + 1], weightManager.fromDouble(justOne));
		if (node.y < (matrix.length - 1)) {
			row = matrix[node.y + 1];
			if (xStrictlyPositive)
				adjacentDistanceConsumer.accept(row[x - 1], weightManager.fromDouble(sqrtTwo));
			adjacentDistanceConsumer.accept(row[x], weightManager.fromDouble(justOne));
			if (xNotTooOnRight)
				adjacentDistanceConsumer.accept(row[x + 1], weightManager.fromDouble(sqrtTwo));
		}

	}

	@Override
	public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter,
			List<Point> path) {
		ObjLocatedCollectorMatrix<Distance> coll;
		coll = new ObjLocatedCollectorMatrix<>(this, objectFilter);
		return this.findInPath(areaToLookInto, coll, path);
	}

	@Override
	public Iterator<ObjectLocated> iterator() {
		return new IteratorNodeIsom();
	}

	//

	// classes

	protected static abstract class ActionOnPointWithObj<D extends Number> extends PointConsumerRowOptimizer<D> {
		private static final long serialVersionUID = 1L;
		ObjectLocated oToManipulate;

		public ActionOnPointWithObj(MatrixInSpaceObjectsManager<D> misom, ObjectLocated oToManipulate) {
			super(misom);
			this.oToManipulate = oToManipulate;
		}
	}

	protected static class AdderObjLocated<D extends Number> extends ActionOnPointWithObj<D> {
		public AdderObjLocated(MatrixInSpaceObjectsManager<D> misom, ObjectLocated oToManipulate) {
			super(misom, oToManipulate);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void acceptImpl(Point p) {
			this.misom.getNodeAt(p).addObject(oToManipulate);
		}
	}

	protected static class RemoverObjLocated<D extends Number> extends ActionOnPointWithObj<D> {
		public RemoverObjLocated(MatrixInSpaceObjectsManager<D> misom, ObjectLocated oToManipulate) {
			super(misom, oToManipulate);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void acceptImpl(Point p) {
			this.misom.getNodeAt(p).removeObject(oToManipulate);
		}
	}

	protected class IteratorNodeIsom implements Iterator<ObjectLocated> {
		int r, c, w, h;
		Iterator<ObjectLocated> nodeIteratingIterator;
		NodeIsom nodeIterating;

		protected IteratorNodeIsom() {
			r = c = 0;
			h = height;
			w = width;
			nodeIteratingIterator = null;
			nodeIterating = null;
		}

		@Override
		public boolean hasNext() {
			return r < h;
		}

		@Override
		public ObjectLocated next() {
			NodeIsom n;
			n = null;
			// find the first available
			if (nodeIteratingIterator == null || (!nodeIteratingIterator.hasNext())) {
				while(hasNext() && ((n = getNodeAt(c, r)) == null || n.countObjectAdded() == 0)) {
					toNext();
				}
			}
			if (n == null || (!hasNext()))
				return null; // nothing more to check
			if (nodeIterating != n) {// is the first NodeIsom one discovered or a new one, not jet iterated?
				nodeIteratingIterator = n.iterator();
				nodeIterating = n;
			}
			return nodeIteratingIterator.next();
		}

		protected void toNext() {
			if (r < h && ++c >= w) {
				c = 0;
				r++;
			}
		}
	}
}