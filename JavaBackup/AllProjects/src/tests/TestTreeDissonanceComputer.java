package tests;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dataStructures.NodeComparable;
import tools.Comparators;

public class TestTreeDissonanceComputer {

	public TestTreeDissonanceComputer() {}

	NodeComparable<Integer> root;

	/** No null pointer checks are performed */
	void addNode(Integer v, List<Integer> path) {
		NodeComparable<Integer> newChild;
		newChild = NodeComparable.newDefaultNodeComparable(v, Comparators.INTEGER_COMPARATOR);
		if (path == null || path.isEmpty()) {
			root = newChild;
		} else {
			NodeComparable<Integer> nIter;
			nIter = root;
			System.out.println("adding " + v);
			Iterator<Integer> iter;
			Integer step;
			if (root == null)
				throw new NullPointerException();
			iter = path.iterator();
			step = iter.next();
			if (!step.equals(root.getKeyIdentifier()))
				return;

			while (iter.hasNext()) {
				step = iter.next();
				System.out.println("iter: " + nIter.getKeyIdentifier() + ", step: " + step);
				nIter = nIter.getChildNCByKey(step);
				System.out.println("and now is: " + nIter);
			}
			System.out.println("adding on iter: " + nIter.getKeyIdentifier());
			nIter.addChildNC(newChild);
		}
	}

	/** See {@link NodeComparable#computeDissonanceAsLong(NodeComparable)}. */
	public long computeDiff(TestTreeDissonanceComputer theBase) {
		if (this.root == theBase.root)
			return 0;
		if (this.root == null)
			return theBase.computeDiff(this);
		return this.root.computeDissonanceAsLong(theBase.root);
	}

	//

	//

	//

	@Override
	public String toString() {
		StringBuilder sb;
		sb = new StringBuilder();
		sb.append("Tree:");
		if (root == null)
			sb.append("nullo");
		else
			toString(sb, root, 0);
		return sb.toString();
	}

	public void toString(StringBuilder sb, NodeComparable<Integer> n, int level) {
		sb.append('\n');
		addTab(sb, level, false);
		sb.append(n.getKeyIdentifier());
		int lev = level + 1;
		n.forEachChildNC(c -> toString(sb, c, lev));
	}

	public void addTab(StringBuilder sb, int tabLevel, boolean newLineNeeded) {
		if (sb != null) {
			if (newLineNeeded)
				sb.append('\n');
			sb.ensureCapacity(sb.length() + tabLevel);
			while (tabLevel-- > 0) {
				sb.append('\t');
			}
		}
	}

	//

	//

	//

	public static void main(String[] args) {
		Integer x;
		TestTreeDissonanceComputer t, altro;
		LinkedList<Integer> l;
		t = new TestTreeDissonanceComputer();
		altro = new TestTreeDissonanceComputer();
		l = new LinkedList<>();
		System.out.println("START");
		d(altro, t);
		x = 7;
		ap(t, x, null);
		System.out.println("added");
		l.add(x);

		for (int i = 0; i < 4; i++)
			ap(t, i * 10, l);
//		d(altro, t);

		l.add(20);
		ap(t, 25, l);
		ap(t, 13, l);
		l.add(25);
		ap(t, 22, l);

		System.out.println("adding " + x + " to altro");
		altro.addNode(x, null);
		System.out.println(altro);
		d(altro, t);

		System.out.println("\n\nyay");
		l.clear();
		System.out.println("---------------------------");
		System.out.println("adding stuff to altro");
		l.add(x);
		altro.addNode(10, l);
		altro.addNode(20, l);
		System.out.println("AND now 10 and 20");
		System.out.println(altro);
		System.out.println("now add some other blabla");
//		altro.addNode(x, null);
		l.add(20);
		ap(altro, 25, l);
		l.add(25);
		ap(altro, 22, l);
		System.out.println("\n\n now do tests between");
		System.out.println(t);
		System.out.println(altro);
		System.out.println(":D");
		d(altro, t);

		System.out.println("now add -8");
		ap(altro, -8, l);
		System.out.println("\n\n do it again, between");
		System.out.println(t);
		System.out.println(altro);
		System.out.println(":D");
		d(altro, t);

		//

		System.out.println("\n\n\n[[[[[[[[[[[[[[[[ now complete altro");
		l.clear();
		l.add(x);
		altro.addNode(30, l);
		System.out.println("after added 30:");
		System.out.println(altro);
		altro.addNode(0, l);
		l.add(20);
		System.out.println("altro");
		ap(altro, 13, l);
		d(altro, t);

		System.out.println("fixing t by adding -8");
		l.add(25);
		ap(t, -8, l);
		System.out.println("diffff");
		d(altro, t);

		for (int i = 0; i < 5; i++)
			System.out.println("---------------------------");
		System.out.println("\n\n now grow them up");
		t.root = null;
		altro.root = null;
		t.addNode(x, null);
		altro.addNode(-3, null);
		pp(t, altro);
		d(altro, t);
		altro.addNode(x, null);
		d(altro, t);
		System.out.println("+++++++++++++ now make it more complex: 2 levels");
		pp(t, altro);
		l.clear();
		l.add(x);
		ap(t, 10, l);
		System.out.println(altro);
		d(altro, t);
		System.out.println("adding -3 and stuffs");
		altro.addNode(-3, l);
		d(altro, t);
		ap(altro, 10, l);
		pp(t, altro);
		d(altro, t);
		System.out.println("]]]]][[[[[level 3");
		l.add(10);
		ap(altro, 55, l);
		d(altro, t);
		l.removeLast();
		ap(t, 16, l);
		d(altro, t);
		pp(t, altro);
		System.out.println("now en-fat");
		ap(altro, 16, l);
		t.addNode(-3, l);
		l.add(10);
		t.addNode(55, l);
		l.removeLast();
		l.add(-3);
		t.addNode(-33, l);
		t.addNode(-74, l);
		altro.addNode(2, l);
		ap(altro, 44, Arrays.asList(7, 10));
		pp(t, altro);
		d(altro, t);
		System.out.println("now fix");
		t.addNode(2, l);
		altro.addNode(-33, l);
		altro.addNode(-74, l);
		l.add(2);
		t.addNode(222, l);
		altro.addNode(222, l);
		l.clear();
		l.add(7);
		l.add(10);
		t.addNode(44, l);
		pp(t, altro);
		d(altro, t);
	}

	static void pp(TestTreeDissonanceComputer t, TestTreeDissonanceComputer altro) {
		System.out.println("print em all");
		System.out.println(t);
		System.out.println(altro);
	}

	static void d(TestTreeDissonanceComputer toBeTested, TestTreeDissonanceComputer theBase) {
		System.out.print("\n\n testing differences:    ->");
		System.out.println(theBase.computeDiff(toBeTested));
	}

	static void ap(TestTreeDissonanceComputer t, Integer v, List<Integer> path) {
		System.out.println("\n adding: " + v);
		t.addNode(v, path);
		System.out.println("tree:");
		System.out.println(t);
		System.out.println();
	}

}