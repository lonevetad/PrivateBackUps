package games.generic.view.dataProviders;

import java.util.function.Consumer;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.view.GameView;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ObjectShaped;
import geometry.ProviderShapesIntersectionDetector;

/**
 * Iterates over ALL object held into {@link GObjectsInSpaceManager} (obtained
 * through {@link GModality#getGObjectInSpaceManager()}) and, if they are inside
 * the given area, provide them.<br>
 * No repetitions should be provided (it depends on
 * {@link GObjectsInSpaceManager#forEach(Consumer)} implementation).
 */
public class DrawableObjProviderObjFiltering extends DrawableObjProvider {

	public DrawableObjProviderObjFiltering(GameView gameView) { super(gameView); }

	@Override
	public void forEachObjInArea(AbstractShape2D shape, Consumer<ObjectLocated> action) {
		GModality gm;
		GObjectsInSpaceManager goism;
		ProviderShapesIntersectionDetector psid;
//		InSpaceObjectsManager<Double> isom;
		gm = this.gameView.getCurrentModality();
		if (gm == null)
			return;
		goism = gm.getGObjectInSpaceManager();
		if (goism == null)
			return;
		psid = goism.getProviderShapesIntersectionDetector();
		if (psid == null)
			return;
		goism.forEach(owid -> {
			ObjectLocated ol;
			if (owid instanceof ObjectLocated) {
				ol = (ObjectLocated) owid;
				if (ol instanceof ObjectShaped) {
					if (psid.areIntersecting(shape, ((ObjectShaped) ol).getShape()))
						action.accept(ol);
				} else if (shape.contains(ol.getLocation())) { action.accept(ol); }
			}
		});
	}
}
