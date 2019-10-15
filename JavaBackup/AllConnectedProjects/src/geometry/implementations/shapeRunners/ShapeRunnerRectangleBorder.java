package geometry.implementations.shapeRunners;

import java.awt.Point;
import java.awt.Polygon;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.AbstractShapeRunnerImpl;
import geometry.implementations.shapes.ShapeRectangle;
import geometry.pointTools.PointConsumer;

public class ShapeRunnerRectangleBorder extends AbstractShapeRunnerImpl {
	private static final long serialVersionUID = 3565885292245201000L;
	public static ShapeRunnerRectangleBorder SINGLETON;

	public static ShapeRunnerRectangleBorder getInstance() {
		if (SINGLETON == null)
			SINGLETON = new ShapeRunnerRectangleBorder();
		return SINGLETON;
	}

	private ShapeRunnerRectangleBorder() {
	}

	@Override
	public ShapeRunnersImplemented getShapeRunnersImplemented() {
		return ShapeRunnersImplemented.RectangleBorder;
	}

	@Override
	protected boolean runShapeImpl(AbstractShape2D shape, PointConsumer action) {
		int[] xx, yy;
		double angDeg;
		ShapeRectangle sr;
		Point corner;
		Polygon poly;
		if (shape == null || action == null || shape.getShapeImplementing() != this.getShapeRunnersImplemented())
			return false;
		sr = (ShapeRectangle) shape;
		angDeg = sr.getAngleRotation();
		poly = sr.toPolygon();
		xx = poly.xpoints;
		yy = poly.ypoints;
		corner = new Point();
		if (angDeg == 0.0 || angDeg == 180.0) {
			corner.x = xx[0];
			corner.y = yy[0];
			ShapeRunnerLine.runHorizontalSpan(action, corner, sr.getWidth());
			corner.x = xx[1]; // the same as 0
//			corner.y = yy[1]; // same as 0
			ShapeRunnerLine.runVerticalSpan(action, corner, sr.getHeight());
			// the 3 is needed instead of 2 because of it runs from left to right
			corner.x = xx[3]; // same as 0
			corner.y = yy[3];
			ShapeRunnerLine.runHorizontalSpan(action, corner, sr.getWidth());
			// same as 3: runs from lower y to higher y
			corner.x = xx[0];
			corner.y = yy[0];
//			corner.y = yy[3]; //same as 2
			ShapeRunnerLine.runVerticalSpan(action, corner, sr.getHeight());
		} else if (angDeg == 90.0 || angDeg == 270.0) {
			// specular to upper one
			corner.x = xx[0];
			corner.y = yy[0];
			ShapeRunnerLine.runHorizontalSpan(action, corner, sr.getHeight());
			corner.x = xx[1];
			ShapeRunnerLine.runVerticalSpan(action, corner, sr.getWidth());
			corner.x = xx[3];
			corner.y = yy[3];
			ShapeRunnerLine.runHorizontalSpan(action, corner, sr.getHeight());
			corner.x = xx[0];
			corner.y = yy[0];
			ShapeRunnerLine.runVerticalSpan(action, corner, sr.getWidth());
		} else
			ShapeRunnerPolygonBorder.runShapePolygon(poly, action);
		return true;
	}
}