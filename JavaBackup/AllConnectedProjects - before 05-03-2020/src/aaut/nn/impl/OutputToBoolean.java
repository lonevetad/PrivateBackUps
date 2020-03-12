package aaut.nn.impl;

import java.util.function.Function;

import aaut.tools.MatrixInput;

public class OutputToBoolean implements Function<MatrixInput, boolean[]> {
	@Override
	public boolean[] apply(MatrixInput t) {
		final boolean[] r;
		r = new boolean[t.getDimensionAriety(0)];
		t.forEach((d, at) -> {
			r[at[0]] = d >= 0;
		});
		return r;
	}
}