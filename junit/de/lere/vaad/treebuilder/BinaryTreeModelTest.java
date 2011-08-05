package de.lere.vaad.treebuilder;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;

import de.lere.vaad.treebuilder.BinaryTreeModel.Edge;
import de.lere.vaad.treebuilder.BinaryTreeModelTest.PosEdge;

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
		assertThat((Collection) edgeList, empty());
	}

	@Test
	public void shouldReturnEmptyEdgeListForOnlyRoot() throws Exception {
		model.insert(2);
		List<Edge<Integer>> edgeList = model.getEdgeList();
		assertTrue(edgeList.isEmpty());
	}

	@Test
	public void shouldReturnOneEdgeListOneChild() throws Exception {
		model.insert(2);
		model.insert(3);
		List<Edge<Integer>> edgeList = model.getEdgeList();
		List<PosEdge> posEdgeList = convertEdgeNodeListToPositionEdgeList(edgeList);

		PosEdge e1 = new PosEdge(1, 3);
		assertEquals(posEdgeList.size(), 1);
		assertTrue(posEdgeList.contains(e1));
	}

	private List<PosEdge> convertEdgeNodeListToPositionEdgeList(
			List<Edge<Integer>> edgeList) {
		List<PosEdge> posEdgeList = new ArrayList<BinaryTreeModelTest.PosEdge>();
		for (Edge<Integer> edge : edgeList) {
			posEdgeList.add(new PosEdge(edge));
		}
		return posEdgeList;
	}

	class PosEdge {
		public PosEdge(Edge<?> n) {
			this.parent = n.parentPos.getPosition();
			this.child = n.childPos.getPosition();
		}

		public PosEdge(int parent, int child) {
			this.parent = parent;
			this.child = child;
		}

		int parent;
		int child;

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj.getClass() != getClass()) {
				return false;
			}
			PosEdge rhs = (PosEdge) obj;
			return new EqualsBuilder().append(PosEdge.this.parent, rhs.parent)
					.append(PosEdge.this.child, rhs.child).isEquals();
		}
	}
}
