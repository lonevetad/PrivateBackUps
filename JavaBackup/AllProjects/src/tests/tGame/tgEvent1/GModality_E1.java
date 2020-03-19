package tests.tGame.tgEvent1;

import games.generic.controlModel.GameController;
import games.generic.controlModel.GameEventManager;
import games.generic.controlModel.GameModel;
import games.generic.controlModel.ThreadGame;
import games.generic.controlModel.subImpl.GameEventManagerFineGrained;
import games.generic.controlModel.subImpl.GameModalityET;

public class GModality_E1 extends GameModalityET {

	public GModality_E1(GameController controller, String modalityName) {
		super(controller, modalityName);
	}

	ThreadGame threadGame;

	@Override
	public void onCreate() {
		super.onCreate();
		checkAndRebuildThreads();
	}

	@Override
	public GameModel newGameModel() {
		return new GModel_E1();
	}

	@Override
	public GameEventManager newEventManager() {
		return new GameEventManagerFineGrained(this);
	}

	@Override
	public void startGame() {
		checkAndRebuildThreads();
		this.threadGame.start();
	}

	@Override
	public void closeAll() {
		super.closeAll();
		this.threadGame = null;
	}

	//

	protected void checkAndRebuildThreads() {
		if (this.threadGame == null)
			this.threadGame = new ThreadGame(new RunGameInstance());
	}

	//

	// previously was ThreadGame_GameRunner_E1
	protected class RunGameInstance implements ThreadGame.TGRunnable {
		boolean isWorking = true;

		@Override
		public void run() {
			while(isWorking) {
				runGameCycle();
			}
		}

		@Override
		public void stopAndDie() {
			this.isWorking = false;
		}
	}
}