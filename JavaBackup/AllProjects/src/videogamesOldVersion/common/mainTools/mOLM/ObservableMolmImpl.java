package common.mainTools.mOLM;

import java.util.LinkedList;
import java.util.List;

import common.mainTools.mOLM.abstractClassesMOLM.ObservableMolm;
import common.mainTools.mOLM.abstractClassesMOLM.ObserverMolm;

public class ObservableMolmImpl implements ObservableMolm {
	private static final long serialVersionUID = -51108870087012656L;

	public ObservableMolmImpl() {
		observers = new LinkedList<>();
	}

	List<ObserverMolm> observers;

	@Override
	public List<ObserverMolm> getMolmObservers() {
		return observers;
	}

	@Override
	public ObservableMolm setMolmObservers(List<ObserverMolm> observers) {
		this.observers = observers;
		return this;
	}
}