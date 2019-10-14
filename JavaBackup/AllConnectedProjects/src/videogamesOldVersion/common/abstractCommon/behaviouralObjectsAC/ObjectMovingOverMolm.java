package common.abstractCommon.behaviouralObjectsAC;

import java.io.Serializable;

import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;

public interface ObjectMovingOverMolm extends ObjectMoving, Serializable, AbstractMOLMManager_Delegating {

	public default ObjectWithID getObjectWithID() {
		return this instanceof ObjectWithID ? (ObjectWithID) this : null;
	}

	@Override
	public default void move(int milliseconds) {
		boolean owidNotNull;
		ObjectWithID owid;
		if (canMove()) {
			owid = getObjectWithID();
			owidNotNull = owid != null;
			if (owidNotNull) removeFromMOLM(owid);
			executeMoving(milliseconds);
			if (owidNotNull) addToMOLM(owid);
		}
	}
}
