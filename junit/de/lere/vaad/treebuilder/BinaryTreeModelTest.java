package de.lere.vaad.treebuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import de.lere.vaad.animation.PositionEdge;
import de.lere.vaad.treebuilder.BinaryTreeModel.Edge;
import de.lere.vaad.treebuilder.events.TreeDeleteEvent;
import de.lere.vaad.treebuilder.events.TreeEventListener;
import de.lere.vaad.treebuilder.events.TreeInsertEvent;
import de.lere.vaad.treebuilder.events.TreeLeftRotateEvent;
import de.lere.vaad.treebuilder.events.TreeModelChangeEvent;
import de.lere.vaad.treebuilder.events.TreeRightRotateEvent;
import de.lere.vaad.treebuilder.events.TreeSearchEvent;
import de.lere.vaad.utils.LMTransformer;
import de.lere.vaad.utils.ListMap;

public class BinaryTreeModelTest {

	private BinaryTreeModel<Integer> model;
	private TreeEventListener listener;

	@Before
	public void setUp() {
		model = new BinaryTreeModel<Integer>();
	}

	@Test
	public void testGetRoot() {
		assertNull("Root of empty tree must be null", model.getRoot());
	}

	@Test
	public void testEmptyOnInitialize() {
		assertSize(model, 0);
	}

	@Test
	public void testHasRoot() throws Exception {
		Node<Integer> root = new Node<Integer>(0);
		model.init(root);
		assertSize(model, 1);
		assertEquals(root, model.getRoot());
	}

	private void assertSize(BinaryTreeModel<?> model, int expectedSize) {
		assertThat(model.size(), equalTo(expectedSize));
	}

	@Test
	public void shouldFireEventOnSearchNothingFoundWithNull() throws Exception {
		ArgumentCaptor<TreeModelChangeEvent> captorTE = getCaptorTE();
		TreeEventListener<Integer> btmlMock = createLinkBtmlMock(model);
		model.search(10);
		verify(btmlMock, atLeastOnce()).update(captorTE.capture());
		assertThat(captorTE.getValue(), instanceOf(TreeSearchEvent.class));
	}

	@Test
	public void shouldFireEventOnSearchWithFoundNode() throws Exception {
		BinaryTreeModel<Integer> model = BinaryTreeModel.createTreeByInsert(5,
				3, 6);
		TreeEventListener<Integer> btmlMock = createLinkBtmlMock(model);
		Node<Integer> search = model.search(6);
		verify(btmlMock).update(
				new TreeSearchEvent<Integer>(model.copy(), model.copy(),
						search, new BinaryTreeModel.StatisticResult(), 6));
	}

	@Test
	public void testEmptyTreeInsertPopulatesRoot() {
		Integer value = Integer.valueOf(0);
		model.insert(value);
		assertThat(model.getRoot().getValue(), equalTo(value));
		assertSize(model, 1);
	}

	@Test
	public void shouldInsertLargerValueRight() throws Exception {
		model.insert(10);
		model.insert(20);
		assertThat(model.getRoot().getRight().getValue(), equalTo(20));
	}

	@Test
	public void shouldFireEventOnInsert() throws Exception {
		ArgumentCaptor<TreeModelChangeEvent> captorTE = getCaptorTE();
		TreeEventListener<Integer> btmlMock = createLinkBtmlMock(model);
		model.insert(10);
		verify(btmlMock, atLeastOnce()).update(captorTE.capture());
		assertThat(captorTE.getValue(), instanceOf(TreeInsertEvent.class));
	}

	@Test
	public void shouldFireEventOnDelete() throws Exception {
		TreeEventListener<Integer> btmlMock = createLinkBtmlMock(model);
		model.insert(10);
		model.delete(10);
		verify(btmlMock, atLeast(1)).update(Mockito.any(TreeDeleteEvent.class));
	}

	@Test
	public void shouldNotFireDeleteEventOnInsert() throws Exception {
		ArgumentCaptor<TreeModelChangeEvent> argument = getCaptorTE();
		TreeEventListener<Integer> btmlMock = createLinkBtmlMock(model);
		model.insert(10);
		verify(btmlMock, atLeastOnce()).update(argument.capture());
		assertThat(argument.getAllValues(),
				not(containsInAnyOrder(instanceOf(TreeDeleteEvent.class))));
	}

	@Test
	public void shouldFireEventOnLeftRotate() throws Exception {
		ArgumentCaptor<TreeModelChangeEvent> argument = getCaptorTE();
		TreeEventListener<Integer> btmlMock = createLinkBtmlMock(model);
		model.insert(10);
		Node<Integer> root = model.getRoot();
		model.leftRotate(root);
		verify(btmlMock, atLeastOnce()).update(argument.capture());
		assertThat(argument.getValue(), instanceOf(TreeLeftRotateEvent.class));
	}

	private ArgumentCaptor<TreeModelChangeEvent> getCaptorTE() {
		ArgumentCaptor<TreeModelChangeEvent> argument = ArgumentCaptor
				.forClass(TreeModelChangeEvent.class);
		return argument;
	}

	private TreeEventListener<Integer> createLinkBtmlMock(
			BinaryTreeModel<Integer> model) {
		TreeEventListener<Integer> btmlMock = mock(TreeEventListener.class);
		model.addListener(btmlMock);
		return btmlMock;
	}

	@Test
	public void shouldNotFireRightRotateEventOnOtherEvent() throws Exception {
		ArgumentCaptor<TreeModelChangeEvent> captor = getCaptorTE();
		TreeEventListener<Integer> btmlMock = createLinkBtmlMock(model);
		model.insert(10);
		model.delete(10);
		verify(btmlMock, atLeastOnce()).update(captor.capture());
		assertThat(captor.getValue(), instanceOf(TreeDeleteEvent.class));
	}

	@Test
	public void shouldFireEventOnRightRotate() throws Exception {
		ArgumentCaptor<TreeModelChangeEvent> captor = getCaptorTE();
		TreeEventListener btmlMock = mock(TreeEventListener.class);
		model.addListener(btmlMock);
		model.insert(10);
		model.rightRotate(model.getRoot());
		verify(btmlMock, atLeastOnce()).update(captor.capture());
		assertThat(captor.getValue(), instanceOf(TreeRightRotateEvent.class));
	}

	@Test
	public void shouldFireTreeEventWithStateBeforeAndAfterChange()
			throws Exception {
		ArgumentCaptor<TreeModelChangeEvent> captor = getCaptorTE();
		TreeEventListener<Integer> btmlMock = createLinkBtmlMock(model);
		BinaryTreeModel<Integer> before = model.copy();
		Node<Integer> insert = model.insert(10);
		BinaryTreeModel<Integer> after = model.copy();
		verify(btmlMock, atLeastOnce()).update(captor.capture());
		TreeInsertEvent expectedEvent = new TreeInsertEvent<Integer>(before,
				after, insert, null);
		TreeModelChangeEvent actual = captor.getValue();
		assertThat(actual, instanceOf(TreeInsertEvent.class));
		assertThat((TreeInsertEvent) actual, equalTo(expectedEvent));
	}

	@Test
	public void copyEmptyModel() throws Exception {
		BinaryTreeModel<Integer> copy = model.copy();
		assertNull(model.getRoot());
		assertNull(copy.getRoot());
	}

	@Test
	public void copyModelCopiesNodeStructure() throws Exception {
		BinaryTreeModel<Integer> origM = BuilderTestUtils
				.createNIntegerTree(15);
		Node<Integer> origR = origM.getRoot();
		BinaryTreeModel<Integer> copyM = origM.copy();
		Node<Integer> copyR = copyM.getRoot();
		assertNotSame(origR, copyR);
		assertTrue(origR.compareStructure(copyR));
	}

	@Test
	public void copyModelDoesNotCopyListeners() throws Exception {
		BinaryTreeModel<Integer> origM = BuilderTestUtils
				.createNIntegerTree(15);
		BinaryTreeModel<Integer> copyM = origM.copy();
		TreeEventListener btml = mock(TreeEventListener.class);
		origM.addListener(btml);
		copyM.insert(2);
		verify(btml, times(0)).update(Mockito.any(TreeInsertEvent.class));
	}

	@Test
	public void shouldInsertEqualValueAccordingToNodeOrder() throws Exception {
		BinaryTreeModel<Integer> model = BinaryTreeModel.createTreeByInsert(3,
				2, 4);
		model.insert(2);
		if (NodeOrder.isEqualChildConsideredLeft(model.getRoot().getLeft()
				.getValue(), 2)) {
			assertThat(model.getRoot().getLeft().getLeft().getValue(),
					equalTo(2));
		} else {
			assertThat(model.getRoot().getLeft().getRight().getValue(),
					equalTo(2));
		}
	}

	@Test
	public void deleteOnEmptyTreeDoesNothing() {
		model.delete(1);
		assertThat(model, equalTo(new BinaryTreeModel<Integer>()));
	}

	@Test
	public void deleteOnOnlyRootEmptysTree() {
		BinaryTreeModel<Integer> createNIntegerTree = BuilderTestUtils
				.createNIntegerTree(1);
		Node<Integer> root = createNIntegerTree.getRoot();
		Node<Integer> deleted = createNIntegerTree.delete(root);
		assertThat(deleted, nullValue());
		assertThat(createNIntegerTree, equalTo(new BinaryTreeModel<Integer>()));
	}

	@Test
	public void deleteOnFilledTreeReducesSizeByOne() {
		BinaryTreeModel<Integer> createNIntegerTree = BinaryTreeModel
				.createTreeByInsert(10, 5, 15, 1, 7, 12, 16);
		Node<Integer> left = createNIntegerTree.getRoot().getLeft();
		Node<Integer> deletedNode = createNIntegerTree.delete(left);
		assertThat(createNIntegerTree.size(), equalTo(6));
		assertThat(deletedNode.getValue(), equalTo(7));
	}

	@Test
	public void deleteOnNodeWithBothChildren() {
		BinaryTreeModel<Integer> createNIntegerTree = BinaryTreeModel
				.createTreeByInsert(10, 5, 15, 1, 7, 12, 16);
		createNIntegerTree.delete(10);
		BinaryTreeModel<Integer> build = BreadthFirstBuilder.build(12, 5, 15,
				1, 7, null, 16);
		assertThat(createNIntegerTree, equalTo(build));
	}

	@Test
	public void massInsertProcudesValidTree() {
		BinaryTreeModel<Integer> model2 = BinaryTreeModel.createTreeByInsert(1,
				2, 3, 4, 5);
		Node<Integer> root = new Node<Integer>(1);
		Node<Integer> n2 = new Node<Integer>(2);
		Node<Integer> n3 = new Node<Integer>(3);
		Node<Integer> n4 = new Node<Integer>(4);
		Node<Integer> n5 = new Node<Integer>(5);
		root.setRight(n2);
		n2.setRight(n3);
		n3.setRight(n4);
		n4.setRight(n5);
		assertTrue(model2.getRoot().compareStructure(root));
	}

	@Test
	public void massInsertProducesValidTreeSimpleLeftInsertion() {
		BinaryTreeModel<Integer> model2 = BinaryTreeModel.createTreeByInsert(
				10, 15, 5, 16, 7, 1, 12);
		BinaryTreeModel<Integer> build = BreadthFirstBuilder.build(10, 5, 15,
				1, 7, 12, 16);
		assertThat(build, equalTo(model2));
	}

	@Test
	public void searchEmptyTreeFindsNothing() {
		Node<Integer> node = model.search(1);
		assertThat(node, nullValue());
	}

	@Test
	public void searchRootFindsRoot() {
		BinaryTreeModel<Integer> model = BinaryTreeModel.createTreeByInsert(1);
		Node<Integer> search = model.search(1);
		assertThat(search, equalTo(model.getRoot()));
	}

	@Test
	public void shouldReturnEmptyEdgeListForEmptyTree() throws Exception {
		List<Edge<Integer>> edgeList = model.getEdgeList();
		assertTrue(edgeList.isEmpty());
	}

	@Test
	public void shouldReturnEmptyEdgeListForOnlyRoot() throws Exception {
		model.insert(2);
		List<Edge<Integer>> edgeList = model.getEdgeList();
		assertTrue(edgeList.isEmpty());
	}

	@Test
	public void shouldReturnOneEdgeListIfOneChild() throws Exception {
		model.insert(2);
		model.insert(3);
		List<Edge<Integer>> edgeList = model.getEdgeList();
		List<PositionEdge> posEdgeList = convertEdgeNodeListToPositionEdgeList(edgeList);

		PositionEdge e1 = new PositionEdge(1, 3);
		assertEquals(posEdgeList.size(), 1);
		assertTrue(posEdgeList.contains(e1));
	}

	@Test
	public void completeTreeShouldContainAllEdgesExceptForLeafs()
			throws Exception {
		int varNum = 1111;
		BinaryTreeModel<Integer> createNElementTree = BuilderTestUtils
				.createNIntegerTree(varNum);
		List<Edge<Integer>> edgeList = createNElementTree.getEdgeList();
		List<PositionEdge> actualList = convertEdgeNodeListToPositionEdgeList(edgeList);

		List<PositionEdge> expectedPosList = new ArrayList<PositionEdge>(varNum);
		for (int i = 1; i <= (varNum / 2); i++) {
			PositionEdge toleft = new PositionEdge(i, i * 2);
			expectedPosList.add(toleft);
			if ((i * 2 + 1) <= varNum) {
				PositionEdge toright = new PositionEdge(i, (i * 2) + 1);
				expectedPosList.add(toright);
			}
		}
		assertThat(
				actualList,
				containsInAnyOrder(expectedPosList.toArray(new PositionEdge[0])));
	}

	private List<PositionEdge> convertEdgeNodeListToPositionEdgeList(
			List<Edge<Integer>> edgeList) {
		List<PositionEdge> posEdgeList = new ArrayList<PositionEdge>();
		for (Edge<Integer> edge : edgeList) {
			posEdgeList.add(new PositionEdge(edge));
		}
		return posEdgeList;
	}

	@Test
	public void testCreateAdjacencyMatrix() throws Exception {
		BinaryTreeModel<Integer> nElementTree = BuilderTestUtils
				.createNIntegerTree(7);
		int[][] expectedMatrix = { { 0, 1, 1, 0, 0, 0, 0 },
				{ 0, 0, 0, 1, 1, 0, 0 }, { 0, 0, 0, 0, 0, 1, 1 },
				{ 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 } };
		int[][] adjMatrix = nElementTree.getAdjancencyMatrix();
		assertThat(adjMatrix.length, equalTo(expectedMatrix.length));
		for (int i = 0; i < adjMatrix.length; ++i) {
			assertThat(adjMatrix[i].length, equalTo(expectedMatrix[i].length));
			for (int j = 0; j < adjMatrix.length; ++j) {
				assertThat(adjMatrix[i][j], equalTo(expectedMatrix[i][j]));
			}
		}
	}

	@Test
	public void testEmptyTreeGeneratesEmptyAdjacencyMatrix() throws Exception {
		int[][] matrix = model.getAdjancencyMatrix();
		assertThat(matrix.length, equalTo(0));
	}

	@Test
	public void testCreateNElements() {
		int size = 8;
		BinaryTreeModel<Integer> nElementTree = BuilderTestUtils
				.createNIntegerTree(size);
		assertSize(nElementTree, size);
	}

	@Test
	public void shouldReturnNodesInOrder() throws Exception {
		BinaryTreeModel<Integer> mi = BuilderTestUtils.createNIntegerTree(8);
		List<Node<Integer>> nodesInOrder = mi.getNodesInOrder();
		List<Integer> asIntLs = ListMap.map(nodesInOrder,
				new LMTransformer<Node<Integer>, Integer>() {

					@Override
					public Integer transform(Node<Integer> input) {
						return input.getValue();
					}
				});
		assertThat(asIntLs, hasItems(8, 4, 2, 5, 1, 6, 3, 7));
	}

	@Test
	public void leftRotationOnLeafNodeDoesNothing() throws Exception {
		BinaryTreeModel<Integer> m = BinaryTreeModel.createTreeByInsert(1);
		Node<Integer> rightRotate = m.leftRotate(m.getRoot());
		assertThat(rightRotate, equalTo(m.getRoot()));
	}

	@Test
	public void testLeftRotation() throws Exception {
		Node<Integer> tree = new Node<Integer>(2);
		Node<Integer> lc = new Node<Integer>(4);
		Node<Integer> rc = new Node<Integer>(1);
		Node<Integer> rlc = new Node<Integer>(5);
		Node<Integer> rrc = new Node<Integer>(3);
		tree.setLeft(lc);
		tree.setRight(rc);
		rc.setLeft(rlc);
		rc.setRight(rrc);
		Node<Integer> result = new BinaryTreeModel<Integer>().init(tree)
				.leftRotate(tree);
		assertThat(result, equalTo(rc));
		BinaryTreeModel<Integer> expected = BuilderTestUtils
				.createNIntegerTree(5);
		assertThat("Expected equal structure",
				result.compareStructure(expected.getRoot()));
	}

	@Test
	public void testLeftRotationWithParent() throws Exception {
		BinaryTreeModel<Integer> fullTree = BreadthFirstBuilder.build(6, 3, 9,
				1, 4, 8, 10);
		Node<Integer> right = fullTree.getRoot().getRight();
		Node<Integer> resultOfleftRotate = fullTree.leftRotate(right);
		assertThat(resultOfleftRotate.getParent(), equalTo(fullTree.getRoot()));
		assertThat(right.getParent(), equalTo(resultOfleftRotate));
		BinaryTreeModel<Integer> expSubTree = BinaryTreeModel
				.createTreeByInsert(10, 9, 8);
		assertThat("Expected equal structure", expSubTree.getRoot()
				.compareStructure(resultOfleftRotate));
	}

	@Test
	public void testRightRotation() throws Exception {

		BinaryTreeModel<Integer> startTree = BuilderTestUtils
				.createNIntegerTree(5);
		Node<Integer> expectedNewRoot = startTree.getRoot().getLeft().copy();
		Node<Integer> resultOfRightRotate = startTree.rightRotate(startTree
				.getRoot());

		Node<Integer> expectedTree = new Node<Integer>(2);
		Node<Integer> lc = new Node<Integer>(4);
		Node<Integer> rc = new Node<Integer>(1);
		Node<Integer> rlc = new Node<Integer>(5);
		Node<Integer> rrc = new Node<Integer>(3);
		expectedTree.setLeft(lc);
		expectedTree.setRight(rc);
		rc.setLeft(rlc);
		rc.setRight(rrc);

		assertThat(resultOfRightRotate, equalTo(expectedNewRoot));
		assertThat("Expected equal structure",
				expectedTree.compareStructure(resultOfRightRotate));
	}

	@Test
	public void testRightRotationWithParent() throws Exception {
		BinaryTreeModel<Integer> fullTree = BreadthFirstBuilder.build(6, 3, 9,
				1, 4, 8, 10);
		Node<Integer> left = fullTree.getRoot().getLeft();
		Node<Integer> resultOfRightRotate = fullTree.rightRotate(left);
		assertThat(resultOfRightRotate.getParent(), equalTo(fullTree.getRoot()));
		assertThat(left.getParent(), equalTo(resultOfRightRotate));
		BinaryTreeModel<Integer> expSubTree = BinaryTreeModel
				.createTreeByInsert(1, 3, 4);
		assertThat("Expected equal structure", expSubTree.getRoot()
				.compareStructure(resultOfRightRotate));
	}

	@Test
	public void heightEmptyTreeIsMinus1() throws Exception {
		int h = model.height();
		assertThat(h, equalTo(-1));
	}

	@Test
	public void heightOfRootIsZero() throws Exception {
		model.insert(0);
		int h = model.height();
		assertThat(h, equalTo(0));
	}

	@Test
	public void heightOfTreeWithOneChildIsOne() throws Exception {
		model.insert(5);
		model.insert(1);
		assertThat(model.height(), equalTo(1));
	}

	@Test
	public void heightOfTreeDegeneratedTreeEqualsNodesMinusOne()
			throws Exception {
		BinaryTreeModel<Integer> model = BinaryTreeModel.createTreeByInsert(1,
				2, 3, 4, 5, 6, 7, 8, 9);
		assertThat(model.height(), equalTo(8));
	}

	@Test
	public void heightOfTreeDegeneratedTreeEqualsLongestPath() throws Exception {
		BinaryTreeModel<Integer> model = BinaryTreeModel.createTreeByInsert(20,
				3, 2, 4, 5, 6, 7, 8, 9, 34, 21, 46, 60);
		assertThat(model.height(), equalTo(7));
	}
}
