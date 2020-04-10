package games.generic.controlModel.misc;

import games.generic.controlModel.GModality;

/**
 * When the game ({@link GModality}, to be precise) shuts down ALL threads
 * should stops and being destroyed. This class is designed (hopefully) to do so
 * through {@link #stopAndDie()}.
 */
public class GThread extends Thread {

	protected GTRunnable tgr;

	public GThread(GTRunnable target) {
		super(target);
		this.tgr = target;
	}

	// makes me stops

	/**
	 * As explained in this class's documentation ({@link GThread}), this method
	 * should stop this thread to run and, then, die (being able do be destroyed and
	 * un-allocated). <br>
	 * To do so, invoke {@link GTRunnable#stopAndDie()}.
	 */
	public void stopAndDie() {
		this.tgr.stopAndDie();
	}

	public static interface GTRunnable extends Runnable {

		/**
		 * Execute the game, usually by invoking {@link GModality#runGameCycle()}.<br>
		 * Implementation example:<br>
		 * 
		 * <pre>
		 * <code>
		 * public void run() {
		 * 	while( this.gameNotShuttedDown ){
		 * 		getGameModality().runGameCycle();
		 * 	}
		 * }
		 * </code>
		 * </pre>
		 * 
		 * Setting a local flag to false should be enough, usually no greater complexity
		 * is required.<br>
		 * (NOTE: To "pause" the game do not rely on {@link #stopAndDie()}, use
		 * {@link GController#pauseGame()} instead!)
		 * <p>
		 * Inherited documentation:
		 * <p>
		 * {@inheritDoc}
		 */
		@Override
		public void run();

		/**
		 * See {@link GThread#stopAndDie()} for more informations. <br>
		 * NOTE: To "pause" the game do not rely on {@link #stopAndDie()}, use
		 * {@link GController#pauseGame()} instead!
		 * <p>
		 * Implementation example:<br>
		 * 
		 * <pre>
		 * <code>
		 * public void stopAndDie(){
		 * 	this.gameNotShuttedDown = false;
		 * }
		 * </code>
		 * </pre>
		 * 
		 */
		public void stopAndDie();
	}
}