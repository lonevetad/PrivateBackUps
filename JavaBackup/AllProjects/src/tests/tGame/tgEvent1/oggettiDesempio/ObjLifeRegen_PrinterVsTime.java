package tests.tGame.tgEvent1.oggettiDesempio;

import java.util.ArrayList;
import java.util.List;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.inventory.AbilityModifyingAttributeRealTime;
import games.generic.controlModel.inventory.AttributeModification;
import games.generic.controlModel.inventory.EquipmentItem;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;

/**
 * E collana che da rigenerazione vitale pari al 25% del danno subito, ma ogni
 * secondo tale ammontare cala fino a 0 (quindi ad ogni evento del danno,
 * incrementa il contatore del totale, poi ogni secondo scala di es 4 e aggiorna
 * le statistiche)
 */
public class ObjLifeRegen_PrinterVsTime extends AbilityModifyingAttributeRealTime implements GEventObserver {

	public ObjLifeRegen_PrinterVsTime() {
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(this.getAttributeToModify().getAttributeModified().getName());
	}

	protected List<String> eventsWatching;

	public ObjLifeRegen_PrinterVsTime(AttributeIdentifier attributeModified) {
		super(attributeModified);
	}

	@Override
	public Integer getObserverID() {
		return ID;
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		EventPrinter ep;
		AttributeModification am;
		if (EventPrinter.PRINTER_EVENT_NAME == ge.getName()) {
			ep = (EventPrinter) ge;
			am = this.getAttributeToModify();
			am.setValue(am.getValue());
		}
	}

	@Override
	public void updateAttributeModifiersAmount(GModality gm, EquipmentItem ei, CreatureOfRPGs ah,
			CreatureAttributes ca) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getEventsWatching() {
		// TODO Auto-generated method stub
		return null;
	}
}