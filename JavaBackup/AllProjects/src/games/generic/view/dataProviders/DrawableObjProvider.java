package games.generic.view.dataProviders;

import java.util.Set;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import dataStructures.isom.InSpaceObjectsManager;
import games.generic.controlModel.GModality;
import games.generic.view.GameView;
import games.generic.view.IsomPainter;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.implementations.shapes.ShapeRectangle;
import tools.Comparators;

/**
 * Class able to provide all {@link ObjectLocated} held by a
 * {@link InSpaceObjectsManager} (provided by
 * {@link GModality#getGObjectInSpaceManager()}) and present in some
 * {@link AbstractShape2D} (usually it's the camera: a {@link ShapeRectangle},
 * as defined in {@link IsomPainter}).
 * <p>
 * And, for those who are wondering: yes, it's NOT a IGuiComponent subclass, it
 * just shares the gameView field.
 */
public abstract class DrawableObjProvider {

	public DrawableObjProvider(GameView gameView) { this.gameView = gameView; }

	protected GameView gameView;

	//

	public GameView getGameView() { return gameView; }

	public void setGameView(GameView gameView) { this.gameView = gameView; }

	/**
	 * The core method.<br>
	 * The possible implementation could:
	 * <ul>
	 * <li>just run into the area and pick the object.</li>
	 * <li>Or just filter a set of object accepting the ones inside the area.</li>
	 * </ul>
	 */
	public abstract void forEachObjInArea(AbstractShape2D shape, Consumer<ObjectLocated> action);

	public Set<ObjectLocated> getObjInArea(AbstractShape2D shape) {
		Set<ObjectLocated> s;
		MapTreeAVL<Integer, ObjectLocated> collected;
		collected = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
		s = collected.toSetValue(ObjectLocated.KEY_EXTRACTOR);
		this.forEachObjInArea(shape, ol -> { if (ol != null) { collected.put(ol.getID(), ol); } });
		return s;
	}
}