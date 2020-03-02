package common.tests.testsLittle;

import java.util.function.BiConsumer;

import common.mainTools.MapTreeAVL;
import common.mainTools.MapTreeAVL.ForEachMode;

public class TestTreeAVL_Basic {

	public static void main(String[] args) {
		MapTreeAVL<Integer, Integer> t;
		BiConsumer<Integer, Integer> printer;
		TestAVLTree[] tests;
		t = new MapTreeAVL<>((x, y) -> {
			if (x == y)
				return 0;
			if (x == null)
				return -1;
			if (y == null)
				return 1;
			return Integer.compare(x, y);
		});
		printer = new Printer<>(t);// (k, v) -> System.out.print(k + " ");
		p(t);
		tests = new TestAVLTree[] { MapTreeAVL::testRotate//
				, MapTreeAVL::testAdd//
				, MapTreeAVL::testDelete };
		for (TestAVLTree test : tests) {
			System.out.println("\n NEW TEST\n");
			test.test(t, printer);
			t.clear();
		}
		System.out.println("\nEND");
	}

	static interface TestAVLTree {
		public void test(MapTreeAVL<Integer, Integer> t, BiConsumer<Integer, Integer> printer);
	}

	static class Printer<K, V> implements BiConsumer<K, V> {
		int size;
		MapTreeAVL<K, V> t;

		Printer(MapTreeAVL<K, V> t) {
			this.t = t;
			size = 0;
		}

		@Override
		public void accept(K k, V v) {
			if (size == 0) {
				System.out.print("Start printing: ");
				size = t.size;
			}
			if (--size > 0)
				System.out.print(k + " ");
			else
				System.out.println(k);
		}
	}

	private static void testDelete(MapTreeAVL<Integer, Integer> t, BiConsumer<Integer, Integer> printer) {
		Integer x;
		int[] ints = { 7, 4, 23, 9, 15, 10, 515, 78, 6, 1 }, indexes;
		BiConsumer<Integer, Integer> fullPrint;
		System.out.println("building tree to delete from");
		for (int i : ints) {
			x = i;
			t.put(x, x);
			p(t);
		}
		System.out.println("###################### Starting tree to delete from");
		p(t);
		t.forEach(printer);

		fullPrint = (k, v) -> {
			MapTreeAVL<Integer, Integer>.NodeAVL n;
			n = t.getNode(k);
			System.out.print(k);
			System.out.print(" ::: --nIs:" + n.nextInserted + "--");
			System.out.print(", --pIs:" + n.prevInserted + "--");
			System.out.print(",, --nIo:" + n.nextInOrder + "--");
			System.out.print(", --pIo:" + n.prevInOrder + "--");
			System.out.println();
		};
		t.forEach(fullPrint);

		System.out.println("§§§§§§§§§§ now delete\n\n");

		indexes = new int[] { 3, 0, 7, 6, 9, 23, 4, 1 };
		for (int i : indexes) {
			x = i;
			System.out.print("removing " + x + ": ");
			System.out.println(t.remove(x));
			// System.out.println("min: " + t.minValue);
			// System.out.println("last: " + t.lastInserted);
			p(t);
			t.forEach(printer);
			t.forEach(ForEachMode.Stack, printer);
			// t.forEach(fullPrint);
		}
	}

	private static void testAdd(MapTreeAVL<Integer, Integer> t, BiConsumer<Integer, Integer> printer) {
		Integer xx;
		System.out.println("\n\nADD: first add is 10");
		t.put(10, 10);
		p(t);
		t.forEach(printer);
		System.out.println("add 3 and 15..now 3:");
		t.put(3, 3);
		p(t);
		System.out.println("now 15");
		t.put(15, 15);
		p(t);
		System.out.println("now the tree grows bigger ...");
		t.put(12, 12);
		p(t);
		System.out.println("prepare to fixup...and....");
		System.out.println("add 11");
		t.put(11, 11);
		p(t);
		System.out.println("FIRST TIME FOR EACH PRINT");
		t.forEach(printer);
		System.out.println("stack and queue:");
		t.forEach(ForEachMode.Stack, printer);
		t.forEach(ForEachMode.Queue, printer);

		System.out.println("\n prepare for a big add");
		for (int x = 0; x < 28; x++) {
			System.out.println("___big adding: " + x);
			xx = x;
			t.put(xx, xx);
			p(t);
			System.out.println("^^");
		}
		p(t);

		t.clear();
		System.out.println("\n prepare for a big add");
		for (int x = 0; x < 28; x++) {
			xx = x;
			t.put(xx, xx);
		}
		t.forEach(printer);
		System.out.println("stack and queue:");
		t.forEach(ForEachMode.Stack, printer);
		t.forEach(ForEachMode.Queue, printer);
		p(t);
		t.put(48, 48);
		t.put(31, 31);
		for (int x = 28; x < 67; x++)
			t.put(xx = x, xx);
		p(t);
		t.forEach(ForEachMode.SortedDecreasing, printer);
		t.forEach(ForEachMode.SortedGrowing, printer);
		t.forEach(ForEachMode.Stack, printer);
		t.forEach(ForEachMode.Queue, printer);
	}

	private static void testRotate(MapTreeAVL<Integer, Integer> t, BiConsumer<Integer, Integer> printer) {
		MapTreeAVL<Integer, Integer>.NodeAVL n, n2;
		n = t.newNode(10, 10);
		t.root = n;
		System.out.println("add 0:");
		p(t);
		n2 = t.newNode(1, 1);
		n.left = n2;
		n2.father = n;
		n2 = t.newNode(33, 33);
		n.right = n2;
		n2.father = n;
		System.out.println("mini albero");
		p(t);
		//
		System.out.println("albero più pieno");
		n = t.root.left;
		n2 = t.newNode(0, 00);
		n.left = n2;
		n2.father = n;
		n2 = t.newNode(2, 2);
		n.right = n2;
		n2.father = n;
		n = t.root.right;
		n2 = t.newNode(15, 15);
		n.left = n2;
		n2.father = n;
		n2 = t.newNode(40, 40);
		n.right = n2;
		n2.father = n;
		// adjust height
		n = t.root;
		n.height = 2;
		n2 = n.left;
		n2.height = 1;
		n2.left.height = n2.right.height = 0;
		n2 = n.right;
		n2.height = 1;
		n2.left.height = n2.right.height = 0;
		p(t);
		//
		System.out.println("\n\n RUOTIAMO:\n");
		System.out.println("sx");
		t.root.rotate(false);
		p(t);
		System.out.println("dx");
		t.root.rotate(true);
		p(t);
		System.out.println("ora dall'altra parte");
		System.out.println("dx");
		t.root.rotate(true);
		p(t);
		System.out.println("sx");
		t.root.rotate(false);
		p(t);

	}

	private static void p(Object o) {
		System.out.println(o);
	}

}
