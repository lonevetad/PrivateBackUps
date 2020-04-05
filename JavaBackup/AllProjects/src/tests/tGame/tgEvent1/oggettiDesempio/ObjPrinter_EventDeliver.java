package tests.tGame.tgEvent1.oggettiDesempio;

import games.generic.controlModel.GModality;
import games.generic.controlModel.subimpl.GModalityET;
import tests.tGame.tgEvent1.GEventInterface_E1;

public class ObjPrinter_EventDeliver extends ObjPrinterTO {

	public ObjPrinter_EventDeliver(long timeThreshold, String text) {
		super(timeThreshold, text);
	}

	@Override
	public void executeAction(GModality modality) {
		GEventInterface_E1 ei;
		System.out.println("sending event with tect: " + this.text);
		ei = (GEventInterface_E1) ((GModalityET) modality).getEventInterface();
		ei.firePrinterEvent(this.text);
//		modality.fireEvent(new EventPrinter(this.text));
	}
}