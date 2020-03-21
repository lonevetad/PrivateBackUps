package tests.tGame.tgEvent1.oggettiDesempio;

import games.generic.controlModel.GEvent;

public class EventPrinter extends GEvent {

	public EventPrinter() {
		super();
	}

	public EventPrinter(Integer iD) {
		super(iD);
	}

	@Override
	public String getType() {
		return "PRINTER";
	}
}