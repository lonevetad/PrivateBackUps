package dataStructures.spaceSubdivisionTree.impl.utils;

import java.io.Serializable;

import dataStructures.spaceSubdivisionTree.SpaceSubdivisionsManagerTree;
import dataStructures.spaceSubdivisionTree.SpaceSubsectionNode;

public interface SubsectionDivisionRuler extends Serializable {
	public ObjectsSubdivision isSubdivisionNeeded(SpaceSubsectionNode subsection,
			SpaceSubdivisionsManagerTree subsectionBelonger);
}