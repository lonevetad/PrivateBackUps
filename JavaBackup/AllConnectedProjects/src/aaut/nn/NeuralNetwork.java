package aaut.nn;

import java.util.function.Function;

import aaut.tools.MatrixInput;

/** The Type E could be a class, a distribution, a rectangle, a label, etc */
public interface NeuralNetwork<NNMI extends MatrixInput, E> extends Function<NNMI, E> {

	public Function<MatrixInput, E> getLayerOutputCaster();
//public void castTo()
}
