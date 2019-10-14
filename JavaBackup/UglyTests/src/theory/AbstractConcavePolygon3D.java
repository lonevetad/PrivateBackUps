package theory;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;

public abstract class AbstractConcavePolygon3D /* PointIntersectorDetector */ {

	public static final Comparator<Point3D> COMPARATOR_POINT_3D = (p1, p2) -> {
		int c;
		c = Integer.compare(p1.z, p2.z);
		if (c != 0)
			return c;
		c = Integer.compare(p1.y, p2.y);
		if (c != 0)
			return c;
		return Integer.compare(p1.x, p2.x);
	};

	public static class Point3D {
		int x, y, z;

		public Point3D(int x, int y, int z) {
			super();
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	public static interface Plain3D {
		int getX(int y, int z);

		int getY(int x, int z);

		int getZ(int x, int Y);
	}

	public static abstract class Face3D {
		Plain3D plain;
		SortedSet<Point3D> vertexes;

		public abstract Point3D getBaricent();

		public abstract boolean isPerpendicular(Point3D p);

		/**
		 * Rotate the plain and the point using the x-lowest point as "point of
		 * rotation" and having the perpendicular (connecting the Plain and the given
		 * point) parallel to y axis (and y value greater than zero). <br>
		 * Return an array object holdint:
		 * <ul>
		 * <li>{@link Point3D}: the original point</li>
		 * <li>{@link Face3D} a new face instance having all of its points z = 0.</li>
		 * <li>{@link Point3D}: the baricent point of this face's polygon.</li>
		 * </ul>
		 */
		public abstract Object[] rotateMakingPointPerpendicularProjectionParallelToY(AbstractConcavePolygon3D poly,
				Point3D p);

		public Object[] rotateMakingPointPerpendicularProjectionParallelToY_try1(AbstractConcavePolygon3D poly,
				Point3D p) {
			Point3D rotatingPoint;// perno in cui avverra la rotazione
			rotatingPoint = vertexes.last();
			///////////////// boh
			return null;
		}
	}

	//

	public AbstractConcavePolygon3D(Collection<Point3D> v) {
	}

	SortedSet<Point3D> vertexes;
	SortedSet<Face3D> faces;

	//

	public SortedSet<Point3D> getVertexes() {
		return vertexes;
	}

	public SortedSet<Face3D> getFaces() {
		return faces;
	}

	//

	public void setVertexes(SortedSet<Point3D> vertexes) {
		this.vertexes = vertexes;
		// set faces from vertexes
	}

	public void setFaces(SortedSet<Face3D> faces) {
		this.faces = faces;
	}

	//

	public abstract Point3D getBaricenter();

	public boolean isInside(Point3D p) {

//		faces.forEach(f->
		for (Face3D f : faces)
			if (isInside(f, p))
				return true;
//		);

		return false;
	}

	public boolean isInside(Face3D f, Point3D p) {
//		int compareP_FaceBaricent, compareP_PolyBaricent;
		Object[] roteated;
		Point3D faceBaricent, polyBaricent;
		if (f.isPerpendicular(p)) {
			roteated = f.rotateMakingPointPerpendicularProjectionParallelToY(this, p);
			if (roteated != null) {
				p = (Point3D) roteated[0];
				f = (Face3D) roteated[1];
//				polyBaricent = getBaricenter();
				polyBaricent = (Point3D) roteated[0];
				faceBaricent = f.getBaricent();

				//
				/**
				 * compareP_FaceBaricent = COMPARATOR_POINT_3D.compare(p, faceBaricent);<br>
				 * compareP_PolyBaricent = COMPARATOR_POINT_3D.compare(p, polyBaricent);<br>
				 * return (compareP_FaceBaricent * compareP_PolyBaricent) >= 0;<br>
				 */
				return (faceBaricent.z >= p.z && p.z >= polyBaricent.z)
						|| (faceBaricent.z <= p.z && p.z <= polyBaricent.z);
			}
		}
		return false;
	}

	//

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
