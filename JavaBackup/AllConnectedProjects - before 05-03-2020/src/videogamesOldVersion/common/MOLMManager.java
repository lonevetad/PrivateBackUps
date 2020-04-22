package common;

import common.abstractCommon.AbstractMOLMFactory;
import common.abstractCommon.behaviouralObjectsAC.AbstractMOLMManager;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;

public class MOLMManager implements AbstractMOLMManager {
	private static final long serialVersionUID = 56160698509L;
	public static final int DEFAULT_MOLM_WIDTH_MICROPIXEL = 32,
			DEFAULT_MOLM_HEIGHT_MICROPIXEL = DEFAULT_MOLM_WIDTH_MICROPIXEL;

	public MOLMManager() {
		super();
		molmsLength = DEFAULT_MOLMS_LENGTH;
	}

	public MOLMManager(MementoMOLMManager mmm, FullReloadEnvironment fre) {
		this();
		this.reloadState(mmm, fre);
	}

	protected int widthMicropixel, heightMicropixel, molmsLength;
	protected AbstractMatrixObjectLocationManager[] molms;
	transient AbstractMOLMFactory molmFactory;

	//

	// TODO GETTER

	@Override
	public int getWidthMicropixel() {
		return widthMicropixel;
	}

	@Override
	public int getHeightMicropixel() {
		return heightMicropixel;
	}

	@Override
	public AbstractMatrixObjectLocationManager[] getMolms() {
		return molms;
	}

	@Override
	public AbstractMOLMFactory getMolmFactory() {
		if (molmFactory == null) this.molmFactory = AbstractMOLMFactory.getOrDefault(null);
		return molmFactory;
	}

	@Override
	public int getMolmsLength() {
		return molmsLength;
	}

	//

	// TODO SETTER

	/**
	 * DO NOT USE ! OR YOU COULD BREAK THE FRAMEWORK, INTRODUCING BUGS IN YOUR
	 * CODE !
	 */
	@Override
	public AbstractMOLMManager setWidthMicropixel(int widthMicropixel) {
		this.widthMicropixel = widthMicropixel;
		return this;
	}

	/**
	 * DO NOT USE ! OR YOU COULD BREAK THE FRAMEWORK, INTRODUCING BUGS IN YOUR
	 * CODE !
	 */
	@Override
	public AbstractMOLMManager setHeightMicropixel(int heightMicropixel) {
		this.heightMicropixel = heightMicropixel;
		return this;
	}

	/**
	 * DO NOT USE ! OR YOU COULD BREAK THE FRAMEWORK, INTRODUCING BUGS IN YOUR
	 * CODE !
	 */
	@Override
	public AbstractMOLMManager setMolms(AbstractMatrixObjectLocationManager[] molms) {
		if (molms != null && molms.length > 0) {
			this.molms = molms;
			this.molmsLength = molms.length;
		}
		return this;
	}

	@Override
	public AbstractMOLMManager setMolmFactory(AbstractMOLMFactory molmFactory) {
		this.molmFactory = AbstractMOLMFactory.getOrDefault(molmFactory);
		return this;
	}

	@Override
	public AbstractMOLMManager setMolmsLength(int lengthArray) {
		if (lengthArray > 0 && lengthArray != molmsLength) {
			this.molmsLength = lengthArray;
			reallocMolms(widthMicropixel, heightMicropixel);
		}
		return this;
	}

	//

	// TODO OTHER

	//

	// TODO INHERITED

	//

	// TODO MEMENTO

	/**
	 * public boolean reloadState(Memento m) {//<br>
	 * MementoMOLMManager mmm;//<br>
	 * if (m != null && m instanceof MementoMOLMManager) {//<br>
	 * mmm = (MementoMOLMManager) m;//<br>
	 * AbstractMOLMManager.super.reloadState(mmm);//<br>
	 * return true;//<br>
	 * }//<br>
	 * return false;//<br>
	 * }
	 */

	@Override
	public MementoMOLMManager createMemento() {
		return new MementoMOLMManager(this);
	}

	// class

	public static class MementoMOLMManager extends AbstractMementoMOLMManager {
		private static final long serialVersionUID = -8940828298707237L;

		public MementoMOLMManager() {
			super();
		}

		public MementoMOLMManager(MOLMManager o) {
			super(o);
		}

		@Override
		public MOLMManager reinstanceFromMe(FullReloadEnvironment fre) {
			return new MOLMManager(this, fre);
		}
	}
}