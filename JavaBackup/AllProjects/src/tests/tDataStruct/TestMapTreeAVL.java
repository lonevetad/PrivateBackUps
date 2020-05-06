package tests.tDataStruct;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;

public class TestMapTreeAVL {

	public TestMapTreeAVL() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		int[] vals;
		MapTreeAVL<Integer, Integer> t;
		Comparator<Integer> c;
		Integer x;
		Consumer<Entry<? extends Integer, ? extends Integer>> printer;

		c = Integer::compareTo;
//		c = (i1, i2) -> {
//			if (i1 == i2)
//				return 0;
//			if (i1 == null)
//				return -1;
//			if (i2 == null)
//				return 1;
//			return Integer.compare(i1, i2);
//		};
		printer = e -> System.out.println(e);
		t = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, MapTreeAVL.BehaviourOnKeyCollision.Replace,
				c);
		System.out.println(t.getClass());
		System.out.println(t);
		vals = new int[] { 2, -2, -3, 0, -2, 5, 4, -4, 10, 0, 3, 100 };
		for (int i : vals) {
			x = i;
			ap(t, x);
		}
		System.out.println("\n\n\n\n++++++++++++++++++++");
		System.out.println("added " + vals.length + " values");
		vals = null;
		t.forEach(printer);

		System.out.println("\n\n remove root: ");
		t.remove(2);
		System.out.println(t);
		t.forEach(printer);

		System.out.println("\n\n remove a leaf: 4");
		t.remove(4);
		System.out.println(t);
		t.forEach(printer);

		System.out.println("\n\n remove min: -4");
		t.remove(-4);
		System.out.println(t);
		t.forEach(printer);
		System.out.println("min is: " + t.peekMinimum().getKey() + ", max:" + t.peekMaximum().getKey() + "\n\n");

		// 1-child
		System.out.println("now add two numbers to create a single-child, one left and one right");
		x = 4;
		t.put(x, x);
		x = 101;
		t.put(x, x);
		System.out.println(t);
		System.out.println("now remove their father: 5 and 100");
		t.remove(5);
		System.out.println(t);
		t.remove(100);
		System.out.println(t);
		t.forEach(printer);

		System.out.println("now remove non-root two child: 10");
		t.remove(10);
		System.out.println(t);
		t.forEach(printer);
		System.out.println("\n min is: " + t.peekMinimum().getKey() + ", max:" + t.peekMaximum().getKey() + "\n");

		System.out.println("add 1 and remove 101");
		x = 1;
		t.put(x, x);
		System.out.println(t);
		t.remove(101);
		System.out.println(t);

		//

		t.clear();
		System.out.println("\n\n\n\n redo \n\n\n");
		for (int i = -32; i < 31; i++) {
			x = i * 1000;
			t.put(x, x);
		}
		System.out.println(t);
		System.out.println("remove 0");
		t.remove(0);
		System.out.println(t);
		System.out.println("remove -1");
		t.remove(-1000);
		System.out.println(t);

		System.out.println("\n\n.....now some more spiky removes: 18, 20 and then 19");
		t.remove(18000);
		t.remove(20000);
		System.out.println("now remove 19:");
		System.out.println(t);
		t.remove(19000);
		System.out.println(t);
		System.out.println("now remove 21:");
		t.remove(21000);
		System.out.println(t);

		System.out.println("\n\n --- now remove 16 and 22:");
		t.remove(16000);
		System.out.println(t);
		System.out.println("..remove 22:");
		t.remove(22000);
		System.out.println(t);

		System.out.println("\n\n --- now remove 30 and 28, making 29 unstable.. will it be a huge difference?");
		t.remove(30000);
		System.out.println(t);
		t.remove(28000);
		System.out.println(t);

		System.out.println("\n\n --- make it big: remove 15");
		t.remove(15000);
		System.out.println(t);

		System.out.println("adding 10500 and 10600");
		x = 10500;
		t.put(x, x);
		x = 10600;
		t.put(x, x);
		System.out.println(t);

		System.out.println("\n\n\n looool clear, add 3 items and remove root");
		t.clear();
		for (int i = 0; i < 3; i++) {
			x = i;
			t.put(x, x);
		}
		System.out.println(t);
		System.out.println("now remove the root");
		t.remove(1);
		System.out.println(t);
		System.out.println("\n now remove min");
		t.removeMinimum();
		System.out.println(t);
		System.out.println("refill 5 elements and then remove min, max and root");
		for (int i = 0; i < 5; i++) {
			x = i;
			t.put(x, x);
		}
		System.out.println(t);
		System.out.println(t.removeMinimum());
		System.out.println(t);
		System.out.println(t.removeMaximum());
		System.out.println(t);
		System.out.println(t.remove(2));
		System.out.println(t);
		System.out.println("there are 2 elements now, remove those elements by removing minimum");
		System.out.println(t.removeMinimum());
		System.out.println(t.removeMinimum());
		System.out.println(t);
		System.out.println("now min is? " + t.peekMinimum());
		System.out.println("now max is? " + t.peekMaximum());

//

		System.out.println("\n\n\n\n\n now reproduce the ones on notebook");
		t.clear();
		vals = new int[] { 10, 7, 15, 4, 9, 13, 20, 2, 5, 8, 14 };
		for (int i : vals) {
			x = i;
			t.put(x, x);
		}
		vals = null;
		System.out.println(t);

		System.out.println("\n now remove max");
		System.out.println(t.removeMaximum());
		System.out.println(t);

		System.out.println("\n now remove min");
		System.out.println(t.removeMinimum());
		System.out.println(t);

		System.out.println("\n\n\n\n remove 2.0\n\n\n");
		t.clear();
		for (int i = 0; i < 31; i++) {
			x = i;
			t.put(x, x);
		}
		System.out.println(t);
		vals = new int[] { 1, 11, 23, 15, 24, 25, 30, 26, 29, 21 };
		System.out.println("removing: " + Arrays.toString(vals));
		for (int i : vals) {
			x = i;
			t.remove(x);
			System.out.println("\n after removing " + i + "we get:");
			System.out.println(t);
		}
	}

	static void ap(MapTreeAVL<Integer, Integer> t, Integer x) {
		System.out.println("\n-----");
		System.out.println("adding: " + x);
		t.put(x, x);
		System.out.println(t);
		System.out.println(t.peekMinimum() + " - " + t.peekMaximum());
	}
}
