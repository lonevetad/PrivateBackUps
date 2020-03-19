package games.generic.controlModel;

public class ThreadGame extends Thread {

	protected TGRunnable tgr;

	public ThreadGame(TGRunnable target) {
		super(target);
		this.tgr = target;
	}

	// makes me stops
	public void stopAndDie() {
		this.tgr.stopAndDie();
	}

	public static interface TGRunnable extends Runnable {

		// makes me stops
		public void stopAndDie();
	}
}