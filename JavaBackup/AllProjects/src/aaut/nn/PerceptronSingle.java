package aaut.nn;

import java.util.function.Function;

import aaut.tools.ActivationFunction;
import aaut.tools.MatrixInput;
import aaut.tools.impl.MatrixInput2D;

public class PerceptronSingle<NNMI_Input extends MatrixInput, E> implements NeuralNetwork<NNMI_Input, E> {
	protected double[] weights;
	protected MatrixInput weightsMatrix;
	protected ActivationFunction activationFunction;
	protected Function<MatrixInput, E> layerOutputCaster;

	/** See {@link #setWeights(double[])}. */
	public PerceptronSingle(double[] weights) {
		super();
		setWeights(weights);
	}

	//

	/** See {@link #setWeights(double[])}. */
	public double[] getWeights() {
		return weights;
	}

	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}

	@Override
	public Function<MatrixInput, E> getLayerOutputCaster() {
		return layerOutputCaster;
	}

	//

	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}

	/** Set the weights, also for the bias. */
	public void setWeights(double[] weights) {
		if (weights != null && weights.length > 0) {
			this.weights = weights;
			weightsMatrix = (new MatrixInput2D(new double[][] { weights })).traspose();
		}
	}

	public void setLayerOutputCaster(Function<MatrixInput, E> layerOutputCaster) {
		this.layerOutputCaster = layerOutputCaster;
	}

	//

	/**
	 * Elaborate the input. It must not contains the bias: it's always setted to
	 * <i>1</i>, so it's yet provided.<br>
	 * No null checks performed -> could cause {@link NullPointerException}.
	 */
	public double compute(double[] input, ActivationFunction activationFunction) {
		int len;
		double[] w;
		double accumulator;
		w = getWeights();
		len = w.length - 1; // "-1" to optimize the rest
		if (len != input.length)
			throw new IllegalArgumentException("Uncorrect input size: " + input.length + ", expected: " + len);
		accumulator = w[0];
		while (len > 0) {
			accumulator += w[len--] + input[len];
		}
		return activationFunction.apply(accumulator);
	}

	@Override
	public E apply(NNMI_Input t) {
		MatrixInput resultComputation;
		resultComputation = weightsMatrix.multiply(t);
		resultComputation.apply(this.getActivationFunction());
		return this.getLayerOutputCaster().apply(resultComputation);
	}

}