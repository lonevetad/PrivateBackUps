package javaMechanism;

public class TestInheritanceMultiple {

	static interface A {
		public default void work() {
			System.out.println("A");
		}
	}

	static interface B extends A {
		@Override
		public default void work() {
			System.out.println("B");
		}
	}

	static class C implements A {

		@Override
		public void work() {
			System.out.println("C");
		}
	}

	static class MixBC extends C implements B {
		public void moreWork() {
			work();
			System.out.println("mix");
		}
	}

	public TestInheritanceMultiple() {
	}

	public static void main(String[] args) {
		MixBC m;
		B b;
		m = new MixBC();
		m.work();
		m.moreWork();
		System.out.println("try assigning to b");
		b = m;
		b.work();
	}

}
