package de.lere.vaad.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Test;

import de.lere.vaad.utils.MathHelper;

public class MathHelperTest {

	@Test
	public void testGetLocationForPosition1() {
		Point2D r1 = newp2d(0, 0);
		Point2D actLoc = MathHelper.getLocation(r1, 1, 56235, 1234);
		assertThat(actLoc, equalTo(r1));
	}

	@Test
	public void testGetLocationForPosition5() {
		Point2D r1 = newp2d(300, 275);
		Point2D actLoc = MathHelper.getLocation(r1, 5, 120, 30);
		double relx = -60;
		double rely = 60;
		assertRelDeltaToRoot(actLoc, r1, relx, rely);
	}

	private void assertRelDeltaToRoot(Point2D actLoc, Point2D root,
			double relx, double rely) {
		assertThat(actLoc,
				equalTo(newp2d(root.getX() + relx, root.getY() + rely)));
	}

	@Test
	public void testGetLocationForPosition6() {
		Point2D r1 = newp2d(300, 275);
		Point2D actLoc = MathHelper.getLocation(r1, 6, 180, 60);
		assertRelDeltaToRoot(actLoc, r1, 90, 120);
	}
	
	@Test
	public void testGetLocationForPosition4() {
		Point2D r1 = newp2d(300, 275);
		Point2D actLoc = MathHelper.getLocation(r1, 4, 120, 60);
		assertRelDeltaToRoot(actLoc, r1, -180, 120);
	}
	
	@Test
	public void testGetLocationForPosition7() {
		Point2D r1 = newp2d(300, 275);
		Point2D actLoc = MathHelper.getLocation(r1, 7, 120, 60);
		assertRelDeltaToRoot(actLoc, r1, 180, 120);
	}
	
	@Test
	public void testGetLocationForPosition8to15() {
		Point2D r1 = newp2d(300, 275);
		int thirdLevelY = 180;
		int[][] positionExpRelX = {
				{8, -210}, //
				{9, -150}, //
				{10, -90}, //
				{11, -30}, //
				{12,30}, //
				{13,90}, //
				{14,150}, //
				{15,210}, //
		};
		
		for (int[] is : positionExpRelX) {
			Point2D actLoc = MathHelper.getLocation(r1, is[0], 120, 60);
			assertRelDeltaToRootWithPos(actLoc, r1, is[1], thirdLevelY, is[0]);
		}
		
	}
	
	@Test
	public void testGetLocationForPosition67(){
		Point2D r1 = newp2d(300,275);
		Point2D location = MathHelper.getLocation(r1, 67, 120, 60);
		assertRelDeltaToRootWithPos(location, r1, -213.75, 360, 67);
	}

	private void assertRelDeltaToRootWithPos(Point2D actLoc, Point2D root, double relx,
			double rely, int position) {
		assertThat("For Position " + position, actLoc,
				equalTo(newp2d(root.getX() + relx, root.getY() + rely)));	
	}

	private Point2D newp2d(double x, double y) {
		return new Point2D.Double(x, y);
	}

	@Test
	public void testLg() {
		assertEquals((double) 0, MathHelper.lg(1), 0.0);
		assertEquals((double) 1, MathHelper.lg(2), 0.0);
		assertEquals((double) 4, MathHelper.lg(16), 0.0);
	}

	@Test
	public void getRelNodeFromHalfPosTest() throws Exception {

		int[][] act_exp = { { 5, -1 }, { 7, 2 }, { 3, 1 } };

		for (int[] ae : act_exp) {
			assertThat(MathHelper.getRelNodeFromHalfPos(ae[0]), equalTo(ae[1]));
		}
	}
	
	@Ignore
	@Test
	public void testFirstEightLocations(){
		Point root = new Point(300,275);
		double absVerticalDistance = 60;
		double firstLevelHorizontalDist = 120;
		ArrayList<Point> resls = new ArrayList<Point>();
		for(int i = 1 ; i <= 20 ; i ++){
			Point location = MathHelper.getLocation(root, i, firstLevelHorizontalDist, absVerticalDistance);
			resls.add(location);
		}
		for (int i = 0; i < resls.size() ; i++) {
			Point p = resls.get(i);
			System.out.println((i+1) + " " + p);
		}
	}
	
	
	@Test
	public void numberOfNodesOnLevelSmallerZeroIsNull() throws Exception {
		assertThat(MathHelper.getMaxNumberOfNodesForLevel(-1),equalTo(0));
	}
	
	@Test
	public void numberOfNodesOnLevelSmallerZeroIsNullGreaterNegNumber() throws Exception {
		assertThat(MathHelper.getMaxNumberOfNodesForLevel(-2345),equalTo(0));
	}
	
	@Test
	public void numberOfNodesOnLevel0Is1() throws Exception {
		assertThat(MathHelper.getMaxNumberOfNodesForLevel(0),equalTo(1));
	}
	
	@Test
	public void numberOfNodesOnLevel1Is2() throws Exception {
		assertThat(MathHelper.getMaxNumberOfNodesForLevel(1),equalTo(2));
	}
	
	@Test
	public void numberOfNodesOnLevel10Is1024() throws Exception {
		assertThat(MathHelper.getMaxNumberOfNodesForLevel(10),equalTo(1024));
	}

}
