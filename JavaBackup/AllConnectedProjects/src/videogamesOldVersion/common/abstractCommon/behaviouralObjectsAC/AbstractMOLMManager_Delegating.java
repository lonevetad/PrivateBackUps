package common.abstractCommon.behaviouralObjectsAC;

import common.abstractCommon.AbstractMOLMFactory;
import common.abstractCommon.Memento;
import common.abstractCommon.referenceHolderAC.MolmsHolder;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;

/**
 * I let this class throwing {@link NullPointerException} if the delegated instance is null because:
 * <ul>
 * <li>i want to help debug</li>
 * <li>i'm lazy</li>
 * </ul>
 */
/*
 * Ragion d'esistere di questa classe : avere tutti metodi di AbstractMOLMManager senza doverli
 * ridefinire ogni volta.<br> Proxy-like pattern.
 */
public interface AbstractMOLMManager_Delegating extends AbstractMOLMManager {

	public AbstractMOLMManager getMolmManagerDelegated();
	/*
	 * { return molmManagerDelegated; }
	 */

	public MolmsHolder setMolmManagerDelegated(AbstractMOLMManager molmManagerDelegated);
	/*
	 * { this. molmManagerDelegated = molmManagerDelegated; return this; }
	 */

	public default AbstractMOLMManager_Delegating setDefaultMolmManagerDelegated() {
		setMolmManagerDelegated(AbstractMOLMManager.newDefaultMOLMManager());
		return this;
	}

	// AbstractMOLMManager molmManagerDelegated;

	//

	// TODO DELEGATED

	@Override
	public default int getMolmsLength() {
		return getMolmManagerDelegated().getMolmsLength();
	}

	@Override
	public default int getWidthMicropixel() {
		return getMolmManagerDelegated().getWidthMicropixel();
	}

	@Override
	public default int getHeightMicropixel() {
		return getMolmManagerDelegated().getHeightMicropixel();
	}

	@Override
	public default AbstractMatrixObjectLocationManager[] getMolms() {
		return getMolmManagerDelegated().getMolms();
	}

	@Override
	public default AbstractMOLMFactory getMolmFactory() {
		return getMolmManagerDelegated().getMolmFactory();
	}

	//

	@Override
	public default AbstractMOLMManager setMolmsLength(int lengthArray) {
		getMolmManagerDelegated().setMolmsLength(lengthArray);
		return this;
	}

	@Override
	public default AbstractMOLMManager setWidthMicropixel(int widthMicropixel) {
		getMolmManagerDelegated().setWidthMicropixel(widthMicropixel);
		return this;
	}

	@Override
	public default AbstractMOLMManager setHeightMicropixel(int heightMicropixel) {
		getMolmManagerDelegated().setHeightMicropixel(heightMicropixel);
		return this;
	}

	@Override
	public default MolmsHolder setMolms(AbstractMatrixObjectLocationManager[] molms) {
		getMolmManagerDelegated().setMolms(molms);
		return this;
	}

	@Override
	public default AbstractMOLMManager setMolmFactory(AbstractMOLMFactory molmFactory) {
		getMolmManagerDelegated().setMolmFactory(molmFactory);
		return this;
	}

	//

	@Override
	public default AbstractMatrixObjectLocationManager getMolmFromOWID(ObjectWithID o) {
		return getMolmManagerDelegated().getMolmFromOWID(o);
	}

	@Override
	public default AbstractMatrixObjectLocationManager getMolmFromNotSolidity(boolean isNotSolid) {
		return getMolmManagerDelegated().getMolmFromNotSolidity(isNotSolid);
	}

	@Override
	public default AbstractMatrixObjectLocationManager[] reallocMolms() {
		return getMolmManagerDelegated().reallocMolms();
	}

	@Override
	public default AbstractMatrixObjectLocationManager[] reallocMolms(int widthMicropixel, int heightMicropixel) {
		return getMolmManagerDelegated().reallocMolms(widthMicropixel, heightMicropixel);
	}

	@Override
	public default AbstractMatrixObjectLocationManager[] resizeMolms(int widthMicropixel, int heightMicropixel) {
		return getMolmManagerDelegated().resizeMolms(widthMicropixel, heightMicropixel);
	}

	//

	@Override
	public default String addToMOLM(ObjectWithID owid) {
		return getMolmManagerDelegated().addToMOLM(owid);
	}

	@Override
	public default String addToMOLM(ObjectWithID owid, boolean removePrec) {
		return getMolmManagerDelegated().addToMOLM(owid, removePrec);
	}

	@Override
	public default String removeFromMOLM(ObjectWithID owid) {
		return getMolmManagerDelegated().removeFromMOLM(owid);
	}

	@Override
	public default String clearMOLMs() {
		return getMolmManagerDelegated().clearMOLM(null);
	}

	@Override
	public default String clearMOLM(AbstractMatrixObjectLocationManager preferredMolm) {
		return getMolmManagerDelegated().clearMOLM(preferredMolm);
	}

	//

	@Override
	public default String doOnMOLM(ObjectWithID owid, DoSomethingWithMolmShapespecOwid doSome) {
		return getMolmManagerDelegated().doOnMOLM(owid, doSome);
	}

	//

	@Override
	public default Memento createMemento() {
		return getMolmManagerDelegated().createMemento();
	}

}