package de.lere.vaad;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import generators.compression.Huffman.NodeH;

import java.awt.Point;
import java.lang.reflect.Field;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Test;

import de.lere.vaad.utils.NodeHelper;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class CorrectedOffsetTest {
	AnimalScript animalScript;

	@Before
	public void bf() {
		animalScript = new AnimalScript("test", "tt", 10, 10);
	}

	@Test
	public void testTextOnCoordinatesWorks() throws Exception {
		// Just to show that coordinates used in a Text works fine
		Coordinates aCoordinate = new Coordinates(10, 10);

		Text aTextBasedOnCoordinate = animalScript.newText(aCoordinate,
				"aText", "aTextName", null);

		assertNotNull(aTextBasedOnCoordinate);
		// should work fine
	}

	@Test(expected = NullPointerException.class)
	public void testOffsetOfCoordinateFails() throws Exception {

		Coordinates aCoordinate = new Coordinates(10, 10);

		Offset aOffsetOfCoord = new Offset(10, 10, aCoordinate, "SE");

		animalScript.newText(aOffsetOfCoord, "aText", "aTextName", null);
		fail("exepcted offset relative to an coordinate used in a Text Object to cause an exception, due to a bad implementation in Animal 2.3.27:10-04-2011");
	}

	@Test
	public void testOffsetOnPrimitivesWorks() throws Exception {

		Coordinates aCoordinate = new Coordinates(10, 10);

		Text aTextBasedOnCoordinate = animalScript.newText(aCoordinate,
				"aText", "aTextName", null);

		Offset offset = new Offset(15, 15, aTextBasedOnCoordinate, "NW");

		Text newText = animalScript.newText(offset, "textOnOffset", "anyname",
				null);

		assertNotNull(newText);
		// works fine
	}

	@Test
	public void testOffsetProbReflectionResolve() throws Exception {

		Coordinates aCoordinate = new Coordinates(10, 10);

		Offset aOffsetOfCoord = new Offset(10, 10, aCoordinate, "");

		assertNull(aOffsetOfCoord.getNode());
		assertEquals(Offset.PRIMITIVE_REFERENCE,
				aOffsetOfCoord.getReferenceMode());

		Offset correctedOffset = CorrectedOffset.getOffsetForCoordinate(
				aCoordinate, new OffsetFactoryImpl(), 10, 10);
		assertNotNull(correctedOffset.getNode());
		assertEquals(Offset.NODE_REFERENCE, correctedOffset.getReferenceMode());

		Text aTextBasedOnCoordinate = animalScript.newText(correctedOffset,
				"aText", "aTextName", null);

	}

	@Test
	public void testOffsetOnCoordinatesWithCorrectionWorks() throws Exception {

		Coordinates aCoordinate = new Coordinates(10, 10);

		Offset correctedOffset = CorrectedOffset.getOffsetForCoordinate(
				aCoordinate, new OffsetFactory() {

					@Override
					public Offset getOffset(Coordinates aCoordinate, int x,
							int y) {
						return new OffsetMock(x, y, aCoordinate, "");
					}

				}, 10, 10);

		assertEquals(correctedOffset.getNode(), aCoordinate);
		assertEquals(Offset.NODE_REFERENCE, correctedOffset.getReferenceMode());

		Text aTextBasedOnCoordinate = animalScript.newText(correctedOffset,
				"aText", "aTextName", null);
		assertNotNull(aTextBasedOnCoordinate);
	}

	class OffsetMock extends Offset {

		private Node mynode;
		private int ref;

		public OffsetMock(int xCoordinate, int yCoordinate, Node baseNode,
				String targetDirection) {
			super(xCoordinate, yCoordinate, baseNode, targetDirection);
			this.mynode = baseNode;
			if (mynode instanceof Coordinates)
				this.ref = NODE_REFERENCE;
		}

		@Override
		public Node getNode() {
			if (super.getNode() == null)
				return mynode;
			else
				throw new AssertionFailedError(
						"expected node to be null but was reset to: "
								+ super.getNode());
		}

		@Override
		public int getReferenceMode() {
			if (super.getNode() == null)
				return this.ref;
			else {
				int nodeRef = Offset.NODE_REFERENCE;
				if (this.ref != nodeRef) {
					throw new AssertionFailedError("expected NODE_REFERENCE: "
							+ nodeRef + " but was: " + super.getReferenceMode());
				}
				return super.getReferenceMode();
			}
		}

	}

	@Test
	public void getOffsetForCoordShouldKeepCoordinates() throws Exception {
		Coordinates coordinates = new Coordinates(250, 120);
		Offset offs = CorrectedOffset.getOffsetForCoordinate(coordinates);
		Point coorp = NodeHelper.convertCoordinatesToAWTPoint(coordinates);
		Point offsp = NodeHelper.convertOffsetToAWTPoint(offs);

		assertThat(offsp, equalTo(coorp));
	}

	@Test
	public void getOffsetForCoordShouldProvideReferenceNode() throws Exception {
		Coordinates coordinates = new Coordinates(250, 120);
		// exec
		Offset offs = CorrectedOffset.getOffsetForCoordinate(coordinates, 1, 2);
		// verify
		int referenceMode = offs.getReferenceMode();
		assertEquals("expected reference 2", 2, referenceMode);

		Point offsp = NodeHelper.convertOffsetToAWTPoint(offs);
		Point expectedRel = new Point(1, 2);
		assertThat(offsp, equalTo(expectedRel));

		Coordinates node = (Coordinates) offs.getNode();
		Point retrievedCoords = NodeHelper.convertCoordinatesToAWTPoint(node);
		assertThat(retrievedCoords,
				equalTo(NodeHelper.convertCoordinatesToAWTPoint(coordinates)));
	}

	@Test
	public void getOffsetForCoordShouldNotChangeCoordinates() throws Exception {
		Coordinates coordinates = new Coordinates(250, 120);
		Point pointBefore = NodeHelper
				.convertCoordinatesToAWTPoint(coordinates);
		CorrectedOffset.getOffsetForCoordinate(coordinates, 1, 2);
		assertThat(NodeHelper.convertCoordinatesToAWTPoint(coordinates),
				equalTo(pointBefore));
	}
}
