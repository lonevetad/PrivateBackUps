package games.generic.controlModel.subImpl;

import java.util.Map;

import games.generic.controlModel.GameController;
import games.generic.controlModel.GameEventManager;
import games.generic.controlModel.GameModality;
import games.generic.controlModel.TimedObject;

public abstract class GameModalityET extends GameModality implements IGameModalityTimeBased, IGameModalityEventBased {
	GameEventManager eventManager;
	protected Map<Integer, TimedObject> timedObjects;

	public GameModalityET(GameController controller, String modalityName) {
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
		this.eventManager = newEventManager();
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

	//

// TODO objects handlers

	/** Proxy-like method */
	public void addTimedObject(TimedObject to) {
		this.getModelTimeBased().addTimeProgressingObject(to);
	}
}