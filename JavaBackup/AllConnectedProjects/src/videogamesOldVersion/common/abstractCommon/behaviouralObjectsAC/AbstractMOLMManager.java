package common.abstractCommon.behaviouralObjectsAC;

import java.util.List;

import common.FullReloadEnvironment;
import common.MOLMManager;
import common.abstractCommon.AbstractMOLMFactory;
import common.abstractCommon.Memento;
import common.abstractCommon.referenceHolderAC.MolmsHolder;
import common.abstractCommon.referenceHolderAC.ShapeSpecificationHolder;
import common.mainTools.Comparators;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager.AbstractMementoMOLM;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID.MementoOWID;
import tools.RedBlackTree;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;

/**
 * Collect and manage (closely) uniformly a group of
 * {@link AbstractMatrixObjectLocationManager}.
 */
public interface AbstractMOLMManager
		extends ObjectSerializableDoingNothingAfter, MolmsHolder, MementoPatternImplementor {
	public static final int DEFAULT_MOLMS_LENGTH = 2;

	public static AbstractMOLMManager newDefaultMOLMManager() {
		return new MOLMManager();
	}

	//

	// TODO GETTER

	public int getWidthMicropixel();

	public int getHeightMicropixel();

	public AbstractMOLMFactory getMolmFactory();

	public default int getMolmsLength() {
		return DEFAULT_MOLMS_LENGTH;
	}

	//

	// TODO SETTER

	public AbstractMOLMManager setWidthMicropixel(int widthMicropixel);

	public AbstractMOLMManager setHeightMicropixel(int heightMicropixel);

	public AbstractMOLMManager setMolmFactory(AbstractMOLMFactory molmFactory);

	public AbstractMOLMManager setMolmsLength(int lengthArray);

	//

	// TODO OTHER

	public default AbstractMatrixObjectLocationManager[] reallocMolms() {
		return reallocMolms(getWidthMicropixel(), getHeightMicropixel());
	}

	/**
	 * Realloc all AbstractMatrixObjectLocationManager_s if the dimensions
	 * changes
	 */
	public default AbstractMatrixObjectLocationManager[] reallocMolms(int widthMicropixel, int heightMicropixel) {
		int i, len;
		AbstractMatrixObjectLocationManager[] ms, molms;
		AbstractMOLMFactory molmFactory;
		ms = null;
		if (heightMicropixel > 1 && widthMicropixel > 1
				&& (getWidthMicropixel() != widthMicropixel || getHeightMicropixel() != heightMicropixel)) {
			molms = getMolms();
			this.setWidthMicropixel(widthMicropixel);
			this.setHeightMicropixel(heightMicropixel);
			// check and instance (if needed) the array
			ms = (molms != null && molms.length >= 1) ? molms
					: (new AbstractMatrixObjectLocationManager[getMolmsLength()]);
			molmFactory = AbstractMOLMFactory.getOrDefault(getMolmFactory());
			len = ms.length;
			i = -1;

			while (++i < len)
				// make the instances
				ms[i] = molmFactory.newMolmInstance(widthMicropixel, heightMicropixel);
			setMolms(ms);
		}
		return ms;
	}

	public default AbstractMatrixObjectLocationManager[] resizeMolms(int widthMicropixel, int heightMicropixel) {
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
				/*
				 * molmNonSolid.resizeMatrix(widthMicropixel, heightMicropixel);
				 * molmSolid.resizeMatrix(widthMicropixel, heightMicropixel);
				 */
				this.setWidthMicropixel(widthMicropixel);
				this.setHeightMicropixel(heightMicropixel);
			}
		}
		return molms;
	}

	//

	// modifiers

	/**
	 * This clears all instances of {@link AbstractMatrixObjectLocationManager}.
	 */
	/**
	 * public default void clearMolms() { //<br>
	 * AbstractMatrixObjectLocationManager[] molms; //<br>
	 * molms = getMolms(); //<br>
	 * if (molms != null && molms.length > 0) { //<br>
	 * for (AbstractMatrixObjectLocationManager molm : molms) { //<br>
	 * molm.clearMatrix(); //<br>
	 * } //<br>
	 * } //<br>
	 * }
	 */

	//

	/***/
	public default String addToMOLM(ObjectWithID owid) {
		return addToMOLM(owid, true);
	}

	public default String addToMOLM(ObjectWithID owid, boolean removePrec) {
		return doOnMOLM(owid, removePrec ? DoSomethingWithMolmShapespecOwid.ADDER_REMOVING_PREVIOUS
				: DoSomethingWithMolmShapespecOwid.ADDER, getMolmFromOWID(owid));
	}

	public default String removeFromMOLM(ObjectWithID owid) {
		return doOnMOLM(owid, DoSomethingWithMolmShapespecOwid.REMOVER, getMolmFromOWID(owid));
	}

	public default String clearMOLMs() {
		return clearMOLM(null);
	}

	public default String clearMOLM(AbstractMatrixObjectLocationManager preferredMolm) {
		return doOnMOLM(null, DoSomethingWithMolmShapespecOwid.CLEANER, preferredMolm);
	}

	public default String doOnMOLM(ObjectWithID owid, DoSomethingWithMolmShapespecOwid doSome) {
		return doOnMOLM(owid, doSome, null);
	}

	/**
	 * @param preferredMolm
	 *            if <code>null</code>, it means "all" molms.
	 */
	public default String doOnMOLM(ObjectWithID owid, DoSomethingWithMolmShapespecOwid doSome,
			AbstractMatrixObjectLocationManager preferredMolm) {
		String error;
		StringBuilder sb;
		AbstractMatrixObjectLocationManager[] molms;
		ShapeSpecification ss;

		if (doSome == null) return "ERROR: on " + this.getClass().getSimpleName()
				+ ".doOnMolm, cannot perform a null operation over molms.";
		ss = null;
		if (owid != null && (owid instanceof ShapeSpecificationHolder)) {
			ss = ((ShapeSpecificationHolder) owid).getShapeSpecification();
		}
		sb = new StringBuilder();
		error = null;

		if (preferredMolm == null) {
			// then, do for ALL molms
			molms = getMolms();
			if (molms != null && molms.length > 0) {
				for (AbstractMatrixObjectLocationManager molm : molms) {
					// let's do something
					error = doSome.doOnMolmShapespecOwid(molm, ss, owid);
					if (error != null) {
						if (sb.length() == 0) {
							sb.append("ERROR(s): on ").append(this.getClass().getSimpleName()).append(".doOnMolm:");
						}
						sb.append('\n').append('\t').append(error);
						error = null; // let the GC do its work
					}
				}
			}
		} else {
			error = doSome.doOnMolmShapespecOwid(preferredMolm, ss, owid);
			if (error != null) {
				sb.append("ERROR: on ").append(this.getClass().getSimpleName()).append(".doOnMolm:").append('\n')
						.append('\t').append(error);
				error = null; // let the GC do its work
			}
		}
		return sb.length() == 0 ? null : sb.toString();
	}

	//

	// TODO MEMENTO

	/** Override designed */
	public default void fillMementoWithMolmsInfo(AbstractMementoMOLMManager_OLD ammm) {
		int i, len;
		RedBlackTree<Integer, MementoOWID> allMementoOWIDFromMolms;
		AbstractMatrixObjectLocationManager.AbstractMementoMOLM mementosFromMolms[], mementoMolm;
		AbstractMatrixObjectLocationManager molms[], molm;
		List<ObjectWithID.MementoOWID> mementosOWIDRecorded;

		molms = getMolms();
		if (molms != null && (len = molms.length) > 0) {
			ammm.allMementoOWIDFromMolms = allMementoOWIDFromMolms = new RedBlackTree<>(Comparators.INTEGER_COMPARATOR);
			ammm.mementosFromMolms = mementosFromMolms = new AbstractMatrixObjectLocationManager.AbstractMementoMOLM[len];
			i = -1;
			while (++i < len) {
				/* for each MOLM */
				molm = molms[i];
				if (molm != null) {
					/* create its memento */
					mementoMolm = molm.createMemento();
					if (mementoMolm != null) {
						mementosFromMolms[i] = mementoMolm;
						/*
						 * now, save ObjectWithID' mementos into our list and
						 * make empty the molm's memento's ones : we don't want
						 * the same ObjectWithID instance create multiple
						 * mementos for each MolmManager's mementos
						 */
						mementosOWIDRecorded = mementoMolm.getMementosRecorded();
						if (mementosOWIDRecorded != null && mementosOWIDRecorded.size() > 0) {
							for (ObjectWithID.MementoOWID mowid : mementosOWIDRecorded) {
								// AbstractMatrixObjectLocationManager.addObjectWithIDFromItsMemento(molm,
								// mowid);
								allMementoOWIDFromMolms.add(mowid.getID(), mowid);
							}
							/* done, make mementosOWIDRecorded empty */
							mementosOWIDRecorded.clear();
						}
					}
				}
			}
		}
	}

	@Override
	public Memento createMemento();

	@Override
	public default boolean reloadState(Memento mem) {
		return reloadState(mem, null);
	}

	/** Returns true if the reload operation was successfull. */
	public default boolean reloadState(Memento mem, FullReloadEnvironment fre) {
		int i, len;
		AbstractMementoMOLMManager_OLD ammManager;
		AbstractMatrixObjectLocationManager molms[], molm;
		AbstractMementoMOLM amm;

		if (mem != null && mem instanceof AbstractMementoMOLMManager_OLD) {
			ammManager = (AbstractMementoMOLMManager_OLD) mem;
			setMolmFactory(AbstractMOLMFactory.getOrDefault(
					// ammManager.molmFactory
					null));
			//
			this.setMolms(molms = new AbstractMatrixObjectLocationManager[len = ammManager.mementosFromMolms.length]);
			// this.reallocMolms(ammm.widthMicropixel, ammm.heightMicropixel);
			/* re-creating all molms */
			i = -1;
			while (++i < len) {
				amm = ammManager.mementosFromMolms[i];
				if (amm != null) {
					molm = amm.reinstanceFromMe(fre);
					if (molm != null) molms[i] = molm;
				}
			}
			/*
			 * recreate OWID instances and add them to the associated MOLM,
			 * obtained via "getMolmFromNotSolidity" ... 12/12/20017 : jet done
			 * by "amm.reinstanceFromMe"
			 */
			/**
			 * ammManager.allMementoOWIDFromMolms.forEach((integ, mowid) ->
			 * {//<br>
			 * ObjectWithID owid;//<br>
			 * Object o;//<br>
			 * ShapeSpecificationHolder ssh;//<br>
			 * AbstractMatrixObjectLocationManager m;//<br>
			 * o = mowid.reinstanceFromMe();//<br>
			 * if (o != null && o instanceof ObjectWithID) {//<br>
			 * owid = (ObjectWithID) o;//<br>
			 * if (owid instanceof ShapeSpecificationHolder) {//<br>
			 * ssh = (ShapeSpecificationHolder) owid;//<br>
			 * // ok, can be added//<br>
			 * m = this.getMolmFromOWID(owid);//<br>
			 * if (m != null) m.addOnShape(ssh.getShapeSpecification(),
			 * owid);//<br>
			 * }//<br>
			 * if (owid instanceof MolmsHolder) //<br>
			 * ((MolmsHolder) owid).setMolms(molms);//<br>
			 * }//<br>
			 * });
			 */

			return true;
		}
		return false;
	}

	// TODO CLASS

	public static abstract class AbstractMementoMOLMManager extends Memento {
		private static final long serialVersionUID = -8948374102435037L;
		int widthMicropixel, heightMicropixel, sizeMolms;
		AbstractMatrixObjectLocationManager[] molms;

		public AbstractMementoMOLMManager() {
			super();
		}

		public AbstractMementoMOLMManager(AbstractMOLMManager amm) {
			this();
			this.widthMicropixel = amm.getWidthMicropixel();
			this.heightMicropixel = amm.getHeightMicropixel();
			this.sizeMolms = amm.getMolmsLength();
			this.molms = amm.getMolms();
		}

		/**
		 * WARNING: it did not call
		 * {@link AbstractMOLMManager#reallocMolms(int, int)}.<br>
		 * The caller must call it !!
		 */
		@Override
		public Object reinstanceFromMe() {
			return reinstanceFromMe(null);
		}

		/**
		 * WARNING: it did not call
		 * {@link AbstractMOLMManager#reallocMolms(int, int) }.<br>
		 * The caller must call it !!
		 */
		public abstract Object reinstanceFromMe(FullReloadEnvironment fre);
	}

	public static abstract class AbstractMementoMOLMManager_OLD extends AbstractMementoMOLMManager {
		private static final long serialVersionUID = -8948374102435038L;
		// int widthMicropixel, heightMicropixel, sizeMolms;
		// AbstractMOLMFactory molmFactory;
		/**
		 * don't wants any repetitions of ObjectWithID's memento .. usefull if
		 * the same ObjectWithID's has been added into different molm
		 */
		RedBlackTree<Integer, MementoOWID> allMementoOWIDFromMolms;
		AbstractMatrixObjectLocationManager.AbstractMementoMOLM[] mementosFromMolms;

		public AbstractMementoMOLMManager_OLD() {
			super();
		}

		public AbstractMementoMOLMManager_OLD(AbstractMOLMManager amm) {
			super(amm);
			this.molms = null;// we use another strategy
			// this.widthMicropixel = amm.getWidthMicropixel();
			// this.heightMicropixel = amm.getHeightMicropixel();
			// this.sizeMolms = amm.getSizeMolms();
			/*
			 * this.molmFactory = (amm.getMolmFactory() !=
			 * AbstractMOLMFactory.DEFAULT_MOLM_FACTORY) ? amm.getMolmFactory()
			 * : null;
			 */
			amm.fillMementoWithMolmsInfo(this);
		}

		/**
		 * WARNING: it did not call
		 * {@link AbstractMOLMManager#reallocMolms(int, int)}.<br>
		 * The caller must call it !!
		 */
		// public Object reinstanceFromMe() { return reinstanceFromMe(null); }

		/**
		 * WARNING: it did not call
		 * {@link AbstractMOLMManager#reallocMolms(int, int) }.<br>
		 * The caller must call it !!
		 */
		// public abstract Object reinstanceFromMe(FullReloadEnvironment fre);
	}
}