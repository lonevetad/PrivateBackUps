package geometry.implementations.shapeRunners;

import java.awt.Point;
import java.awt.Polygon;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.AbstractShapeRunnerImpl;
import geometry.pointTools.PointConsumer;

public class ShapeRunnerPolygonBorder extends AbstractShapeRunnerImpl {
	private static final long serialVersionUID = -6501855552021048L;
	public static final ShapeRunnerPolygonBorder SINGLETON = new ShapeRunnerPolygonBorder();

	private ShapeRunnerPolygonBorder() {
	}

	@Override
	public ShapeRunnersImplemented getShapeRunnersImplemented() {
		return ShapeRunnersImplemented.PolygonBorder;
	}

	@Override
	protected boolean runShapeImpl(AbstractShape2D shape, PointConsumer action) {
		Polygon polygon;
		if (shape == null || action == null || shape.getShapeImplementing() != this.getShapeRunnersImplemented())
			return false;
		polygon = shape.toPolygon();
		return runShapePolygon(polygon, action);
	}

	public static boolean runShapePolygon(Polygon polygon, PointConsumer action) {
		int i, len;
		int[] xx, yy;
		Point p, lastp;
		len = polygon.npoints;
		xx = polygon.xpoints;
		yy = polygon.ypoints;
		lastp = new Point(xx[i = len - 1], yy[i]);
		p = new Point();
		i = -1;
		while (++i < len) {
			p.x = xx[i];
			p.y = yy[i];
			ShapeRunnerLine.runSpan(action, lastp, p);
			lastp.x = p.x;
			lastp.y = p.y;
		}
		return true;
	}
}