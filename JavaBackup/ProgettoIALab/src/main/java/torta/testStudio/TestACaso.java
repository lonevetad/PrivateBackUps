package torta.testStudio;

import aima.core.probability.bayes.BayesianNetwork;

public class TestACaso {

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.out.println("Hello: " + i);
		}

		// BayesianNetwork bn;
		System.out.println(BayesianNetwork.class.toString());
		System.out.println(aima.core.probability.bayes.BayesianNetwork.class.toString());

	}
}
