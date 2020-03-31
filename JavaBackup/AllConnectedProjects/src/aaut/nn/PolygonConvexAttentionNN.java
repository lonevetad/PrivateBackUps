package aaut.nn;

import java.awt.Polygon;
import java.util.LinkedList;

import aaut.nn.old.PolygonAttentionNN;
import aaut.tools.MatrixInput;
import aaut.tools.impl.MatrixInput2D;
import geometry.pointTools.PolygonUtilities;
import tools.MathUtilities;

public class PolygonConvexAttentionNN<NNMI_Input extends MatrixInput, E> extends PolygonAttentionNN<NNMI_Input, E> {

	public PolygonConvexAttentionNN(Polygon poly) {
		super(poly);
	}

	@Override
	public MatrixInput2D build(Polygon poly) {
		double[][] polyWeights;
		polyWeights = weightsFromConvexPolygon(poly);
		return (new MatrixInput2D(polyWeights)).traspose();
	}

	protected static double[][] weightsFromConvexPolygon(Polygon poly) {
		int i, n;
		double xc, yc;
		int[] xx, yy;
		double[] row;
		double[][] polyWeights;
		LinkedList<double[]> rows;
		// 0) check se è convesso o no ... dato per assunto
		// 1) calcola il centroide
		// 2) per ogni lato, ipotizzato positivo, ...
		// 2.1) si vede se lo classifica correttamente
		// 2.1.1) se no, invertire il segno
		if (poly == null || ((n = poly.npoints) < 3) || (!PolygonUtilities.isConvex(poly)))
			throw new IllegalArgumentException("poly is null, degenerated or convex");
		xx = poly.xpoints;
		yy = poly.ypoints;
		xc = yc = 0;
		i = -1;
		{
			double nn;
			long lcx, lcy;
			nn = n;
			lcx = lcy = 0;
			while (++i < n) {
				lcx += xx[i];
				lcy += yy[i];
			}
			xc = lcx / nn;
			yc = lcy / nn;
		}
//		polyWeig = new double[n][3]; // 3 = {bias, x, y}
		rows = new LinkedList<>();
		i = -1;
		while (++i < n) {
			row = setRowValues(i, xx, yy, xc, yc);
			if (row != null)
				rows.add(row);
//			row[0]=
		}
		n = rows.size();
		polyWeights = new double[n][];
		i = -1;
//		while (! rows.isEmpty()) {
		while (++i < n) {
			polyWeights[i] = rows.removeFirst();
		}
		return polyWeights;
	}

	protected static double[] setRowValues(int index, int[] xx, int[] yy, double xc, double yc) {
		double[] row;
		int thisx, thisy, nextx, nexty;
		thisx = xx[index];
		thisy = yy[index];
		if (++index >= xx.length)
			index = 0;
		nextx = xx[index];
		nexty = yy[index];
		if (thisx == nextx && thisy == nexty)
			return null;
		row = new double[3];
//			throw new IllegalArgumentException("Cannot build the Net: degenerate point at: " + index);
		if (thisx == nextx) {
			row[2] = 0;
			if (xc >= nextx) {
				row[0] = -thisx;
				row[1] = 1;
			} else {
				row[0] = thisx;
				row[1] = -1;
			}
		} else if (thisy == nexty) {
			row[1] = 0;
			if (yc >= thisy) {
				row[0] = -thisy;
				row[2] = 1;
			} else {
				row[0] = thisy;
				row[2] = -1;
			}
		} else {
			int divisor, dx, dy, m, y_coeff;
			dx = thisx - nextx;
			dy = thisy - nexty;
			if (dx < 0) {
				dx = -dx;
				dy = -dy;
			}
			divisor = MathUtilities.mcd(dx, dy < 0 ? -dy : dy);
			if (divisor != 1) {
				dx /= divisor;
				dy /= divisor;
			}
//			//  y - mx - c = 0 -> c = y-mx
			m = -dy;
			y_coeff = dx;
			row[0] = -(thisy * y_coeff + m * thisx); // m is yet negated
			row[1] = m;
			row[2] = y_coeff;
			if (evaluatePoint(xc, yc, row) < 0.0) {
				row[0] = -row[0];
				row[1] = -row[1];
				row[2] = -row[2];
			}
		}
		return row;
	}

}