package games.generic.controlModel;

import java.util.Set;
import java.util.function.Consumer;

public interface GObjectsHolder<T extends ObjectWithID> {
//{
	public Set<T> getObjects();

	public boolean add(T o);

	public boolean remove(T o);

	public boolean contains(T o);

	public void forEach(Consumer<T> action);

	// OLD

//	public Set<ObjectWithID> getObjects();
//
//	public boolean add(ObjectWithID o);
//
//	public boolean remove(ObjectWithID o);
//
//	public boolean contains(ObjectWithID o);
//
//	public void forEach(Consumer<ObjectWithID> action);
}