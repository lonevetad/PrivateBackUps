package shitter;

public class TestIntegerStream {

	public TestIntegerStream() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		long seed;
		IntegerStream is;
		is = new IntegerStream(15);
		seed = System.currentTimeMillis();
		System.out.println("SEED: " + seed);
		for (int x = 5; x > 0; x--) {
			System.out.println("start test " + x);
			is.setSeed(seed + x);
			System.out.println("with tree: ");
			System.out.println(is.helper);
			System.out.println("we extract:");
			is.forEach((ir, iseq) -> {
//				System.out.println(is.helper);
				System.out.println("- At " + iseq + " we have: " + ir);
			});
			System.out.println("\n-----------------------------------------------------------------\n");
		}
		System.out.println("End");
	}
}