package games.theRisingAngel.loaders;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

import dataStructures.MapTreeAVL;
import dataStructures.SetMapped;
import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.loaders.ObjectLoadable;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.FactoryObjGModalityBased;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.providers.EquipItemProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.generic.controlModel.subimpl.LoaderEquipments;
import games.theRisingAngel.GControllerTRAn;
import games.theRisingAngel.GModalityTRAnBaseWorld;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EquipmentTypesTRAn;
import games.theRisingAngel.enums.RaritiesTRAn;
import games.theRisingAngel.enums.TribesTRAn;
import games.theRisingAngel.enums.TribesTRAn.Tribe;
import games.theRisingAngel.inventory.equipsWithAbilities.ArmProtectionShieldingDamageByMoney;
import games.theRisingAngel.inventory.equipsWithAbilities.HelmetOfPlanetaryMeteors;
import games.theRisingAngel.inventory.equipsWithAbilities.NecklaceOfPainRinvigoring;
import games.theRisingAngel.loaders.factories.FactoryEquip;
import games.theRisingAngel.loaders.factories.FactoryItems;
import tools.LoggerMessages;
import tools.impl.LoggerOnFile;
import tools.json.JSONParser;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.types.JSONArray;
import tools.json.types.JSONObject;
import tools.json.types.JSONString;

public class LoaderEquipTRAn extends LoaderEquipments implements ObjectLoadable {
	private static final long serialVersionUID = 1L;

	public static enum TribeEquipLoadMode {
		Ignore, SetGrouped, EachItemsIndividually
	}

	public static final TribeEquipLoadMode TRIBE_EQUIP_LOAD_MODE = TribeEquipLoadMode.EachItemsIndividually;

	public LoaderEquipTRAn(GameObjectsProvider<EquipmentItem> objProvider) {
		super(objProvider);
		this.tribesFullSetDropStates = new EnumMap<>(Tribe.class);
	}

	protected Map<Tribe, EquipTRAnFullSetFactory> tribesFullSetDropStates;

	@Override
	public LoadStatusResult loadInto(GController gc) {
		final LoaderEquipTRAn thisLoader = this;
//		objProvider.addObj(ADamageReductionCurrencyBased.NAME,
//				gmm -> new ADamageReductionCurrencyBased(DamageTypesTRAr.Physical));
//		objProvider.addObj(ADamageReductionCurrencyBased.NAME,
//				gmm -> new ADamageReductionCurrencyBased(DamageTypesTRAr.Magical));
//		objProvider.addObj(AMoreDamageReceivedMoreLifeRegen.NAME, gmm -> new AMoreDamageReceivedMoreLifeRegen());
		objProvider.addObj(ArmProtectionShieldingDamageByMoney.NAME,
				(gm) -> new ArmProtectionShieldingDamageByMoney((GModalityRPG) gm));
		objProvider.addObj(NecklaceOfPainRinvigoring.NAME, //
				(gm) -> new NecklaceOfPainRinvigoring((GModalityRPG) gm));

		objProvider.addObj(HelmetOfPlanetaryMeteors.NAME, (gm) -> new HelmetOfPlanetaryMeteors((GModalityRPG) gm));

		try {
			final int[] index = { 0 };
//			JSONArray equips;
//			equips = (JSONArray) JSONParser
//					.parseFile(LoaderConfigurations.RESOURCE_REPOSITORY_PULL_FACT + "equipItems.json");
//
//			equips.forEach(
			JSONParser.forEachInArray(//
					JSONParser.charactersIteratorFrom(
							new File(LoaderConfigurationsTRAn.RESOURCE_REPOSITORY_PULL_FACT + "equipItems.json")),
					(indexEquip, rawEquip) -> {
						// TODO
						FactoryEquip factory;
						FactoryItems factoryItem;
						JSONObject equipEquipJSON, attributeModsJSON;
						AttributeModification[] attrMods;
						equipEquipJSON = (JSONObject) rawEquip;
						factory = new FactoryEquip();
						factoryItem = factory.getFactoryItem(); // it's a delegator that recycles code
						factoryItem.name = equipEquipJSON.getFieldValue("name").asString();
						factoryItem.rarity = equipEquipJSON.getFieldValue("rarity").asInt();
						factoryItem.price = equipEquipJSON.getFieldValue("price").asArrayInt();

						attributeModsJSON = (JSONObject) equipEquipJSON.getFieldValue("attributeModifiers");
						attrMods = new AttributeModification[attributeModsJSON.getFieldsAmount()];
						index[0] = 0;
						attributeModsJSON.forEachField((fieldName, attrValueJSON) -> {
							AttributeIdentifier attribute;
							attribute = AttributesTRAn.valueOf(fieldName);
							attrMods[index[0]++] = new AttributeModification(attribute, attrValueJSON.asInt());
						});
						factory.attrMods = attrMods;
						if (equipEquipJSON.hasField("description")) {
							factoryItem.description = equipEquipJSON.getFieldValue("description").asString();
						}

						{ // abilities
							JSONValue a;
							JSONArray abilArray;
							a = equipEquipJSON.getFieldValue("abilities");
							if (a != null) {
								if (a.isType(JSONTypes.ArrayHomogeneousType)
										&& (abilArray = (JSONArray) a).length() > 0) {
									factory.abilities = new ArrayList<>(abilArray.length());
									abilArray.forEach((indexAbil, abilityDataRaw) -> {
										JSONObject ao;
										ao = (JSONObject) abilityDataRaw;
										FactoryEquip.AbilityData data;
										data = new FactoryEquip.AbilityData();
										data.name = ao.getFieldValue("name").asString();
										if (ao.hasField("level")) {
											data.level = ao.getFieldValue("level").asInt();
										} else {
											data.level = 0;
										}
										factory.abilities.add(data);
									});
								}
							}
						}
						{
							JSONObject dimensionRaw = (JSONObject) equipEquipJSON.getFieldValue("dimensionInventory");
							factoryItem.dimensionInInventory = new Dimension(
									dimensionRaw.getFieldValue("width").asInt(),
									dimensionRaw.getFieldValue("height").asInt());
						}
						try {
							factory.type = EquipmentTypesTRAn.valueOf(equipEquipJSON.getFieldValue("type").asString());
						} catch (Exception e) {
							System.out.println("Unknown type for equip ++" + factoryItem.name + "++ : --"
									+ equipEquipJSON.getFieldValue("type") + "--");
							e.printStackTrace();
							factory.type = EquipmentTypesTRAn.Special;
						}
						thisLoader.saveObjectFactory(factoryItem.name, factory);
					});

		} catch (FileNotFoundException e) {
			gc.getLogger().logException(e);
			return LoadStatusResult.CriticalFail;
		}

//		leff = new LoaderEquipFromFile("", "equipItems.json");
//		leff.readAllFile();
//		log.logAndPrint("total equip loaded: " + leff.factories.size());
//		for (FactoryEquip fe : leff.factories) {
//			objProvider.addObj(fe.getFactoryItem().name, fe.getFactoryItem().rarity, fe);
//		}

		switch (TRIBE_EQUIP_LOAD_MODE) {
		case EachItemsIndividually: {
			int rarityIndex;
			rarityIndex = TribesTRAn.RARITY_TRIBE_EQUIPMENT_PIECES.getIndex();
			TribesTRAn.ALL_EQUIP_TYPES_ON_TRIBE_SETS.forEach(equipType -> {
				for (Tribe tribe : TribesTRAn.ALL_TRIBES) {
					thisLoader.saveObjectFactory(TribesTRAn.getNameEquipFor(tribe, equipType), rarityIndex,
							new EquipTRAnFactory(tribe, equipType));
				}
			});
			break;
		}
		case SetGrouped: {
			int rarityIndex;
			rarityIndex = TribesTRAn.RARITY_TRIBE_EQUIPMENT_PIECES.getIndex();
			for (Tribe tribe : TribesTRAn.ALL_TRIBES) {
				EquipTRAnFullSetFactory fullSetFactory;
				fullSetFactory = new EquipTRAnFullSetFactory(tribe);
				tribesFullSetDropStates.put(tribe, fullSetFactory);
				thisLoader.saveObjectFactory(tribe.getName(), rarityIndex, fullSetFactory);
			}
			break;
		}
		case Ignore: {
			break;
		}
		default:
			if (TRIBE_EQUIP_LOAD_MODE != null) {
				throw new IllegalArgumentException("Unexpected value: " + TRIBE_EQUIP_LOAD_MODE);
			}
		}

		System.out.println("::: LOADED " + thisLoader.objProvider.getObjectsFactoriesCount() + " equip factories");
		return LoadStatusResult.Success;
	}

	@Override
	public JSONValue toJSON() {
		JSONObject o;
		Function<EquipmentTypesTRAn, JSONString> equipToJSONStringConverter;

		o = new JSONObject();
		equipToJSONStringConverter = (eqType) -> { return new JSONString(eqType.getName()); };

		tribesFullSetDropStates.forEach((t, f) -> {
			o.addField(t.getName(),
					// f.piecesNotYetDropped.toSetValue(EquipmentItem::getEquipmentType)
					new JSONArray(//
							JSONTypes.ArrayHomogeneousType, //
							new SetMapped<EquipmentTypesTRAn, JSONString>(f.piecesNotYetDropped.keySet(),
									equipToJSONStringConverter)//
											.toArray(new JSONString[f.piecesNotYetDropped.size()]), //
							JSONTypes.String)//
			);
		});
		return o;
	}

	//

	public static class EquipTRAnFullSetFactory implements FactoryObjGModalityBased<EquipmentItem> {
		protected final Tribe tribe;
		protected final static Random rand = GController.RANDOM;
		protected MapTreeAVL<EquipmentTypesTRAn, EquipmentItem> piecesNotYetDropped;

		public EquipTRAnFullSetFactory(Tribe tribe) {
			super();
			this.tribe = tribe;
			piecesNotYetDropped = null;
		}

		protected void checkAndRefill(GModalityRPG gmrpg) {
//			MapTreeAVL<EquipmentTypesTRAn, EquipmentItem> pnyd = this.piecesNotYetDropped;
//			if (pnyd.isEmpty()) {
//				TribesTRAn.ALL_EQUIP_TYPES_ON_TRIBE_SETS.forEach(
//						(EquipmentTypesTRAn equipTypePiece) -> { pnyd.put(equipTypePiece.getID(), equipTypePiece); });
//			}
			if (this.piecesNotYetDropped == null || this.piecesNotYetDropped.isEmpty()) {
				this.piecesNotYetDropped = tribe.newEquipmentSet(gmrpg);
			}
		}

		@Override
		public EquipmentItem newInstance(GModality gm) {
			boolean isLastOne;
			int size;
			MapTreeAVL<EquipmentTypesTRAn, EquipmentItem> pnyd;
			Entry<EquipmentTypesTRAn, EquipmentItem> e;
//			EquipmentTypesTRAn equipType;

			this.checkAndRefill((GModalityRPG) gm);
			pnyd = this.piecesNotYetDropped;
			e = pnyd.getAt( //
					(isLastOne = ((size = pnyd.size()) == 1)) ? 0 : rand.nextInt(size));
//			equipType = e.getValue();
			if (isLastOne) {
				this.piecesNotYetDropped = null;
			} else {
				pnyd.remove(e.getKey());
			}
			return e.getValue();
		}

	}

	public static class EquipTRAnFactory implements FactoryObjGModalityBased<EquipmentItem> {
		protected final Tribe tribe;
		protected final EquipmentTypesTRAn equipType;

		public EquipTRAnFactory(Tribe tribe, EquipmentTypesTRAn equipType) {
			super();
			this.tribe = tribe;
			this.equipType = equipType;
		}

		@Override
		public EquipmentItem newInstance(GModality gm) {
			return this.tribe.newEquipmentPiece((GModalityRPG) gm, equipType);
		}
	}

	//

	public static void main(String[] args) {
		int size;
		int[] rarities;
//		LoaderEquipFromFile leff;
//		LinkedList<FactoryEquip> factories;
//		FactoryEquip fe;
		LoaderEquipTRAn loader;
		EquipItemProvider equipItemProvider;
		Map<String, FactoryObjGModalityBased<EquipmentItem>> allObjectFactories;
		final LoggerMessages log;

		GControllerTRAn gc;
		GModalityTRAnBaseWorld gm;

		{
			/*
			 * this temporary variable required because the compiler thinks that the
			 * assignement in the "catch" branch could follow a preceeding assigmento to
			 * "log", even if the exception rises, in case of error, some lines before it.
			 */
			LoggerMessages logTemp;
			try {
				LoggerOnFile logOnFile;
				logOnFile = new LoggerOnFile(//
						LoggerMessages.CONSOLE_BASE_PATH + File.separatorChar + "testsManualOutput" //
								+ File.separatorChar + "outputLoaderEquipTRAn.txt");
				logTemp = LoggerMessages.loggerOrDefault(logOnFile);
			} catch (IOException e) {
				logTemp = LoggerMessages.LOGGER_DEFAULT;
				e.printStackTrace();
			}
			log = logTemp;
		}

		gc = new GControllerTRAn();
		gm = (GModalityTRAnBaseWorld) gc.newModalityByName(GModalityTRAnBaseWorld.NAME);
		Objects.requireNonNull(gm);

		equipItemProvider = new EquipItemProvider();
		loader = new LoaderEquipTRAn(equipItemProvider);

		log.logAndPrint("Starting reading Equip TRAn");
		loader.loadInto(gc);
		allObjectFactories = loader.objProvider.getObjectsIdentified();
		size = allObjectFactories.size();
		rarities = new int[RaritiesTRAn.values().length];
		Arrays.fill(rarities, 0);

		allObjectFactories.forEach((name, factoryEquip) -> {
			if (factoryEquip instanceof FactoryEquip) {
				FactoryEquip fe;
				fe = (FactoryEquip) factoryEquip;
//				log.logAndPrint(fe.toString());
//				log.logAndPrint("\n");
				rarities[fe.getFactoryItem().rarity]++;
			} else {
				try {
					EquipmentItem ei;
					ei = factoryEquip.newInstance(gm);
					rarities[ei.getRarityIndex()]++;
					// log.logAndPrint(ei.toString());
				} catch (Exception e) {
					log.logAndPrint("ERROR WITH EQUIP: ");
					log.logAndPrint(name);
					log.logAndPrint("\n");
					log.logException(e);
					log.logAndPrint("\n");
				}
			}
		});

		//

		log.logAndPrint("total: " + size);
		log.logAndPrint("\n");
		log.logAndPrint("RARITIES proportion:");
		log.logAndPrint("\n");
		log.logAndPrint(Arrays.toString(rarities));
		log.logAndPrint("\n");
	}
}