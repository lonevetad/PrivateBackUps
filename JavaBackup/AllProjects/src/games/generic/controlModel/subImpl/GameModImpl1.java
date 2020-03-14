package games.generic.controlModel.subImpl;

import games.generic.controlModel.GameController;
import games.generic.controlModel.GameEventManager;
import games.generic.controlModel.GameModality;
import games.generic.controlModel.GameModel;

public class GameModImpl1 extends GameModality implements IGameModalityTimeBased, IGameModalityEventBased {
	GameEventManager eventManager;

	public GameModImpl1(GameController controller, String modalityName) {
		super(controller, modalityName);
	}

	//

	protected GameModelTimeBased getModelTimeBased() {
		return (GameModelTimeBased) model;
	}

	//

	//

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		this.eventManager = newEventManager();
	}

	@Override
	public GameEventManager newEventManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameModel newGameModel() {
		return null;
	}

	@Override
	public void startGame() {

	}

	//

	@Override
	public void doOnEachCycle(long millisecToElapse) {
		progressEnlapsedTIme(millisecToElapse);
	}

	@Override
	public void progressEnlapsedTIme(long millisecToElapse) {
		this.getModelTimeBased().getTimedObjects().forEach((id, to) -> {
			to.act(millisecToElapse); // fai progredire QUALSIASI cosa: abilità che si ricaricano col tempo,
										// rigenerazioni, movimento di proiettili e cose, etc
		});
	}

}