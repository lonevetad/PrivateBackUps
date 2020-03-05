package videogamesOldVersion.games.theRisingArmy.abstractTRAr;

import java.util.LinkedList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import videogamesOldVersion.common.GameObjectInMap;
import videogamesOldVersion.common.GameObjectInMap.MementoGameObjectInMap;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;
import videogamesOldVersion.games.theRisingArmy.main.AbilitySpellCastCost;
import videogamesOldVersion.games.theRisingArmy.main.SharedStuffs_TRAr.SubTypePlayableObjectTRAr;
import videogamesOldVersion.games.theRisingArmy.main.SharedStuffs_TRAr.SuperTypePlayableObjectTRAr;

/** Could be anything: see {@link SuperTypePlayableObjectTRAr}. */
public abstract class GameObjectTRAr extends GameObjectInMap implements AbilitiesListHolder {
	private static final long serialVersionUID = 156084018058801320L;

	public GameObjectTRAr() {
		super();
	}

	public GameObjectTRAr(ShapeSpecification ss) {
		super(ss);
		castCost = new AbilitySpellCastCost();
		abilities = new LinkedList<>();
	}

	@SuppressWarnings("unchecked")
	public GameObjectTRAr(GameObjectTRAr o) {
		super(o);
		this.supertypes = (LinkedList<SuperTypePlayableObjectTRAr>) o.supertypes.clone();
		this.castCost = o.castCost;
	}

	public GameObjectTRAr(MementoGameObjectInMap o) {
		super(o);
		throw new NotImplementedException();// "Too lazy for memento"
	}

	//// closed world logic

	public boolean canBeSTAPped // like Magic the Gathering
			, canHaveAbilities;
	public int shapeshifterAbilityCounter;
	////
	// Integer idTrarObject;
	List<AbstractAbilityTRAr> abilities;
	LinkedList<SuperTypePlayableObjectTRAr> supertypes;
	LinkedList<SubTypePlayableObjectTRAr> subtypes;
	AbilitySpellCastCost castCost;

	//

	// TODO GETTER

	/**
	 * Should be used to recognize each object in a match and to synchronize players
	 * in a multiplayer match (pvp?) (for instance: object X has activated nth
	 * ability)<br>
	 * 12/12/2017: replaced by {@link GameObjectInMap#getGameObjectID()}
	 */

	// public Integer getIdTrarObject() { return idTrarObject; }

	/***/
	@Override
	public List<AbstractAbilityTRAr> getAbilities() {
		return abilities;
	}

	/** The original ones */
	public AbilitySpellCastCost getCastCost() {
		return castCost;
	}

	/***/
	public LinkedList<SuperTypePlayableObjectTRAr> getSupertypes() {
		return supertypes;
	}

	public LinkedList<SubTypePlayableObjectTRAr> getSubtypes() {
		return subtypes;
	}

	public boolean isPermanent() {
		LinkedList<SuperTypePlayableObjectTRAr> p;
		p = supertypes;
		// Iterator<PlayableObjectTypeTRAr> iter;
		if (p != null & p.size() > 0) {
			// iter = p.iterator();
			for (SuperTypePlayableObjectTRAr pott : p) {
				if (pott.isPermanent)
					return true;
			}
		}
		return false;
		// return playableObjectTypeTRAr == null ? false :
		// playableObjectTypeTRAr.isPermanent;
	}

	public boolean isCanBeSTAPped() {
		return canBeSTAPped;
	}

	public boolean isCanHaveAbilities() {
		return canHaveAbilities;
	}

	/**
	 * Closed world logic.<br>
	 * To tune if this object has got a specific {@link SubTypePlayableObjectTRAr},
	 * an {@link AbstractAbilityTRAr} like "Shapeshifter" could set this to true
	 */
	public boolean isShapeshifter() {
		return shapeshifterAbilityCounter > 0;
	}

	//

	// TODO SETTER

	/*
	 * public GameObjectTRAr setIdTrarObject(Integer idTrarObject) {
	 * this.idTrarObject = idTrarObject; return this; }
	 */

	public GameObjectTRAr setCastCost(AbilitySpellCastCost castCost) {
		this.castCost = castCost;
		return this;
	}

	@Override
	public GameObjectTRAr setAbilities(List<AbstractAbilityTRAr> abilities) {
		this.abilities = abilities;
		return this;
	}

	public GameObjectTRAr setSupertypes(LinkedList<SuperTypePlayableObjectTRAr> supertypes) {
		this.supertypes = supertypes;
		return this;
	}

	public GameObjectTRAr setSubtypes(LinkedList<SubTypePlayableObjectTRAr> subtypes) {
		this.subtypes = subtypes;
		return this;
	}

	public GameObjectTRAr setShapeshifter(boolean isShapeshifter) {
		if (isShapeshifter)
			shapeshifterAbilityCounter++;
		else
			shapeshifterAbilityCounter--;
		if (shapeshifterAbilityCounter < 0)
			shapeshifterAbilityCounter = 0;
		return this;
	}

	//

	// TODO PUBLIC

	//

	public boolean hasSupertype(SuperTypePlayableObjectTRAr pott) {
		return (pott != null && supertypes != null && this.supertypes.contains(pott));
	}

	public GameObjectTRAr addSupertype(SuperTypePlayableObjectTRAr pott) {
		if (pott != null && supertypes != null && (!this.supertypes.contains(pott)))
			supertypes.add(pott);
		return this;
	}

	public GameObjectTRAr removeSupertype(SuperTypePlayableObjectTRAr pott) {
		if (pott != null && supertypes != null)
			supertypes.remove(pott);
		return this;
	}

	public boolean hasSubtype(SubTypePlayableObjectTRAr pott) {
		return (pott != null && (isShapeshifter() || subtypes != null && this.subtypes.contains(pott)));
	}

	public GameObjectTRAr addSubtype(SubTypePlayableObjectTRAr pott) {
		if (pott != null && subtypes != null && (!this.subtypes.contains(pott)))
			subtypes.add(pott);
		return this;
	}

	public GameObjectTRAr removeSubtype(SubTypePlayableObjectTRAr pott) {
		if (pott != null && subtypes != null)
			subtypes.remove(pott);
		return this;
	}

	//

	@Override
	public GameObjectTRAr addAbility(AbstractAbilityTRAr ability) {
		if (ability != null && abilities != null) {
			abilities.add(ability);
			ability.onBeingGainedBy(this);
		}
		return this;
	}

	@Override
	public GameObjectTRAr removeAbility(AbstractAbilityTRAr ability) {
		if (ability != null && abilities != null && (!abilities.isEmpty())) {
			abilities.remove(ability);
			ability.onBeingRemovedFrom(this);
		}
		return this;
	}
}