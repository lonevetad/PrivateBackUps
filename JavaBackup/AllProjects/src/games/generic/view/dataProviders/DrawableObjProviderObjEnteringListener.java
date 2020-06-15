package games.generic.view.dataProviders;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.view.GameView;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.ObjectNamedID;
import tools.UniqueIDProvider;

/** Instead of simply scanning, just ... deprecated */
@Deprecated
public abstract class DrawableObjProviderObjEnteringListener extends DrawableObjProvider implements GEventObserver {
	protected static final UniqueIDProvider idProvider = UniqueIDProvider.newBasicIDProvider();

	public DrawableObjProviderObjEnteringListener(GameView gameView) {
		super(gameView);
		this.ID = idProvider.getNewID();
		eventsListened = new ArrayList<String>();
		eventsListened.add(getIdentifierObjEnteredInGame().getName());
		eventsListened.add(getIdentifierObjRemovedFromGame().getName());
	}

	protected Integer ID;
	protected List<String> eventsListened;

	@Override
	public Integer getID() { return ID; }

	@Override
	public List<String> getEventsWatching() { // TODO Auto-generated method stub
		return eventsListened;
	}

	//

	public abstract ObjectNamedID getIdentifierObjEnteredInGame();

	public abstract ObjectNamedID getIdentifierObjRemovedFromGame();

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) { // TODO Auto-generated method stub
	}

	@Override
	public void forEachObjInArea(AbstractShape2D shape, Consumer<ObjectLocated> action) {}

}
