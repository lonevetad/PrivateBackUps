package videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC;

import java.io.Serializable;

/**
 * This interface could be implemented to all of classes that does something
 * "timed". For example, update its image, move, regenerate health, reload a
 * weapon or an ability, complete some kind of transaction process.<br>
 * Can be used to express the idea of a "task".
 */
public interface ObjectActingOnPassingTime extends Serializable {

	/**
	 * Warns that some time has been passed. Usually it's expressed in milliseconds,
	 * but could me any other amount, like <i>turns</i>.
	 */
	public void act(int milliseconds);
}