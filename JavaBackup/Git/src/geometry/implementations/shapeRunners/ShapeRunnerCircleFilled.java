package geometry.implementations.shapeRunners;

import java.awt.Point;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.AbstractShapeRunnerImpl;
import geometry.implementations.shapes.ShapeCircle;
import geometry.pointTools.PointConsumer;

public class ShapeRunnerCircleFilled extends AbstractShapeRunnerImpl {
	private static final long serialVersionUID = 72014502659888L;
	public static final ShapeRunnerCircleFilled SINGLETON = new ShapeRunnerCircleFilled();

	@Override
	public ShapeRunnersImplemented getShapeRunnersImplemented() {
		return ShapeRunnersImplemented.Disk;
	}

	@Override
	protected boolean runShapeImpl(AbstractShape2D shape, PointConsumer action) {
		return PointConsumer.FORCE_EARLY_STOPPING ? runShapeImpl_NoEarlyStop(shape, action)
				: runShapeImpl_WithEarlyStop(shape, action);
	}

	protected boolean runShapeImpl_NoEarlyStop(AbstractShape2D shape, PointConsumer action) {
		int xcentre, ycentre, r, dx, dy, diameter, r2, xc1, xc2, xc3, xc4, yc1, /* yc2, yc3, */ yc4/* , dy2 */;
		ShapeCircle sc;
		Point point;
		if (shape == null || action == null || shape.getShapeImplementing() != this.getShapeRunnersImplemented())
			return false;
		sc = (ShapeCircle) shape;
		diameter = sc.getDiameter();
		r = diameter >> 1;
		xcentre = sc.getXCenter();
		ycentre = sc.getYCenter();
		point = new Point(xcentre, ycentre);
		if (r >= 0)// TODO
			throw new UnsupportedOperationException("DEBUG NEEDED");
		if (r < 1)
			return false;
		switch (r) {
		case 1: {
			action.accept(point);
			return true;
		}
		case 2: {
			action.accept(point);
			point.x++;
			action.accept(point);
			point.y++;
			action.accept(point);
			point.x--;
			action.accept(point);
			return true;
		}
		case 3: {
			point.y--;
			point.x--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3);
			point.y++;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3);
			point.y++;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3);
			return true;
		}
		case 4: {
			point.y--;
			action.accept(point);
			point.x++;
			action.accept(point);
			point.y += 3;
			action.accept(point);
			point.x--;
			action.accept(point);
			point.x--;
			point.y--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 4);
			point.y--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 4);
			return true;
		}
		case 5: {
			point.y -= 2;
			point.x--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3);
			point.y += 5;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3);
			point.x--;
			point.y--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 5);
			point.y--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 5);
			point.y--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 5);
			return true;
		}
		case 6: {
			point.y -= 2;
			action.accept(point);
			point.x++;
			action.accept(point);//
			point.y++;
			point.x -= 2;
			ShapeRunnerLine.runHorizontalSpan(action, point, 4);
			point.x--;
			point.y++;
			ShapeRunnerLine.runHorizontalSpan(action, point, 6);
			point.y++;
			ShapeRunnerLine.runHorizontalSpan(action, point, 6);
			point.y++;
			point.x++;
			ShapeRunnerLine.runHorizontalSpan(action, point, 4);
			point.y++;
			point.x++;
			action.accept(point);
			point.x++;
			action.accept(point);
			return true;
		}
		default: {
		}
		}
		/*
		 * perform eight circle's slice, thanks to its regularity. Start from lowest y
		 * (the south) and go up.
		 */
		if ((diameter & 0x1) == 0) {
			xc1 = xc4 = xcentre;
			xc2 = xc3 = xcentre + 1;
			yc1 = /* yc2 = */ ycentre + 1;
			/* yc3 = */ yc4 = ycentre;
			r--;
		} else {
			xc1 = xc2 = xc3 = xc4 = xcentre;
			yc1 = /* yc2 = yc3 = */ yc4 = ycentre;
		}
		r2 = r * r;
		dy = 0;
		dx = r;
		while (dy <= dx) {
			point.x = xc1 - dx;
			point.y = yc1 + dy;
			action.accept(point);
			ShapeRunnerLine.runHorizontalSpan(action, point, (point.x - (xc2 + dx)) + 1);
			//
			point.x = xc1 - dx;
			point.y = yc4 - dy;
			ShapeRunnerLine.runHorizontalSpan(action, point, (point.x - (xc2 + dx)) + 1);
//--
			point.x = xc2 + dy;
			point.y = yc1 + dx;
			ShapeRunnerLine.runHorizontalSpan(action, point, (point.x - (xc1 - dy)) + 1);
			// --
			point.x = xc4 - dy;
			point.y = yc4 - dx;
			ShapeRunnerLine.runHorizontalSpan(action, point, (point.x - (xc3 + dy)) + 1);
			dy++;
			dx = (int) Math.round(Math.sqrt(r2 - dy * dy));
		}
		return true;
	}

	protected boolean runShapeImpl_WithEarlyStop(AbstractShape2D shape, PointConsumer action) {
		int xcentre, ycentre, r, dx, dy, diameter, r2, xc1, xc2, xc3, xc4, yc1, /* yc2, yc3, */ yc4/* , dy2 */;
		ShapeCircle sc;
		Point point;
		if (shape == null || action == null || shape.getShapeImplementing() != this.getShapeRunnersImplemented())
			return false;
		sc = (ShapeCircle) shape;
		diameter = sc.getDiameter();
		r = diameter >> 1;
		xcentre = sc.getXCenter();
		ycentre = sc.getYCenter();
		point = new Point(xcentre, ycentre);

		if (r < 1)
			return false;
		switch (r) {
		case 1: {
			if (!action.canContinue())
				return true;
			action.accept(point);
			return true;
		}
		case 2: {
			if (!action.canContinue())
				return true;
			action.accept(point);
			point.x++;
			if (!action.canContinue())
				return true;
			action.accept(point);
			point.y++;
			if (!action.canContinue())
				return true;
			action.accept(point);
			point.x--;
			if (!action.canContinue())
				return true;
			action.accept(point);
			return true;
		}
		case 3: {
			point.y--;
			point.x--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3);
			point.y++;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3);
			point.y++;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3);
			return true;
		}
		case 4: {
			point.y--;
			if (!action.canContinue())
				return true;
			action.accept(point);
			point.x++;
			if (!action.canContinue())
				return true;
			action.accept(point);
			point.y += 3;
			if (!action.canContinue())
				return true;
			action.accept(point);
			point.x--;
			if (!action.canContinue())
				return true;
			action.accept(point);
			point.x--;
			point.y--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 4);
			point.y--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 4);
			return true;
		}
		case 5: {
			point.y -= 2;
			point.x--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3);
			point.y += 5;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3);
			point.x--;
			point.y--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 5);
			point.y--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 5);
			point.y--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 5);
			return true;
		}
		case 6: {
			point.y -= 2;
			if (!action.canContinue())
				return true;
			action.accept(point);
			point.x++;
			if (!action.canContinue())
				return true;
			action.accept(point);//
			point.y++;
			point.x -= 2;
			ShapeRunnerLine.runHorizontalSpan(action, point, 4);
			point.x--;
			point.y++;
			ShapeRunnerLine.runHorizontalSpan(action, point, 6);
			point.y++;
			ShapeRunnerLine.runHorizontalSpan(action, point, 6);
			point.y++;
			point.x++;
			ShapeRunnerLine.runHorizontalSpan(action, point, 4);
			point.y++;
			point.x++;
			if (!action.canContinue())
				return true;
			action.accept(point);
			point.x++;
			if (!action.canContinue())
				return true;
			action.accept(point);
			return true;
		}
		default: {
		}
		}
		/*
		 * perform eight circle's slice, thanks to its regularity. Start from lowest y
		 * (the south) and go up.
		 */
		if ((diameter & 0x1) == 0) {
			xc1 = xc4 = xcentre;
			xc2 = xc3 = xcentre + 1;
			yc1 = /* yc2 = */ ycentre + 1;
			/* yc3 = */ yc4 = ycentre;
			r--;
		} else {
			xc1 = xc2 = xc3 = xc4 = xcentre;
			yc1 = /* yc2 = yc3 = */ yc4 = ycentre;
		}
		r2 = r * r;
		dy = 0;
		dx = r;
		while (dy <= dx) {
			point.x = xc1 - dx;
			point.y = yc1 + dy;
			if (!action.canContinue())
				return true;
			action.accept(point);
			ShapeRunnerLine.runHorizontalSpan(action, point, (point.x - (xc2 + dx)) + 1);
			//
			point.x = xc2 + dx;
			point.y = yc4 - dy;
			ShapeRunnerLine.runHorizontalSpan(action, point, (point.x - (xc1 - dx)) + 1);
//--
			point.x = xc1 - dy;
			point.y = yc1 + dx;
			ShapeRunnerLine.runHorizontalSpan(action, point, (point.x - (xc2 + dy)) + 1);
			// --
			point.x = xc4 - dy;
			point.y = yc4 - dx;
			ShapeRunnerLine.runHorizontalSpan(action, point, (point.x - (xc3 + dy)) + 1);
			// --dy++;
			dx = (int) Math.round(Math.sqrt(r2 - dy * dy));
		}
		return true;
	}
}