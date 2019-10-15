package geometry.implementations.intersectors;

import java.awt.geom.Point2D;
import java.util.List;

import geometry.AbstractShape2D;
import geometry.ShapesIntersectionDetector;

public class LineToCircleIntersector implements ShapesIntersectionDetector {
	private static final long serialVersionUID = 1111L;

	@Override
	public boolean areIntersecting(AbstractShape2D s1, AbstractShape2D s2) {

	}

	@Override
	public List<Point2D> computeIntersectionPoints(AbstractShape2D s1, AbstractShape2D s2) {
		return null;
//		MathUtilities.getCircleToCircleIntersection(centre0, radius0, centre1, radius1);
	}
}