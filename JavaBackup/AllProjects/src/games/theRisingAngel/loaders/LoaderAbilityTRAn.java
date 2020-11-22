package games.theRisingAngel.loaders;

import java.util.function.Consumer;

import games.generic.controlModel.GController;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gEvents.EventDamage;
import games.generic.controlModel.gObj.DamageDealerGeneric;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.inventoryAbil.AbilityGeneric;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AbilityBonusDependingOnOtherBonuses;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.FactoryObjGModalityBased;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.subimpl.LoaderAbilities;
import games.theRisingAngel.GModalityTRAn;
import games.theRisingAngel.abilities.AAttrSingleBonusMalusRandomFixed;
import games.theRisingAngel.abilities.AAttrSingleBonusMalusRandomPercentage;
import games.theRisingAngel.abilities.ADamageReductionCurrencyBased;
import games.theRisingAngel.abilities.ADamageReductionOnLifeLowerToPhysicalAttributes;
import games.theRisingAngel.abilities.AFireShpereOrbiting;
import games.theRisingAngel.abilities.ALifeHealingMakesEarnBaseCurrency;
import games.theRisingAngel.abilities.ALoseManaBeforeLife;
import games.theRisingAngel.abilities.AMeditationMoreRegen;
import games.theRisingAngel.abilities.AMoreDamageReceivedMoreLifeRegen;
import games.theRisingAngel.abilities.AProtectButMakesSoft;
import games.theRisingAngel.abilities.ARandomScatteringOrbs;
import games.theRisingAngel.abilities.ARandomScatteringOrbsImpl;
import games.theRisingAngel.abilities.ARegenToLeech;
import games.theRisingAngel.abilities.AShieldingButWeakining;
import games.theRisingAngel.abilities.AShieldingEachCurableResources;
import games.theRisingAngel.abilities.ASimpleFixedBufferVanishingTRAn;
import games.theRisingAngel.abilities.AVampireBerserker;
import games.theRisingAngel.events.EventsTRAn;
import games.theRisingAngel.misc.AttributesTRAn;

public class LoaderAbilityTRAn extends LoaderAbilities {

	public LoaderAbilityTRAn(GameObjectsProvider<AbilityGeneric> objProvider) { super(objProvider); }

	@Override
	public void loadInto(GController gcontroller) {
//		objProvider.addObj(ADamageReductionCurrencyBased.NAME + DamageTypesTRAn.Physical.getName(),
//				ADamageReductionCurrencyBased.RARITY,
//				gc -> new ADamageReductionCurrencyBased(DamageTypesTRAn.Physical));
//		objProvider.addObj(ADamageReductionCurrencyBased.NAME + DamageTypesTRAn.Magical.getName(),
//				ADamageReductionCurrencyBased.RARITY, gc -> new ADamageReductionCurrencyBased(DamageTypesTRAn.Magical));
		objProvider.addObj(AMoreDamageReceivedMoreLifeRegen.NAME, AMoreDamageReceivedMoreLifeRegen.RARITY,
				gc -> new AMoreDamageReceivedMoreLifeRegen());
		objProvider.addObj(AFireShpereOrbiting.NAME, AFireShpereOrbiting.RARITY, gc -> new AFireShpereOrbiting());
		objProvider.addObj(AShieldingButWeakining.NAME, AShieldingButWeakining.RARITY,
				gm -> new AShieldingButWeakining());
		objProvider.addObj(ALifeHealingMakesEarnBaseCurrency.NAME, ALifeHealingMakesEarnBaseCurrency.RARITY,
				gm -> new ALifeHealingMakesEarnBaseCurrency());
		objProvider.addObj(ARandomScatteringOrbs.NAME, gm -> new ARandomScatteringOrbsImpl((GModalityTRAn) gm));
		objProvider.addObj("Wounded Berseker", 3, gm -> {
			ASimpleFixedBufferVanishingTRAn a;
			a = new ASimpleFixedBufferVanishingTRAn("Wounded Berseker",
					new AttributeModification[] { new AttributeModification(AttributesTRAn.Strength, 8),
							new AttributeModification(AttributesTRAn.Defense, 1),
							new AttributeModification(AttributesTRAn.Constitution, 4),
							new AttributeModification(AttributesTRAn.Intelligence, -6),
							new AttributeModification(AttributesTRAn.Wisdom, -4) }) {
				private static final long serialVersionUID = 2588519748901517L;

				@Override
				protected boolean isAcceptableEvent(IGEvent e) {
					return EventsTRAn.DamageReceived.getName() == e.getName() && //
					// I am the receiver?
					((EventDamage) e).isTarget((LivingObject) this.getOwner());
				}
			};
			a.addEventWatched(EventsTRAn.DamageReceived);
			a.setCumulative(true);
			a.setAbilityEffectDuration(5000);
			a.setVanishingEffectDuration(3000);
			a.setRarityIndex(3);
			a.setMaxAmountStackedTriggerCharges(5);
			return a;
		});
		objProvider.addObj("Frenzy for a miss", 2, gm -> {
			ASimpleFixedBufferVanishingTRAn a;
			a = new ASimpleFixedBufferVanishingTRAn("Frenzy for a miss",
					new AttributeModification[] { new AttributeModification(AttributesTRAn.Strength, 6),
							new AttributeModification(AttributesTRAn.ProbabilityPerThousandHitPhysical, 4), // frenzy
							new AttributeModification(AttributesTRAn.ProbabilityPerThousandHitMagical, 4), // frenzy
							new AttributeModification(AttributesTRAn.Dexterity, -5),
							new AttributeModification(AttributesTRAn.Intelligence, -6),
							new AttributeModification(AttributesTRAn.Wisdom, -7) }) {

				private static final long serialVersionUID = 777962548965262L;

				@Override
				protected boolean isAcceptableEvent(IGEvent e) {
					return EventsTRAn.DamageMissed.getName() == e.getName() && //
					// I am the one who missed the attack?
					((EventDamage) e).isSource((DamageDealerGeneric) this.getOwner());
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
			ASimpleFixedBufferVanishingTRAn a;
			a = new ASimpleFixedBufferVanishingTRAn("Immunoadrenaline",
					new AttributeModification[] { new AttributeModification(AttributesTRAn.DamageReductionMagical, -10),
							new AttributeModification(AttributesTRAn.DamageReductionPhysical, -10),
							new AttributeModification(AttributesTRAn.Velocity,
									GModalityTRAn.SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE_TRAN), // frenzy
							new AttributeModification(AttributesTRAn.RegenLife, 10) }) {
				private static final long serialVersionUID = 2588519748901515L;

				@Override
				protected boolean isAcceptableEvent(IGEvent e) {
					return EventsTRAn.DamageReceived.getName() == e.getName() && //
					// I am the receiver?
					((EventDamage) e).isTarget((LivingObject) this.getOwner());
				}
			};
			a.setCumulative(false);
			a.addEventWatched(EventsTRAn.DamageReceived);
			a.setAbilityEffectDuration(2000);
			a.setVanishingEffectDuration(1000);
			a.setRarityIndex(2);
			return a;
		});
		objProvider.addObj("Bloodlust", 3, gm -> {
			ASimpleFixedBufferVanishingTRAn a;
			a = new ASimpleFixedBufferVanishingTRAn("Bloodlust",
					new AttributeModification[] { new AttributeModification(AttributesTRAn.Strength, 4),
							new AttributeModification(AttributesTRAn.Health, 4),
							new AttributeModification(AttributesTRAn.RegenLife, 1),
							new AttributeModification(AttributesTRAn.Velocity, // frenzy
									GModalityTRAn.SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE_TRAN >> 2),
							new AttributeModification(AttributesTRAn.Intelligence, -1),
							new AttributeModification(AttributesTRAn.Wisdom, -3),
							new AttributeModification(AttributesTRAn.RegenMana, 2) }) {
				private static final long serialVersionUID = 287962548965262L;

				@Override
				protected boolean isAcceptableEvent(IGEvent e) {
					return EventsTRAn.DamageInflicted.getName() == e.getName() && //
					// I am the one who missed the attack?
					((EventDamage) e).isSource((DamageDealerGeneric) this.getOwner());
				}
			};
			a.setCumulative(true);
			a.addEventWatched(EventsTRAn.DamageInflicted);
			a.setAbilityEffectDuration(2000);
			a.setVanishingEffectDuration(2500);
			a.setRarityIndex(3);
			a.setMaxAmountStackedTriggerCharges(7);
			return a;
		});
		objProvider.addObj(ALoseManaBeforeLife.NAME, ALoseManaBeforeLife.RARITY, gm -> new ALoseManaBeforeLife());
		objProvider.addObj(AShieldingEachCurableResources.NAME, AShieldingEachCurableResources.RARITY,
				gm -> new AShieldingEachCurableResources());
		objProvider.addObj(AVampireBerserker.NAME, AVampireBerserker.RARITY, gm -> new AVampireBerserker());
		objProvider.addObj(ARegenToLeech.NAME, ARegenToLeech.RARITY, gm -> new ARegenToLeech());
		objProvider.addObj(AAttrSingleBonusMalusRandomFixed.NAME, AAttrSingleBonusMalusRandomFixed.RARITY,
				gm -> new AAttrSingleBonusMalusRandomFixed());
		objProvider.addObj(AAttrSingleBonusMalusRandomPercentage.NAME, AAttrSingleBonusMalusRandomPercentage.RARITY,
				gm -> new AAttrSingleBonusMalusRandomPercentage());

		//
		forEachLevel_ZeroToMaz(ml -> {
			objProvider.addObj(AMeditationMoreRegen.NAME + ml, ml,
//					((Function<Integer, FactoryObjGModalityBased<AbilityGeneric>>) (level -> {
//						return gm -> new AMeditationMoreRegen(level);
//					})).apply(maxLevel) // moved to a function because reminds TOO MUCH to JavaScript ...
					newAMeditationMoreRegen_LevelBased(ml)//
			);
			objProvider.addObj(ADamageReductionCurrencyBased.NAME + ml, ml,
					gm -> new ADamageReductionCurrencyBased(ml));

			// other ones using applicable to a set of levels/rarities/ ?
		});
		objProvider.addObj(AProtectButMakesSoft.NAME, AProtectButMakesSoft.RARITY, gm -> new AProtectButMakesSoft());

		objProvider.addObj("Mag(ic)netic Dynamo", 3, gm -> {
			AbilityBonusDependingOnOtherBonuses a;
			a = new AbilityBonusDependingOnOtherBonuses("Mag(ic)netic Dynamo", //
					new AttributeIdentifier[] { AttributesTRAn.DamageBonusMagical,
							AttributesTRAn.DamageReductionMagical, AttributesTRAn.ShieldMax,
							AttributesTRAn.RegenShield }//
			, new AttributeIdentifier[][] { //
					new AttributeIdentifier[] { AttributesTRAn.RegenShield }, //
					new AttributeIdentifier[] { AttributesTRAn.ShieldMax }, //
					new AttributeIdentifier[] { AttributesTRAn.DamageReductionMagical }, //
					new AttributeIdentifier[] { AttributesTRAn.DamageBonusMagical }//
			});
			a.setRarityIndex(3);
			return a;
		});
		objProvider.addObj("Muscles Meat", 3, gm -> {
			AbilityBonusDependingOnOtherBonuses a;
			a = new AbilityBonusDependingOnOtherBonuses("Muscles Meat", //
					new AttributeIdentifier[] { AttributesTRAn.DamageBonusPhysical,
							AttributesTRAn.DamageReductionPhysical, AttributesTRAn.LifeMax, AttributesTRAn.RegenLife }//
			, new AttributeIdentifier[][] { //
					new AttributeIdentifier[] { AttributesTRAn.RegenLife }, //
					new AttributeIdentifier[] { AttributesTRAn.LifeMax }, //
					new AttributeIdentifier[] { AttributesTRAn.DamageReductionPhysical }, //
					new AttributeIdentifier[] { AttributesTRAn.DamageBonusPhysical }//
			});
			a.setRarityIndex(3);
			return a;
		});
		objProvider.addObj("Assassin's Instinct", 3, gm -> {
			AbilityBonusDependingOnOtherBonuses a;
			a = new AbilityBonusDependingOnOtherBonuses("Assassin's Instinct", //
					new AttributeIdentifier[] { AttributesTRAn.ProbabilityPerThousandHitPhysical, //
							AttributesTRAn.ProbabilityPerThousandHitMagical, //
							AttributesTRAn.CriticalProbabilityPerThousand, //
							AttributesTRAn.ProbabilityPerThousandAvoidPhysical, //
							AttributesTRAn.ProbabilityPerThousandAvoidMagical, //
							AttributesTRAn.CriticalProbabilityPerThousandAvoid, //
			}//
			, new AttributeIdentifier[][] { //
					new AttributeIdentifier[] { AttributesTRAn.ProbabilityPerThousandAvoidPhysical }, //
					new AttributeIdentifier[] { AttributesTRAn.ProbabilityPerThousandAvoidMagical }, //
					new AttributeIdentifier[] { AttributesTRAn.CriticalProbabilityPerThousandAvoid }, //
					new AttributeIdentifier[] { AttributesTRAn.ProbabilityPerThousandHitPhysical }, //
					new AttributeIdentifier[] { AttributesTRAn.ProbabilityPerThousandHitMagical }, //
					new AttributeIdentifier[] { AttributesTRAn.CriticalProbabilityPerThousand }//
			});
			a.setRarityIndex(3);
			return a;
		});
		objProvider.addObj("Siphon of Will", 3, gm -> {
			AbilityBonusDependingOnOtherBonuses a;
			a = new AbilityBonusDependingOnOtherBonuses("Siphon of Will", //
					new AttributeIdentifier[] { AttributesTRAn.LifeLeechPercentage, //
							AttributesTRAn.ManaLeechPercentage //
			}//
			, new AttributeIdentifier[][] { //
					new AttributeIdentifier[] { AttributesTRAn.RegenLife, AttributesTRAn.DamageBonusPhysical }, //
					new AttributeIdentifier[] { AttributesTRAn.RegenMana, AttributesTRAn.DamageBonusMagical } //
			});
			a.setRarityIndex(3);
			return a;
		});
		objProvider.addObj("Offense is the best Defence", 3, gm -> {
			AbilityBonusDependingOnOtherBonuses a;
			a = new AbilityBonusDependingOnOtherBonuses("Offense is the best Defence", //
					new AttributeIdentifier[] { AttributesTRAn.LifeLeechPercentage, //
							AttributesTRAn.ManaLeechPercentage //
			}//
			, new AttributeIdentifier[][] { //
					new AttributeIdentifier[] { AttributesTRAn.RegenLife, AttributesTRAn.DamageBonusPhysical }, //
					new AttributeIdentifier[] { AttributesTRAn.RegenMana, AttributesTRAn.DamageBonusMagical } //
			});
			a.setRarityIndex(3);
			return a;
		});
		objProvider.addObj("You cannot touch me, but...", 2, gm -> {
			AbilityBonusDependingOnOtherBonuses a;
			a = new AbilityBonusDependingOnOtherBonuses("You cannot touch me, but...", //
					new AttributeIdentifier[] { AttributesTRAn.DamageReductionPhysical, //
							AttributesTRAn.DamageReductionMagical, //
							AttributesTRAn.CriticalMultiplierPercentageReduction //
			}//
			, new AttributeIdentifier[][] { //
					new AttributeIdentifier[] { AttributesTRAn.ProbabilityPerThousandAvoidPhysical }, //
					new AttributeIdentifier[] { AttributesTRAn.ProbabilityPerThousandAvoidMagical }, //
					new AttributeIdentifier[] { AttributesTRAn.CriticalProbabilityPerThousandAvoid } //
			});
			a.setRarityIndex(2);
			return a;
		});
		objProvider.addObj("Gonna deflect 'em all", 2, gm -> {
			AbilityBonusDependingOnOtherBonuses a;
			a = new AbilityBonusDependingOnOtherBonuses("Gonna deflect 'em all", //
					new AttributeIdentifier[] { AttributesTRAn.ProbabilityPerThousandAvoidPhysical, //
							AttributesTRAn.ProbabilityPerThousandAvoidMagical, //
							AttributesTRAn.CriticalProbabilityPerThousandAvoid//
			}//
			, new AttributeIdentifier[][] { //
					new AttributeIdentifier[] { AttributesTRAn.DamageReductionPhysical }, //
					new AttributeIdentifier[] { AttributesTRAn.DamageReductionMagical }, //
					new AttributeIdentifier[] { AttributesTRAn.CriticalMultiplierPercentageReduction } //
			});
			a.setRarityIndex(2);
			return a;
		});
		objProvider.addObj(ADamageReductionOnLifeLowerToPhysicalAttributes.NAME,
				ADamageReductionOnLifeLowerToPhysicalAttributes.RARITY,
				gm -> new ADamageReductionOnLifeLowerToPhysicalAttributes());

		//

		System.out.println("objProvider ABILITY size: " + objProvider.getObjectsFactoriesCount());
	}

	private void forEachLevel_ZeroToMaz(Consumer<Integer> c) {
		for (int maxLevel = 5; maxLevel >= 0; maxLevel--) {
			c.accept(maxLevel);
		}
	}

	//

	private FactoryObjGModalityBased<AbilityGeneric> newAMeditationMoreRegen_LevelBased(int level) {
		return gm -> new AMeditationMoreRegen(level);
	}
}