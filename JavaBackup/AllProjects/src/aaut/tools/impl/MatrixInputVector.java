package aaut.tools.impl;

import aaut.tools.MatrixInput;

/*** IMplements the {@link MatrixInput} using a very long array of double. */
public class MatrixInputVector extends MatrixInput {

	public MatrixInputVector(int[] dimensions) {
		super(dimensions);
		if (!checkLength(dimensions))
			throw new IllegalArgumentException(
					"Invalid dimensions: values are zero, negative or the resulting matrix would have more than Integer.MAX elements ("
							+ Integer.MAX_VALUE + ")");
	}

	protected double[] allValues; // TODO

	protected boolean checkLength(int[] dimensions) {
		int len, pow;
		len = dimensions.length;
		pow = 1;
		while (--len >= 0) {
			pow *= dimensions[len];
			if (pow <= 0)
				return false;
		}
		return true;
	}

	public int index(int[] at) {
		int pow, index, len, at_i, dimAriety;
		index = 0;
		len = this.getDimensionsCount();
		pow = 1;
		while (--len >= 0) {
			at_i = at[len];
			dimAriety = this.getDimensionAriety(len);
			if (dimAriety >= at_i)
				return -1;
			index += pow * at_i;
			pow *= dimAriety;
		}
		return index;
	}

	@Override
	public void setValueAt(int[] at, double value) {
		int i;
		i = index(at);
		if (i < 0)
			throw new IllegalArgumentException("path exceed dimensions");
		allValues[i] = value;
	}

	@Override
	public double getValueAt(int[] at) {
		int i;
		i = index(at);
		if (i < 0)
			throw new IllegalArgumentException("path exceed dimensions");
		return allValues[i];
	}

	@Override
	public MatrixInput multiply(MatrixInput nnmi) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void forEach(ValuePathConsumer dc) {
		// TODO Auto-generated method stub
	}
}