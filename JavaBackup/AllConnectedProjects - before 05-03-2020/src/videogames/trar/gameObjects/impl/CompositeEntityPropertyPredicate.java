package videogames.trar.gameObjects.impl;

import java.util.LinkedList;
import java.util.List;

import videogames.trar.gameMechanism.ModelTRAr;
import videogames.trar.gameObjects.EntityPropertyPredicate;
import videogames.trar.gameObjects.EntityTRAr;

public class CompositeEntityPropertyPredicate implements EntityPropertyPredicate {

	/**
	 * Calls {@link #CompositeEntityPropertyPredicate(boolean)} passing
	 * <code>false</code> as default value.
	 */
	public CompositeEntityPropertyPredicate() {
		this(false);
	}

	public CompositeEntityPropertyPredicate(boolean isDisjunctive) {
		this.isDisjunctive = isDisjunctive;
		this.composingProperties = null;
	}

	protected boolean isDisjunctive;
	protected List<EntityPropertyPredicate> composingProperties;

	//

	//

	public boolean isDisjunctive() {
		return isDisjunctive;
	}

	public CompositeEntityPropertyPredicate setDisjunctive(boolean isDisjunctive) {
		this.isDisjunctive = isDisjunctive;
		return this;
	}

	public void clearEntityPropertyPredicate() {
		if (composingProperties != null)
			composingProperties.clear();
	}

	public void addEntityPropertyPredicate(EntityPropertyPredicate e) {
		if (e == null || composingProperties == null)
			return;
		composingProperties.remove(e);
	}

	public void removeEntityPropertyPredicate(EntityPropertyPredicate e) {
		if (e == null)
			return;
		if (composingProperties == null)
			composingProperties = new LinkedList<>();
		composingProperties.add(e);
	}

	//

	//

	@Override
	public boolean evaluateProperty(EntityTRAr owner, ModelTRAr environment) {
		if (composingProperties == null || composingProperties.isEmpty())
			return isDisjunctive;
//		if (isDisjunctive) {
//			for (EntityPropertyPredicate e : composingProperties)
//				if (e != null && e.evaluateProperty(owner, environment))
//					return true;
//			return false;
//		} else {
//			for (EntityPropertyPredicate e : composingProperties)
//				if (e == null || (!e.evaluateProperty(owner, environment)))
//					return false;
//			return true;
//		}
		boolean isd;
		isd = isDisjunctive;
		for (EntityPropertyPredicate e : composingProperties)
			if (isd == (e != null && e.evaluateProperty(owner, environment)))
				return isd;
		return !isd;
	}
}