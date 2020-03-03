package common.tests.testsLittle;

public class TestCircularMoving {

	public static final int timeEachReset_DEFAULT = 60000;
	public static final double PI_TWO = Math.PI * 2;

	public TestCircularMoving() {
		timeEachReset = timeEachReset_DEFAULT;
	}

	int ray, time, distancePerSecond, timeEachReset;
	double diameter;

	public void setRay(int ray) {
		this.ray = ray;
		diameter =
				// Math.PI*(ray<<1);
				PI_TWO * ray;
	}

	double calculateAngleRotation() {
		return ((time * distancePerSecond) / 1000.0) % diameter;
	}

	void progressTime(int millis) {
		if (millis > 0) {
			if ((time += millis) >= timeEachReset) time %= timeEachReset;
		}
	}

}
