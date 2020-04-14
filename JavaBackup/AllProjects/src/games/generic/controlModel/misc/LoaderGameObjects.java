package games.generic.controlModel.misc;

import games.generic.controlModel.ObjectNamed;

/**
 * Loads every definitions of a single game object's concept. See
 * {@link GameObjectsProvider} for examples of those concepts.
 * <p>
 * Those implementation could be a "hard coded" definition, where instances of
 * factories ({@link FactoryObjGModalityBased}) of this class generic parameter
 * are created directly by the code, or could rely to informations stored
 * somewhere. like a text file, a database, etc.
 */
public abstract class LoaderGameObjects<E extends ObjectNamed> implements LoaderGeneric {
	protected GameObjectsProvider<E> objProvider;

	public LoaderGameObjects(GameObjectsProvider<E> objProvider) {
		this.objProvider = objProvider;
	}

	public GameObjectsProvider<E> getObjProvider() {
		return objProvider;
	}

	public void setObjProvider(GameObjectsProvider<E> ombp) {
		this.objProvider = ombp;
	}

}