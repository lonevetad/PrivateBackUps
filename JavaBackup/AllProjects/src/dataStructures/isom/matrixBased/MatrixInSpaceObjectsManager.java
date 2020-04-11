package dataStructures.isom.matrixBased;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import dataStructures.isom.InSpaceObjectsManager;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.ObjLocatedCollectorIsom;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ProviderShapeRunner;
import geometry.ProviderShapesIntersectionDetector;
import geometry.implementations.shapes.ShapeRectangle;
import geometry.pointTools.PointConsumer;
import tools.LoggerMessages;
import tools.NumberManager;

/** Rectangular matrix-based implementation */
public abstract class MatrixInSpaceObjectsManager<Distance extends Number> extends InSpaceObjectsManager<Distance> {
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
		this.shape = new ShapeRectangle(0, width >> 1, height >> 1, true, width, height);
		reinstanceMatrix();

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

	//

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
	public boolean add(ObjectLocated o) {
		return false;
	}

	@Override
	public boolean contains(ObjectLocated o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(ObjectLocated o) {
		// TODO Auto-generated method stub
		return false;
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
	public ObjLocatedCollectorIsom newObjLocatedCollector(Predicate<ObjectLocated> objectFilter) {
		return new ObjLocatedCollectorMatrix<>(this, objectFilter);
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

	protected abstract class ActionOnPointWithObj implements PointConsumer {
		private static final long serialVersionUID = 1L;
		ObjectLocated oToManipulate;

		public ActionOnPointWithObj(ObjectLocated oToManipulate) {
			super();
			this.oToManipulate = oToManipulate;
		}
	}

	protected class AdderObjLocated extends ActionOnPointWithObj {
		public AdderObjLocated(ObjectLocated oToManipulate) {
			super(oToManipulate);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void accept(Point p) {
			getNodeAt(p).addObject(oToManipulate);
		}
	}

	protected class RemoverObjLocated extends ActionOnPointWithObj {
		public RemoverObjLocated(ObjectLocated oToManipulate) {
			super(oToManipulate);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void accept(Point p) {
			getNodeAt(p).removeObject(oToManipulate);
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