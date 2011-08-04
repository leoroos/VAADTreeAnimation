package de.lere.vaad.treebuilder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;


public class BinaryTreeModelTest {

	
	private BinaryTreeModel model;

	@Before
	public void setUp(){		
		model = new BinaryTreeModel();
	}
	@Test
	public void testGetRoot(){
		assertNull("Root of empty tree must be null", model.getRoot());
	}
	
	@Test
	public void testEmptyOnInitialize(){
		assertSize(model, 0);
	}
	
	@Test
	public void testHasRoot() throws Exception {
		Node root = new Node("dummy");
		model.init(root);
		assertSize(model,1);
		assertEquals(root,model.getRoot());
	}
	
	private void assertSize(BinaryTreeModel model, int expectedSize) {
		assertThat(model.size(), equalTo(expectedSize));
	}
}
