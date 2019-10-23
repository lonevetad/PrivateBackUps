package common.mainTools.mOLM;

import java.util.Map;

import common.FullReloadEnvironment;
import common.mainTools.Comparators;
import common.mainTools.dataStruct.MapTreeAVL;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;

public class MOLM_TrackinOWIDs extends MatrixObjectLocationManager {
	private static final long serialVersionUID = -41045081L;

	public MOLM_TrackinOWIDs() {
		super();
		reinstanceAllOWID();
	}

	public MOLM_TrackinOWIDs(int width, int height) {
		super(width, height);
		reinstanceAllOWID();
	}

	public MOLM_TrackinOWIDs(MementoMOLM m, FullReloadEnvironment fre) {
		super(m, fre);
		reinstanceAllOWID();
	}

	//

	// RedBlackTree
	Map<Integer, ObjectWithID> allOWID;

	//

	// TODO GETTER

	public Map<Integer, ObjectWithID> getAllOWID() {
		return allOWID;
	}

	//

	// TODO OTHER

	protected void reinstanceAllOWID() {
		allOWID = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.INTEGER_COMPARATOR);
	}

	public static MOLM_TrackinOWIDs newInstance() {
		return newInstance(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public static MOLM_TrackinOWIDs newInstance(int w, int h) {
		return new MOLM_TrackinOWIDs(w, h);
	}

	public static MOLM_TrackinOWIDs newDefaultInstance() {
		return newDefaultInstance(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public static MOLM_TrackinOWIDs newDefaultInstance(int w, int h) {
		return (MOLM_TrackinOWIDs) MOLM_TrackinOWIDs.newInstance(w, h).setPathFinder(PathFinder.getInstance())
				.setShapeRunners(ShapeRunners.getInstance());
	}

	//

	// TODO OVERRIDE

	@Override
	public void clearMatrix() {
		super.clearMatrix();
		allOWID.clear();
	}

	@Override
	public String addOnShape(ShapeSpecification ss, ObjectWithID toBeAdded) {
		String error;
		error = super.addOnShape(ss, toBeAdded);
		if (error != null)
			allOWID.put(toBeAdded.getID(), toBeAdded);
		return error;
	}

	@Override
	public String addOnShape(ShapeSpecification ss, ObjectWithID toBeAdded, boolean removePrevIfShapeSpecHolder) {
		String error;
		error = super.addOnShape(ss, toBeAdded, removePrevIfShapeSpecHolder);
		if (error != null)
			allOWID.put(toBeAdded.getID(), toBeAdded);
		return error;
	}

	@Override
	public String removeOnShape(ShapeSpecification ss, ObjectWithID toBeRemoved) {
		String error;
		error = super.removeOnShape(ss, toBeRemoved);
		if (error != null)
			allOWID.remove(toBeRemoved.getID());
		return error;
	}

	@Override
	public String removeOnShape(ShapeSpecification ss, ObjectWithID toBeRemoved, boolean removePrevIfShapeSpecHolder) {
		String error;
		error = super.removeOnShape(ss, toBeRemoved, removePrevIfShapeSpecHolder);
		if (error != null)
			allOWID.remove(toBeRemoved.getID());
		return error;
	}

	@Override
	public String replaceOnShape(ShapeSpecification ss, ObjectWithID toBeAdded, ObjectWithID toBeRemoved) {
		String error;
		error = super.replaceOnShape(ss, toBeAdded, toBeRemoved);
		if (error != null) {
			allOWID.remove(toBeRemoved.getID());
			allOWID.put(toBeAdded.getID(), toBeAdded);
		}
		return error;
	}

	@Override
	public String replaceOnShape(ShapeSpecification ss, ObjectWithID toBeAdded, ObjectWithID toBeRemoved,
			boolean removePrevIfShapeSpecHolder) {
		String error;
		error = super.replaceOnShape(ss, toBeAdded, toBeRemoved, removePrevIfShapeSpecHolder);
		if (error != null) {
			allOWID.remove(toBeRemoved.getID());
			allOWID.put(toBeAdded.getID(), toBeAdded);
		}
		return error;
	}

	//

	// TODO MEMENTO

	@Override
	public void processOWIDReloadedFromMemento(ObjectWithID owid) {
		if (owid != null)
			allOWID.put(owid.getID(), owid);
	}

	@Override
	public MementoMOLM createMemento() {
		return new MementoMOLM_Tracking(this);
	}

	// class

	public static class MementoMOLM_Tracking extends MementoMOLM {
		private static final long serialVersionUID = -89526050825007L;

		public MementoMOLM_Tracking() {
			super();
		}

		public MementoMOLM_Tracking(MOLM_TrackinOWIDs molm) {
			super(molm);
		}

		@Override
		public MOLM_TrackinOWIDs reinstanceFromMe(FullReloadEnvironment fre) {
			return new MOLM_TrackinOWIDs(this, fre);
		}
	}
}