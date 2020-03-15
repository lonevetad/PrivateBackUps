package tests.tGame.tgEvent1;

import games.generic.controlModel.GameController;
import games.generic.controlModel.GameEventManager;
import games.generic.controlModel.GameModel;
import games.generic.controlModel.subImpl.GameEventManagerFineGrained;
import games.generic.controlModel.subImpl.GameModalityET;

public class GModality_E1 extends GameModalityET {

	public GModality_E1(GameController controller, String modalityName) {
		super(controller, modalityName);
	}

	Thread threadGame;

	@Override
	public void onCreate() {
		this.threadGame = new Thread(this::runGame);

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
		this.threadGame.start();
	}

	@Override
	public void closeAll() {
		super.closeAll();
		this.threadGame = null;
	}

}
