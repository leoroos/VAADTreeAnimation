package de.lere.vaad;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.awt.geom.Point2D;

import org.junit.Test;

public class MathHelperTest {

	@Test
	public void testGetLocation1() {
		Point2D r1 = newp2d(0, 0);
		Point2D actLoc = MathHelper.getLocation(r1, 1, 1234, 56235);
		assertThat(actLoc, equalTo(r1));
	}

	@Test
	public void testGetLocation2() {
		Point2D r1 = newp2d(300, 275);
		Point2D actLoc = MathHelper.getLocation(r1, 5, 60, 120);
		double relx = -60;
		double rely = 120;
		assertRelDeltaToRoot(actLoc, r1, relx, rely);
	}

	private void assertRelDeltaToRoot(Point2D actLoc, Point2D root,
			double relx, double rely) {
		assertThat(actLoc,
				equalTo(newp2d(root.getX() + relx, root.getY() + rely)));
	}

	@Test
	public void testGetLocation3() {
		Point2D r1 = newp2d(300, 275);
		Point2D actLoc = MathHelper.getLocation(r1, 6, 60, 120);
		assertRelDeltaToRoot(actLoc, r1, 60, 120);
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

}
