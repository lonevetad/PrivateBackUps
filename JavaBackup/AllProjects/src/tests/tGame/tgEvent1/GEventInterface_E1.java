package tests.tGame.tgEvent1;

import games.theRisingAngel.events.GEventInterfaceTRAr;
import tests.tGame.tgEvent1.oggettiDesempio.EventPrinter;

public class GEventInterface_E1 extends GEventInterfaceTRAr {

	public void firePrinterEvent(String text) {
		System.out.println("--- firing printer event with: " + text);
		this.getGameEventManager().addEvent(new EventPrinter(text));
	}
}