package games.generic.controlModel.inventoryAbil;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import dataStructures.mtAvl.MapTreeAVLLightweight.TreeAVLDelegator;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GameObjectsProvidersHolder;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.misc.AttributesHolder;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.subimpl.GModalityRPG;
import tools.Comparators;

/**
 * Top class for equippable object.<br>
 * It's a base class.
 * <p>
 * Each Equipment should implements the set of "character attribute/statistics
 * modifiers" (i.e.: {@link AttributeModification}) as it's suggested by
 * {@link #getBaseAttributeModifiers()} and {@link #getUpgrades()}.
 * <p>
 * Note: Instead of creating an array of attributes (that mimics the character's
 * attributes) and apply to that array all modifiers, wasting memory in
 * almost-empty arrays, just collect all those {@link AttributeModification}
 * (into {@link #getBaseAttributeModifiers()} and {@link #getUpgrades()} that
 * provides a set of {@link AttributeModification}) and apply them one by one.
 */
public abstract class EquipmentItem extends InventoryItem {
	private static final long serialVersionUID = -55232021L;
	protected final EquipmentType equipmentType;
	protected EquipmentSet belongingEquipmentSet;
	protected Set<EquipItemAbility> abilities;
	protected final List<AttributeModification> baseAttributeModifiers;
	protected Set<EquipmentUpgrade> upgrades;

	public EquipmentItem(GModalityRPG gmrpg, EquipmentType equipmentType, String name) {
		super(name);
		this.belongingEquipmentSet = null;
		this.equipmentType = equipmentType;
		this.baseAttributeModifiers = new LinkedList<>();
		onCreate(gmrpg);
	}

	//

	public EquipmentType getEquipmentType() {
		return this.equipmentType;
	}

	public EquipmentSet getBelongingEquipmentSet() {
		return belongingEquipmentSet;
	}

	/** Beware: could return null if this item has no abilities. */
	public Set<EquipItemAbility> getAbilities() {
		checkAbilitiesSet();
		return this.abilities;
	}

	public List<AttributeModification> getBaseAttributeModifiers() {
		return baseAttributeModifiers;
	}

	public Set<EquipmentUpgrade> getUpgrades() {
		return upgrades;
	}

	//

	public void setBelongingEquipmentSet(EquipmentSet belongingEquipmentSet) {
		this.belongingEquipmentSet = belongingEquipmentSet;
	}

	//

	//

	/**
	 * Function called on creation time to fill fields like particular abilities (if
	 * no already done in loading time)
	 */
	protected abstract void enrichEquipment(GModality gm, GameObjectsProvidersHolder providersHolder);

	protected void onCreate(GModality gm) {
		enrichEquipment(gm, gm.getGameObjectsProvider());
	}

	protected void checkAbilitiesSet() {
		if (this.abilities == null) {
			MapTreeAVL<String, EquipItemAbility> m;
			m = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, MapTreeAVL.BehaviourOnKeyCollision.Replace,
					Comparators.STRING_COMPARATOR);
			this.abilities = m.toSetValue(EquipItemAbility.NAME_EXTRACTOR);
		}
	}

	protected void checkUpgradeSet() {
		if (this.upgrades == null) {
			MapTreeAVL<String, EquipmentUpgrade> mapEquipUpgrades;
			mapEquipUpgrades = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
			this.upgrades = mapEquipUpgrades.toSetValue(EquipmentUpgrade.KEY_EXTRACTOR);
		}
	}

	//

	public BaseCreatureRPG getCreatureWearingEquipments() {
		EquipmentSet es;
		es = this.getBelongingEquipmentSet();
		return (es == null) ? null : es.getCreatureWearingEquipments();
	}

	public EquipmentItem addAttributeModifier(AttributeModification am) {
		if (am != null)
			this.baseAttributeModifiers.add(am);
		return this;
	}

	public EquipmentItem addAbility(EquipItemAbility am) {
		if (am != null) {
			checkAbilitiesSet();
			this.abilities.add(am);
			am.setEquipItem(this);
		}
		return this;
	}

	public EquipmentItem removeAbility(EquipItemAbility am) {
		if (am != null) {
			checkAbilitiesSet();
			if (this.abilities.remove(am))
				am.setEquipItem(null);
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public EquipmentItem removeAbilityByName(String name) {
		if (name != null) {
			TreeAVLDelegator<String, EquipItemAbility> backMapDeleg;
			MapTreeAVL<String, EquipItemAbility> backMap;
			EquipItemAbility am;
			checkAbilitiesSet();
			backMapDeleg = (TreeAVLDelegator<String, EquipItemAbility>) this.abilities;
			backMap = backMapDeleg.getBackTree();
			am = backMap.get(name);
			if (am != null) {
				backMap.remove(name);
				am.setEquipItem(null);
			}
		}
		return this;
	}

	public EquipmentItem addUpgrade(EquipmentUpgrade am) {
		if (am != null) {
			checkUpgradeSet();
			this.upgrades.add(am);
			am.setEquipmentAssigned(this);
		}
		return this;
	}

	public EquipmentItem removeUpgrade(EquipmentUpgrade eu) {
		if (eu != null) {
			checkUpgradeSet();
			if (this.upgrades.remove(eu))
				eu.setEquipmentAssigned(null);
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public EquipmentItem removeUpgradeByName(String name) {
		if (name != null) {
			TreeAVLDelegator<String, EquipmentUpgrade> backMapDeleg;
			MapTreeAVL<String, EquipmentUpgrade> backMap;
			EquipmentUpgrade eu;
			checkAbilitiesSet();
			backMapDeleg = (TreeAVLDelegator<String, EquipmentUpgrade>) this.upgrades;
			backMap = backMapDeleg.getBackTree();
			eu = backMap.get(name);
			if (eu != null) {
				backMap.remove(name);
				eu.setEquipmentAssigned(null);
			}
		}
		return this;
	}

	/**
	 * Should apply ({@link AttributeModification}) and activate this optional
	 * {@link EquipItemAbility}.
	 * <p>
	 * See {@link EquipItemAbility#onEquip(GModality)} for further informations.
	 */
	public void onEquip(final GModality gm) {
		final AttributesHolder ah;
		final CreatureAttributes ca;
		Set<EquipItemAbility> abl;
		List<AttributeModification> attmod;
		Set<EquipmentUpgrade> upg;
		Consumer<AttributeModification> modifierApplier;
		gm.addGameObject(this); // add me to the game, especially if i'm a event-listener, a timed-object, etc
		ah = this.getCreatureWearingEquipments(); // assumed to be true
		ca = ah.getAttributes();
		modifierApplier = eam -> ca.applyAttributeModifier(eam);
		attmod = this.getBaseAttributeModifiers();
		if (attmod != null) {
			attmod.forEach(modifierApplier);
		}
		upg = this.getUpgrades();
		if (upg != null) {
			upg.forEach(up -> {
				// apply all upgrade's modifiers
				up.getAttributeModifiers().forEach(modifierApplier);
			});
		}
		abl = this.getAbilities();
		if (abl != null) {
			abl.forEach(ea -> ea.onEquip(gm));
		}
	}

	/**
	 * The opposite of {@link #onEquip(GModality)}.
	 * <p>
	 * See {@link EquipItemAbility#onUEquipping(GModality)} for further
	 * informations.
	 */
	public void onUnEquipping(final GModality gm) {
		final AttributesHolder ah;
		final CreatureAttributes ca;
		Set<EquipItemAbility> abl;
		List<AttributeModification> attmod;
		Set<EquipmentUpgrade> upg;
		Consumer<AttributeModification> modifierRemover;
		gm.removeGameObject(this);
		ah = this.getCreatureWearingEquipments(); // assumed to be true
		ca = ah.getAttributes();
		attmod = this.getBaseAttributeModifiers();
		modifierRemover = eam -> ca.removeAttributeModifier(eam);
		attmod = this.getBaseAttributeModifiers();
		if (attmod != null) {
			attmod.forEach(modifierRemover);
		}
		upg = this.getUpgrades();
		if (upg != null) {
			upg.forEach(up -> {
				// remove all upgrade's modifiers
				up.getAttributeModifiers().forEach(modifierRemover);
			});
		}
		abl = this.getAbilities();
		if (abl != null) {
			abl.forEach(ea -> ea.onUnEquipping(gm));
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [name=" + name + ", ID=" + getID() + ",\n\t rarityIndex, ="
				+ rarityIndex + ", equipmentType=" + equipmentType + ",\n\tbaseAttributeModifiers="
				+ baseAttributeModifiers + ",\n\t upgrades=" + upgrades + ",\n\t abilities=" + //
				abilitiesToString() + "]";
	}

	protected String abilitiesToString() {
		StringBuilder sb;
		if (abilities == null)
			return "null";
		sb = new StringBuilder(16);
		abilities.forEach(ea -> sb.append(ea));
		return sb.toString();
	}
}