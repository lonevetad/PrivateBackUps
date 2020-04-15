package games.generic.controlModel.inventoryAbil;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GameObjectsProvidersHolderRPG;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.misc.AttributesHolder;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.subimpl.GModalityRPG;
import tools.Comparators;

/**
 * Top class for equippable object.<br>
 * It's a base class.
 * <p>
 * Each Equipment should implements the set of "character statistics modifiers"
 * as follows (maybe through a class")
 * <p>
 * Set of power ups, of statistics' modifiers (i.e., character's parameters'
 * modifiers).<br>
 * Instead of create an array (that mimics the character's statistics) and apply
 * to that array all modifiers, wasting memory in almost-empty arrays, just
 * collect all statistic modifications.
 */
public abstract class EquipmentItem extends InventoryItem {
	private static final long serialVersionUID = -55232021L;
	protected final EquipmentType equipmentType;
	protected EquipmentSet belongingEquipmentSet;
	protected Set<EquipItemAbility> abilities;
	protected List<AttributeModification> attributeModifiers;

	public EquipmentItem(GModalityRPG gmrpg, EquipmentType equipmentType, String name) {
		super(name);
		this.belongingEquipmentSet = null;
		this.equipmentType = equipmentType;
		this.attributeModifiers = new LinkedList<>();
		enrichWithAbilities(((GameObjectsProvidersHolderRPG) gmrpg.getGameObjectsProvider()).getAbilitiesProvider());
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

	public List<AttributeModification> getAttributeModifiers() {
		return attributeModifiers;
	}

	//

	public void setBelongingEquipmentSet(EquipmentSet belongingEquipmentSet) {
		this.belongingEquipmentSet = belongingEquipmentSet;
	}

	//

	//

	protected abstract void enrichWithAbilities(AbilitiesProvider ap);

	protected void checkAbilitiesSet() {
		if (this.abilities == null) {
			MapTreeAVL<Integer, EquipItemAbility> m;
			m = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
			this.abilities = m.toSetValue(EquipItemAbility.ID_EXTRACTOR);
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
			this.attributeModifiers.add(am);
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
			this.abilities.remove(am);
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
		gm.addGameObject(this);
		ah = this.getCreatureWearingEquipments(); // assumed to be true
		ca = ah.getAttributes();
		attmod = this.getAttributeModifiers();
		if (attmod != null) {
			attmod.forEach(eam -> ca.applyAttributeModifier(eam));
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
		gm.removeGameObject(this);
		ah = this.getCreatureWearingEquipments(); // assumed to be true
		ca = ah.getAttributes();
		attmod = this.getAttributeModifiers();
		if (attmod != null) {
			attmod.forEach(eam -> ca.removeAttributeModifier(eam));
		}
		abl = this.getAbilities();
		if (abl != null) {
			abl.forEach(ea -> ea.onUnEquipping(gm));
		}
	}
}