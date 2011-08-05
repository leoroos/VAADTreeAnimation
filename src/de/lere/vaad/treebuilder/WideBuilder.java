package de.lere.vaad.treebuilder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WideBuilder {
	
	 	<T extends Comparable<T>> Node<T> wideBuild(List<T> args){		
		if(args == null){
			throw new IllegalArgumentException("List must not be null. It can be empty though.");
		}		
		if (args.size() == 0) {
			return null;
		}	
		LinkedList<T> values = new LinkedList<T>(args);
		
		Node<T> root = new Node<T>(values.removeFirst());
		LinkedList<Node<T>> valueNodes = new LinkedList<Node<T>>();
		while(!values.isEmpty()){
			T value = values.removeFirst();
			Node<T> node = null;			
			if(value != null){
				node = new Node<T>(value);
			}
			valueNodes.add(node);
		}
				
		LinkedList<Node<T>> queue = new LinkedList<Node<T>>();
		queue.add(root);
		while(!queue.isEmpty()){
			Node<T> toBeParent = queue.removeFirst();
			if(toBeParent != null){
				if(!valueNodes.isEmpty()){
					Node<T> left = valueNodes.removeFirst();
					toBeParent.setLeft(left);
					queue.add(left);
				}
				if(!valueNodes.isEmpty()){
					Node<T> right = valueNodes.removeFirst();
					toBeParent.setRight(right);
					queue.add(right);
				}
			}
		}
		return root;
	}

	 <T extends Comparable<T>> Node<T> wideBuild(T... args) {
		if (args == null) {
			return null;
		}
		return wideBuild(Arrays.asList(args));
	}
	
	 public <T extends Comparable<T>> BinaryTreeModel<T> buildTree(List<T> lst){
		Node<T> node = wideBuild(lst);
		BinaryTreeModel<T> model = new BinaryTreeModel<T>();
		return model.init(node);		
	}
	
	public <T extends Comparable<T>> BinaryTreeModel<T> buildTree(T... args){
		Node<T> node = wideBuild(args);
		BinaryTreeModel<T> model = new BinaryTreeModel<T>();
		return model.init(node);
	}
}