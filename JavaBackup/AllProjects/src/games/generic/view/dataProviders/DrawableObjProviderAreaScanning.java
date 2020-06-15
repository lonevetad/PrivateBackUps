package games.generic.view.dataProviders;

import java.util.Map;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import dataStructures.isom.NodeIsom;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.controlModel.gObj.ObjectInSpace;
import games.generic.view.GameView;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.Comparators;

/**
 * Scan the WHOLE area searching for object and prove them.<br>
 */
public class DrawableObjProviderAreaScanning extends DrawableObjProvider {

	public DrawableObjProviderAreaScanning(GameView gameView) {
		super(gameView);
		objectPainted = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
	}

	protected Map<Integer, ObjectInSpace> objectPainted;

	@Override
	public void forEachObjInArea(AbstractShape2D shape, Consumer<ObjectLocated> action) {
		GModality gm;
		GObjectsInSpaceManager goism;
		final Map<Integer, ObjectInSpace> m;
		Consumer<ObjectLocated> oisPainter;
//		InSpaceObjectsManager<Double> isom;
		gm = this.gameView.getCurrentModality();
		if (gm == null)
			return;
		goism = gm.getGObjectInSpaceManager();
		if (goism == null)
			return;
//		isom = goism.getOIMManager();
//		isom.

		// * NOTE: beware of repetitions!
		(m = this.objectPainted).clear();
		oisPainter = ol -> {
			Integer id;
			ObjectInSpace ois;
			ois = (ObjectInSpace) ol;
			id = ol.getID();
			if (!m.containsKey(id)) {
				m.put(id, ois);
				action.accept(ois);
			}
		};
		goism.runOnShape(shape, p -> {
			NodeIsom n;
			n = goism.getNodeAt(p);
			if (n == null)
				return;
			n.forEachHeldObject(oisPainter);
		});
	}

}