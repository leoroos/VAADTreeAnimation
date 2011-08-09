package de.lere.vaad.utils;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Point;

import org.junit.Test;

import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.BreadthFirstBuilder;
import de.lere.vaad.treebuilder.BuilderTestUtils;
import de.lere.vaad.treebuilder.Node;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Offset;


public class NodeHelperTest {

	@Test
	public void testConversionFromCoords() throws Exception {
		
		Point cp = NodeHelper.convertCoordinatesToAWTPoint(new Coordinates(1,2));
		assertThat(cp, equalTo(new Point(1,2)));
		
	}
	
	@Test
	public void testConversionFromOffsetRelToCoord() throws Exception {
		Coordinates cm = new Coordinates(1, 2);
		
		Offset offset = CorrectedOffset.getOffsetForCoordinate(cm, 1, 2);
		Point convertOffsetToAWTPoint = NodeHelper.convertOffsetToAWTPoint(offset);
		
		assertThat(convertOffsetToAWTPoint, equalTo(new Point(2,4)));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConversionFrom() throws Exception {
		AnimalScript animalScript = new AnimalScript("nothing", "42", 12, 12);
		Text newText = animalScript.newText(new Coordinates(1, 2), "any", null, null);
		Offset offset = new Offset(1,2,newText,"SW");
		//exec
		Point convertOffsetToAWTPoint = NodeHelper.convertOffsetToAWTPoint(offset);
		//verify
		assertThat(convertOffsetToAWTPoint, equalTo(new Point(2,4)));
	}	
}
