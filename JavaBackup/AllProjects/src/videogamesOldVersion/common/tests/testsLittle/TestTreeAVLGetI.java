package common.tests.testsLittle;

import common.mainTools.MapTreeAVL;

public class TestTreeAVLGetI {

	public static void main(String[] args) {
		int j;
		Integer x;
		MapTreeAVL<Integer, Integer> t;
		t = new MapTreeAVL<>(Integer::compareTo);
		System.out.println("START");
		for (j = 0; j < 28; j++) {
			x = j;
			t.put(x, x);
		}
		System.out.println(t);
		while (--j >= 0)
			System.out.println(j + ": " + (x = t.get(j).getKey()) + ", eq? " + (j == x.intValue()));
		System.out.println("\n\n---------\n");
		j = t.size();
		for (j = t.size(); j < 97; j++) {
			x = j;
			t.put(x, x);
		}
		System.out.println(t);

		System.out.println("start test get on bigger");
		while (--j >= 0)
			if (j != (x = t.get(j).getKey()).intValue())
				System.out.println("err on: " + j + ": " + x);
		System.out.println("end test get on bigger");
		System.out.println("END");
	}
}