package aaut.tools.impl;

import java.util.Arrays;

import aaut.tools.ActivationFunction;
import aaut.tools.MatrixInput;

/** Optimized version of {@link MatrixInput2D_Tree}. */
public class MatrixInput2D extends MatrixInput {
	private static final long serialVersionUID = 7484 + 152336026L;

	public MatrixInput2D(int[] dimensions) {
		this(dimensions, true);
	}

	public MatrixInput2D(int[] dimensions, boolean shouldBuild) {
		super(dimensions);
		if (dimensions == null || dimensions.length != 2)
			throw new IllegalArgumentException("Not a 2D matrix. Dimensions given: " + Arrays.toString(dimensions));

		if (shouldBuild) {
			this.matrix = new double[dimensions[0]][dimensions[1]];
		}
	}

	public MatrixInput2D(double[][] m) {
		this(new int[2], false);
		setMatrix(m);
	}

	protected double[][] matrix;

	/** Use with caution */
	public double[][] getMatrix() {
		return matrix;
	}

	public static boolean isNullOrRagged(double[][] matrix) {
		int h, w;
		double[] row;
		if (matrix == null)
			return true;
		h = matrix.length;
		if (h == 0)
			return true;
		w = matrix[0].length;
		while (--h >= 1)
			if ((row = matrix[h]) == null || row.length != w)
				return true;
		return false;
	}

	public void setMatrix(double[][] matrix) {
		if (isNullOrRagged(matrix))
			return;
		this.matrix = matrix;
		this.dimensionsSizes[0] = matrix.length;
		this.dimensionsSizes[1] = matrix[0].length;
	}

	@Override
	public double getValueAt(int[] at) {
		return matrix[at[0]][at[1]];
	}

	@Override
	public void setValueAt(int[] at, double value) {
		matrix[at[0]][at[1]] = value;
	}

	@Override
	public MatrixInput multiply(MatrixInput nnmi) {
		int i, mineNumRows, nnmiNumColumns, sharedDimensions;
//		int[] at;
		double accumulator;
		double[] myRowVals, retRowVals; // nnmiRowVals,
		double[][] mym, nnmim, retm;
		MatrixInput2D mRet, mi;
		if (nnmi == null || (!(nnmi instanceof MatrixInput2D))//
				|| nnmi.getDimensionsCount() != 2 || //
				(sharedDimensions = this.getDimensionAriety(1)) != nnmi.getDimensionAriety(0)) {
			System.out.println("\tnnmi.getDimensionsCount() : " + nnmi.getDimensionsCount() //
					+ "\n\tthis.getDimensionAriety(1): " + this.getDimensionAriety(1)//
					+ "\n\tnnmi.getDimensionAriety(0): " + nnmi.getDimensionAriety(0)//
			);
			return null;
		}
		mi = (MatrixInput2D) nnmi;
		mineNumRows = this.getDimensionAriety(0);
		nnmiNumColumns = nnmi.getDimensionAriety(1);
		mRet = new MatrixInput2D(new int[] { mineNumRows, nnmiNumColumns }, true);
//		at = new int[2];
		mym = this.matrix;
		nnmim = mi.matrix;
		retm = mRet.matrix;

//	if (mineNumRows >= nnmiNumColumns) {
//try to otpimise
		for (int r = 0; r < mineNumRows; r++) {
			myRowVals = mym[r];
//			nnmiRowVals = nnmim[r];
			retRowVals = retm[r];
			for (int c = 0; c < nnmiNumColumns; c++) {
//				at[1] = c;
				i = sharedDimensions;
				accumulator = 0;
				while (--i >= 0) {
//					at[0] = i;
					accumulator += (myRowVals[i] * nnmim[i][c]);
				}
				retRowVals[c] = accumulator;
			}
//	}
		}
		return mRet;
	}

	@Override
	public void forEach(ValuePathConsumer dc) {
		int w, h;
		int[] path;
		double[] row, m[];
		h = (m = matrix).length;
		w = m[0].length;
		path = new int[2];
		for (int r = 0; r < h; r++) {
			path[0] = r;
			row = m[r];
			for (int c = 0; c < w; c++) {
				path[1] = c;
				dc.apply(row[c], path);
			}
		}
	}

	@Override
	public void apply(ActivationFunction af) {
		int w, h;
		double[] row, m[];
		h = (m = matrix).length;
		w = m[0].length;
		for (int r = 0; r < h; r++) {
			row = m[r];
			for (int c = 0; c < w; c++) {
				row[c] = af.apply(row[c]);
			}
		}
	}

	//

	//

	public MatrixInput2D traspose() {
		int w, h;
		double[] row;
		double[][] myr, retr;
		MatrixInput2D ret;
		ret = new MatrixInput2D(new int[] { w = this.dimensionsSizes[1], h = this.dimensionsSizes[0] }, true);
		myr = this.matrix;
		retr = ret.matrix;
		if (h >= w) {
			for (int r = 0; r < h; r++) {
				row = myr[r];
				for (int c = 0; c < w; c++) {
					retr[c][r] = row[c];
				}
			}
		} else {
			for (int r = 0; r < w; r++) {
				row = retr[r];
				for (int c = 0; c < h; c++) {
					row[c] = myr[c][r];
				}
			}
		}
		return ret;
	}

	@Override
	public String toString() {
		StringBuilder sb;
		sb = new StringBuilder(Math.max((this.matrix.length * this.matrix[0].length) << 5, 1024));
		sb.append("Matrix 2D of size: {w: ");
		sb.append(this.matrix[0].length);
		sb.append(", h: ");
		sb.append(this.matrix.length);
		sb.append("}, values:");
		this.forEach((d, at) -> {
			sb.append("\n- ");
			sb.append(d);
			sb.append("\t at: ");
			sb.append(Arrays.toString(at));
		});

		return sb.toString();
	}
}