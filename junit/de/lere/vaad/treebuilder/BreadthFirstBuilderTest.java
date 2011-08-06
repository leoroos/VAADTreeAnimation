package de.lere.vaad.treebuilder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


public class BreadthFirstBuilderTest {
	
	private BreadthFirstBuilder testee;

	@Before
	public void setup(){
		testee = new BreadthFirstBuilder();
	}

	@Test
	public void getEmptyForNothing() throws Exception {
		Node<String> inorderbuild = testee.breathBuild();
		assertNull(inorderbuild);
	}
	
	@Test
	public void getRootForOne() throws Exception {
		String string = "IamOmega";
		Node<String> inorderbuild = testee.breathBuild(string);
		assertThat(1, equalTo(inorderbuild.size()));
		assertThat((String)inorderbuild.getValue(),equalTo(string));
	}
	
	@Test
	public void checkOneLevel() throws Exception {
		Node<String> root = new Node<String>("x");
		Node<String> lc = new Node<String>("lc");
		Node<String> rc = new Node<String>("rc");
		root.setLeft(lc);
		root.setRight(rc);		
		Node<String> inorderbuild = testee.breathBuild("x","lc","rc");
		assertTrue(root.compareStructure(inorderbuild));
	}
	
	@Test
	public void check2Level(){
		Node<String> root = new Node<String>("x");
		Node<String> lc = new Node<String>("lc");
		Node<String> rc = new Node<String>("rc");
		Node<String> lrc = new Node<String>("lrc");
		// exec
		root.setLeft(lc);
		root.setRight(rc);
		lc.setRight(lrc);
		Node<String> inorderbuild= testee.breathBuild("x", "lc", "rc", null, "lrc");
		assertTrue(root.compareStructure(inorderbuild));
	}
	
	@Test
	public void buildSimpleTree() throws Exception {
		Node<String> root = new Node<String>("x");
		Node<String> lc = new Node<String>("lc");
		Node<String> rc = new Node<String>("rc");
		Node<String> lrc = new Node<String>("lrc");
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
