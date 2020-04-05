package aaut.nn;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import aaut.tools.MatrixInput;

public abstract class PerceptronArray<NNMI_Input extends MatrixInput, E> implements NeuralNetwork<NNMI_Input, E> {

	protected List<NeuralNetwork<MatrixInput, MatrixInput>> layers;

	public List<NeuralNetwork<MatrixInput, MatrixInput>> getLayers() {
		return layers;
	}

	public void setLayers(List<NeuralNetwork<MatrixInput, MatrixInput>> layers) {
		this.layers = layers;
	}

	//

	public void forEach(Consumer<? super NeuralNetwork<MatrixInput, MatrixInput>> action) {
		layers.forEach(action);
	}

	public int size() {
		return layers.size();
	}

	public boolean isEmpty() {
		return layers.isEmpty();
	}

	public Object[] toArray() {
		return layers.toArray();
	}

	public boolean add(PerceptronSingle<MatrixInput, MatrixInput> e) {
		return layers.add(e);
	}

	public boolean remove(Object o) {
		return layers.remove(o);
	}

	public void clear() {
		layers.clear();
	}

	public NeuralNetwork<MatrixInput, MatrixInput> get(int index) {
		return layers.get(index);
	}

	public void add(int index, NeuralNetwork<MatrixInput, MatrixInput> element) {
		layers.add(index, element);
	}

	public NeuralNetwork<MatrixInput, MatrixInput> remove(int index) {
		return layers.remove(index);
	}

	public Stream<NeuralNetwork<MatrixInput, MatrixInput>> stream() {
		return layers.stream();
	}
}