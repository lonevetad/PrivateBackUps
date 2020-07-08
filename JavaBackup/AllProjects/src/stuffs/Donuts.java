package stuffs;

import java.util.Arrays;

/**
 * Original code <a href=
 * "https://www.dropbox.com/s/79ga2m7p2bnj1ga/donut_deobfuscated.c?dl=0">here</a>.
 */
public class Donuts {
	public static void main(String[] args) {
		double A = 0, B = 0;
		double i, j;
		int k, tw;
		double[] z = new double[1760]; /* not 7040? */
		char[] b = new char[1760];
		System.out.println("\u001b[2J");
		tw = args.length > 0 ? Integer.parseInt(args[0]) : 1;
		if (tw < 0)
			tw = 0;
		while (true) {
			Arrays.fill(b, (char) 32);
			Arrays.fill(z, (char) 0); // what about 7040?
			for (j = 0; j < 6.28; j += 0.07) {
				for (i = 0; i < 6.28; i += 0.02) {
					double c = Math.sin(i);
					double d = Math.cos(j);
					double e = Math.sin(A);
					double f = Math.sin(j);
					double g = Math.cos(A);
					double h = d + 2;
					double D = 1 / (c * h * e + f * g + 5);
					double l = Math.cos(i);
					double m = Math.cos(B);
					double n = Math.sin(B);
					double t = c * h * g - f * e;
					int x = (int) (40 + 30 * D * (l * h * m - t * n));
					int y = (int) (12 + 15 * D * (l * h * n + t * m));
					int o = x + 80 * y;
					int N = (int) (8 * ((f * e - c * d * g) * m - c * d * e - f * g - l * d * n));
					if (22 > y && y > 0 && x > 0 && 80 > x && D > z[o]) {
						z[o] = D;
						b[o] = ".,-~:;=!*#$@".charAt(N > 0 ? N : 0);
					}
				}
			}
			System.out.println("\u001b[H"); // printf("\x1b[H");
			for (k = 0; k < 1760; k++) { // 1761 o 1760?
				// putchar(k % 80 ? b[k] : 10);
				System.out.print(k % 80 != 0 ? b[k] : 10);
				A += 0.00004;
				B += 0.00002;
			}
			try {
				if (tw > 0)
					Thread.sleep(tw);
				cls();
			} catch (Exception exc) {
			}
		}
	}

	private static boolean clsMode = true;
	private static Runnable clsFn = null;
	private static Process p;
	static {
		if (System.getProperty("os.name").contains("Windows"))
			clsFn = () -> {
				try {
					(p != null ? p : (p = new ProcessBuilder("cmd", "/c", "cls").inheritIO().start())).waitFor();
				} catch (Exception e) {
					clsExc(e);
				}
			};
		else
			clsFn = () -> {
				try {
					Runtime.getRuntime().exec("clear");
				} catch (Exception e) {
					clsExc(e);
				}
			};
	}

	static void clsExc(Exception e) {
		e.printStackTrace();
		clsMode = false;
		clsFn = null;
	}

	static void cls() {
		if (clsMode)
			clsFn.run();
		else {
			System.out.print("\033[H\033[2J");
			System.out.flush();
		}
	}
}