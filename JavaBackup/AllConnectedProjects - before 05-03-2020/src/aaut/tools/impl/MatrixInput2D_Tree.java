package aaut.tools.impl;

import java.util.Arrays;

import aaut.tools.MatrixInput;

public class MatrixInput2D_Tree extends MatrixInputTree {
	private static final long serialVersionUID = 5460857245405L;

	public MatrixInput2D_Tree(int[] dimensions) {
		super(dimensions);
		if (dimensions == null || dimensions.length != 2)
			throw new IllegalArgumentException("Not a 2D matrix. Dimensions given: " + Arrays.toString(dimensions));
	}

	@Override
	public MatrixInput multiply(MatrixInput nnmi) {
		int i, mineNumRows, nnmiNumColumns, sharedDimensions;
		int[] at;
		double accumulator;
		double[] myRowVals, retRowVals;
		MatrixInput2D_Tree m;
		if (nnmi == null || nnmi.getDimensionsCount() != 2 || //
				(sharedDimensions = this.getDimensionAriety(1)) != nnmi.getDimensionAriety(0))
			return null;
		mineNumRows = this.getDimensionAriety(0);
		nnmiNumColumns = nnmi.getDimensionAriety(1);
		m = new MatrixInput2D_Tree(new int[] { mineNumRows, nnmiNumColumns });
		at = new int[2];
//		if (mineNumRows >= nnmiNumColumns) {
//try to otpimise
		for (int r = 0; r < mineNumRows; r++) {
			myRowVals = // myRow
					root.getSubdimensionAt(r).getValuesLastDimension();
			retRowVals = m.root.getSubdimensionAt(r).getValuesLastDimension();
			for (int c = 0; c < nnmiNumColumns; c++) {
				at[1] = c;
				i = sharedDimensions;
				accumulator = 0;
				while (--i >= 0) {
					at[0] = i;
					accumulator += (myRowVals[i] * nnmi.getValueAt(at));
				}
				retRowVals[c] = accumulator;
			}
//		}
		}
		return m;
	}
}