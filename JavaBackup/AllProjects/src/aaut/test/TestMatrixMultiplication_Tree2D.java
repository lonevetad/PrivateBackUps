package aaut.test;

import aaut.tools.MatrixInput;
import aaut.tools.impl.MatrixInput2D_Tree;

public class TestMatrixMultiplication_Tree2D extends TestMatrixMultiplication {
	@Override
	protected MatrixInput getAndFill(double[][] v) {
		int i;
		int[] at;
		MatrixInput2D_Tree m;
		i = 0;
		m = new MatrixInput2D_Tree(new int[] { v.length, v[0].length });
		at = new int[] { 0 };
		for (double[] d : v) {
			at[0] = i++;
			m.setValuesOfLastDimensionsAt(at, d);
		}
		return m;
	}

	public static void main(String[] args) {
		TestMatrixMultiplication_Tree2D t;
		t = new TestMatrixMultiplication_Tree2D();
		t.doTest();
	}
}