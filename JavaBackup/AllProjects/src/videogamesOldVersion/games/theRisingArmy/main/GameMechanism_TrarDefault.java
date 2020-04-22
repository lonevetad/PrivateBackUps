package videogamesOldVersion.games.theRisingArmy.main;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

import videogamesOldVersion.common.GameMechanism;
import videogamesOldVersion.common.GameObjectInMap;
import videogamesOldVersion.common.abstractCommon.GameModelGeneric;
import videogamesOldVersion.common.abstractCommon.MainController;
import videogamesOldVersion.common.abstractCommon.referenceHolderAC.LoggerMessagesHolder;
import videogamesOldVersion.common.mainTools.RunnableSuspendible;
import videogamesOldVersion.games.theRisingArmy.MainController_TheRisingArmy;
import videogamesOldVersion.games.theRisingArmy.abilities.AbilityAndCasterPair;
import videogamesOldVersion.games.theRisingArmy.abilities.AbilityEnvironmentTrar;
import videogamesOldVersion.games.theRisingArmy.abstractTRAr.AbilitiesListHolder;
import videogamesOldVersion.games.theRisingArmy.abstractTRAr.AbstractAbilityTRAr;

public class GameMechanism_TrarDefault extends GameMechanism {
	private static final long serialVersionUID = -4091214044093L;

	public GameMechanism_TrarDefault(MainController mainController) {
		super(mainController);
	}

	public GameMechanism_TrarDefault() {
		super();
	}

	Deque<AbilityAndCasterPair> stackAbility = new LinkedList<>();

	//

	// TODO PUBLIC

	@Override
	protected RunnableSuspendible newRunnableGame() {
		return null;
	}

	@Override
	protected GameModelGeneric newGameModelGeneric() {
		return new GameModelTRAr((MainController_TheRisingArmy) this.getMainController());
	}

	/** Put the given ability onto the stack */
	protected String castAbility(AbilityAndCasterPair ability) {
		String error;
		error = null;
		if (ability == null) {
			error = "ERROR: on castAbility, the ability is null";
		} else {
			// TODO castAbility is it ok?
			stackAbility.addFirst(ability);
		}
		return error;
	}

	/** Perform the given ability yet taken from the stack. */
	protected String activateAbility(AbilityAndCasterPair acp) {
		String error;
		GameModelGeneric gm;
		Map<Integer, GameObjectInMap> allPermanents;
		AbstractAbilityTRAr ability;

		ability = acp.getAbility();
		error = null;
		if (ability == null) {
			error = "ERROR: on activateAbility, the ability is null";
		} else {
			// TODO activateAbility what to do? ..
			// ..modify the Model:

			// abilities could be modified, as described on its doc. Do it
			gm = this.getGameModel();
			if (gm != null) {
				allPermanents = gm.getAllGameObjectInMap();
				if (allPermanents != null && (!allPermanents.isEmpty())) {
					// for each permanent, give the gven ability to it to alter
					// it
					allPermanents.forEach((id, goim) -> {
						AbilitiesListHolder abilitiesHolder;
						if (goim != null && goim instanceof AbilitiesListHolder) {
							abilitiesHolder = (AbilitiesListHolder) goim;
							// for each permanent's ability, try to modify the
							// given one
							for (AbstractAbilityTRAr ab : abilitiesHolder.getAbilities()) {
								// try the modification
								// if (ab != null) ab.(ability);
							}
						}
					});
					// ability altered : perform it
					error = ability.act(AbilityEnvironmentTrar.instance
							.set((MainController_TheRisingArmy) getMainController(), acp.getCaster()));
				}
			} else {
				error = "ERROR: on activateAbility, the game model is null";
			}
		}
		return error;
	}

	//

	//

	public static class RunnableGameTrarDefault extends RunnableSuspendible {

		public RunnableGameTrarDefault(LoggerMessagesHolder lmh) {
			super(lmh);
		}

		@Override
		public void doOnEachCycle() {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean canCycle() {
			// TODO Auto-generated method stub
			return false;
		}

	}
}