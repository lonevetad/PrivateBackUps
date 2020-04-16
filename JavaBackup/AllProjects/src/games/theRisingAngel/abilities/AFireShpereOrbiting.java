package games.theRisingAngel.abilities;

import java.awt.Point;

import games.generic.controlModel.GModality;
import games.generic.controlModel.inventoryAbil.EquipItemAbility;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import geometry.AbstractShape2D;
import geometry.ObjectShaped;
import geometry.implementations.shapes.ShapeCircle;

// TODO to be completed
public class AFireShpereOrbiting extends AOrbitingDamagingObj implements EquipItemAbility {
	private static final long serialVersionUID = 1L;
	public static final int BLOB_RADIUS = 3, BLOB_RADIUS_FROM_CENTRE = 7;
	public static final String NAME = "Planetary Fire Spheres";

	public AFireShpereOrbiting() {
		super();
	}

	protected EquipmentItem eqipItem; // related to this ability

	@Override
	public void move(GModality gm, int milliseconds) {
		// TODO Auto-generated method stub

	}

	@Override
	public EquipmentItem getEquipItem() {
		return eqipItem;
	}

	@Override
	public void setEquipItem(EquipmentItem equipmentItem) {
		this.eqipItem = equipmentItem;
	}

	@Override
	public void setShape(AbstractShape2D shape) {
		// TODO Auto-generated method stub

	}

	@Override
	public ObjectShaped newOrbitingObj() {
		return new FlamingOrb();
	}

	@Override
	protected void updateCenterPosition(GModality modality) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getOrbitingObjectRadius(int index, ObjectShaped os) {
		return orbitingObjectRadius;
	}

	//

	//

	class FlamingOrb implements ObjectShaped {
		private static final long serialVersionUID = 1L;
		ShapeCircle shape;

		FlamingOrb() {
			Point p;
			p = getLocation();
			shape = new ShapeCircle(p.x, p.y, true, BLOB_RADIUS);
		}

		@Override
		public Integer getID() {
			return null; // don't care
		}

		@Override
		public AbstractShape2D getShape() {
			return shape;
		}

		@Override
		public void setShape(AbstractShape2D shape) {
			this.shape = (ShapeCircle) shape;
		}

	}
}