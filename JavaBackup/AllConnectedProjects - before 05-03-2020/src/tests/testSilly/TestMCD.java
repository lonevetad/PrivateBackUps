package tests.testSilly;

import tools.MathUtilities;

public class TestMCD {
	public static void main(String[] args) {
		int i, len;
		int[] a, b;
		a = new int[] { 8, 5, 8, 12, 13, 60, 4, 60, 60, 12321, 8888, 19, 1, 2 };
		b = new int[] { 4, 5, 12, 16, 13, 72, 55, 12, 81, 666, 1234321, 17, 1, 2 };
		len = a.length;
		i = -1;
		while (++i < len) {
			System.out.println("- " + i + " -> (" + a[i] + ", " + b[i] + ") : " + MathUtilities.mcd(a[i], b[i]));
		}
	}
}