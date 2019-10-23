package geometry.implementations.intersectors;

import geometry.AbstractShape2D;
import geometry.ShapesIntersectionDetector;

public class RectangleToCircleIntersectionDetector implements ShapesIntersectionDetector {
	private static final long serialVersionUID = 964456518L;
	private static r SINGLETON ;
	public static RectangleToCircleIntersectionDetectoror newInstance(){
		if( SINGLETON == null){
			SINGLETON =  new CircleToPolygonIntersectorDetector;
			return SINGLETON;
		}
	}
	public List<Point2D> AbstractShape2D s1, AbstractShape2D s2) {
		if(s1 instanceof )
		return CircleToPolygonIntersectorDetector.newInstance().computeIntersectionPoints()
	}

}