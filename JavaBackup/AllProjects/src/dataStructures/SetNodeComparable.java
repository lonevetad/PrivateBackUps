package dataStructures;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.function.BiConsumer;

import grammars.NodeParsedSentence;
import grammars.transfer.TransferRule;
import tools.CloserGetter;
import tools.ClosestMatch;
import tools.DifferenceCalculator;
import tools.NodeComparableSynonymIndexed;

//class ValueTreeNodeComparableIdentified<K, V> implements Serializable {
//
//	public ValueTreeNodeComparableIdentified(V value, TreeComparable<K> treeIdentifier) {
//		super();
//		this.value = value;
//		this.treeIdentifier = treeIdentifier;
//	}
//
//	protected TreeComparable<K> treeIdentifier;
//	protected V value;
//
//	// delegator
//
//	public void addNode(K v, Iterable<K> path) { treeIdentifier.addNode(v, path); }
//
//	public NodeComparable<K> getRootNode() { return this.treeIdentifier.root; }
//}
/** See {@link SortedSetEnhanced} and {@link NodeComparable}. */
@Deprecated
public class SetNodeComparable<K> implements SortedSetEnhancedDelegating<NodeComparable<K>> {

	public SetNodeComparable(Comparator<K> keyComparator) { this(keyComparator, null); }

	public SetNodeComparable(Comparator<K> keyComparator, DifferenceCalculator<K> differenceCalcKey) {
		super();
		this.keyComparator = keyComparator;
		this.differenceCalcKey = differenceCalcKey != null ? differenceCalcKey : keyComparator::compare;
		this.nodeComparator = NodeComparable.newNodeComparatorDefault(keyComparator);
		this.backMap = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, nodeComparator);
		this.nodes = this.backMap.toSetKey();
//		SortedSetEnhanced.differenceCalcFromSetComparator(sortedSetComparator)
	}

	protected final Comparator<K> keyComparator;
	protected final Comparator<NodeComparable<K>> nodeComparator;
	protected final DifferenceCalculator<K> differenceCalcKey;
	protected final MapTreeAVL<NodeComparable<K>, K> backMap;
	protected final SortedSetEnhanced<NodeComparable<K>> nodes;

	@Override
	public SortedSetEnhanced<NodeComparable<K>> newSortedSetEnhanced() {
		MapTreeAVL<NodeComparable<K>, K> bm = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				nodeComparator);
		return bm.toSetKey();
	}

	@Override
	public Comparator<NodeComparable<K>> getKeyComparator() { return this.nodeComparator; }

	@Override
	public ClosestMatch<NodeComparable<K>> closestMatchOf(NodeComparable<K> key) { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedSet<NodeComparable<K>> getDelegator() { return nodes; }

	//

	//

	//

//	public void addValueTreeNodeComparableIdentified()
	public void addValue(K value, NodeComparable<K> root) {

	}

	public K getBestValueFor(NodeComparable<K> subtreeToTransfer) {
		ClosestMatch<Entry<NodeParsedSentence, TransferRule>> ruleMatched = this.rulesGivenLHS
				.closestMatchOf(subtreeToTransfer);
		if (ruleMatched == null)
			return null;
		// a "ClosestMatch" could have an exact match or just approximation
		return ruleMatched.getClosetsMatchToOriginal((eo, e1, e2) -> CloserGetter.getCloserTo(eo,
				(e11, e22) -> NodeComparableSynonymIndexed.DIFF_COMPUTER_NODE.getDifference(e11.getKey(), e22.getKey()),
				e1, e2)).getValue();
	}

	public void forEachRule(BiConsumer<K, NodeComparable<K>> c) {

	}

}