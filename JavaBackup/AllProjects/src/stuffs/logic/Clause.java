package stuffs.logic;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.function.Function;

import dataStructures.MapTreeAVL;

public class Clause {

	public Clause() {
		literalBackMap = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, AtomKey.COMPARATOR);
		literals = literalBackMap.toSetValue(AtomKey.KEY_EXTRACTOR); // toSetKey();
	}

	protected MapTreeAVL<AtomKey, AtomLogicProposition> literalBackMap;
	protected SortedSet<AtomLogicProposition> literals;

	//

	public void clear() { literalBackMap.clear(); }

	public boolean containsLiteral(boolean isPositive, int id) {
//		return containsLiteral(new AtomLogicProposition(isPositive, id));
		return this.literalBackMap.containsKey(new AtomKey(isPositive, id));
	}

	protected boolean containsLiteral(AtomKey a) { return this.literalBackMap.containsKey(a); }

	public boolean containsLiteral(String name) {
		AtomLogicProposition a;
		a = AtomLogicProposition.fromString(name);
		if (a == null)
			return false;
		return containsLiteral(a);
	}

	public boolean containsLiteral(AtomLogicProposition a) { return literals.contains(a); }

	public void addLiteral(boolean isPositive, int id) { addLiteral(new AtomLogicProposition(isPositive, id)); }

	public void addLiteral(String name) {
		AtomLogicProposition a;
		a = AtomLogicProposition.fromString(name);
		if (a == null)
			return;
		addLiteral(a);
	}

	public void removeLiteral(AtomLogicProposition a) { literalBackMap.remove(AtomKey.KEY_EXTRACTOR.apply(a)); }

	public void removeLiteral(boolean isPositive, int id) { literalBackMap.remove(new AtomKey(isPositive, id)); }

	public void removeLiteral(String name) {
		AtomLogicProposition a;
		a = AtomLogicProposition.fromString(name);
		if (a == null)
			return;
		removeLiteral(a);
	}

	public void addLiteral(AtomLogicProposition a) { literalBackMap.put(AtomKey.KEY_EXTRACTOR.apply(a), a); }

	public boolean isEmpty() { return this.literalBackMap.isEmpty(); }

	public int unitCount() { return this.literalBackMap.size(); }

	public int size() { return this.literalBackMap.size(); }

	public boolean isUnit() { return unitCount() == 1; }

	/** Used for algorithm like DPPL. */
	public OccurrenceAtomResult occurrenceCheck(AtomLogicProposition a) {
		AtomKey ank;
		AtomKey ak;
		ak = AtomKey.KEY_EXTRACTOR.apply(a);
		ank = new AtomKey(!a.isPositive, a.ID);
		return (a.isPositive) ? oc(ak, ank) : oc(ank, ak);
	}

	public OccurrenceAtomResult occurrenceCheck(boolean isPositive, int id) {
//		return occurrenceCheck(new AtomLogicProposition(isPositive, id));
		return oc(new AtomKey(isPositive, id), new AtomKey(!isPositive, id));
	}

	public OccurrenceAtomResult occurrenceCheck(String name) {
		AtomLogicProposition a;
		a = AtomLogicProposition.fromString(name);
		if (a == null)
			return OccurrenceAtomResult.Unknown;
		return occurrenceCheck(a);
	}

	protected OccurrenceAtomResult oc(AtomKey a, AtomKey an) {
		boolean ha, hna;
		if (!a.isPositive) { // swap
			AtomKey t;
			t = an;
			an = a;
			a = t;
		}
		ha = containsLiteral(a);
		hna = containsLiteral(an);
		if (ha) {
			return hna ? OccurrenceAtomResult.BothOccurrence : OccurrenceAtomResult.OnlyPositive;
		} else {
			return hna ? OccurrenceAtomResult.OnlyNegative : OccurrenceAtomResult.Absent;
		}
	}

	//

	public static enum OccurrenceAtomResult {
		Absent, OnlyPositive, OnlyNegative, BothOccurrence, Unknown
	}

	protected static class AtomKey {
		boolean isPositive;
		Integer ID;
		public static final Comparator<AtomKey> COMPARATOR = (c1, c2) -> {
			if (c1 == c2)
				return 0;
			if (c1 == null)
				return -1;
			if (null == c2)
				return +1;
			if (c1.ID.equals(c2.ID)) {
				return (c1.isPositive == c2.isPositive) ? 0 : //
				c1.isPositive ? 1 : -1;
			}
			return c1.ID > c2.ID ? 1 : -1;
		};
		public static final Function<AtomLogicProposition, AtomKey> KEY_EXTRACTOR = a -> new AtomKey(a.isPositive,
				a.ID);

		public AtomKey(boolean isPositive, Integer ID) {
			super();
			this.isPositive = isPositive;
			this.ID = ID;
		}
	}

}