package de.lere.vaad.treebuilder;

import java.util.ArrayList;
import java.util.List;

public class BuilderTestUtils {

	public static BinaryTreeModel<Integer> createNIntegerTree(int varNum) {
		ArrayList<Integer> nodes = new ArrayList<Integer>(varNum);
		for (int i = 1; i <= varNum; i++) {
			nodes.add(i);
		}
		BinaryTreeModel<Integer> buildTree = new BreadthFirstBuilder()
				.buildTree(nodes);
		return buildTree;
	}
	

}
