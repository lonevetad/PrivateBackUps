package common.abstractCommon;

import java.io.Serializable;

public abstract class Memento implements Serializable {
	private static final long serialVersionUID = -8888855858885L;

	public Memento() {
	}

	public abstract Object reinstanceFromMe();
}