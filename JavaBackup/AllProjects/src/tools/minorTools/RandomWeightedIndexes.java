package tools.minorTools;

import java.util.Arrays;
import java.util.Random;

/**
 * Extract randomly an index (from 0 to <code>length -1</code>) based on those
 * indexes' weights.
 */
public class RandomWeightedIndexes {

	public RandomWeightedIndexes(int[] values, Random r) {
		if (r == null)
			throw new IllegalArgumentException("Null Random instance");
		this.rng = r;
		setValues(values);
	}

	public RandomWeightedIndexes(int[] values) {
		this(values, new Random());
	}

	public RandomWeightedIndexes(int[] values, long seed) {
		this(values, new Random(seed));
	}

	protected long sumValues;
	protected int[] values;
	protected long[] summedValues;
	protected transient Random rng;

	//

	protected void setValues(int[] v) {
		int i, n, val;
		long s;
		long[] sv;
		if (v == null || (n = v.length) < 2)
			throw new IllegalArgumentException("arrays of values is null or with few elemens");
		i = -1;
		s = 0;
		sv = new long[n];
		while(++i < n) {
			if ((val = v[i]) <= 0)
				throw new IllegalArgumentException("Values must be strictly positive, error at index: " + i);
			s += val;
			sv[i] = s;
		}
		this.values = new int[n];
		System.arraycopy(v, 0, this.values, 0, n);
		this.sumValues = s;
		this.summedValues = sv;
	}

	public int getWeight(int index) {
		return this.values[index];
	}

	/**
	 * WARNINGS: To modify ALL values, just create a new instance: this method is
	 * <code>O(n)</code>.
	 */
	public void setWeight(int index, int weight) {
		int oldval, n;
		long newSum;
		int[] vals;
		long[] sv;
		if (weight <= 0)
			throw new IllegalArgumentException("Values must be strictly positive: " + weight);
		vals = this.values; // cache
		oldval = vals[index];
		this.sumValues -= oldval;
		vals[index] = weight;
		newSum = index == 0 ? 0 : vals[--index]; // "--" to use the "++index"
		// update the summed values
		n = vals.length;
		sv = this.summedValues; // cache
		while(++index < n) {
			sv[index] = (newSum += vals[index]);
		}
//		this.sumValues = --sv[n - 1]; // the max cap is excluded
	}

	public int next() {
		boolean isLess;
		int i, j, mid;
		long v;
		long[] vals;
		v = sumValues;
		if (v < Integer.MAX_VALUE)
			v = rng.nextInt((int) v);
		else {
			v = rng.nextLong();
			if (v > 0)
				v %= sumValues;
			else if (v < 0)
				v = (v % sumValues) + sumValues;
		}
		System.out.print(" (v:" + v + ") ");
//		System.out.println("\t\t(v:" + v + ") ");
		j = (vals = this.summedValues).length - 1;
		i = 0;
		while(i < j) {
			mid = (i + j) >> 1;
//			System.out.println("\t\t\t(i:" + i + ", mid:" + mid + ", j:" + j + ")");
			if (mid == 0)
				return 0;
//		else if(mid==(v.length-1)) // will be prevented by while's condition
			isLess = v < vals[mid];
			if (vals[mid - 1] <= v && isLess)
				return mid;
			if (isLess)
				j = mid;
			else {
				if (i == mid)
					i++;
				else
					i = mid;
			}
			/*
			 * else else if (vals[mid - 1] <= v && v < vals[mid]) { return mid; } else { j =
			 * mid; }
			 */

		}
		return i;
	}

	public static void main(String[] args) {
		int i;
		int[] vals, indexes;
		RandomWeightedIndexes rw;
		vals = new int[] { 12, 7, 20, 4, 8, 32, 3 };
		rw = new RandomWeightedIndexes(vals);
		indexes = new int[vals.length];
		System.out.println(Arrays.toString(vals));
		for (i = 0; i < vals.length; i++)
			indexes[i] = 0;
		for (int r = 1000; r > 0; r--) {
			System.out.print(r + " -> ");
			i = rw.next();
			System.out.println("\t" + i);
			indexes[i]++;
		}
		System.out.println(Arrays.toString(rw.summedValues));
		System.out.println(Arrays.toString(indexes));
	}
}