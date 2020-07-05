package games.theRisingAngel.abilities;

import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.ASimpleFixedBufferVanishing;
import games.theRisingAngel.GModalityTRAn;

public class ASimpleFixedBufferVanishingTRAn extends ASimpleFixedBufferVanishing {
	private static final long serialVersionUID = -5605240560L;
	public static final String NAME = "Wounded Berseker";
	public static final int RARITY = 2;

	public static ASimpleFixedBufferVanishingTRAn newInstanceTRAn(String name, boolean isCumulative,
			AttributeModification[] attributesMods) {
		ASimpleFixedBufferVanishingTRAn a;
		a = new ASimpleFixedBufferVanishingTRAn(name, attributesMods);
		a.setCumulative(isCumulative);
		return a;
	}

	public ASimpleFixedBufferVanishingTRAn(String name, AttributeModification[] attributesMods) {
		super(name, attributesMods);
	}

	@Override
	public int getVanishingTimeThresholdUpdate() { return GModalityTRAn.TIME_SUBUNITS_EACH_TIME_UNIT_TRAn; }
}