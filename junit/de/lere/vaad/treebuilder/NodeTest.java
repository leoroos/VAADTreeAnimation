package de.lere.vaad.treebuilder;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class NodeTest {

	private Node<String> root;
	private Node<String> lc;
	private Node<String> rc;
	private Node<String> lrc;
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
		Node<String> node1 = createDummyNode();
		Node<String> node2 = createDummyNode();
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
	public void testCompareEqualStructureDeeper() throws Exception {
		int deep = 200;
		BinaryTreeModel<Integer> createNIntegerTree = BuilderTestUtils.createNIntegerTree(deep);
		BinaryTreeModel<Integer> createNIntegerTree2 = BuilderTestUtils.createNIntegerTree(deep);
		assertTrue(createNIntegerTree.getRoot().compareStructure(createNIntegerTree2.getRoot()));
	}
	
	@Test
	public void testCompareUnEqualStructureDeeper() throws Exception {
		int deep = 15;
		BinaryTreeModel<Integer> m1 = BuilderTestUtils.createNIntegerTree(deep);
		BinaryTreeModel<Integer> m2 = BuilderTestUtils.createNIntegerTree(deep);
		
		Node<Integer> last = null;
		Node<Integer> next = m2.getRoot();
		while(next != null){
			last = next;			
			Random random = new Random(22);
			int nextInt = random.nextInt(1);
			switch (nextInt) {
			case 0:
				next = next.getLeft();
				break;
			case 1:
				next = next.getRight();
				break;	
			default:
				throw new RuntimeException("WTF");
			}
		}
		
		last.setLeft(new Node<Integer>(Integer.MAX_VALUE));
		assertFalse("expected different structurecomparison with " + deep + " nodes",m1.getRoot().compareStructure(m2.getRoot()));
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

	@Ignore("has no assert")
	@Test
	public void testNodetoString() throws Exception {
		BreadthFirstBuilder builder = new BreadthFirstBuilder();
		Node<String> inorderbuild = builder.breathBuild("x", "lc", "rc", null,
				"lrc");
		System.out.println(inorderbuild.toString());
	}

	private void assertTillLevelOne(Node<Integer> daRoot, Integer lc,
			Integer root, Integer rc) {
		assertThat(daRoot.getValue(), equalTo(root));
		Node<Integer> left = daRoot.getLeft();
		if (lc == null) {
			assertNull(left);
		} else {
			assertNotNull(left);
			assertThat(left.getValue(), equalTo(lc));
		}
		Node<Integer> right = daRoot.getRight();
		if (rc == null) {
			assertNull(right);
		} else {
			assertNotNull(right);
			assertThat(right.getValue(), equalTo(rc));
		}
	}

	@Test
	public void shouldInsertLargerValueRight() throws Exception {
		iroot.insert(20);
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
		BinaryTreeModel<Integer> buildTree = BuilderTestUtils
				.createNIntegerTree(10000);

		List<Node<Integer>> nodes2 = buildTree.getNodesInOrder();
		for (Node<Integer> node : nodes2) {
			assertThat(node.getValue(), equalTo(node.getPosition()));
		}
	}

	@Test
	public void testSubtreeToString() throws Exception {
		root.setLeft(lc);
		root.setRight(rc);
		lc.setRight(lrc);
		String subtreeToString = root.structureToString();
		// System.out.println(subtreeToString);
		assertThat(subtreeToString,
				equalToIgnoringWhiteSpace(IOUtils
						.toString(getResource("NodeTesttestToStringExpected"))));
	}

	private InputStreamReader getResource(String name) {
		InputStream resourceAsStream = this.getClass().getResourceAsStream(
				"resources/" + name);
		try {
			return new InputStreamReader(resourceAsStream, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return new InputStreamReader(resourceAsStream);
		}
	}

	@Test
	public void copySingleNode() throws Exception {
		Node<String> node = new Node<String>("hallo");
		Node<String> c = node.copy();
		assertNodesEqual(node, c);
		assertNotSame(c, node);
	}
	
	
	@Test
	public void copiedHasNoCopiedParent() throws Exception {
		Node<String> root = BreadthFirstBuilder.build("x",null,"y").getRoot();
		assertTrue("parent of child should have same structure as parent",root.getRight().getParent().compareStructure(root));
		Node<String> parentOfCopy = root.getRight().copy().getParent();
		assertFalse("copy of child should have no parent", root.getRight().getParent().compareStructure(parentOfCopy));
		assertNull(parentOfCopy);
	}
	
	
	@Test
	public void testChildOfCopiedHasParent() throws Exception {
		Node<Integer> root = BuilderTestUtils.createNIntegerTree(15).getRoot();
		Node<Integer> copiedRC = root.getRight().copy();
		assertNull(copiedRC.getParent());
		Node<Integer> parentOfchildOfCopied = copiedRC.getLeft().getParent();
		assertTrue("child of copied should have parent", copiedRC.compareStructure(parentOfchildOfCopied));
		assertSame(copiedRC, parentOfchildOfCopied);
		assertTrue("right child of copied should have parent too", copiedRC.compareStructure(copiedRC.getRight().getParent()));
	}

	@Test
	public void shouldCopyCompleteSubTreeStructure() throws Exception {
		Node<String> rootOrig = BreadthFirstBuilder.build("x", "y", "z", "u",
				"v").getRoot();		
		Node<String> copy = rootOrig.copy();
		//copied nodes are not the same but have the same structure
		assertNotSame(rootOrig, copy);
		assertNodesEqual(rootOrig, copy);
		assertTrue("expected original and copy to have same structure",
				rootOrig.compareStructure(copy));

		//children have same structure but are not the same
		Node<String> llcroot = getLL(rootOrig);
		Node<String> llcopy = getLL(copy);
		assertTrue(llcroot.compareStructure(llcopy));
		assertNotSame(llcroot, llcopy);

	}

	private <T extends Comparable<T>> Node<T> getLL(Node<T> n) {
		return n.getLeft().getLeft();
	}

	private void assertNodesEqual(Node<String> node, Node<String> c) {
		assertEquals(node.getValue(), c.getValue());
		assertEquals(node.getUid(), c.getUid());
		if(node.getRight() == null)
			assertNull(c.getRight());
		else {
			assertTrue(node.getRight().compareStructure(c.getRight()));
		}
		if(node.getLeft() == null) {
			assertNull(c.getLeft());
		} else {
			assertTrue(node.getLeft().compareStructure(c.getLeft()));
		}
	}
	
	
	@Test
	public void sameNodeShouldBeEqual() throws Exception {
		Node<Integer> node = new Node<Integer>(1);
		assertThat(node, equalTo(node));
	}
	
	
	@Test
	public void differentNodesNotEqual() throws Exception {
		Node<Integer> node = new Node<Integer>(1);
		Node<Integer> node2 = new Node<Integer>(2);
		assertThat(node, not(equalTo(node2)));
	}
	
	
	@Test
	public void nodesEqualAfterCopy() throws Exception {
		Node<Integer> node = new Node<Integer>(1);
		Node<Integer> copy = node.copy();
		assertThat(copy, equalTo(node));
	}
	
	
	@Test
	public void nodesWithDifferentStructureEqual() throws Exception {
		Node<Integer> node = new Node<Integer>(1);
		Node<Integer> copy = node.copy();
		copy.setLeft(new Node<Integer>(2));
		assertFalse(copy.compareStructure(node));
		assertThat(copy, equalTo(node));
	}
	
	@Test
	public void getMaximumDeliversRightmostChild(){
		BinaryTreeModel<Integer> nIntegerTree = BuilderTestUtils.createNIntegerTree(7);
		Node<Integer> root2 = nIntegerTree.getRoot();
		Node<Integer> maximumNode = root2.getMaximum();
		assertThat(maximumNode.getValue(), equalTo(7));
	}
	
	@Test
	public void getMinimumDeliversLeftMostchild(){
		BinaryTreeModel<Integer> nIntegerTree = BuilderTestUtils.createNIntegerTree(7);
		Node<Integer> root2 = nIntegerTree.getRoot();
		Node<Integer> maximumNode = root2.getMinimum();
		assertThat(maximumNode.getValue(), equalTo(4));
	}
	
	@Test
	public void getMaximumDeliversRootIfThereAreNoRightChilds(){
		BinaryTreeModel<Integer> nIntegerTree = BuilderTestUtils.createNIntegerTree(1);
		Node<Integer> root2 = nIntegerTree.getRoot();
		Node<Integer> maximumNode = root2.getMaximum();
		assertThat(maximumNode.getValue(), equalTo(1));
	}
	
	@Test
	public void getMinimumDeliversRootIfThereAreNoLeftChilds(){
		BinaryTreeModel<Integer> nIntegerTree = BuilderTestUtils.createNIntegerTree(1);
		Node<Integer> root2 = nIntegerTree.getRoot();
		Node<Integer> maximumNode = root2.getMinimum();
		assertThat(maximumNode.getValue(), equalTo(1));
	}
	
	@Test
	public void getSuccessorDeliversSuccessor(){
		BinaryTreeModel<Integer> nIntegerTree = BuilderTestUtils.createNIntegerTree(7);
		Node<Integer> root2 = nIntegerTree.getRoot();
		Node<Integer> predeccesor = root2.getSuccessor();
		assertThat(predeccesor.getValue(), equalTo(6));
	}
	
	@Test
	public void getPredecessorDeliversPredecessor(){
		BinaryTreeModel<Integer> nIntegerTree = BuilderTestUtils.createNIntegerTree(7);
		Node<Integer> root2 = nIntegerTree.getRoot();
		Node<Integer> predeccesor = root2.getPredeccesor();
		assertThat(predeccesor.getValue(), equalTo(5));
	}
	
	@Test
	public void getPredecessorDeliversNullIfLeftSubtreeEmpty(){
		BinaryTreeModel<Integer> nIntegerTree = BuilderTestUtils.createNIntegerTree(1);
		Node<Integer> root2 = nIntegerTree.getRoot();
		Node<Integer> predeccesor = root2.getPredeccesor();
		assertThat(predeccesor, nullValue());
	}
	
	@Test
	public void getSuccessorDeliversNullIfRightSubtreeEmpty(){
		BinaryTreeModel<Integer> nIntegerTree = BuilderTestUtils.createNIntegerTree(1);
		Node<Integer> root2 = nIntegerTree.getRoot();
		Node<Integer> predeccesor = root2.getSuccessor();
		assertThat(predeccesor, nullValue());
	}
}
