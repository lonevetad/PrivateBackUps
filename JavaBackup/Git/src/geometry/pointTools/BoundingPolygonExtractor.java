package geometry.pointTools;

import java.awt.Polygon;

import geometry.AbstractShape2D;
import geometry.ProviderObjectsInSpace;

public interface BoundingPolygonExtractor {
	public Polygon getBoundingPolygon(ProviderObjectsInSpace oisp, AbstractShape2D whereToLookFor);
}