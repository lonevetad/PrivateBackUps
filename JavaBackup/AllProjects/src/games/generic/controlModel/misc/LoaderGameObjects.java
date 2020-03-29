package games.generic.controlModel.misc;

import games.generic.controlModel.GController;
import games.generic.controlModel.ObjectNamed;

public abstract class LoaderGameObjects<E extends ObjectNamed> {
	protected ObjGModalityBasedProvider<E> objProvider;

	public LoaderGameObjects(ObjGModalityBasedProvider<E> objProvider) {
		this.objProvider = objProvider;
	}

	public ObjGModalityBasedProvider<E> getObjProvider() {
		return objProvider;
	}

	public void setObjProvider(ObjGModalityBasedProvider<E> ombp) {
		this.objProvider = ombp;
	}

	public abstract void loadInto(GController gm);
}