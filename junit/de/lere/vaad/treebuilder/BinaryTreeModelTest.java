package de.lere.vaad.treebuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import de.lere.vaad.treebuilder.BinaryTreeModel.Edge;

public class BinaryTreeModelTest {

	private BinaryTreeModel<Integer> model;

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
	public void deleteOnEmptyTreeDoesNothing() {
		fail("DO IT FAGGOT!");
	}

	@Test
	public void deleteOnRootEmptysTree() {
		fail("DO IT FAGGOT!");
	}

	@Test
	public void searchEmptyTreeFindsNothing() {
		fail("DO IT FAGGOT!");
	}

	@Test
	public void searchRootFindsRoot() {
		fail("DO IT FAGGOT!");
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
	public void completeTreeShouldContainAllEdgesExceptForLeaves() throws Exception {
		int varNum = 1 << 10;
		BinaryTreeModel<Integer> createNElementTree = BuilderTestUtils.createNElementTree(varNum);
		
		List<Edge<Integer>> edgeList = createNElementTree.getEdgeList();
		List<PositionEdge> actualList = convertEdgeNodeListToPositionEdgeList(edgeList);
		
		List<PositionEdge> expectedPosList = new ArrayList<PositionEdge>(varNum >> 1);
		for(int i = 1 ; i < (varNum >> 1) ; i ++ ){
			PositionEdge toleft = new PositionEdge(i, i << 1);
			PositionEdge toright = new PositionEdge(i, (i << 1) + 1);
			expectedPosList.add(toleft);
			expectedPosList.add(toright);
		}
		assertThat(actualList, containsInAnyOrder(expectedPosList.toArray(new PositionEdge[0])));
	}

	private List<PositionEdge> convertEdgeNodeListToPositionEdgeList(
			List<Edge<Integer>> edgeList) {
		List<PositionEdge> posEdgeList = new ArrayList<PositionEdge>();
		for (Edge<Integer> edge : edgeList) {
			posEdgeList.add(new PositionEdge(edge));
		}
		return posEdgeList;
	}
}
