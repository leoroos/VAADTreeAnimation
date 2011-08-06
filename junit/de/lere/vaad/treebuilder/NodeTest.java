package de.lere.vaad.treebuilder;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.lere.vaad.EndOfTheWorldException;
import extras.lifecycle.checkpoint.annotation.Checkpointing;

public class NodeTest {

	private Node<String> root;
	private Node<String> lc  ;
	private Node<String> rc  ;
	private Node<String> lrc ;
	private Node<Integer> iroot;

	@Before
	public void beforename() {
		root = new Node<String>("x");
		lc = new Node<String>("lc");
		rc = new Node<String>("rc");
		lrc = new Node<String>("lrc");
		iroot = new Node<Integer>(10);
	}

	@Test
	public void canAccessPerUID() throws Exception {
		Node<String> node = createDummyNode();
		NodeUID uid = node.getUid();
		assertNotNull("UID must not be null", uid);
	}

	private Node<String> createDummyNode() {
		return new Node<String>("dummy");
	}

	@Test
	public void uidIsUnique() throws Exception {
		Node node1 = createDummyNode();
		Node node2 = createDummyNode();
		assertThat("UID must be different", node1, not(equalTo(node2)));
	}

	@Test
	public void buildSimpleTree() throws Exception {
		// exec
		root.setLeft(lc);
		root.setRight(rc);
		lc.setRight(lrc);
		// validate
		assertThat(rc, equalTo(root.getRight()));
		assertThat(lc, equalTo(root.getLeft()));
		assertThat(lrc, equalTo(root.getLeft().getRight()));
	}

	@Test
	public void testCompareStructureEqual() throws Exception {
		// exec
		root.setLeft(lc);
		root.setRight(rc);
		lc.setRight(lrc);

		Node<String> root2 = new Node<String>("x");
		root2.setLeft(lc);
		root2.setRight(rc);
		// val
		assertTrue(root.compareStructure(root2));
	}

	@Test
	public void testCompareStructureUnequal() throws Exception {
		Node<String> root2 = new Node<String>("root2");
		assertFalse(root.compareStructure(root2));
	}

	@Test
	public void testCompareStructureWorksWithNull() throws Exception {
		Node<String> root = new Node<String>("x");
		assertFalse(root.compareStructure(null));
	}

	@Test
	public void testNodetoString() throws Exception {
		BreadthFirstBuilder builder = new BreadthFirstBuilder();
		Node<String> inorderbuild = builder.wideBuild("x", "lc", "rc", null, "lrc");
		System.out.println(inorderbuild.toString());
	}
	
	private void assertTillLevelOne(Node<Integer> daRoot, Integer lc, Integer root, Integer rc) {
		assertThat(daRoot.getValue(), equalTo(root));
		Node<Integer> left = daRoot.getLeft();
		if (lc == null) {
			assertNull(left);
		} else{
			assertNotNull(left);
			assertThat(left.getValue(), equalTo(lc));
		}
		Node<Integer> right = daRoot.getRight();
		if (rc == null) {
			assertNull(right);
		}else {
			assertNotNull(right);
			assertThat(right.getValue(), equalTo(rc));
		}
	}

	@Test
	public void shouldInsertLargerValueRight() throws Exception {
		iroot.insert(20);
		System.out.println(iroot);
		assertTillLevelOne(iroot, null, 10, 20);
	}

	@Test
	public void shouldInsertEqualValueToTheLeft() throws Exception {
		iroot.insert(10);
		assertTillLevelOne(iroot, 10, 10, null);
	}
	
	@Test
	public void rootPositionShouldBeOne() throws Exception {
		assertThat(iroot.getPosition(), equalTo(1));
	}
	
	@Test
	public void positionOfLeftChildShouldBeTwo() throws Exception {
		Node<Integer> left = new Node<Integer>(5);
		iroot.setLeft(left);
		assertThat(left.getPosition(), equalTo(2));
	}
	
	@Test
	public void positionOfRightChildShouldBeThree() throws Exception {
		Node<Integer> node = new Node<Integer>(15);
		iroot.setRight(node);
		assertThat(node.getPosition(), equalTo(3));
	}
	
	@Test
	public void getPositionStresstest() throws Exception {
		BinaryTreeModel<Integer> buildTree = BuilderTestUtils.createNElementTree(10000);
		
		List<Node<Integer>> nodes2 = buildTree.getNodes();
		for (Node<Integer> node : nodes2) {
			assertThat(node.getValue(), equalTo(node.getPosition()));
		}
	}
	
	

}
