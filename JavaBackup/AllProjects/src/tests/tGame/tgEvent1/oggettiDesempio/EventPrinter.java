package tests.tGame.tgEvent1.oggettiDesempio;

import games.generic.controlModel.subImpl.GEvent;

public class EventPrinter extends GEvent {
	public static final String PRINTER_EVENT_TYPE = "PRINTER";
	protected String text;

	public EventPrinter(String text) {
		super();
		this.text = text;
	}

	//

	public String getText() {
		return text;
	}

	@Override
	public String getType() {
		return PRINTER_EVENT_TYPE;
	}

	@Override
	public String toString() {
		return "EventPrinter [ID=" + ID + ", getType()=" + getType() + ", text=" + text + "]";
	}
}