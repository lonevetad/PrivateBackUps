package dataStructures;

import java.util.Map;
import java.util.Set;

import tools.SynonymSet;

public class NodeComparableSynonymIndexed extends NodeComparable.NodeComparableDefaultAlghoritms<SynonymSet> {
	private static final long serialVersionUID = 5640520473L;

	public NodeComparableSynonymIndexed() { this(new String[] {}); }

	public NodeComparableSynonymIndexed(String[] aaaa) {
		this.alternatives = new SynonymSet(aaaa);
		this.childrenByElemGramm = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, SynonymSet.SYNONYM_COMPARATOR);
		this.childrenByElemGrammBackMap = new SetMapped<>(
				((MapTreeAVL<SynonymSet, NodeComparableSynonymIndexed>) this.childrenByElemGramm)
						.toSetValue(n -> n.getKeyIdentifier()), //
				// generics type converter, need by the Java Compiler
				(ndt) -> { return (NodeComparable<SynonymSet>) ndt; });
	}

	//

	protected final SynonymSet alternatives; // the "node key"
	protected Map<SynonymSet, NodeComparableSynonymIndexed> childrenByElemGramm;
	protected Set<NodeComparable<SynonymSet>> childrenByElemGrammBackMap; // the "node children"

	//

	@Override
	public SynonymSet getKeyIdentifier() { return this.alternatives; }

	@Override
	public Set<NodeComparable<SynonymSet>> getChildrenNC() { return this.childrenByElemGrammBackMap; }

	@Override
	public NodeComparable<SynonymSet> getChildNCByKey(SynonymSet key) { return this.childrenByElemGramm.get(key); }

	// delegators

	public void addAlternative(String s) { this.alternatives.addAlternative(s); }

	public void removeAlternative(String t) { this.alternatives.removeAlternative(t); }

	public boolean hasAlternative(String t) { return this.alternatives.hasAlternative(t); }

	public boolean canBeIdentifiedBy(SynonymSet eg) { return this.alternatives.areIntersecting(eg); }
}