package tools.minorStuffs;

public class Itoa {

	/** Rewritten from C */
	public static char[] itoa(int n) {
		boolean isNegative;
		int i, minIndex;
		char[] buff;
		char[] ret;
		buff = new char[32];
		i = minIndex = 0;
		if (isNegative = n < 0) {
			n = -n;
			minIndex = 1;
		}
		while (n > 9) {
			buff[i++] = (char) ('0' + (n % 10));
			n /= 10;
		}
		buff[i++] = (char) ('0' + (n % 10));
//		ret = (char*) malloc( (i+minIndex+1)*sizeof(char) );
		ret = new char[(i + minIndex + 1)];
		ret[i] = '\0';
		if (isNegative)
			ret[0] = '-';

		while (--i >= 0)
			ret[minIndex++] = buff[i];
		return ret;
	}

	public static void main(String[] args) {
		int[] a;
		a = new int[] { 32, 458, 1, 0, -96, 1024, 3, 7856, 123456 };
		for (int n : a)
			System.out.println(itoa(n));
	}
}