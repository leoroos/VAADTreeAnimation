package de.lere.vaad.treebuilder;

import java.util.Arrays;
import java.util.LinkedList;

public class InOrderBuilder {

	public static Node inorderbuild(Object... args) {
		LinkedList<Node> linkedList = new LinkedList<Node>();
		if (args == null || args.length == 0) {
			return null;
		}
		
		LinkedList<Object> values = new LinkedList<Object>(Arrays.asList(args));
		
		Node root = new Node(values.removeFirst());
		LinkedList<Node> valueNodes = new LinkedList<Node>();
		while(!values.isEmpty()){
			Object value = values.removeFirst();
			Node node = null;			
			if(value != null){
				node = new Node(value);
			}
			valueNodes.add(node);
		}
				
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(root);
		while(!queue.isEmpty()){
			Node toBeParent = queue.removeFirst();
			if(toBeParent != null){
				if(!valueNodes.isEmpty()){
					Node left = valueNodes.removeFirst();
					toBeParent.setLeft(left);
					queue.add(left);
				}
				if(!valueNodes.isEmpty()){
					Node right = valueNodes.removeFirst();
					toBeParent.setRight(right);
					queue.add(right);
				}
			}
		}
		return root;
	}

	/**
	 * Creates a node with passed value if that is not null. If the passed
	 * value is null null will be returned and no node will be linked
	 * 
	 * @param nextVal
	 * @return
	 */
	private static Node createAndLinkIfNotNull(Object nextVal) {
		Node nextr = null;
		if (nextVal != null) {
			nextr = new Node(nextVal);
		}
		return nextr;
	}

}