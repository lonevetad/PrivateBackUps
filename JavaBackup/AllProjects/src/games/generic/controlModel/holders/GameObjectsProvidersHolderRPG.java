package games.generic.controlModel.holders;

import java.util.Map;
import java.util.Random;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.InventoryItem;
import games.generic.controlModel.items.InventoryItemNotEquippable;
import games.generic.controlModel.misc.GMapProvider;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.objects.creature.BaseCreatureRPG;
import games.generic.controlModel.objects.creature.CreatureSimple;
import games.generic.controlModel.providers.AbilitiesProvider;
import games.generic.controlModel.providers.CreaturesProvider;
import games.generic.controlModel.providers.EquipItemProvider;
import games.generic.controlModel.providers.EquipmentUpgradesProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import tools.Comparators;

/**
 * One of the core classes.
 * <p>
 * Huge class acting as a "database for instantiation" collecting a series of
 * useful game objects. In particular, it holds ("provides" through
 * {@link GameObjectsProvidersHolderRPG}) the list below of superclasses and
 * manages them, like calculating the drops and spawning inside the game.
 * <ul>
 * <li>{@link InventoryItem} (More precisely, {@link EquipmentItem} and
 * {@link InventoryItemNotEquippable}).</li>
 * <li>{@link AbilityGeneric}</li>
 * <li>{@link CreatureSimple}</li>
 * </ul>
 * Manages: dropping items (for instance, upon killing an enemy or opening a
 * chest), defining abilities, etc.
 * <p>
 * Useful classes/interfaces used here:
 * <ul>
 * <li>{@link EquipmentItem}</li>
 * <li>{@link EquipItemProvider}</li>
 * <li>{@link AbilityGeneric}</li>
 * <li>{@link AbilitiesProvider}</li>
 * <li>{@link CreatureSimple}</li>
 * <li>{@link CreaturesProvider}</li>
 * <li>{@link EquipmentUpgrade}</li>
 * </ul>
 */
public abstract class GameObjectsProvidersHolderRPG implements GameObjectsProvidersHolder {

	public GameObjectsProvidersHolderRPG(GModalityRPG gModality) {
		this.gModality = gModality;
		this.providers = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
		this.abilitiesProvider = newAbilitiesProvider();
		this.equipmentsProvider = newEquipItemProvider();
		this.creaturesProvider = newCreatureProvider();
		this.equipUpgradesProvider = newEquipUpgradesProvider();
		this.mapsProvider = newMapsProvider();
		this.providers.put(AbilitiesProvider.NAME, abilitiesProvider);
		this.providers.put(EquipItemProvider.NAME, equipmentsProvider);
		this.providers.put(EquipmentUpgradesProvider.NAME, equipUpgradesProvider);
		this.providers.put(CreaturesProvider.NAME, creaturesProvider);
		this.providers.put(GMapProvider.NAME_FOR_GOPROVIDER, mapsProvider);
//		this.random = new Random();
	}

	protected GModalityRPG gModality;
	protected Map<String, GameObjectsProvider<? extends ObjectNamed>> providers;
	protected AbilitiesProvider abilitiesProvider;
	protected EquipItemProvider equipmentsProvider;
	protected EquipmentUpgradesProvider equipUpgradesProvider;
	protected CreaturesProvider<BaseCreatureRPG> creaturesProvider;
	protected GMapProvider mapsProvider;
	// for random stuffs
//	protected RandomWeightedIndexes equipItemsWeights;
//	protected Random random;

	//
	@Override
	public Map<String, GameObjectsProvider<? extends ObjectNamed>> getProviders() { return providers; }

	public GModalityRPG getgModality() { return gModality; }

	public AbilitiesProvider getAbilitiesProvider() { return abilitiesProvider; }

	public EquipItemProvider getEquipmentsProvider() { return equipmentsProvider; }

	public EquipmentUpgradesProvider getEquipUpgradesProvider() { return equipUpgradesProvider; }

	public CreaturesProvider<BaseCreatureRPG> getCreaturesProvider() { return creaturesProvider; }

	public Random getRandom() { return gModality.getRandom(); }

//

	public void setAbilitiesProvider(AbilitiesProvider ap) { this.abilitiesProvider = ap; }

	public void setEquipmentsProvider(EquipItemProvider equipmentsProvider) {
		this.equipmentsProvider = equipmentsProvider;
	}

	public void setEquipUpgradesProvider(EquipmentUpgradesProvider equipUpgradesProvider) {
		this.equipUpgradesProvider = equipUpgradesProvider;
	}

	public void setCreaturesProvider(CreaturesProvider<BaseCreatureRPG> creaturesProvider) {
		this.creaturesProvider = creaturesProvider;
	}

//	public void setEquipItemsWeights(RandomWeightedIndexes equipItemsWeights) {this.equipItemsWeights = equipItemsWeights;}

	//

	public void setgModality(GModalityRPG gModality) { this.gModality = gModality; }

	public AbilitiesProvider newAbilitiesProvider() { return new AbilitiesProvider(); }

	public abstract EquipItemProvider newEquipItemProvider();

	public abstract EquipmentUpgradesProvider newEquipUpgradesProvider();

	/**
	 * Should return something based on {@link BaseCreatureRPG}.
	 */
	public abstract CreaturesProvider<BaseCreatureRPG> newCreatureProvider();
//	public abstract CreaturesProvider<? extends CreatureSimple> newCreatureProvider();

	public abstract GMapProvider newMapsProvider();
}