package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AbilityModifyingAttributesRealTime;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
import games.theRisingAngel.misc.AttributesTRAn;
import games.theRisingAngel.misc.CreatureAttributesTRAn;

/**
 * Reduces all resources' regeneration to 1 and add a portion of it (for
 * instance, the half of that amounts), capped to 50% as final value, to that
 * resource leech. <br>
 * BEWARE: if the regeneration was negative, then is set to 1 but the resource
 * leech could became negative too.
 */
public class ARegenToLeech extends AbilityModifyingAttributesRealTime {
	private static final long serialVersionUID = -5649806420997L;
	public static final String NAME = "Mors tua vita mea";
	public static final int RARITY = 2, CAP_PERCENTAGE = 50;
	protected static final AttributesTRAn[][] rigenToLeetchPairs = {
			{ AttributesTRAn.RegenLife, AttributesTRAn.LifeLeechPercentage },
			{ AttributesTRAn.RegenMana, AttributesTRAn.ManaLeechPercentage }

	};

	public ARegenToLeech() {
		super(NAME, new AttributeIdentifier[] { AttributesTRAn.RegenLife, AttributesTRAn.RegenMana,
				AttributesTRAn.LifeLeechPercentage, AttributesTRAn.ManaLeechPercentage, });
	}

	@Override
	public void updateAttributesModifiersValues(GModality gm, EquipmentItem ei, CreatureSimple ah,
			CreatureAttributes ca) {
		int regen, i, n;
		CreatureAttributesTRAn cat;
		AttributesTRAn[] pair;
		cat = (CreatureAttributesTRAn) ca;
		n = rigenToLeetchPairs.length;
		i = -1;
		while (++i < n) {
			pair = rigenToLeetchPairs[i];
			// pick the original value plus the base-attributes gaining
			regen = cat.getOriginalValue(pair[0].getIndex()) - 1;
			this.attributesToModify[i << 1].setValue(-regen);
			// recycle regen as temporary variable for leech
			regen >>= 1;
			if (regen == 0) {
				regen = 1; // just do not waste life regeneration
			} else {
				if (regen > CAP_PERCENTAGE)
					regen = CAP_PERCENTAGE;
				else if (regen < -CAP_PERCENTAGE)
					regen = -CAP_PERCENTAGE; // eh eh, beware of negatives
			}
			this.attributesToModify[1 + (i << 1)].setValue(regen);
		}
	}
}