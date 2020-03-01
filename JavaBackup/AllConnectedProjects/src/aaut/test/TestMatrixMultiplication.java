package aaut.test;

import aaut.tools.MatrixInput;
import aaut.tools.MatrixInput.ValuePathConsumer;

public abstract class TestMatrixMultiplication extends TestMatrixGeneric {

	public TestMatrixMultiplication() {
	}

	protected abstract MatrixInput getAndFill(double[][] v);

	//

	/** Array of triple: <code>(firstMatrix, secondMatrix, expectedMatrix)</code> */
	public double[][][][] getExamples() {
		return new double[][][][] { //
				new double[][][] { //
						new double[][] { //
								new double[] { 1, 1, 2 }//
								, new double[] { 0, 1, -3 }//
						}, //
						new double[][] { //
								new double[] { 1, 1, 1 }//
								, new double[] { 2, 5, 1 }//
								, new double[] { 0, -2, 1 }//
						}, //
						new double[][] { //
								new double[] { 3, 2, 4 }//
								, new double[] { 2, 11, -2 }//
						} }, // // example 2
				new double[][][] { //
						new double[][] { //
								new double[] { 1, 2, 0 }//
								, new double[] { 3, -1, 4 }//
						}, //
						new double[][] { //
								new double[] { 1 }//
								, new double[] { 0 }//
								, new double[] { -1 } //
						}, //
						new double[][] { //
								new double[] { 1 }//
								, new double[] { -1 }//
						} }, // // example 3

				new double[][][] { //
						new double[][] { //
								new double[] { 1, 0, 2 }//
								, new double[] { 0, 3, -1 }//
						}, //
						new double[][] { //
								new double[] { 4, 1 }//
								, new double[] { -2, 2 }//
								, new double[] { 0, 3 }//
						}, //
						new double[][] { //
								new double[] { 4, 7 }//
								, new double[] { -6, 3 }//
						} }

		};
	}

	protected void performTestOn(double[][] m1i, double[][] m2i, double[][] multExpected_i) {
		MatrixInput m1, m2, mult, multExpected;
		System.out.println("the first matrix:");
		m1 = getAndFill(m1i);
		m1.forEach(ValuePathConsumer.PRINTER);

		System.out.println("\n the second matrix:");
		m2 = getAndFill(m2i);
		m2.forEach(ValuePathConsumer.PRINTER);

		System.out.println("\n mult expected matrix:");
		multExpected = getAndFill(multExpected_i);
		multExpected.forEach(ValuePathConsumer.PRINTER);

		System.out.println("\n mult matrix result:");
		mult = m1.multiply(m2);
		mult.forEach(ValuePathConsumer.PRINTER);
	}

	public void doTest() {
		int i;
		double[][] m1i, m2i, multExpected;
		double[][][][] allExamplesTriple;
		i = 0;
		System.out.println("START");
		allExamplesTriple = this.getExamples();
		for (double[][][] triple : allExamplesTriple) {
			System.out.println("\n\n\n EXAMPLE n: " + i++ + "\n\n");
			m1i = triple[0];
			m2i = triple[1];
			multExpected = triple[2];
			performTestOn(m1i, m2i, multExpected);
		}
		System.out.println("\n\n END");
	}
}