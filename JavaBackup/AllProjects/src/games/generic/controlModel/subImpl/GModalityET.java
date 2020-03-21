package games.generic.controlModel.subImpl;

import java.util.Map;

import games.generic.controlModel.GController;
import games.generic.controlModel.GEvent;
import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GEventManager;
import games.generic.controlModel.GModality;
import games.generic.controlModel.gameObj.TimedObject;

/**
 * Game modality based on real time and events.
 * <p>
 * ALL {@link GEvent}-firing methods are delegated to {@link GEventInterface}
 * (obtained through {@link GModalityET#getEventInterface()}) to make this class
 * thinner.
 */
public abstract class GModalityET extends GModality implements IGameModalityTimeBased, IGameModalityEventBased {
	protected GEventInterface eventInterface;
	protected Map<Integer, TimedObject> timedObjects;

	public GModalityET(GController controller, String modalityName) {
		super(controller, modalityName);
	}

	//

	protected GameModelTimeBased getModelTimeBased() {
		return (GameModelTimeBased) model;
	}

	//

	//

	/** Access ALL {@link GEvent}-firing methods through this instance. */
	public GEventInterface getEventInterface() {
		return eventInterface;
	}

	/**
	 * Should not be used, use with caution or use
	 * {@link GModalityET#getEventInterface()}) instead.
	 */
	public GEventManager getEventManager() {
		return eventInterface.getGameEventManager();
	}

	@Override
	public void onCreate() {
		this.eventInterface = newEventInterface();
	}

	//

	@Override
	public void doOnEachCycle(long millisecToElapse) {
		progressEnlapsedTIme(millisecToElapse);
	}

	@Override
	public void progressEnlapsedTIme(final long millisecToElapse) {
		this.getModelTimeBased().forEachTimedObject((to) -> {
			to.act(this, millisecToElapse); // fai progredire QUALSIASI cosa: abilità che si ricaricano col tempo,
			// rigenerazioni, movimento di proiettili e cose, etc
		});
		this.eventInterface.getGameEventManager().performAllEvents();
	}

	//

// TODO objects handlers

	/** Proxy-like method */
	public void addTimedObject(TimedObject to) {
		this.getModelTimeBased().addTimedObject(to);
	}
}