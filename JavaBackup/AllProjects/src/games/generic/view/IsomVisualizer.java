package games.generic.view;

import java.util.Map;

import dataStructures.MapTreeAVL;
import dataStructures.isom.NodeIsom;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.controlModel.gObj.ObjectInSpace;
import geometry.implementations.shapes.ShapeRectangle;
import tools.Comparators;

public abstract class IsomVisualizer extends GuiComponent {

	public IsomVisualizer(GameView view) {
		super(view);
		cameraView = new ShapeRectangle();
		objectPainted = null;
	}

	protected ShapeRectangle cameraView;
//	protected Rectangle cameraView;
	protected Map<Integer, ObjectInSpace> objectPainted;
	protected Drawer drawer;

	public ShapeRectangle getCameraView() { return cameraView; }

	public Drawer getDrawer() { return drawer; }

	public void setDrawer(Drawer drawer) { this.drawer = drawer; }

	/**
	 * Upon moving the main character, or just to see another part of the map, just
	 * move the camera position to a position (the given point is the top-left
	 * corner).
	 */
	public void setCameraPosition(int x, int y) {
		int w, h;
		w = this.cameraView.getWidth();
		h = this.cameraView.getHeight();
		this.cameraView.setCenter(x + (w >> 1), y + (h >> 1));
	}

	public void setCameraViewDimension(int width, int height) {
		int x, y;
		int w, h;
		ShapeRectangle sr;
		sr = this.cameraView;
		w = sr.getWidth();
		h = sr.getHeight();
		x = sr.getXCenter();
		y = sr.getYCenter();
		sr.setWidth(width);
		sr.setHeight(height);
		sr.setCenter(x + ((width - w) >> 1), y + ((height - h) >> 1));
	}

	/** Should uses {@link #getDrawer()}. */
	protected abstract void paintObject(ObjectInSpace o);

	protected void paintOn(/* and a Graphics or consumer */) {
		final Map<Integer, ObjectInSpace> m;
		GModality gm;
		GObjectsInSpaceManager goism;
		gm = this.getGameModality();
		if (gm == null)
			return;
		goism = gm.getGObjectInSpaceManager();
		if (goism == null)
			return;
		if (objectPainted == null) {
			objectPainted = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
		}
		m = objectPainted;
		m.clear();
		goism.runOnShape(this.cameraView, p -> {
			int objAmount;
			NodeIsom n;
			ObjectInSpace o;
			Integer id;
			n = goism.getNodeAt(p);
			if (n == null)
				return;
			objAmount = n.countObjectAdded();
			while (--objAmount >= 0) {
				o = (ObjectInSpace) n.getObject(objAmount);
				id = o.getID();
				if (!m.containsKey(id)) {
					paintObject(o);
					m.put(id, o);
				}
			}
		});
	}

	//

	//

//	protected class 
}