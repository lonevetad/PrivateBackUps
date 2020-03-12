package common.mainTools.mOLM.abstractClassesMOLM;

import java.io.Serializable;

import common.abstractCommon.behaviouralObjectsAC.AbstractMOLMManager;
import common.mainTools.mOLM.abstractClassesMOLM.ObservableMolm.MolmEvent;

public interface ObserverMolm extends Serializable {
	/**
	 * See
	 * {@link ObservableMolm#notifyMolmObservers(MolmEvent, Object, AbstractMOLMManager)}.
	 */
	public void update(MolmEvent me, Object o, AbstractMOLMManager molmManager
	// , int indexMolm
	);
}