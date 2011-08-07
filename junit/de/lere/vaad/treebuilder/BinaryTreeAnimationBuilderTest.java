package de.lere.vaad.treebuilder;

import static de.lere.vaad.treebuilder.BuilderTestUtils.createNIntegerTree;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.MapTransformer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.UnhandledException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import algoanim.animalscript.AnimalScript;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import de.lere.vaad.CorrectedOffset;
import de.lere.vaad.MathHelper;

public class BinaryTreeAnimationBuilderTest {

	private static final Coordinates BUILDER_GRAPHROOT_COORDS = new Coordinates(
			300, 275);
	private static final int MODEL_SIZE = 8;
	private static final int FIRST_LEVEL_WIDTH = 240;
	private static final int VERTICAL_SPACING = 60;

	@Spy
	AnimalScript language = new AnimalScript("dummynator", "terminator", 640,
			480);

	private BinaryTreeAnimationBuilder<Integer> testee;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		testee = new BinaryTreeAnimationBuilder<Integer>(language);
		BinaryTreeLayout layout = createTestLayout();
		testee.setLayout(layout);
	}

	private BinaryTreeLayout createTestLayout() {
		BinaryTreeLayout layout = new BinaryTreeLayout(
				BUILDER_GRAPHROOT_COORDS, FIRST_LEVEL_WIDTH, VERTICAL_SPACING);
		return layout;
	}

	@Test
	public void shouldGenerateCorrectGraphRepresentationForCurrentBinaryTreeModel() {
		String expected = getStringFromResource("BinaryTreeAnimationBuilderTestExample6Nodes");
		BinaryTreeModel<Integer> model = createNIntegerTree(7);
		testee.setModel(model);
		testee.buildCurrentGraph();
		String animationCode = language.getAnimationCode();
		assertThat(animationCode, containsString(expected));
	}

	private String getStringFromResource(String name) {
		InputStream resourceAsStream = getClass().getResourceAsStream(
				"resources/" + name);
		try {
			return IOUtils.toString(resourceAsStream);
		} catch (IOException e) {
			throw new UnhandledException(e);
		}
	}

	@Test
	public void getPositionsforNoNodes() throws Exception {
		List<Coordinates> generatePositions = testee.generatePositions();
		assertThat(generatePositions, hasSize(0));
	}

	@Test
	public void getPositionsforOnlyRoot() throws Exception {
		BinaryTreeAnimationBuilder<String> btab = new BinaryTreeAnimationBuilder<String>(
				language);
		BinaryTreeModel<String> model = new BreadthFirstBuilder()
				.buildTree("root");
		btab.setModel(model);
		btab.setLayout(new BinaryTreeLayout(new Coordinates(123, 456), 0, 0));
		Coordinates generatePositions2 = btab.generatePositions().get(0);
		Point actual = coordToPoint(generatePositions2);
		assertThat(actual, equalTo(new Point(123, 456)));
	}

	private Point coordToPoint(Coordinates c) {
		return new Point(c.getX(), c.getY());
	}

	@Test
	public void generatesNodesAccordingToLayout() throws Exception {
		int elements = 20;
		BinaryTreeModel<Integer> nElementTree = BuilderTestUtils
				.createNIntegerTree(elements);
		testee.setModel(nElementTree);
		testee.setLayout(new BinaryTreeLayout(BUILDER_GRAPHROOT_COORDS, 120, 60));
		Coordinates[] expected = new Coordinates[elements];
		// expected[0] = new Coordinates(300,275);
		expected[0] = BUILDER_GRAPHROOT_COORDS;
		expected[1] = new Coordinates(180, 335);
		expected[2] = new Coordinates(420, 335);
		expected[3] = new Coordinates(120, 395);
		expected[4] = new Coordinates(240, 395);
		expected[5] = new Coordinates(360, 395);
		expected[6] = new Coordinates(480, 395);
		expected[7] = new Coordinates(90, 455);
		expected[8] = new Coordinates(150, 455);
		expected[9] = new Coordinates(210, 455);
		expected[10] = new Coordinates(270, 455);
		expected[11] = new Coordinates(330, 455);
		expected[12] = new Coordinates(390, 455);
		expected[13] = new Coordinates(450, 455);
		expected[14] = new Coordinates(510, 455);
		expected[15] = new Coordinates(75, 515);
		expected[16] = new Coordinates(105, 515);
		expected[17] = new Coordinates(135, 515);
		expected[18] = new Coordinates(165, 515);
		expected[19] = new Coordinates(195, 515);
		List<Coordinates> coordinates = testee.generatePositions();
		List<Point> actual = convertCoordsToPoint(coordinates);
		List<Point> expectedls = convertCoordsToPoint(expected);
		assertThat(coordinates, hasSize(expected.length));
		assertThat(actual, hasItems(expectedls.toArray(new Point[0])));
	}

	@Test
	public void testGetLabelsNoNodes() throws Exception {
		List<String> labelsFromModel = testee.getLabelsFromModel();
		assertThat(labelsFromModel, hasSize(0));
	}

	@Test
	public void testGetLabelsTwoNodes() throws Exception {
		testee.setModel(BreadthFirstBuilder.build(1, 2));
		List<String> labelsFromModel = testee.getLabelsFromModel();
		assertThat(labelsFromModel, hasItems("1", "2"));
	}

	private List<Point> convertCoordsToPoint(Coordinates[] expected) {
		return convertCoordsToPoint(Arrays.asList(expected));
	}

	private List<Point> convertCoordsToPoint(List<Coordinates> coordinates) {
		ArrayList<Point> result = new ArrayList<Point>();
		for (Coordinates coords : coordinates) {
			Point point = new Point(coords.getX(), coords.getY());
			result.add(point);
		}
		return result;
	}

	@Test
	public void testEmptyAdjacencyEmptyList() throws Exception {
		BinaryTreeModel<Integer> nElementTree = createNIntegerTree(0);
		int[][] adjMatrix = testee.getAdjancencyMatrix(nElementTree
				.getNodesInOrder());
		assertThat(adjMatrix.length, equalTo(0));
	}

	@Test
	public void testCreateSimpleAdjacencyMatrixForGivenNodes() throws Exception {
		BinaryTreeModel<Integer> nElementTree = createNIntegerTree(2);
		// exec
		int[][] adjMatrix = testee.getAdjancencyMatrix(nElementTree
				.getNodesInOrder());
		// verify
		int[][] expected = {//
		{ 0, 0 },// 2
				{ 1, 0 },// 1
		};
		assertThat(adjMatrix, arrayContaining(expected));
	}

	@Test
	public void testCreateAdjacencyMatrixForGivenNodes() throws Exception {
		BinaryTreeModel<Integer> nElementTree = createNIntegerTree(7);
		// exec
		int[][] adjMatrix = testee.getAdjancencyMatrix(nElementTree
				.getNodesInOrder());
		// verify
		int[][] expectedMatrix = {// Matrix is in Node Order
		{ 0, 0, 0, 0, 0, 0, 0 }, // 4
				{ 1, 0, 1, 0, 0, 0, 0 }, // 2->4,5
				{ 0, 0, 0, 0, 0, 0, 0 }, // 5
				{ 0, 1, 0, 0, 0, 1, 0 }, // 1->2,3
				{ 0, 0, 0, 0, 0, 0, 0 }, // 6
				{ 0, 0, 0, 0, 1, 0, 1 }, // 3->6,7
				{ 0, 0, 0, 0, 0, 0, 0 }, // 7
		};
		assertThat(adjMatrix, arrayContaining(expectedMatrix));
	}

}