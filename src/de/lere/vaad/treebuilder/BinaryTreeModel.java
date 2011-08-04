package de.lere.vaad.treebuilder;


/**
 * @author leo
 *	Represents the logical structure of a binary tree. Contains the root and convience-method for child-node access.
 */
public class BinaryTreeModel {
	
	private Node root;
	
	public Node getRoot() {
		return root;
	}

	public int size() {
		if(root == null)
			return 0;
		return root.size();
	}

	
	/**
	 * Initializes this tree model with the passed tree structure. 
	 * The passed node is interpreted as root node.
	 * 
	 * @param rootnode
	 */
	public void init(Node rootNode) {
		this.root = rootNode;
	}
	
}
