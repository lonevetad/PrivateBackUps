package aaut.test;

import aaut.tools.MatrixInput;
import aaut.tools.impl.MatrixInput2D;

public class TestMatrixMultiplication_Matrix2D extends TestMatrixMultiplication {

	@Override
	protected MatrixInput getAndFill(double[][] v) {
		MatrixInput2D m;
		m = new MatrixInput2D(new int[] { 1, 1 }, false);
		m.setMatrix(v);
		return m;
	}

	public static void main(String[] args) {
		TestMatrixMultiplication_Matrix2D t;
		t = new TestMatrixMultiplication_Matrix2D();
		System.out.println("TestMatrixMultiplication_Matrix2D");
		t.doTest();
	}
}