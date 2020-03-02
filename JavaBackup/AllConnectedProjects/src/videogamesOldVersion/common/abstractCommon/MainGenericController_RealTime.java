package common.abstractCommon;

import common.abstractCommon.referenceHolderAC.MainHolder;
import common.gui.MainGUI;

public //
abstract //
class MainGenericController_RealTime extends MainController {
	private static final long serialVersionUID = 1L;

	public MainGenericController_RealTime(LoaderGeneric loaderGeneric, MainGUI mgg) {
		super(loaderGeneric, mgg);
	}

	RunnerGame runnerGame;
	ThreadGame threadGame;

	//

	// TODO GETTER

	public RunnerGame getRunnerGame() {
		return runnerGame;
	}

	public ThreadGame getThreadGame() {
		return threadGame;
	}

	//

	// TODO SETTER

	//

	// TODO OTHER

	@Override
	public boolean allocThreads() {
		boolean allocated;
		allocated = super.allocThreads();
		if (threadGame == null) {
			addThreadToPool(threadGame = new ThreadGame(this));
			allocated = true;
		}
		return allocated;
	}

	@Override
	public void startThreads() {
		super.startThreads();
		allocThreads();
		threadGame.start();
	}

	@Override
	public void destroyThreads() {
		super.destroyThreads();
		this.isWorking = false;
		threadGame.interrupt();
		removeThreadFromPool(threadGame);
		threadGame = null;
	}

	@Override
	public void pauseThreads() {
		super.pauseThreads();
		this.isPlaying = false;
	}

	//

	// TODO CLASSES

	public static class ThreadGame extends Thread implements MainHolder {
		private static final long serialVersionUID = 5640198491L;

		public ThreadGame(MainGenericController_RealTime m) {
			this(m, new RunnerGame(m));
		}

		private ThreadGame(MainGenericController_RealTime m, RunnerGame rg) {
			super(m.runnerGame = rg);
			this.rg = rg;
		}

		RunnerGame rg;

		/*
		 * ThreadGame(MainGeneric m, Runnable r) { super(r); }
		 */@Override
		public MainController getMain() {
			return rg.getMain();
		}

		@Override
		public ThreadGame setMain(MainController main) {
			this.rg.setMain(main);
			return this;
		}
	}

	public static class RunnerGame implements Runnable, MainHolder {
		private static final long serialVersionUID = 5640198490L;
		public boolean timeResetNeeded;
		int frames, lastFramesPerformed;
		long lastTime/* , millisLastFrame */;
		MainController m;

		RunnerGame(MainController mm) {
			m = mm;
			resetTime();
		}

		@Override
		public MainController getMain() {
			return m;
		}

		@Override
		public RunnerGame setMain(MainController main) {
			this.m = main;
			return this;
		}

		/** Ritorna l'ammontare di frames calcolati nell'ultimo secondo */
		public int getLastFramePerformed() {
			return lastFramesPerformed;
		}

		public void resetTime() {
			frames = lastFramesPerformed = 0;
			// deltaTimePassed = MILLISECONDS_STANDARD_WAIT_AFTER_RESUME;
			// millisLastFrame =
			lastTime = System.currentTimeMillis();
		}

		@Override
		public void run() {
			int deltaTimePassed;
			// int millisFrameCounterRefresh;
			long currentTime;

			System.out.println("Thread Game started : isStillWorking:" + m.isWorking());
			while (m.isWorking()) {

				// millisFrameCounterRefresh = 0;
				try {
					while (m.isPlaying) {

						currentTime = System.currentTimeMillis();
						if (timeResetNeeded) {
							timeResetNeeded = false;
							Thread.sleep(deltaTimePassed = MILLISECONDS_STANDARD_WAIT_AFTER_RESUME);
							// millisLastFrame =
							lastTime = currentTime;// System.currentTimeMillis();
							lastFramesPerformed = frames = 1;
						} else {
							deltaTimePassed = (int) (currentTime - lastTime);
							lastTime = currentTime;
						}

						if (deltaTimePassed == 0) {
							Thread.sleep(deltaTimePassed = MILLISECONDS_STANDARD_WAIT_AFTER_RESUME);
							lastTime = System.currentTimeMillis();
						} else if (deltaTimePassed > MAX_MILLISECONDS_EACH_GAME_FRAME)
							deltaTimePassed = MAX_MILLISECONDS_EACH_GAME_FRAME;
						//

						// at the end of the stuffs, calculate FPS
						/*
						 * if ((millisFrameCounterRefresh += deltaTimePassed) >= 1000) {
						 * millisFrameCounterRefresh -= 1000; lastFramesPerformed = frames; frames =
						 * 0; } else { frames++; }
						 */
						//

						// DO STUFFS

						m.act(deltaTimePassed);
					} // end of cycle
					System.out.println("MainGenericController_RealTime exit from cycle isPlaying");
					Thread.sleep(500);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
					return;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}