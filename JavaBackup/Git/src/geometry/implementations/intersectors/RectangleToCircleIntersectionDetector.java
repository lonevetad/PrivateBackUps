package geometry.implementations.intersectors;

import geometry.AbstractShape2D;
import geometry.ShapesIntersectionDetector;

public class RectangleToCircleIntersectionDetector implements ShapesIntersectionDetector {

	@Override
	public boolean areIntersecting(AbstractShape2D s1, AbstractShape2D s2) {
		return false;
	}

}