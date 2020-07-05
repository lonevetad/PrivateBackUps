package games.generic.controlModel.inventoryAbil.abilitiesImpl;

import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.inventoryAbil.AttributeModification;

public class ASimpleFixedBufferVanishing extends AbilityAttributesModsVanishingOverTime {
	private static final long serialVersionUID = 6560887921022805536L;

	public static ASimpleFixedBufferVanishing newInstance(String name, boolean isCumulative,
			AttributeModification[] attributesMods) {
		ASimpleFixedBufferVanishing a;
		a = new ASimpleFixedBufferVanishing(name, attributesMods);
		a.setCumulative(isCumulative);
		return a;
	}

	public ASimpleFixedBufferVanishing(String name, AttributeModification[] attributesMods) {
		super(name, attributesMods);
	}

	protected int abilityEffectDuration, vanishingEffectDuration;

	//

	@Override
	public int getAbilityEffectDuration() { return abilityEffectDuration; }

	@Override
	public int getVanishingEffectDuration() { return vanishingEffectDuration; }

	@Override
	public void setAbilityEffectDuration(int abilityEffectDuration) {
		this.abilityEffectDuration = abilityEffectDuration;
	}

	@Override
	public void setVanishingEffectDuration(int vanishingEffectDuration) {
		this.vanishingEffectDuration = vanishingEffectDuration;
	}

	@Override
	public ASimpleFixedBufferVanishing addEventWatched(ObjectNamed objNamed) {
		this.eventsWatching.add(objNamed.getName());
		return this;
	}

	@Override
	public void doUponAbilityRefreshed() {}

	@Override
	public void doUponAbilityStartsVanishing() {}

	@Override
	public int getVanishingTimeThresholdUpdate() { return 0; }
}