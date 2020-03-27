package tests.tGame.tgEvent1.oggettiDesempio;

import java.util.ArrayList;
import java.util.List;

import games.generic.UniqueIDProvider;
import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;

public class ObserverPrinterEvent implements GEventObserver {
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
	public Integer getID() {
		return ID;
	}

	@Override
	public List<String> getEventsWatching() {
		return this.eventsWatching;
	}
}