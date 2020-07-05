package tests.tGame.tgEvent1.oggettiDesempio;

import java.util.ArrayList;
import java.util.List;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gObj.GameObjectGeneric;
import tools.UniqueIDProvider;

public class ObserverPrinterEvent implements GEventObserver, GameObjectGeneric {
	protected Integer ID;
	protected List<String> eventsWatching;

	public ObserverPrinterEvent() {
		this.ID = UniqueIDProvider.GENERAL_UNIQUE_ID_PROVIDER.getNewID();
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(EventPrinter.PRINTER_EVENT_NAME);
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		EventPrinter ep;
		ep = (EventPrinter) ge;
		System.out.println("ObsPE receives a message: " + ep.getText());
	}

	@Override
	public Integer getID() { return ID; }

	@Override
	public List<String> getEventsWatching() { return this.eventsWatching; }

	@Override
	public String getName() { return "ObserverPrinterEvent"; }

	@Override
	public void onAddedToGame(GModality gm) {}

	@Override
	public void onRemovedFromGame(GModality gm) {}
}