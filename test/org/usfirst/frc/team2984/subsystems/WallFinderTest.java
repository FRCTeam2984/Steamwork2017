package org.usfirst.frc.team2984.subsystems;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team2984.robot.subsystems.WallFinder;
import org.usfirst.frc.team2984.robot.util.DistanceSensor;
import org.usfirst.frc.team2984.robot.util.Wall;
import org.usfirst.frc.team2984.util.DummyReporter;

import edu.wpi.first.wpilibj.HLUsageReporting;

public class WallFinderTest {
	private WallFinder wallFinder;
	
	private DistanceSensor left;
	private DistanceSensor right;
	
	@Before
	public void before() {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
		
		left = mock(DistanceSensor.class);
		right = mock(DistanceSensor.class);
		wallFinder = new WallFinder(left, right, 1);
	}
	
	@Test
	public void testDistanceGivenNoAngleAndZeroDistance() {
		when(left.getDistanceInInches()).thenReturn(0D);
		when(right.getDistanceInInches()).thenReturn(0D);
		Wall wall = wallFinder.getWall();
		assertEquals(0, wall.getDistance(), 0.00001);
	}
	
	@Test
	public void testDistanceGivenNoAngleAndOneDistance() {
		when(left.getDistanceInInches()).thenReturn(1D);
		when(right.getDistanceInInches()).thenReturn(1D);
		Wall wall = wallFinder.getWall();
		assertEquals(1, wall.getDistance(), 0.00001);
	}
	
	@Test
	public void testDistanceGiven45AngleAndOneDistance() {
		when(left.getDistanceInInches()).thenReturn(1.5);
		when(right.getDistanceInInches()).thenReturn(0.5);
		Wall wall = wallFinder.getWall();
		assertEquals(1, wall.getDistance(), 0.00001);
	}
	
	@Test
	public void testDistanceGiven30AngleAndThreeDistance() {
		when(left.getDistanceInInches()).thenReturn(3.288675135);
		when(right.getDistanceInInches()).thenReturn(2.711324865);
		Wall wall = wallFinder.getWall();
		assertEquals(3, wall.getDistance(), 0.00001);
	}
	
	@Test
	public void testAngleGivenNoAngleAndZeroDistance() {
		when(left.getDistanceInInches()).thenReturn(0D);
		when(right.getDistanceInInches()).thenReturn(0D);
		Wall wall = wallFinder.getWall();
		assertEquals(0, wall.getAngle(), 0.00001);
	}
	
	@Test
	public void testAngleGivenPiOverSixAngleAndOneDistance() {
		when(left.getDistanceInInches()).thenReturn(1.288675135);
		when(right.getDistanceInInches()).thenReturn(0.711324865);
		Wall wall = wallFinder.getWall();
		assertEquals(30, wall.getAngle(), 0.00001);
	}
	
	@Test
	public void testAngleGivenNegativePiOverSixAngleAndOneDistance() {
		when(left.getDistanceInInches()).thenReturn(0.711324865);
		when(right.getDistanceInInches()).thenReturn(1.288675135);
		Wall wall = wallFinder.getWall();
		assertEquals(-30, wall.getAngle(), 0.00001);
	}
}
