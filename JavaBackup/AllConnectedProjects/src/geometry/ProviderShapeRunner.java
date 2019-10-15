package geometry;

import geometry.implementations.shapeRunners.ShapeRunnerCircleBorder;
import geometry.implementations.shapeRunners.ShapeRunnerCircleFilled;
import geometry.implementations.shapeRunners.ShapeRunnerLine;
import geometry.implementations.shapeRunners.ShapeRunnerPoint;
import geometry.implementations.shapeRunners.ShapeRunnerPolygonBorder;
import geometry.implementations.shapeRunners.ShapeRunnerRectangleBorder;
import geometry.pointTools.PointConsumer;

public class ProviderShapeRunner implements AbstractShapeRunner {
	private static final long serialVersionUID = 342405452L;
	private static final AbstractShapeRunner[] RUNNERS;
	private static ProviderShapeRunner SINGLETON;
	static {
		RUNNERS = new AbstractShapeRunner[ShapeRunnersImplemented.values().length];
		RUNNERS[ShapeRunnersImplemented.Circumference.ordinal()] = ShapeRunnerCircleBorder.getInstance();
		RUNNERS[ShapeRunnersImplemented.Disk.ordinal()] = ShapeRunnerCircleFilled.getInstance();
		RUNNERS[ShapeRunnersImplemented.Line.ordinal()] = ShapeRunnerLine.getInstance();
		RUNNERS[ShapeRunnersImplemented.Point.ordinal()] = ShapeRunnerPoint.getInstance();
		RUNNERS[ShapeRunnersImplemented.PolygonBorder.ordinal()] = ShapeRunnerPolygonBorder.getInstance();
		RUNNERS[ShapeRunnersImplemented.RectangleBorder.ordinal()] = ShapeRunnerRectangleBorder.getInstance();
	}

	public static ProviderShapeRunner getInstance() {
		if (SINGLETON == null)
			SINGLETON = new ProviderShapeRunner();
		return SINGLETON;
	}

	private ProviderShapeRunner() {
	}

	public AbstractShapeRunner getShapeRunner(ShapeRunnersImplemented sri) {
		return RUNNERS[sri.ordinal()];
	}

	@Override
	public ShapeRunnersImplemented getShapeRunnersImplemented() {
		return null;
	}

	@Override
	public boolean runShape(AbstractShape2D shape, PointConsumer action) {
		AbstractShapeRunner r;
		if (shape == null || action == null)
			return false;
		r = this.getShapeRunner(shape.getShapeImplementing());
		return r == null ? false : r.runShape(shape, action);
	}
}