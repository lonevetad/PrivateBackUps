package tests.staticAbilities;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SwinHolder<E extends SomethingWithIDName> implements Serializable, Iterable<E> {

	private static final long serialVersionUID = 3210874840993633L;

	private Map<Long, E> idReference;
	private Map<String, List<E>> nameReference;

	public SwinHolder() {
		this.idReference = new java.util.TreeMap<>(Long::compareTo);
		this.nameReference = new java.util.TreeMap<>(String::compareTo);
	}

	public boolean contains(E a) {
		return this.idReference.get(a.getInstanceID()) != null;
	}

	public void add(E e) {
		if (e == null || contains(e))
			return;
		List<E> l;
		idReference.put(e.getInstanceID(), e);
		l = nameReference.get(e.getName());
		if (l == null) {
			l = new LinkedList<>();
			l.add(e);
			nameReference.put(e.getName(), l);
		} else {
			if (!l.contains(e))
				l.add(e);
		}
	}

	public boolean remove(E e) {
		return remove(e.getInstanceID());
	}

	public boolean remove(Long id) {
		List<E> l;
		if (id == null)
			return false;
		E e;
		e = this.idReference.get(id);
		if (e == null)
			return false;
		idReference.remove(id);
		l = nameReference.get(e.getName());
		l.remove(e);
		if (l.isEmpty())
			nameReference.remove(e.getName());
		return true;
	}

	public E getById(Long id) {
		return this.idReference.get(id);
	}

	public List<E> getByName(String name) {
		return this.nameReference.get(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<E> iterator() {
//		if (this.idReference instanceof Iterable<?>)
//			return ((Iterable<Entry<Long, E>>) this.idReference).iterator();
		try {
			Method m;
			m = this.idReference.getClass().getMethod("iteratorValue");
			if (m != null) {
				return (Iterator<E>) m.invoke(this.idReference);
			}
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException nsme) {
			// silent
		}
		return new IteratorE();
	}

	class IteratorE implements Iterator<E> {
		protected Iterator<Long> iter;

		IteratorE() {
			this.iter = idReference.keySet().iterator();
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public E next() {
			if (!hasNext())
				return null;
			return idReference.get(iter.next());
		}

	}
}