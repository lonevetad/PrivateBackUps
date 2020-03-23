package games.theRisingAngel;

/**
 * 
 * Each Equipment should implements the set of "character statistics modifiers"
 * as follows (maybe through a class")
 * <p>
 * Set of power ups, of statistics' modifiers (i.e., character's parameters'
 * modifiers).<br>
 * Instead of create an array (that mimics the character's statistics) and apply
 * to that array all modifiers, wasting memory in almost-empty arrays, just
 * collect all statistic modifications.
 */
public abstract class EquipmentSetTRAr {

	protected static final int firstIndexRings, firstIndexNecklace, firstIndexBracelets;
	static {
		firstIndexNecklace = EquipmentTypes.Necklace.ordinal();
		firstIndexBracelets = firstIndexNecklace + EquipmentTypes.NECKLACE_AMOUNT;
		firstIndexRings = firstIndexBracelets + EquipmentTypes.BRACELET_AMOUNT;
	}

	protected final EquipmentItem[] equippedItems;

	public EquipmentSetTRAr() {
		equippedItems = new EquipmentItem[EquipmentTypes.TOTAL_AMOUNT_EQUIPMENTS_WEARABLES];
	}

	/**
	 * Get the set of {@link EquipmentItem}.<br>
	 * NOTE: unused slots (i.e. "places" where there is no equipped objects for that
	 * type) are <code>null</code>.
	 */
	public EquipmentItem[] getEquippedItems() {
		return equippedItems;
	}

	/** NOTE: Should check if is yet equipped an intem in that slot */
	public void addEquipmentItem(EquipmentItem ei) {
		EquipmentTypes et;
		if (ei == null)
			return;
		et = ei.getEquipmentType();
		switch (et) {
//		case Earrings:break; // are put at the beginning
		case Rings:
			addRing((EIRing) ei);
			break;
		case Necklace:
			addNecklaceOrBracelet(ei, firstIndexNecklace, EquipmentTypes.NECKLACE_AMOUNT);
			break;
		case Bracelet:
			addNecklaceOrBracelet(ei, firstIndexBracelets, EquipmentTypes.BRACELET_AMOUNT);
			break;
		default:
			int i;
			i = ei.getEquipmentType().ordinal();
			// everybody else
			if (equippedItems[i] == null) {
				equippedItems[i] = ei;
			} else {
				swapEquipmentItem(ei, equippedItems[i]);
			}
			break;
		}
	}

	// TODO
	public abstract void addEquipmentItemAt(EquipmentItem ei, int index);

	protected void addNecklaceOrBracelet(EquipmentItem ei, int index, int equipItemSlotToCheck) {
		boolean notAdded;
		int firstIndex;
		firstIndex = index;
		notAdded = true;
		while(notAdded && equipItemSlotToCheck-- >= 0) {
			if (equippedItems[index] == null) {
				equippedItems[index] = ei;
				notAdded = false;
			}
			equipItemSlotToCheck++;
			index++;
		}
		if (notAdded) {
			swapEquipmentItem(ei, equippedItems[firstIndex]);
		}
	}

	protected void addRing(EIRing ring) {
		boolean notAdded;
		int s, i, fingersLeftToCheck, slotIndexMinimum;
		notAdded = true;
		s = ring.getSlotFingerSize();
//		lastAvailableIndex = firstIndexNecklace;
		if (s == 1) {
			// first check the first slots, then second slot (to distribute)
			slotIndexMinimum = 0;
			do {
				fingersLeftToCheck = EquipmentTypes.TOTAL_FINGERS_AMOUNT;
				i = firstIndexRings + slotIndexMinimum;
//				while(notAdded && i <= lastAvailableIndex) {
				while(notAdded && fingersLeftToCheck-- >= 0) {
					if (equippedItems[i] == null) {
						equippedItems[i] = ring;
						notAdded = false;
					}
					i += EquipmentTypes.RING_SLOTS_EACH_FINGERS; // jump to the next finger
				}
			} while(notAdded && ++slotIndexMinimum < EquipmentTypes.RING_SLOTS_EACH_FINGERS);
			if (notAdded) {
				swapEquipmentItem(ring, equippedItems[firstIndexRings]);
			}
		} else { // >= 2
			int maxIndexToCheck, k;
			// cercare prima un posto libero
			// poi se fallisce, cercare le prime due dita con un solo anello
			// se trovate, allora compattare ed inserire

			// looks for the first finger having enough "place"
			slotIndexMinimum = 0; // used as "starting slot
			maxIndexToCheck = 1 + (EquipmentTypes.RING_SLOTS_EACH_FINGERS - s);
			do {
				fingersLeftToCheck = EquipmentTypes.TOTAL_FINGERS_AMOUNT;
				i = firstIndexRings + slotIndexMinimum;
//				while(notAdded && i <= lastAvailableIndex) {
				while(notAdded && fingersLeftToCheck-- >= 0) {
					// check all slots, an amount of slots equal to th ring's size
					k = 0;
					while(equippedItems[i + k] == null && ++k < s)
						;
					if (k == maxIndexToCheck) { // found where to place the item
						while(--k > 0) { // place the ring
							equippedItems[i + k] = ring;
						}
						notAdded = false;
					}
					i += EquipmentTypes.RING_SLOTS_EACH_FINGERS; // jump to the next finger
				}
			} while(notAdded && ++slotIndexMinimum < maxIndexToCheck);
		}
	}

	/** TODO use player's inventory and ei's location */
	public abstract void swapEquipmentItem(EquipmentItem newEI, EquipmentItem oldEI);

}
