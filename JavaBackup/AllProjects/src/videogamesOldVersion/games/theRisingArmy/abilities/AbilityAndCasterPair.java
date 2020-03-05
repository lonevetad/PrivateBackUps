package games.theRisingArmy.abilities;

import games.theRisingArmy.abstractTRAr.AbstractAbilityTRAr;
import games.theRisingArmy.abstractTRAr.GameObjectTRAr;

public class AbilityAndCasterPair {

	public AbilityAndCasterPair() {
	}

	public AbilityAndCasterPair(AbstractAbilityTRAr ability, GameObjectTRAr caster) {
		super();
		set(ability, caster);
	}

	AbstractAbilityTRAr ability;
	GameObjectTRAr caster;

	public AbstractAbilityTRAr getAbility() {
		return ability;
	}

	public GameObjectTRAr getCaster() {
		return caster;
	}

	public AbilityAndCasterPair setAbility(AbstractAbilityTRAr ability) {
		this.ability = ability;
		return this;
	}

	public AbilityAndCasterPair setCaster(GameObjectTRAr caster) {
		this.caster = caster;
		return this;
	}

	//

	public AbilityAndCasterPair set(AbstractAbilityTRAr ability, GameObjectTRAr caster) {
		this.ability = ability;
		this.caster = caster;
		return this;
	}

}