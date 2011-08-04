package de.lere.vaad.treebuilder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class InOrderBuilderTest {

	@Test
	public void getEmptyForNothing() throws Exception {
		Node inorderbuild = InOrderBuilder.inorderbuild();
		assertNull(inorderbuild);
	}
	
	@Test
	public void getRootForOne() throws Exception {
		String string = "IamOmega";
		Node inorderbuild = InOrderBuilder.inorderbuild(string);
		assertThat(1, equalTo(inorderbuild.size()));
		assertThat((String)inorderbuild.getValue(),equalTo(string));
	}
	
	@Test
	public void checkOneLevel() throws Exception {
		Node root = new Node("x");
		Node lc = new Node("lc");
		Node rc = new Node("rc");
		root.setLeft(lc);
		root.setRight(rc);		
		Node inorderbuild = InOrderBuilder.inorderbuild("x","lc","rc");
		assertTrue(root.compareStructure(inorderbuild));
	}
	
	@Test
	public void check2Level(){
		Node root = new Node("x");
		Node lc = new Node("lc");
		Node rc = new Node("rc");
		Node lrc = new Node("lrc");
		// exec
		root.setLeft(lc);
		root.setRight(rc);
		lc.setRight(lrc);
		Node inorderbuild= InOrderBuilder.inorderbuild("x", "lc", "rc", null, "lrc");
		assertTrue(root.compareStructure(inorderbuild));
	}
	
	@Test
	public void buildSimpleTree() throws Exception {
		Node root = new Node("x");
		Node lc = new Node("lc");
		Node rc = new Node("rc");
		Node lrc = new Node("lrc");
		// exec
		root.setLeft(lc);
		root.setRight(rc);
		lc.setRight(lrc);
		// validate
		assertThat(rc, equalTo(root.getRight()));
		assertThat(lc, equalTo(root.getLeft()));
		assertThat(lrc, equalTo(root.getLeft().getRight()));
	}
	
}
