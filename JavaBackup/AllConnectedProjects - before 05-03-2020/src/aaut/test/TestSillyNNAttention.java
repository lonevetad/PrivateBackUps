package aaut.test;

import java.awt.Point;
import java.awt.Rectangle;

import aaut.tools.ActivationFunction;
import aaut.tools.impl.MatrixInput2D;

@Deprecated
public class TestSillyNNAttention {

	public static void main(String[] args) {
		NN_Rectangle nnr;
		nnr = new NN_Rectangle();
		nnr.setActivationFunction(ActivationFunction.DefaultActivationFunctions.POSITIVE_STEP);
		System.out.println("START");
		nnr.setWeights(50, 40, 100, 80);
		System.out.println("contains (120,105)? ");
		System.out.println(nnr.contains(110, 105));
		System.out.println("END");
	}

	//

	//

	static class NN_Rectangle {
		protected int x, y, width, height;
		/** 4 rows (neurons) x 3 values (bias's, x's and y's weights) */
		/** First layer: rectangle-polygon */
		protected final double[][] neuronsWeightsPolygon;
		protected final MatrixInput2D nwp, secondLayerDecider; // summer
		protected ActivationFunction activationFunction;

		public NN_Rectangle() {
			neuronsWeightsPolygon = new double[4][3];
			nwp = new MatrixInput2D(new int[] { 4, 3 }, false);
			nwp.setMatrix(neuronsWeightsPolygon);
			secondLayerDecider = (new MatrixInput2D(new double[][] { new double[] { -3.5, 1, 1, 1, 1 } })).traspose();
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		public ActivationFunction getActivationFunction() {
			return activationFunction;
		}

		public void setActivationFunction(ActivationFunction activationFunction) {
			this.activationFunction = activationFunction;
		}

		public void setWeights(Rectangle r) {
			setWeights((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
		}

		public void setWeights(int x, int y, int width, int height) {
			double[] r;
			// 0 = bias, 1 = x, 2 = y
			r = neuronsWeightsPolygon[0]; // positive: lower-horizontal
			r[0] = -y;
			r[1] = 0;
			r[2] = 1;
			r = neuronsWeightsPolygon[1]; // negative: higher-horizontal
			r[0] = (y + height);
			r[1] = 0;
			r[2] = -1;
			r = neuronsWeightsPolygon[2]; // positive: lower-vertical
			r[0] = -x;
			r[1] = 1;
			r[2] = 0;
			r = neuronsWeightsPolygon[3]; // negative: higher-vertical
			r[0] = (x + width);
			r[1] = -1;
			r[2] = 0;
		}

		public boolean contains(Point p) {
			return contains(p.getX(), p.getY());
		}

		public boolean contains(double x, double y) {
			double finalValue;
			double[] neuronsInputSecondLayer;
			double[][] p_represent;
			MatrixInput2D nw_t, mat_p, outputLevel;
			// firstly, do the first layer
			nw_t = this.nwp; // .traspose();
			p_represent = new double[3][];
			p_represent[0] = new double[] { 1 };// bias
			p_represent[1] = new double[] { x };// x
			p_represent[2] = new double[] { y };// y
			mat_p = new MatrixInput2D(p_represent);

			outputLevel = (MatrixInput2D) nw_t.multiply(mat_p);// results as a (h:4; w:1) matrix
			neuronsInputSecondLayer = new double[5];// the first is the bias, others the neuron's output
			neuronsInputSecondLayer[0] = 1; // the bias
			outputLevel.apply(activationFunction);
			outputLevel.forEach((val, path) -> {
				// patu[1] is always 0 because it's one-column matrix
				neuronsInputSecondLayer[path[0] + 1] = val; // "+1" because of bias
			});

			nw_t = new MatrixInput2D(new double[][] { neuronsInputSecondLayer });
			outputLevel = (MatrixInput2D) (nw_t.multiply(secondLayerDecider));
			outputLevel.apply(activationFunction);
			// this is another one layer
//			for (double d : neuronsOutput)
//				if (d < 0)// all neurons must agree on "is inside"
//					return false;
//			res.getValueAt(at)
			finalValue = outputLevel.getValueAt(new int[] { 0, 0 }); // get the only one value
			return finalValue >= 0;
		}

	}
}