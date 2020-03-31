package geometry.implementations.shapeRunners;

import java.awt.Point;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.AbstractShapeRunnerImpl;
import geometry.implementations.shapes.ShapeCircle;
import geometry.pointTools.PointConsumer;

public class ShapeRunnerCircleFilled extends AbstractShapeRunnerImpl {
	private static final long serialVersionUID = 72014502659888L;
	private static ShapeRunnerCircleFilled SINGLETON = null;

	public static ShapeRunnerCircleFilled getInstance() {
		if (SINGLETON == null)
			SINGLETON = new ShapeRunnerCircleFilled();
		return SINGLETON;
	}

	@Override
	public ShapeRunnersImplemented getShapeRunnersImplemented() {
		return ShapeRunnersImplemented.Disk;
	}

	@Override
	public boolean runShape(AbstractShape2D shape, PointConsumer action) {
		return super.runShape(shape, action);
	}

	@Override
	protected boolean runShapeImpl(AbstractShape2D shape, PointConsumer action) {
		return PointConsumer.FORCE_EARLY_STOPPING ? runShapeImpl_WithEarlyStop(shape, action)
				: runShapeImpl_NoEarlyStop(shape, action);
	}

	protected boolean runShapeImpl_NoEarlyStop(AbstractShape2D shape, PointConsumer action) {
		int xcentre, ycentre, r, r2, dx, dy, diameter, xc, xc_1, yc, yc_1;
//		double r2;
		ShapeCircle sc;
		Point point;
		if (shape == null || action == null || shape.getShapeImplementing() != this.getShapeRunnersImplemented())
			return false;
		sc = (ShapeCircle) shape;
		diameter = sc.getDiameter();
		r = diameter >> 1;
		xc = xcentre = sc.getXCenter();
		ycentre = sc.getYCenter();
		point = new Point(xcentre, ycentre);
		if (r < 1)
			return false;
		switch (diameter) {
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
			point.x = --xc;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3);
			point.y++;
			point.x = xc;
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
			xc = point.x;
			point.y--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 4);
			point.y--;
			point.x = xc;
			ShapeRunnerLine.runHorizontalSpan(action, point, 4);
			return true;
		}
		case 5: {
			point.y -= 2;
			point.x--;
			xc = point.x;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3);
			point.y += 4;
			point.x = xc;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3);
			point.x = --xc;
//			point.x--;
			point.y--;
			point.x = xc;
			ShapeRunnerLine.runHorizontalSpan(action, point, 5);
			point.y--;
			point.x = xc;
			ShapeRunnerLine.runHorizontalSpan(action, point, 5);
			point.y--;
			point.x = xc;
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
			xc = point.x - 1;
			ShapeRunnerLine.runHorizontalSpan(action, point, 4);
			point.x = xc;
			point.y++;
			ShapeRunnerLine.runHorizontalSpan(action, point, 6);
			point.x = xc; /// -6;
			point.y++;
			ShapeRunnerLine.runHorizontalSpan(action, point, 6);
			point.y++;
//			point.x++;
			point.x = xc + 1;
			ShapeRunnerLine.runHorizontalSpan(action, point, 4);
			point.y++;
//			point.x++;
			point.x = xc + 2;
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
			xc = xcentre;
			yc = ycentre;
			xc_1 = xcentre + 1;
			yc_1 = ycentre + 1;
			r--;
		} else {
			xc = xc_1 = xcentre;
			yc_1 = yc = ycentre;
		}
		r2 = r * r;
//		r2 = diameter / 2.0;
//		r2 *= r2;
		dy = 0;
		dx = r;
		while (dy <= dx) {
			// recycle diameter as the length of the span
			point.x = xc - dx;
			point.y = yc_1 + dy;
			diameter = ((xc_1 + dx) + 1) - point.x;
			ShapeRunnerLine.runHorizontalSpan(action, point, diameter);
			point.y = yc - dy;
			point.x = xc - dx;
			ShapeRunnerLine.runHorizontalSpan(action, point, diameter);
//--
			point.x = xc - dy;
			point.y = yc_1 + dx;
			diameter = ((xc_1 + dy) + 1) - point.x;
			ShapeRunnerLine.runHorizontalSpan(action, point, diameter);
			point.y = yc - dx;
			point.x = xc - dy;
			ShapeRunnerLine.runHorizontalSpan(action, point, diameter);
			// --
			dy++;
			dx = (int) Math.round(Math.sqrt(r2 - dy * dy));
		}
		return true;
	}

	protected boolean runShapeImpl_WithEarlyStop(AbstractShape2D shape, PointConsumer action) {
		int xcentre, ycentre, r, r2, dx, dy, diameter, xc, xc_1, yc, yc_1;
//		double r2;
		ShapeCircle sc;
		Point point;
		if (shape == null || action == null || shape.getShapeImplementing() != this.getShapeRunnersImplemented())
			return false;
		sc = (ShapeCircle) shape;
		diameter = sc.getDiameter();
		r = diameter >> 1;
		xcentre = sc.getXCenter();
		ycentre = sc.getYCenter();
		point = new Point(xc = xcentre, ycentre);

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
			if (!action.canContinue())
				return true;
			point.x = --xc;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3);
			point.y++;
			if (!action.canContinue())
				return true;
			point.x = xc;
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
			xc = point.x;
			point.y--;
			if (!action.canContinue())
				return true;
			ShapeRunnerLine.runHorizontalSpan(action, point, 4);
			point.y--;
			if (!action.canContinue())
				return true;
			point.x = xc;
			ShapeRunnerLine.runHorizontalSpan(action, point, 4);
			return true;
		}
		case 5: {
			point.y -= 2;
			point.x--;
			xc = point.x;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3);
			point.y += 5;
			if (!action.canContinue())
				return true;
			point.x = xc;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3);
			point.x = --xc;
//			point.x--;
			point.y--;
			if (!action.canContinue())
				return true;
			point.x = xc;
			ShapeRunnerLine.runHorizontalSpan(action, point, 5);
			point.y--;
			point.x = xc;
			ShapeRunnerLine.runHorizontalSpan(action, point, 5);
			point.y--;
			if (!action.canContinue())
				return true;
			point.x = xc;
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
			if (!action.canContinue())
				return true;
			ShapeRunnerLine.runHorizontalSpan(action, point, 4);
			point.x = xc - 1;
			point.y++;
			if (!action.canContinue())
				return true;
			ShapeRunnerLine.runHorizontalSpan(action, point, 6);
			point.x = xc; /// -6;
			point.y++;
			if (!action.canContinue())
				return true;
			ShapeRunnerLine.runHorizontalSpan(action, point, 6);
			point.y++;
//			point.x++;
			point.x = xc + 1;
			if (!action.canContinue())
				return true;
			ShapeRunnerLine.runHorizontalSpan(action, point, 4);
			point.y++;
//			point.x++;
			point.x = xc + 1;
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
			xc = xcentre;
			yc = ycentre;
			xc_1 = xcentre + 1;
			yc_1 = ycentre + 1;
			r--;
		} else {
			xc = xc_1 = xcentre;
			yc_1 = yc = ycentre;
		}
		r2 = r * r;
//		r2 = diameter / 2.0;
//		r2 *= r2;
		dy = 0;
		dx = r;
		while (dy <= dx) {
			// recycle diameter as the length of the span
			point.x = xc - dx;
			point.y = yc_1 + dy;
			diameter = ((xc_1 + dx) + 1) - point.x;
			if (!action.canContinue())
				return true;
			ShapeRunnerLine.runHorizontalSpan(action, point, diameter);
//			point.x = xc;
			point.x = xc - dx;
			point.y = yc - dy;
			if (!action.canContinue())
				return true;
			ShapeRunnerLine.runHorizontalSpan(action, point, diameter);
			point.x = xc - dy;
			point.y = yc_1 + dx;
			diameter = ((xc_1 + dy) + 1) - point.x;
			if (!action.canContinue())
				return true;
			ShapeRunnerLine.runHorizontalSpan(action, point, diameter);
			point.y = yc - dx;
			point.x = xc - dy;
			if (!action.canContinue())
				return true;
			ShapeRunnerLine.runHorizontalSpan(action, point, diameter);
			// --
			dy++;
			dx = (int) Math.round(Math.sqrt(r2 - dy * dy));
		}
		return true;
	}
}