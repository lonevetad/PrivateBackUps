package common;

import java.awt.Graphics;

import common.mainTools.Comparators;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;
import common.mainTools.mOLM.abstractClassesMOLM.PainterMOLMNullItem;
import tools.RedBlackTree;

public class PainterMolmPaintingOncePerRun extends PainterMolm {

	private static final long serialVersionUID = 1L;

	public PainterMolmPaintingOncePerRun() {
		super();
		commonCostructor();
	}

	public PainterMolmPaintingOncePerRun(Graphics g) {
		super(g);
		commonCostructor();
	}

	public PainterMolmPaintingOncePerRun(Graphics g, PainterMOLMNullItem pmoni) {
		super(g, pmoni);
		commonCostructor();
	}

	//

	protected RedBlackTree<Integer, ObjectWithID> objectsPaintedOnLastRun;

	//

	protected void commonCostructor() {
		objectsPaintedOnLastRun = new RedBlackTree<>(Comparators.INTEGER_COMPARATOR);
	}

	//

	public static PainterMolmPaintingOncePerRun newInstance() {
		return newInstance(PainterMOLMNullItem.newInstance_MicropixelPurpose());
	}

	public static PainterMolmPaintingOncePerRun newInstance(PainterMOLMNullItem pmoni) {
		return new PainterMolmPaintingOncePerRun(null, pmoni);
	}

	//

	@Override
	public void prepareForPaintingRun() {
		objectsPaintedOnLastRun.clear();
	}

	@Override
	public boolean canBePainted(ObjectWithID o) {
		return super.canBePainted(o) && (!objectsPaintedOnLastRun.hasKey(o.getID()));
	}

	@Override
	public void processAfterPaint(ObjectWithID o) {
		objectsPaintedOnLastRun.add(o.getID(), o);
	}
}
