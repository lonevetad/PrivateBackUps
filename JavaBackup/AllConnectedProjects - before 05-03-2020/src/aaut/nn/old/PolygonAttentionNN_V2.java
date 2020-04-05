package aaut.nn.old;

import java.awt.Polygon;

import aaut.tools.MatrixInput;
import aaut.tools.impl.MatrixInput2D;

public class PolygonAttentionNN_V2<NNMI_Input extends MatrixInput, E> extends PolygonAttentionNN<NNMI_Input, E> {

	public PolygonAttentionNN_V2(Polygon poly) {
		super(poly);
	}

	/**
	 * L'idea era: Creare una struttura a grafo di "lati che non si intersecano" e
	 * poi ripetere l'algoritmo della classe padre per ogni sotto-poligono così
	 * creato. In più, se un segmento è annotato in due sotto-poligoni (e avrà quasi
	 * sicuramente un coefficiente diverso), allora rimuoverlo e fare "merge" dei
	 * corrispondenti poligoni (o meglio, rimuovere tutti i lati "adiacenti tra di
	 * loro" che sono preseti in entramb i poligoni, perchè sono lati che separano
	 * tali poligoni .. è come unire due liste linkate, o due frammenti di DNA)
	 * <ol>
	 * <li>Inizializzare il grafo come i vari nodi ed i collegamenti con i due nodi
	 * ad essi connessi (in caso di nodi che fanno overlap o hanno una distanza
	 * sotto una certa soglia [bassa], allora collegarli)</li>
	 * <li>Per Ogni lato, se esiste un altro lato che interseca col primo, allora
	 * aggiungere al grafo un nodo che connette tuti e 4 i punti di tale coppia di
	 * lati (disconnettere i lati originali, però)</li>
	 * <li>annotarsi in qualche modo i sottopoligoni che mano a mano si formano</li>
	 * <li>ripetere il tutto ogni volta che si crea un nuovo nodo</li>
	 * <li>rimuovere da una "lista di lati potenzialmente intersecati" il nodo del
	 * ciclo più esterno (il primo punto) se tale lato non interseca più
	 * nessuno</li>
	 * </ol>
	 * La complessità sarà .. pessima, almeno quadratca, se non cubica (e ci può
	 * essere un fattore logaritmico, in aggiunta)
	 */
	@Override
	public MatrixInput2D build(Polygon poly) {
	}
}