package games.theRisingArmy.abstractTRAr;

import java.io.Serializable;
import java.util.List;

public interface AbilitiesListHolder extends Serializable {

	public List<AbstractAbilityTRAr> getAbilities();

	public AbilitiesListHolder setAbilities(List<AbstractAbilityTRAr> abilities);

	//

	public default AbilitiesListHolder addAbility(AbstractAbilityTRAr ability) {
		List<AbstractAbilityTRAr> list;
		list = getAbilities();
		if (ability != null && list != null /* &&(!list.contains(ability)) */) {
			list.add(ability);
			if (this instanceof GameObjectTRAr) ability.onBeingGainedBy((GameObjectTRAr) this);
		}
		return this;
	}

	public default AbilitiesListHolder removeAbility(AbstractAbilityTRAr ability) {
		List<AbstractAbilityTRAr> list;
		list = getAbilities();
		if (ability != null && list != null && (!list.isEmpty())/* &&(!list.contains(ability)) */) {
			list.remove(ability);
			if (this instanceof GameObjectTRAr) ability.onBeingRemovedFrom((GameObjectTRAr) this);
		}
		return this;
	}
}