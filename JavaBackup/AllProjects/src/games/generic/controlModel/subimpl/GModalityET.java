package games.generic.controlModel.subimpl;

import games.generic.controlModel.GController;
import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GEventManager;
import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GModel;
import games.generic.controlModel.gObj.TimedObject;

/**
 * Game modality based on real time and events.
 * <p>
 * ALL {@link GEvent}-firing methods are delegated to {@link GEventInterface}
 * (obtained through {@link GModalityET#getEventInterface()}) to make this class
 * thinner.
 * <p>
 * Useful classes/interfaces used here:
 * <ul>
 * <li>{@link }></li>
 * </ul>
 */
public abstract class GModalityET extends GModality implements IGameModalityTimeBased, IGameModalityEventBased {

	protected GEventInterface eventInterface;

	public GModalityET(GController controller, String modalityName) {
		super(controller, modalityName);
	}

	//

	@Override
	public GModelTimeBased getModelTimeBased() {
		return (GModelTimeBased) model;
	}

	/** Access ALL {@link GEvent}-firing methods through this instance. */
	@Override
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

	public GModelET getGModelEventTimedObjectsHolder() {
		return (GModelET) this.getModel();
	}

	//

	@Override
	public void setEventInterface(GEventInterface eventInterface) {
		this.eventInterface = eventInterface;
		this.getGModelEventTimedObjectsHolder().setEventManager(eventInterface.getGameEventManager());
	}

	//
	@Override

	public void onCreate() {
		this.eventInterface = newEventInterface();
		super.onCreate();
		this.getGModelEventTimedObjectsHolder().setEventManager(getEventManager());
	}

	@Override
	public GModel newGameModel() {
		return new GModelET();
	}

	//

	@Override
	public void doOnEachCycle(int millisecToElapse) {
		progressElapsedTime(millisecToElapse);
	}

	@Override
	public void progressElapsedTime(final int millisecToElapse) {
		GEventManager gem;
		this.getModelTimeBased().forEachTimedObject((to) -> {
			to.act(this, millisecToElapse); // fai progredire QUALSIASI cosa: abilità che si ricaricano col tempo,
			// rigenerazioni, movimento di proiettili e cose, etc
		});
		gem = this.eventInterface.getGameEventManager();
		if (gem != null)
			gem.performAllEvents();
	}

	//

// TODO objects handlers

	/**
	 * Proxy-like method.
	 */
	public void addTimedObject(TimedObject to) {
		this.getModelTimeBased().addTimedObject(to);
	}

	/**
	 * Proxy-like method.
	 */
	public void addEventObserver(GEventObserver geo) {
		this.getEventManager().addEventObserver(geo);
	}

//	public void fireEvent(GEvent event) { this.getEventManager(). }
}