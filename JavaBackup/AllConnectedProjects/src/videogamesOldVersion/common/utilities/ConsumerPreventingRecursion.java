package common.utilities;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

public class ConsumerPreventingRecursion<T> implements Consumer<T>, Serializable {
	private static final long serialVersionUID = 986520518L;

	public ConsumerPreventingRecursion() {
		neverCalled = true;
		whatToDo = null;
	}

	public ConsumerPreventingRecursion(Consumer<T> whatToDo) {
		this();
		Objects.requireNonNull(whatToDo);
		neverCalled = true;
		this.whatToDo = whatToDo;
	}

	private boolean neverCalled;
	private Consumer<T> whatToDo;

	/**
	 * Just to prevent bugs, this method works on a try-block. Any exception
	 * raised is re-thrown.
	 * <p>
	 */
	@Override
	public void accept(T t) {
		if (neverCalled) {
			neverCalled = false;
			try {
				whatToDo.accept(t);
				neverCalled = true;
			} catch (Throwable th) {
				neverCalled = true;
				throw th;
			}
		}
	}
}