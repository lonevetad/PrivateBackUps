package common.abstractCommon.behaviouralObjectsAC;

import java.io.Serializable;

import javax.swing.Action;

public interface MyAction<E> extends Serializable {

	/** Return an error, if eny */
	public String act(E e);
}