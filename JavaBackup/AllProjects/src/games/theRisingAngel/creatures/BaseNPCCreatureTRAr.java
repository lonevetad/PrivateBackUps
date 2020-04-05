package games.theRisingAngel.creatures;

import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.subimpl.CreatureAttributesModsCaching;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.AttributesTRAr;
import games.theRisingAngel.CreatureUIDProvider;
import games.theRisingAngel.inventory.EquipmentSetTRAr;

/**
 * This is NOT a {@link PlayerGeneric}, even if it's similar (but there's no
 * multiple inheritance, so ... interfaces and redundancy).
 */
public class BaseNPCCreatureTRAr extends BaseCreatureTRAr {
	private static final long serialVersionUID = 1L;

	public BaseNPCCreatureTRAr(GModalityRPG gModRPG, String name) {
		super(gModRPG, name);
		this.isDestroyed = false;
		this.ID = CreatureUIDProvider.newID();
		this.equipmentSet = new EquipmentSetTRAr();
		this.equipmentSet.setCreatureWearingEquipments(this);
		this.attributes = new CreatureAttributesModsCaching(AttributesTRAr.VALUES.length);
		this.life = 1;
		ticks = 0;
		accumulatedTimeLifeRegen = 0;
	}

	//

	//

}