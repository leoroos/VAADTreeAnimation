package de.lere.vaad.treebuilder;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

import algoanim.properties.GraphProperties;


public class BinaryTreeLayoutTest {

	private BinaryTreeLayout binaryTreeLayout;
	private int verticalSpacing;
	private int firstLevelWidth;
	private Point graphRootPoint;

	@Before
	public void setUp() {
		graphRootPoint = new Point(300, 300);
		firstLevelWidth = 120;
		verticalSpacing = 30;
		binaryTreeLayout = new BinaryTreeLayout(graphRootPoint, firstLevelWidth, verticalSpacing, new GraphProperties() );
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void throwExceptionWhenRootNull() throws Exception {
		new BinaryTreeLayout(null, firstLevelWidth, verticalSpacing, new GraphProperties() );
	}
	
	
	@Test
	public void calculatesNWGraphBoundary() throws Exception {
		Point p = binaryTreeLayout.getNWBoundary();
		assertThat(p, equalTo(new Point(60,300)));
	}
	
	@Test
	public void calculatesNWGraphBoundaryRespectsIngoresNoSmallerZeroPossible() throws Exception {
		binaryTreeLayout = new BinaryTreeLayout(graphRootPoint, 300, verticalSpacing, new GraphProperties() );
		Point p = binaryTreeLayout.getNWBoundary();
		assertThat(p, equalTo(new Point(-300,300)));
	}
	
	@Test
	public void calculatesNEGraphBoundary() throws Exception {
		Point p = binaryTreeLayout.getNEBoundary();
		assertThat(p, equalTo(new Point(540,300)));
	}
	
}
