package games.generic.controlModel.inventoryAbil;

/**
 * Items designed for inventory purpose but not directly equippable, like
 * potions.<br>
 * Most usefull for RPG.
 */
public abstract class InventoryItemNotEquippable extends InventoryItem {
	private static final long serialVersionUID = 47193518837000L;

	public InventoryItemNotEquippable(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
}