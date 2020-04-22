package common.abstractCommon;

import java.io.Serializable;

import common.mainTools.mOLM.MatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;

public interface AbstractMOLMFactory extends Serializable {

	public static final AbstractMOLMFactory DEFAULT_MOLM_FACTORY = //
			(AbstractMOLMFactory & Serializable) //
			// MOLM_TrackinOWIDs
			MatrixObjectLocationManager::newDefaultInstance;

	public AbstractMatrixObjectLocationManager newMolmInstance(int widthMicropixel, int heightMicropixel);

	//

	public static AbstractMOLMFactory getOrDefault(AbstractMOLMFactory amf) {
		return amf == null ? DEFAULT_MOLM_FACTORY : amf;
	}
}