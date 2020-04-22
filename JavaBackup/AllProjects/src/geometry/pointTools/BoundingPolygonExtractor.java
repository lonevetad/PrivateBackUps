package geometry.pointTools;

import java.awt.Polygon;

import geometry.AbstractShape2D;
import geometry.AbstractObjectsInSpaceManager;

public interface BoundingPolygonExtractor {
	public Polygon getBoundingPolygon(AbstractObjectsInSpaceManager oisp, AbstractShape2D whereToLookFor);
}