package games.old;

import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.subimpl.CreatureAttributesCaching;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.creatures.BaseCreatureTRAn;
import games.theRisingAngel.misc.AttributesTRAn;

/**
 * This is NOT a {@link PlayerGeneric}, even if it's similar (but there's no
 * multiple inheritance, so ... interfaces and redundancy).
 */
@Deprecated
public class BaseNPCCreatureTRAr extends BaseCreatureTRAn {
	private static final long serialVersionUID = 1L;

	public BaseNPCCreatureTRAr(GModalityRPG gModRPG, String name) {
		super(gModRPG, name);
		this.isDestroyed = false;
		this.attributes = new CreatureAttributesCaching(AttributesTRAn.VALUES.length);
		this.life = 1;
	}

	//

	//

}