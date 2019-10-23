package appunti.testRoomJoiner;

import java.io.Serializable;

public // abstract
class AbstractCommand implements Runnable, Serializable {
	private static final long serialVersionUID = 56205905690099L;

	public AbstractCommand() {
	}

	@Override
	public void run() {
		System.out.println("I run !");
	}
}