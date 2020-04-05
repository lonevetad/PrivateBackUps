package games.generic.controlModel.misc;

import games.generic.controlModel.GModality;

public interface FactoryObjGModalityBased<T> {
	public T newInstance(GModality gm);
}