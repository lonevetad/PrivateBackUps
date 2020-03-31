package common.abstractCommon.referenceHolderAC;

import java.io.Serializable;
import java.util.Iterator;

import common.abstractCommon.behaviouralObjectsAC.ObjectSerializableDoingNothingAfter;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;

public interface MolmsHolder
		extends ObjectSerializableDoingNothingAfter, Iterable<AbstractMatrixObjectLocationManager> {

	public AbstractMatrixObjectLocationManager[] getMolms();

	/** Delegate here cache-settings like molmNonSolid and molmSolid */
	public MolmsHolder setMolms(AbstractMatrixObjectLocationManager[] molms);

	/**
	 * Override designed.<br>
	 * Describe how to fetch a particular instance of
	 * {@link AbstractMatrixObjectLocationManager} starting
	 */
	public default AbstractMatrixObjectLocationManager getMolmFromOWID(ObjectWithID o) {
		return o == null ? null : getMolmFromNotSolidity(o.isNotSolid());
	}

	public default AbstractMatrixObjectLocationManager getMolmFromNotSolidity(boolean isNotSolid) {
		AbstractMatrixObjectLocationManager[] molms;
		molms = getMolms();
		if (molms == null || molms.length <= 0) return null;
		if (molms.length == 1) return molms[0];
		return isNotSolid ? molms[0] : molms[1];
		// this.molmNonSolid : molmSolid;
	}

	@Override
	public default Iterator<AbstractMatrixObjectLocationManager> iterator() {
		return new MolmsIterator(this);
	}

	//

	//

	public static class MolmsIterator implements Iterator<AbstractMatrixObjectLocationManager>, Serializable {
		private static final long serialVersionUID = -89561068728L;
		int len, i;
		AbstractMatrixObjectLocationManager[] molms;

		MolmsIterator(MolmsHolder mh) {
			this.molms = mh.getMolms();
			len = molms == null ? -1 : molms.length;
			i = 0;
		}

		@Override
		public boolean hasNext() {
			return len >= 0 && i < len;
		}

		@Override
		public AbstractMatrixObjectLocationManager next() {
			if (hasNext())
				return molms[i++];
			else
				throw new RuntimeException("Molms finished");
		}

	}
}