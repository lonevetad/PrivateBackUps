package common.abstractCommon.behaviouralObjectsAC;

import java.util.ArrayList;
import java.util.List;

import common.MOLMManager;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;
import common.mainTools.mOLM.abstractClassesMOLM.ObservableMolm;
import common.mainTools.mOLM.abstractClassesMOLM.ObserverMolm;

public class MOLMManagerObservable extends MOLMManager implements ObservableMolm {
	private static final long serialVersionUID = 84501560265206L;

	public MOLMManagerObservable() {
		super();
		molmObservers = new ArrayList<>();
	}

	List<ObserverMolm> molmObservers;

	@Override
	public List<ObserverMolm> getMolmObservers() {
		return molmObservers;
	}

	@Override
	public ObservableMolm setMolmObservers(List<ObserverMolm> observers) {
		this.molmObservers = observers;
		return this;
	}

	// override

	@Override
	public AbstractMOLMManager setMolms(AbstractMatrixObjectLocationManager[] molms) {
		if (molms != null && molms.length > 0) {
			AbstractMatrixObjectLocationManager[] molmsPrev;
			molmsPrev = getMolms();
			super.setMolms(molms);
			notifyMolmObservers(MolmEvent.MolmsInstancesChanged, molmsPrev, this);
		}
		return this;
	}

	@Override
	public AbstractMatrixObjectLocationManager[] resizeMolms(int widthMicropixel, int heightMicropixel) {
		AbstractMatrixObjectLocationManager[] molms;
		molms = null;
		if (heightMicropixel > 1 && widthMicropixel > 1) {
			molms = getMolms();
			if (molms == null || molms.length <= 0) {
				// || molmNonSolid == null || molmSolid == null)
				reallocMolms(widthMicropixel, heightMicropixel);
				molms = getMolms();
			} else {
				// ms = molms;
				for (AbstractMatrixObjectLocationManager molm : molms) {
					molm.resizeMatrix(widthMicropixel, heightMicropixel);
				}
				this.setWidthMicropixel(widthMicropixel);
				this.setHeightMicropixel(heightMicropixel);
				notifyMolmObservers(MolmEvent.MapResized, this);
			}
		}
		return molms;
	}

	@Override
	public String addToMOLM(ObjectWithID owid, boolean removePrec) {
		String error;
		error = super.addToMOLM(owid, removePrec);
		if (error != null) notifyMolmObservers(MolmEvent.ObjectAdded, owid, this);
		return error;
	}

	@Override
	public String removeFromMOLM(ObjectWithID owid) {
		String error;
		error = super.removeFromMOLM(owid);
		if (error != null) notifyMolmObservers(MolmEvent.ObjectRemoved, owid, this);
		return error;
	}

	@Override
	public String clearMOLM(AbstractMatrixObjectLocationManager preferredMolm) {
		String error;
		error = super.clearMOLM(preferredMolm);
		if (error != null) notifyMolmObservers(MolmEvent.MapCleaned, preferredMolm, this);
		return error;
	}
}