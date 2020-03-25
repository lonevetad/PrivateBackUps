package games.theRisingAngel.inventory;

import games.generic.controlModel.GModality;
import games.generic.controlModel.inventory.EquipmentItem;
import games.generic.controlModel.inventory.EquipmentSet;
import games.theRisingAngel.creatures.BaseNPCCreatureTRAr;

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
public class EquipmentSetTRAr extends EquipmentSet {

	protected static final int firstIndexRings, firstIndexNecklace, firstIndexBracelets;
	static {
		firstIndexNecklace = EquipmentTypesTRAr.Necklace.ordinal();
		firstIndexBracelets = firstIndexNecklace + EquipmentTypesTRAr.NECKLACE_AMOUNT;
		firstIndexRings = firstIndexBracelets + EquipmentTypesTRAr.BRACELET_AMOUNT;
	}

	protected final EquipmentItem[] equippedItems;

	public EquipmentSetTRAr() {
		equippedItems = new EquipmentItem[EquipmentTypesTRAr.TOTAL_AMOUNT_EQUIPMENTS_WEARABLES];
	}

	@Override
	public EquipmentItem[] getEquippedItems() {
		return equippedItems;
	}

	// TODO
	// TODO
	// TODO
	// TODO
	// TODO
//	public void addEquipmentItemAt(GModality gm, EquipmentItem ei, int index) {  }

	@Override
	public void swapEquipmentItem(GModality gm, EquipmentItem newEI, EquipmentItem oldEI) {
	}

	// TODO
	// TODO
	// TODO
	// TODO
	// TODO

	@Override
	public void addEquipmentItem(GModality gm, EquipmentItem ei) {
		EquipmentTypesTRAr et;
		if (ei == null)
			return;
		et = (EquipmentTypesTRAr) ei.getEquipmentType();
		switch (et) {
//		case Earrings:break; // are put at the beginning
		case Rings:
			addRing(gm, (EIRing) ei);
			break;
		case Necklace:
			addNecklaceOrBracelet(gm, ei, firstIndexNecklace, EquipmentTypesTRAr.NECKLACE_AMOUNT);
			break;
		case Bracelet:
			addNecklaceOrBracelet(gm, ei, firstIndexBracelets, EquipmentTypesTRAr.BRACELET_AMOUNT);
			break;
		default:
			int i;
			i = ei.getEquipmentType().getIndex(); // id as index
			// everybody else
			if (equippedItems[i] == null) {
				equippedItems[i] = ei;
				equipAt(gm, ei, i);
			} else {
				swapEquipmentItem(gm, ei, equippedItems[i]);
			}
			break;
		}
	}

	//

	//

	protected void equipAt(GModality gm, EquipmentItem ei, int index) {
		equippedItems[index] = ei;
		ei.setBelongingEquipmentSet(this);
		ei.onEquip(gm);
	}

	protected void addNecklaceOrBracelet(GModality gm, EquipmentItem ei, int index, int equipItemSlotToCheck) {
		boolean notAdded;
		int firstIndex;
		firstIndex = index;
		notAdded = true;
		while(notAdded && equipItemSlotToCheck-- >= 0) {
			if (equippedItems[index] == null) {
				equippedItems[index] = ei;
				ei.setBelongingEquipmentSet(this);
				notAdded = false;
			}
			equipItemSlotToCheck++;
			index++;
		}
		if (notAdded) {
			swapEquipmentItem(gm, ei, equippedItems[firstIndex]);
		}
	}

	protected void addRing(GModality gm, EIRing ring) {
		boolean notAdded;
		int s, i, fingersLeftToCheck, slotIndexMinimum;
		notAdded = true;
		s = ring.getSlotFingerSize();
//		lastAvailableIndex = firstIndexNecklace;
		if (s == 1) {
			// first check the first slots, then second slot (to distribute)
			slotIndexMinimum = 0;
			do {
				fingersLeftToCheck = EquipmentTypesTRAr.TOTAL_FINGERS_AMOUNT;
				i = firstIndexRings + slotIndexMinimum;
//				while(notAdded && i <= lastAvailableIndex) {
				while(notAdded && fingersLeftToCheck-- >= 0) {
					if (equippedItems[i] == null) {
						equipAt(gm, ring, i);
						notAdded = false;
					}
					i += EquipmentTypesTRAr.RING_SLOTS_EACH_FINGERS; // jump to the next finger
				}
			} while(notAdded && ++slotIndexMinimum < EquipmentTypesTRAr.RING_SLOTS_EACH_FINGERS);
			if (notAdded) {
				swapEquipmentItem(gm, ring, equippedItems[firstIndexRings]);
			}
		} else { // >= 2
			int maxIndexToCheck, k;
			// cercare prima un posto libero
			// poi se fallisce, cercare le prime due dita con un solo anello
			// se trovate, allora compattare ed inserire

			// looks for the first finger having enough "place"
			slotIndexMinimum = 0; // used as "starting slot
			maxIndexToCheck = 1 + (EquipmentTypesTRAr.RING_SLOTS_EACH_FINGERS - s);
			do {
				fingersLeftToCheck = EquipmentTypesTRAr.TOTAL_FINGERS_AMOUNT;
				i = firstIndexRings + slotIndexMinimum;
//				while(notAdded && i <= lastAvailableIndex) {
				while(notAdded && fingersLeftToCheck-- >= 0) {
					// check all slots, an amount of slots equal to th ring's size
					k = 0;
					while(equippedItems[i + k] == null && ++k < s)
						;
					if (k == maxIndexToCheck) { // found where to place the item
						while(--k > 0) { // place the ring, ignore the 0: it's used in equipAt
							equippedItems[i + k] = ring;
						}
						equipAt(gm, ring, i);
						notAdded = false;
					}
					i += EquipmentTypesTRAr.RING_SLOTS_EACH_FINGERS; // jump to the next finger
				}
			} while(notAdded && ++slotIndexMinimum < maxIndexToCheck);
		}
	}

	//

	//

	//

	//

	//

	public static void main(String[] args) {
		BaseNPCCreatureTRAr c;
		EquipmentSetTRAr est;
		c = new BaseNPCCreatureTRAr();
		est = (EquipmentSetTRAr) c.getEquipmentSet();
		est.setCreatureWearingEquipments(c);

		// TODO aggiungere anelli
	}
}