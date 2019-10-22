package geometry.implementations;

import geometry.AbstractShape2D;
import geometry.ProviderAbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.shapes.ShapeCircle;
import geometry.implementations.shapes.ShapeLine;
import geometry.implementations.shapes.ShapePoint;
import geometry.implementations.shapes.ShapePolygon;
import geometry.implementations.shapes.ShapePolygonRegular;
import geometry.implementations.shapes.ShapeRectangle;

public class ProviderAbstractShape2DImpl implements ProviderAbstractShape2D {
	private static ProviderAbstractShape2DImpl SINGLETON;

	public static ProviderAbstractShape2DImpl getInstance() {
		if (SINGLETON == null)
			SINGLETON = new ProviderAbstractShape2DImpl();
		return SINGLETON;
	}

	private ProviderAbstractShape2DImpl() {
	}

	@Override
	public AbstractShape2D newEmptyShape(ShapeRunnersImplemented sri) {
		switch (sri) {
		case Arrow:
			break;
		case ArrowBorderBodySameLength:
			break;
		case Circumference:
			return new ShapeCircle(false);
		case Cone:
			break;
		case Disk:
			return new ShapeCircle(true);
		case EllipseNoRotation:
			break;
		case EllipseNoRotationBorder:
			break;
		case Line:
			return new ShapeLine();
		case Point:
			return new ShapePoint();
		case Polygon:
			return new ShapePolygon(true);
		case PolygonRegularBorder:
			return (new ShapePolygonRegular()).setFilled(false).setCornersAmounts(5);
		case PolygonBorder:
			return new ShapePolygon(false);
		case Rectangle:
			return new ShapeRectangle(true);
		case RectangleBorder:
			return new ShapeRectangle(false);
		case Triangle:
			break;
		case TriangleBorder:
			break;
		default:
			break;

		}
		return null;
	}

}
