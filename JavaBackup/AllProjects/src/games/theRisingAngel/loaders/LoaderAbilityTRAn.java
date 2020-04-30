package games.theRisingAngel.loaders;

import games.generic.controlModel.GController;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gEvents.EventDamage;
import games.generic.controlModel.inventoryAbil.AbilityGeneric;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.ASimpleFixedBufferVanishing;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.subimpl.LoaderAbilities;
import games.theRisingAngel.GModalityTRAn;
import games.theRisingAngel.abilities.ADamageReductionCurrencyBased;
import games.theRisingAngel.abilities.AFireShpereOrbiting;
import games.theRisingAngel.abilities.AHealingMakesEarnBaseCurrency;
import games.theRisingAngel.abilities.AMoreDamageReceivedMoreLifeRegen;
import games.theRisingAngel.abilities.ARandomScatteringOrbs;
import games.theRisingAngel.abilities.ARandomScatteringOrbsIMpl;
import games.theRisingAngel.abilities.AShieldingButWeakining;
import games.theRisingAngel.events.EventsTRAn;
import games.theRisingAngel.misc.AttributesTRAn;
import games.theRisingAngel.misc.DamageTypesTRAn;

public class LoaderAbilityTRAn extends LoaderAbilities {

	public LoaderAbilityTRAn(GameObjectsProvider<AbilityGeneric> objProvider) {
		super(objProvider);
	}

	@Override
	public void loadInto(GController gcontroller) {
		objProvider.addObj(ADamageReductionCurrencyBased.NAME + DamageTypesTRAn.Physical.getName(), 3,
				gc -> new ADamageReductionCurrencyBased(DamageTypesTRAn.Physical));
		objProvider.addObj(ADamageReductionCurrencyBased.NAME + DamageTypesTRAn.Magical.getName(), 3,
				gc -> new ADamageReductionCurrencyBased(DamageTypesTRAn.Magical));
		objProvider.addObj(AMoreDamageReceivedMoreLifeRegen.NAME, 3, gc -> new AMoreDamageReceivedMoreLifeRegen());
		objProvider.addObj(AFireShpereOrbiting.NAME, 4, gc -> new AFireShpereOrbiting());
		objProvider.addObj(AShieldingButWeakining.NAME, 2, gm -> new AShieldingButWeakining());
		objProvider.addObj(AHealingMakesEarnBaseCurrency.NAME, 4, gm -> new AHealingMakesEarnBaseCurrency());
		objProvider.addObj(ARandomScatteringOrbs.NAME, gm -> new ARandomScatteringOrbsIMpl((GModalityTRAn) gm));
		objProvider.addObj("Wounded Berseker", 3, gm -> {
			ASimpleFixedBufferVanishing a;
			a = ASimpleFixedBufferVanishing.newInstance("Wounded Berseker", true,
					new AttributeModification[] { new AttributeModification(AttributesTRAn.Strength, 8),
							new AttributeModification(AttributesTRAn.Constitution, 5),
							new AttributeModification(AttributesTRAn.Intelligence, -6),
							new AttributeModification(AttributesTRAn.Wisdom, -4) });
			a.addEventWatched(EventsTRAn.DamageReceived);
			a.setAbilityEffectDuration(5000);
			a.setVanishingEffectDuration(3000);
			a.setRarityIndex(3);
			a.setMaxAmountStackedTriggerCharges(5);
			return a;
		});
		objProvider.addObj("Frenzy for a miss", 2, gm -> {
			ASimpleFixedBufferVanishing a;
			a = new ASimpleFixedBufferVanishing("Frenzy for a miss",
					new AttributeModification[] { new AttributeModification(AttributesTRAn.Strength, 6),
							new AttributeModification(AttributesTRAn.ProbabilityHitPhysical, 4), // frenzy
							new AttributeModification(AttributesTRAn.ProbabilityHitMagical, 4), // frenzy
							new AttributeModification(AttributesTRAn.Dexterity, -5),
							new AttributeModification(AttributesTRAn.Intelligence, -6),
							new AttributeModification(AttributesTRAn.Wisdom, -7) }) {
				private static final long serialVersionUID = 777962548965262L;

				@Override
				protected boolean isAcceptableEvent(IGEvent e) {
					return EventsTRAn.DamageMissed.getName() == e.getName() && //
					this.getOwner() == ((EventDamage) e).getSource(); // I am the one who missed the attack?
				}
			};
			a.setCumulative(false);
			a.addEventWatched(EventsTRAn.DamageMissed);
			a.setAbilityEffectDuration(4000);
			a.setVanishingEffectDuration(2000);
			a.setRarityIndex(2);
			a.setMaxAmountStackedTriggerCharges(5);
			return a;
		});
		objProvider.addObj("Immunoadrenaline", 2, gm -> {
			ASimpleFixedBufferVanishing a;
			a = ASimpleFixedBufferVanishing.newInstance("Immunoadrenaline", false,
					new AttributeModification[] { new AttributeModification(AttributesTRAn.DamageReductionMagical, -10),
							new AttributeModification(AttributesTRAn.DamageReductionPhysical, -10),
							new AttributeModification(AttributesTRAn.Velocity,
									GModalityTRAn.SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE_TRAN), // frenzy
							new AttributeModification(AttributesTRAn.RigenLife, 10) });
			a.addEventWatched(EventsTRAn.DamageReceived);
			a.setAbilityEffectDuration(2000);
			a.setVanishingEffectDuration(0);
			a.setRarityIndex(2);
			return a;
		});
	}
}