package dataStructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

public class SetMapped<OriginalType, T> implements Set<T> {

	public SetMapped(Set<OriginalType> backSet, Function<OriginalType, T> newTypeExtractor) {
		this.backSet = backSet;
		this.newTypeExtractor = newTypeExtractor;
	}

	protected final Set<OriginalType> backSet;
	protected final Function<OriginalType, T> newTypeExtractor;
	protected Function<T, OriginalType> reverseMapper;

	public Set<OriginalType> getBackSet() {
		return backSet;
	}

	public Function<OriginalType, T> getNewTypeExtractor() {
		return newTypeExtractor;
	}

	public Function<T, OriginalType> getReverseMapper() {
		return reverseMapper;
	}

	public SetMapped<OriginalType, T> setReverseMapper(Function<T, OriginalType> reverseMapper) {
		this.reverseMapper = reverseMapper;
		return this;
	}

	@Override
	public int size() {
		return backSet.size();
	}

	@Override
	public void clear() {
		backSet.clear();
	}

	@Override
	public boolean isEmpty() {
		return backSet.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return backSet.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return new IteratorMapped(backSet.iterator());
	}

	@Override
	public Object[] toArray() {
		int[] i;
		Object[] a;
		a = new Object[backSet.size()];
		i = new int[] { 0 };
		backSet.forEach(o -> a[i[0]++] = newTypeExtractor.apply(o));
		return a;
	}

	@Override
	public <Tt> Tt[] toArray(Tt[] a) {
		throw new UnsupportedOperationException("Too lazy to implement");
	}

	@Override
	public boolean add(T e) {
		if (reverseMapper == null)
			throw new UnsupportedOperationException("Cannot modify the original set without a reverse-mapper");
		return backSet.add(reverseMapper.apply(e));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		if (reverseMapper == null)
			throw new UnsupportedOperationException("Cannot modify the original set without a reverse-mapper");
		return backSet.remove(reverseMapper.apply((T) o));
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return backSet.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException("Cannot modify the original set");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Cannot modify the original set");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Cannot modify the original set");
	}

	//

	protected class IteratorMapped implements Iterator<T> {
		protected final Iterator<OriginalType> iter;

		public IteratorMapped(Iterator<OriginalType> iter) {
			super();
			this.iter = iter;
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public T next() {
			return newTypeExtractor.apply(iter.next());
		}
	}
}