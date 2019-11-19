package tests.tGeom;

import java.awt.image.BufferedImage;

import geometry.AbstractShape2D;
import geometry.implementations.shapes.ShapeCircle;

public class TestRunnerCircleBorder extends TestGeneric {
	static final int PIXEL_SQUARE_POINT = 20; //

	public TestRunnerCircleBorder() {
	}

	protected class CircleRunnerBorder extends ShapeModel {
		protected CircleRunnerBorder(AbstractShape2D s1) {
			super(new ShapeCircle(false));
		}

		BufferedImage bi;

		@Override
		void init() {
			// TODO Auto-generated method stub

		}
	}

	@Override
	ShapeModel newModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	ShapeView newView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void init() {
		// TODO Auto-generated method stub

	}

}
