package common.gui;

import common.abstractCommon.behaviouralObjectsAC.ObjectGuiUpdatingOnFrame;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.DoSomethingWithNode;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;

/**
 * Deprecated because MOLM's object should not hold View's components (referencing to MVC Pattern)
 * but just the Model data.
 */
@Deprecated
public class AnimationMOLMObjectsUpdater implements DoSomethingWithNode {
	private static final long serialVersionUID = -940980790L;
	private int milliseconds;
	// private RedBlackTree<Integer, ObjectTiled> jetUpdatedObjects

	public AnimationMOLMObjectsUpdater() {
		milliseconds = 1;
	}

	public void prepareForNewCycle(int milliseconds) {
		if (milliseconds > 0) {
			this.milliseconds = milliseconds;
		}
		// jetUpdatedObjects.clear();
	}

	@Override
	public Object doOnItem(AbstractMatrixObjectLocationManager molm, ObjectWithID item, int x, int y) {
		if (item != null && item instanceof ObjectGuiUpdatingOnFrame) {
			((ObjectGuiUpdatingOnFrame) item).updateGui(milliseconds);
		}
		return null;
	}
}
