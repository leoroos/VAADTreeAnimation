package de.lere.vaad.treebuilder;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.lere.vaad.splaytree.SplayTreeModel;

public class SplayTreeModelTest {

	@Test
	/** 
	 * 								p						x
	 * 							  /   \					  /   \
	 * 							 x	   C		==>		 A	   p	
	 * 							/ \							  / \
	 * 						   A   B 						 B   C
	 */
	public void findLeftNodeOfRootDoesZigOperation() {
		BinaryTreeModel<Integer> binaryTree = BinaryTreeModel
				.createTreeByInsert(10, 5, 15, 1, 7);
		SplayTreeModel<Integer> model = SplayTreeModel.from(binaryTree);
		Node<Integer> found = model.search(5);
		assertThat(model.getRoot(), equalTo(found));
		assertThat(model.getRoot().getValue(), equalTo(5));
		assertThat(model.getRoot().getLeft().getValue(), equalTo(1));
		assertThat(model.getRoot().getRight().getValue(), equalTo(10));
		assertThat(model.getRoot().getRight().getLeft().getValue(), equalTo(7));
		assertThat(model.getRoot().getRight().getRight().getValue(),
				equalTo(15));
	}

	@Test
	/** 
	 * 								p						x
	 * 							  /   \					  /   \
	 * 							 A	   x		==>		 P	   C	
	 * 								  / \				/ \ 	 
	 * 						         B   C 			   A   B	 
	 */
	public void findRightNodeOfRootDoesZigOperation() {
		BinaryTreeModel<Integer> binaryTree = BinaryTreeModel
				.createTreeByInsert(10, 5, 15, 12, 17);
		SplayTreeModel<Integer> model = SplayTreeModel.from(binaryTree);
		Node<Integer> found = model.search(15);
		assertThat(model.getRoot(), equalTo(found));
		assertThat(model.getRoot().getValue(), equalTo(15));
		assertThat(model.getRoot().getRight().getValue(), equalTo(17));
		assertThat(model.getRoot().getLeft().getValue(), equalTo(10));
		assertThat(model.getRoot().getLeft().getLeft().getValue(), equalTo(5));
		assertThat(model.getRoot().getLeft().getRight().getValue(), equalTo(12));
	}

	@Test
	/**
	 * 					g						x		
	 * 				  /   \					   / \
	 * 				 p 	   D				  A    p
	 * 				/ \							  / \
	 * 			   x   C			==>			 B   g
	 * 			  / \								/ \
	 * 			 A   B							   C   D
	 */
	public void doesZigZigWhenXisLeftChildOfPandPisLeftChildOfG() {
		BinaryTreeModel<Integer> binaryTree = BinaryTreeModel
				.createTreeByInsert(20, 10, 30, 5, 8, 1, 7);
		SplayTreeModel<Integer> model = SplayTreeModel.from(binaryTree);
		Node<Integer> found = model.search(5);
		assertThat(model.getRoot(), equalTo(found));
		assertThat(model.getRoot().getLeft().getValue(), equalTo(1));
		assertThat(model.getRoot().getRight().getValue(), equalTo(10));
		assertThat(model.getRoot().getRight().getLeft().getValue(), equalTo(7));
		assertThat(model.getRoot().getRight().getRight().getValue(),
				equalTo(20));
		assertThat(model.getRoot().getRight().getRight().getLeft().getValue(),
				equalTo(8));
		assertThat(model.getRoot().getRight().getRight().getRight().getValue(),
				equalTo(30));
	}

	@Test
	/**
	 * 					g						x		
	 * 				   / \					   / \
	 * 				  D   p					  p   A
	 * 				 	 / \		==>		 / \
	 * 					C	x				g   B
	 * 					   / \			   / \
	 * 					  B   A			  D   C 
	 */
	public void doesZigZigWhenXisRightChildOfPandPisRightChildOfG() {
		BinaryTreeModel<Integer> binaryTree = BinaryTreeModel
				.createTreeByInsert(20, 10, 30, 25, 35, 32, 40);
		SplayTreeModel<Integer> model = SplayTreeModel.from(binaryTree);
		Node<Integer> found = model.search(35);
		assertThat(model.getRoot(), equalTo(found));
		assertThat(model.getRoot().getValue(), equalTo(35));
		assertThat(model.getRoot().getRight().getValue(), equalTo(40));
		assertThat(model.getRoot().getLeft().getValue(), equalTo(10));
		assertThat(model.getRoot().getLeft().getRight().getValue(), equalTo(32));
		assertThat(model.getRoot().getLeft().getLeft().getValue(), equalTo(20));
		assertThat(model.getRoot().getLeft().getLeft().getLeft().getValue(),
				equalTo(20));
		assertThat(model.getRoot().getLeft().getLeft().getRight().getValue(),
				equalTo(25));
	}

	@Test
	/**
	 * 				  g								 x 
	 * 		   		/   \						   /   \
	 * 			   p     D						  p	    g
	 * 			  / \				==>			 / \   / \
	 * 			 A   x							A	B C   D
	 * 				/ \
	 * 			   B   C
	 */
	public void doesZigZagIfXisRightChildOfPAndPIsLeftChildOfG() {

	}

	@Test
	/**
	 * 				g								
	 * 			  /   \								 x
	 * 			 D     p						   /   \
	 * 				  / \			==>			  g     p
	 * 				 x   A						 / \   / \
	 * 				/ \							D   C B   A
	 * 			   C   B
	 *  		
	 */
	public void doesZigZagIfXisLeftChildOfPAndPIsRightChildOfG() {

	}
}
