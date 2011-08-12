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
		// p,x,C,A,B
		BinaryTreeModel<Integer> binaryTree = BinaryTreeModel
				.createTreeByInsert(10, 5, 15, 1, 7);
		SplayTreeModel<Integer> model = SplayTreeModel.from(binaryTree);
		Node<Integer> found = model.search(5);
		assertThat(model.getRoot(), equalTo(found));
		BinaryTreeModel<Integer> expected = BinaryTreeModel.createTreeByInsert(
				5, 1, 10, 7, 15);
		assertThat(model, equalTo(expected));
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
		// p,A,x,B,C
		BinaryTreeModel<Integer> binaryTree = BinaryTreeModel
				.createTreeByInsert(10, 5, 15, 12, 17);
		SplayTreeModel<Integer> model = SplayTreeModel.from(binaryTree);
		Node<Integer> found = model.search(15);
		assertThat(model.getRoot(), equalTo(found));
		BinaryTreeModel<Integer> expected = BinaryTreeModel.createTreeByInsert(
				15, 10, 17, 5, 12);
		assertThat(model.getRoot().getValue(), equalTo(15));
		assertThat(model, equalTo(expected));
	}

	@Test
	/**
	 * 					g						x		
	 * 				  /   \					   / \
	 * 				 p 	   D				  A    p
	 * 				/ \							  / \
	 * 			   x   C				==>		 B   g
	 * 			  / \								/ \
	 * 			 A   B							   C   D
	 */
	public void doesZigZigWhenXisLeftChildOfPandPisLeftChildOfG() {
		// g,p,D,x,C,A,B
		BinaryTreeModel<Integer> binaryTree = BinaryTreeModel
				.createTreeByInsert(20, 10, 30, 15, 8, 1, 9);
		SplayTreeModel<Integer> model = SplayTreeModel.from(binaryTree);
		Node<Integer> found = model.search(8);
		assertThat(model.getRoot(), equalTo(found));
		BinaryTreeModel<Integer> expected = BinaryTreeModel.createTreeByInsert(
				8, 1, 10, 9, 20, 15, 30);
		assertThat(model, equalTo(expected));
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
		// g,D,p,C,x,B,A
		BinaryTreeModel<Integer> binaryTree = BinaryTreeModel
				.createTreeByInsert(20, 10, 30, 25, 35, 32, 40);
		SplayTreeModel<Integer> model = SplayTreeModel.from(binaryTree);
		Node<Integer> found = model.search(35);
		assertThat(model.getRoot(), equalTo(found));
		BinaryTreeModel<Integer> expected = BinaryTreeModel.createTreeByInsert(
				35, 30, 40, 20, 32, 10, 25);
		assertThat(model, equalTo(expected));
	}

	@Test
	/**
	 * 			  g								 x 
	 *	  		/   \						   /   \
	 * 		   p     D						  p	    g
	 * 		  / \				==>			 / \   / \
	 * 		 A   x							A	B C   D
	 * 			/ \
	 * 	  	   B   C
	 */
	public void doesZigZagIfXisRightChildOfPAndPIsLeftChildOfG() {
		// g,p,D,A,x,B,C
		BinaryTreeModel<Integer> binaryTree = BinaryTreeModel
				.createTreeByInsert(30, 20, 40, 10, 25, 22, 28);
		SplayTreeModel<Integer> model = SplayTreeModel.from(binaryTree);
		Node<Integer> found = model.search(25);
		assertThat(model.getRoot(), equalTo(found));
		BinaryTreeModel<Integer> expected = BinaryTreeModel.createTreeByInsert(
				25, 20, 30, 10, 22, 28, 40);
		assertThat(model, equalTo(expected));
	}

	@Test
	/**
	 * 				g								
	 * 			  /   \								 x
	 * 		 	 D     p						   /   \
	 * 				  / \			==>			  g     p
	 * 		 	     x   A						 / \   / \
	 * 			    / \							D   C B   A
	 * 		  	   C   B
	 *  		
	 */
	public void doesZigZagIfXisLeftChildOfPAndPIsRightChildOfG() {
		// g,D,p,x,A,C,B
		BinaryTreeModel<Integer> binaryTree = BinaryTreeModel
				.createTreeByInsert(20, 10, 30, 25, 35, 23, 27);
		SplayTreeModel<Integer> model = SplayTreeModel.from(binaryTree);
		Node<Integer> found = model.search(25);
		assertThat(model.getRoot(), equalTo(found));
		BinaryTreeModel<Integer> expected = SplayTreeModel.createTreeByInsert(
				25, 20, 30, 10, 23, 27, 35);
		assertThat(model, equalTo(expected));
	}
}
