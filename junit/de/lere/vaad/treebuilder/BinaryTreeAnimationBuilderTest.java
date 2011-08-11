package de.lere.vaad.treebuilder;

import static de.lere.vaad.treebuilder.BuilderTestUtils.createNIntegerTree;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

import generators.compression.Huffman.NodeH;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.lang.UnhandledException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import algoanim.animalscript.AnimalScript;
import algoanim.util.Coordinates;
import de.lere.vaad.utils.GraphObject;
import de.lere.vaad.utils.ListMap;
import de.lere.vaad.utils.NodeHelper;
import de.lere.vaad.utils.StrUtils;

public class BinaryTreeAnimationBuilderTest {

	private static final Point BUILDER_GRAPHROOT_COORDS = new Point(300, 275);
	private static final int MODEL_SIZE = 8;
	private static final int FIRST_LEVEL_WIDTH = 240;
	private static final int VERTICAL_SPACING = 60;

	private BinaryTreeModel<Integer> emptyModel;
	@Spy
	AnimalScript language = new AnimalScript("dummynator", "terminator", 640,
			480);

	@Mock
	BinaryTreeModel<Integer> btmock;
	@Mock
	BinaryTreeModel<Integer> btmock2;

	private TreeEventListenerAggregator<Integer> testee;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		testee = new TreeEventListenerAggregator<Integer>(language);
		BinaryTreeLayout layout = createTestLayout();
		testee.setLayout(layout);
		emptyModel = new BinaryTreeModel<Integer>();
	}

	private BinaryTreeLayout createTestLayout() {
		BinaryTreeLayout layout = new BinaryTreeLayout(
				BUILDER_GRAPHROOT_COORDS, FIRST_LEVEL_WIDTH, VERTICAL_SPACING);
		return layout;
	}

	

	@Test
	public void performNoAnimationOnEmptyInsert() throws Exception {
		testee.update(new TreeInsertEvent<Integer>(emptyModel, emptyModel, null));
		assertNoGraphLine(lastLine());
	}

	private String lastLine() {
		return StrUtils.getLastLine(language.getAnimationCode());
	}

	@Test
	public void setModelAddsListener() {
		testee.setModel(btmock);
		verify(btmock).addListener(testee);
	}

	@Test
	public void setModelRemovesOldListener() {
		testee.setModel(btmock);
		testee.setModel(emptyModel);
		verify(btmock).removeListener(testee);
	}

	@Test
	public void setModelAddsNewModelIfOldExists() {
		testee.setModel(btmock);
		testee.setModel(btmock2);
		verify(btmock2).addListener(testee);
	}

	@Test
	public void setNonEmptyModelShowsInitialModel() {
		BinaryTreeModel<Integer> model = BuilderTestUtils.createNIntegerTree(3);
		testee.setModel(model);
		String animationCode = language.getAnimationCode();
		String lastLine = StrUtils.getLastLine(animationCode);
		GraphObject parse = GraphObject.parse(lastLine);
		assertThat(parse.nodes, hasSize(3));
		assertThat(parse.edges, hasSize(2));
	}

	@Test
	public void setEmtpyModelShowsNothing() {
		testee.setModel(emptyModel);
		String animationCode = language.getAnimationCode();
		String lastLine = StrUtils.getLastLine(animationCode);
		assertNoGraphLine(lastLine);
	}

	private void assertNoGraphLine(String lastLine) {
		try {
			GraphObject.parse(lastLine);
			fail("expected not a graph but was: " + lastLine);
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	private void assertLineIsGraphOfExpectedDimensions(String line, int nodes,
			int edges) {
		GraphObject gr = GraphObject.parse(line);
		assertThat("actual nodes of graph objects does not equal the expected",
				gr.nodes, hasSize(nodes));
		assertThat("amount of edges differ from expected", gr.edges,
				hasSize(edges));
	}

	private void assertNthLastAnimationLineIsGraphOfExpectedDimenstion(
			int lineNum, int nodes, int edges) {
		String line = nthLastLine(lineNum);
		assertLineIsGraphOfExpectedDimensions(line, nodes, edges);
	}

	@Test
	public void setNewOverOldHidesOld() throws Exception {
		emptyModel.insert(2);
		testee.setModel(emptyModel);
		assertNthLastAnimationLineIsGraphOfExpectedDimenstion(1, 1, 0);
		BinaryTreeModel<Integer> model5n = BuilderTestUtils
				.createNIntegerTree(5);
		testee.setModel(model5n);

		String animationCode = language.getAnimationCode();
		String nthLastLine = StrUtils.getNthLastLine(animationCode, 2);
		assertThat(Arrays.asList(nthLastLine.split(" ")), both(hasItem("hide"))
				.and(hasItem("\"" + testee.getLayout().graphName + "\"")));

		assertNthLastAnimationLineIsGraphOfExpectedDimenstion(1, 5, 4);
	}

	@Test
	public void setNewModelOverNoModelHidesNothing() throws Exception {
		emptyModel.insert(Integer.MIN_VALUE);
		testee.setModel(emptyModel);
		int nthLast = 2;
		String nthLastLine = nthLastLine(nthLast);
		assertThat(Arrays.asList(nthLastLine.split(" ")), not(hasItem("hide")));
		assertLineIsGraphOfExpectedDimensions(
				lastLine(), 1, 0);
	}

	private String nthLastLine(int nthLast) {
		return StrUtils.getNthLastLine(language.getAnimationCode(), nthLast );
	}

	@Test
	public void renderDeletionOnDegeneratedModelDoesNotFail() {
		BinaryTreeModel<Integer> model = BinaryTreeModel.createTreeByInsert(2,
				2, 2);
		testee.setModel(model);
		model.delete(2);
		model.delete(2);
		model.delete(2);
		model.delete(2);
		fail();
		System.out.println(language.getAnimationCode());
	}

	@Test
	public void deleteRootOfNotZeroLevelTree() {
		BinaryTreeModel<Integer> model = BinaryTreeModel.createTreeByInsert(2,
				2, 2);
		testee.setModel(model);
		Node<Integer> root = model.getRoot();
		Node<Integer> delete = model.delete(root);
		assertEquals(root, delete);

		Node<Integer> root2 = model.getRoot();
		assertThat(root2, not(equalTo(root)));
		assertThat(root2.getParent(), nullValue());
		Node<Integer> delete2 = model.delete(root2);
		assertThat(root2, equalTo(delete2));
		fail();
		System.out.println(language.getAnimationCode());
	}
}