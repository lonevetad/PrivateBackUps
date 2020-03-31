package games.generic.controlModel;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

import games.generic.controlModel.subImpl.GModelTimeBased;
import tools.ObjectWithID;

/**
 * Interface for classes that holds and manages in some way (at least in a
 * similar way to {@link Collection}) and provides them.<br>
 * Example of subclasses are:
 * <ul>
 * <li>{@link GModelTimeBased.TimedObjectHolder}</li>
 * <li></li>
 * <li></li>
 * </ul>
 */
public interface GObjectsHolder {
//	 OLD
//<T extends ObjectWithID> {
//	public Set<T> getObjects();
//	public boolean add(T o);
//	public boolean remove(T o);
//	public boolean contains(T o);
//	public T get(Integer id);
//	public void forEach(Consumer<T> action);

	/**
	 * Returns all {@link ObjectWithID} held by this specific holder.<br>
	 * BEWARE: In some implementations, other GObjectsHolder instances may held
	 * sub-GObjectsHolder to perform some fine-grained tasks, differentiations, etc
	 * or just hold other classes' "Models". In that case DO NOT use this method,
	 * but accessors like {@link #get(Integer)} or traversal-method like
	 * {@link #forEach(Consumer)} to assure to process the wanted object or all
	 * objects.
	 */
	public Set<ObjectWithID> getObjects();

	public boolean add(ObjectWithID o);

	public boolean remove(ObjectWithID o);

	/** Similar to {@link Collection#clear()}. */
	public boolean removeAll();

	public boolean contains(ObjectWithID o);

	public ObjectWithID get(Integer id);

	public void forEach(Consumer<ObjectWithID> action);
}