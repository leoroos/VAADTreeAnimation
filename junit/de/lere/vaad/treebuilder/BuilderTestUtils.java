package de.lere.vaad.treebuilder;

import java.util.ArrayList;

public class BuilderTestUtils {


	public static BinaryTreeModel<Integer> createNElementTree(int varNum) {
		ArrayList<Integer> nodes = new ArrayList<Integer>(varNum);
		for(int i = 1; i <= varNum ; i++){
			nodes.add(i);
		}
		BinaryTreeModel<Integer> buildTree = new WideBuilder().buildTree(nodes);
		return buildTree;
	}
	
}
