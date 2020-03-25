package games.theRisingAngel.abilities;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.inventory.AbilityModifyingAttributeRealTime;
import games.generic.controlModel.inventory.EquipmentItem;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;

/* TODO
 * E collana che da rigenerazione vitale pari al 25% del danno subito, ma ogni
		 * secondo tale ammontare cala fino a 0 (quindi ad ogni evento del danno,
		 * incrementa il contatore del totale, poi ogni secondo scala di es 4 e aggiorna
		 * le statistiche)
 * */
public class AMoreDamageReceivedMoreLifeRegen extends AbilityModifyingAttributeRealTime implements GEventObserver {

	public AMoreDamageReceivedMoreLifeRegen() {
		// TODO Auto-generated constructor stub
	}

	public AMoreDamageReceivedMoreLifeRegen(AttributeIdentifier attributeModified) {
		super(attributeModified);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer getObserverID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAttributeModifiersAmount(GModality gm, EquipmentItem ei, CreatureOfRPGs ah,
			CreatureAttributes ca) {
		// TODO Auto-generated method stub

	}

}
