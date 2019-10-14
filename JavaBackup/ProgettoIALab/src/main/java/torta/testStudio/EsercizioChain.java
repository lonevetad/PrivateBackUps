package torta.testStudio;

import java.util.List;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.Node;
import aima.core.probability.domain.Domain;

public class EsercizioChain {

	public EsercizioChain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		int n, i;

		RandomVariable rv;
		double[] distribution, childsDist;
		Node nodoRadice, nodo, nodoPrev;
		List<RandomVariable> variables;
		BayesianNetwork bn;

		rv = new aima.core.probability.util.RandVar("LAL", new aima.core.probability.domain.BooleanDomain()); // IntegerRV.SINGLETON;

		System.out.println("START");
		System.out.println(rv);

		// CREATE NODES

//		nodiRadice = new LinkedList<>();
		// definiamo il nodo radice
		distribution = new double[] { 0.25, 0.75 };
		nodoRadice = nodoPrev = new aima.core.probability.bayes.impl.FullCPTNode(rv, distribution);
		// adesso i nodi figli
		n = 10; // per esempio
		i = 0; // parte da 0 e non da -1 perche' la radice e' gia' creata
		{
			// creiamo una distribuzione per tutti i figli. Non e' importante il valore
			// numerico, ma la cardinalita'
			double f1, f2;
			f1 = 0.625;
			f2 = 0.35;
			childsDist = new double[] { f1, (1.0 - f1), f2, (1.0 - f2) };
		}
		while (++i < n) {
			nodo = new aima.core.probability.bayes.impl.FullCPTNode(rv, childsDist, new Node[] { nodoPrev });
			nodoPrev = nodo;
		}

		//
		bn = new aima.core.probability.bayes.impl.BayesNet(new Node[] { nodoRadice });

		System.out.println(bn.toString());
		variables = bn.getVariablesInTopologicalOrder();
		System.out.println("Variables");
		System.out.println(variables.toString());

		System.out.println("nodes:");
		variables.forEach(rvar -> {
			System.out.print("..");
			System.out.println(nodeToString(bn.getNode(rvar)));
		});
		System.out.println();
	}

	static String nodeToString(Node n) {
		return "(" + n.getRandomVariable().getName() + "-" + n.getCPD().toString() + "), ";
	}

	//

	//

	static class IntegerRV implements RandomVariable {
		static final IntegerRV SINGLETON = new IntegerRV();

		public IntegerRV() {
			this.name = "IntegerRV";
			this.domain = IntegerDomain.SINGLETON;
		}

		String name;
		Domain domain;

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Domain getDomain() {
			return domain;
		}
	}

	static class IntegerDomain implements Domain {
		static final IntegerDomain SINGLETON = new IntegerDomain();

		@Override
		public boolean isFinite() {
			return true;
		}

		@Override
		public boolean isInfinite() {
			return !isInfinite();
		}

		@Override
		public int size() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isOrdered() {
			return true;
		}

	}

}
