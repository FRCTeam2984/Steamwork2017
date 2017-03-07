package org.usfirst.frc.team2984.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.usfirst.frc.team2984.robot.util.MathUtil;

public class MathUtilTest {
	
	@Test
	public void shortestDeltaAngleReturnsZeroGivenEqualAngles() {
		double start = 50;
		double end = 50;
		double expected = 0;
		assertEquals(expected, MathUtil.shortestDeltaAngle(start, end), 0.00001);
	}
	
	@Test
	public void shortestDeltaAngleReturnsNegativeGivenAnglesGoingOverZero() {
		double start = 0;
		double end = 300;
		double expected = -60;
		assertEquals(expected, MathUtil.shortestDeltaAngle(start, end), 0.00001);
	}
	
	@Test
	public void shortestDeltaAngleReturnsPositiveGivenAnglesGoingOverZero() {
		double start = 300;
		double end = 10;
		double expected = 70;
		assertEquals(expected, MathUtil.shortestDeltaAngle(start, end), 0.00001);
	}
	
	@Test
	public void shortestDeltaAngleReturnsZeroGivenEqualAngleMultiplesOfThreeSixty() {
		double start = 750;
		double end = 30;
		double expected = 0;
		assertEquals(expected, MathUtil.shortestDeltaAngle(start, end), 0.00001);
	}
	
	@Test
	public void shortestDeltaAngleReturnsPositiveGivenNegativeAndPositiveAngle() {
		double start = -60;
		double end = 60;
		double expected = 120;
		assertEquals(expected, MathUtil.shortestDeltaAngle(start, end), 0.00001);
	}
	
}
