package javaMechanism;

public class MethodsStrange {

	public static void main(String[] args) {
		test_intToCharArray();
	}

	public static char[] intToCharArray(int x) {
		boolean isNeg;
		int leftMostDigit, digitCount, temp;
		char[] c;
		digitCount = 2; // almeno una cifra + lo '\0'
		leftMostDigit = 0;
		if (isNeg = (x < 0)) {
			digitCount++; // il segno meno
			x = -x;
		}
		if (x < 10) {
			// una cifra sola
			digitCount++;
			c = // (char*) malloc(
					new char[digitCount];
			// * sizeof(char) ); // in C si fa cosi', in C++ ??
			c[digitCount - 1] = '\0';
			if (isNeg) c[leftMostDigit++] = '-'; // il segno meno
			c[leftMostDigit] = (char) ('0' + x);
			return c;
		}

		temp = x;
		// calcoliamo il logaritmo base 10, ossia il numero di cifre
		while (temp >= 10) {
			digitCount++;
			temp /= 10; // operazione lenta, ma non so come farla meglio ..
		}
		c = // (char*) malloc(
				new char[digitCount];
		// * sizeof(char) ); // in C si fa cosi', in C++ ??
		c[--digitCount] = '\0';
		if (isNeg) c[leftMostDigit++] = '-'; // il segno meno
		while (--digitCount >= leftMostDigit) {
			c[digitCount] = (char) ('0' + (x % 10)); // modulo
			x /= 10;
		}
		return c;
	}

	//

	// TODO TESTS

	public static void test_intToCharArray() {
		int[] a;
		// String s;

		a = new int[] { 7, 5, -3, 0, 12, 25, -33, 144, 1024, -666, -54321 };

		for (int x : a) {
			System.out.println(x + "\t: " + new String(intToCharArray(x)));
		}
	}
}