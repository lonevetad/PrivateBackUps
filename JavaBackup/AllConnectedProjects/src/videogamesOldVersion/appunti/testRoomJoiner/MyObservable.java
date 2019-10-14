package appunti.testRoomJoiner;

import java.util.Observable;

public class MyObservable extends Observable {

	public MyObservable() {
		super();
	}

	@Override
	public void notifyObservers(Object arg) {
		setChanged();
		super.notifyObservers(arg);
	}
}
