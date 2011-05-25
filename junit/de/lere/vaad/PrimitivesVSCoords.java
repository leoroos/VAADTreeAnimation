package de.lere.vaad;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Test;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class PrimitivesVSCoords {
	AnimalScript animalScript;

	@Before
	public void bf() {
		animalScript = new AnimalScript("test", "tt", 10, 10);
	}

	@Test
	public void testCoordsWithText() throws Exception {

		Coordinates aCoordinate = new Coordinates(10, 10);

		Text aTextBasedOnCoordinate = animalScript.newText(aCoordinate,
				"aText", "aTextName", null);

		// works fine
	}

	@Test
	public void testOffsetProb() throws Exception {

		Coordinates aCoordinate = new Coordinates(10, 10);

		Offset aOffsetOfCoord = new Offset(10, 10, aCoordinate, "SE");

		try {
			Text aTextBasedOnCoordinate = animalScript.newText(aOffsetOfCoord,
					"aText", "aTextName", null);
			fail();
		} catch (NullPointerException e) {
			// expected
		}
	}

	@Test
	public void testWorkOnPrimitives() throws Exception {

		Coordinates aCoordinate = new Coordinates(10, 10);

		Text aTextBasedOnCoordinate = animalScript.newText(aCoordinate,
				"aText", "aTextName", null);

		Offset offset = new Offset(15, 15, aTextBasedOnCoordinate, "NW");

		animalScript.newText(offset, "textOnOffset", "anyname", null);

		// works fine
	}

	@Test
	public void testOffsetProbReflectionResolve() throws Exception {

		Coordinates aCoordinate = new Coordinates(10, 10);

		Offset aOffsetOfCoord = new Offset(10, 10, aCoordinate, "");

		assertNull(aOffsetOfCoord.getNode());
		assertEquals(Offset.PRIMITIVE_REFERENCE, aOffsetOfCoord
				.getReferenceMode());

		Offset correctedOffset = CorrectedOffset.getOffsetForCoordinate(aCoordinate, new OffsetFactoryImpl(), 10, 10);
		assertNotNull(correctedOffset.getNode());
		assertEquals(Offset.NODE_REFERENCE, correctedOffset.getReferenceMode());

		Text aTextBasedOnCoordinate = animalScript.newText(correctedOffset,
				"aText", "aTextName", null);

	}



	@Test
	public void testOffsetIfGoodSet() throws Exception {

		Coordinates aCoordinate = new Coordinates(10, 10);

		Offset correctedOffset = CorrectedOffset.getOffsetForCoordinate(aCoordinate, new OffsetFactory() {
			
			@Override
			public Offset getOffset(Coordinates aCoordinate, int x, int y) {
				return new OffsetMock(x, y, aCoordinate, "");
			}
			
		} , 10, 10);

		assertEquals(correctedOffset.getNode(), aCoordinate);
		assertEquals(Offset.NODE_REFERENCE, correctedOffset.getReferenceMode());

		Text aTextBasedOnCoordinate = animalScript.newText(correctedOffset,
				"aText", "aTextName", null);

	}

	class OffsetMock extends Offset {

		private Node mynode;
		private int ref;

		public OffsetMock(int xCoordinate, int yCoordinate, Node baseNode,
				String targetDirection) {
			super(xCoordinate, yCoordinate, baseNode, targetDirection);
			this.mynode = baseNode;
			if(mynode instanceof Coordinates)
				this.ref = NODE_REFERENCE;
		}

		@Override
		public Node getNode() {
			if (super.getNode() == null)
				return mynode;
			else
				throw new AssertionFailedError("expected node to be null but was reset to: " + super.getNode());
		}
		
		@Override
		public int getReferenceMode() {
			if (super.getNode() == null)
				return this.ref;
			else{
				int nodeRef = Offset.NODE_REFERENCE;
				if(this.ref != nodeRef){
					throw new AssertionFailedError("expected NODE_REFERENCE: " + nodeRef + " but was: " + super.getReferenceMode());
				}
				return super.getReferenceMode();
			}
		}

	}
}
