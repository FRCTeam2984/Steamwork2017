package org.usfirst.frc.team2984.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team2984.robot.util.Wall;

public class WallTest {
	
	@Before
	public void before() {
		
	}
	
	@Test
	public void testDistanceConstructorWithZeroAngleAndClose() {
		double distanceLeft = 1;
		double distanceRight = 1;
		double width = 1;
		Wall expected = new Wall(1, 0);
		
		assertEquals(expected, new Wall(distanceLeft, distanceRight, width));
	}
	
	@Test
	public void testDistanceConstructorWithFortyFiveAngleAndClose() {
		double distanceLeft = 1;
		double distanceRight = 0;
		double width = 1;
		Wall expected = new Wall(0.5, 45);
		
		assertEquals(expected, new Wall(distanceLeft, distanceRight, width));
	}
	
	@Test
	public void testDistanceConstructorWithNegativeFortyFiveAngleAndClose() {
		double distanceLeft = 0;
		double distanceRight = 1;
		double width = 1;
		Wall expected = new Wall(0.5, -45);
		
		assertEquals(expected, new Wall(distanceLeft, distanceRight, width));
	}
	
	@Test
	public void testDistanceConstructorWithZeroAngleAndFar() {
		double distanceLeft = 8;
		double distanceRight = 8;
		double width = 1;
		Wall expected = new Wall(8, 0);
		
		assertEquals(expected, new Wall(distanceLeft, distanceRight, width));
	}
	
	@Test
	public void testDistanceConstructorWithFortyFiveAngleAndFar() {
		double distanceLeft = 10;
		double distanceRight = 9;
		double width = 1;
		Wall expected = new Wall(9.5, 45);
		
		assertEquals(expected, new Wall(distanceLeft, distanceRight, width));
	}
	
	@Test
	public void testDistanceConstructorWithNegativeFortyFiveAngleAndFar() {
		double distanceLeft = 9;
		double distanceRight = 10;
		double width = 1;
		Wall expected = new Wall(9.5, -45);
		
		assertEquals(expected, new Wall(distanceLeft, distanceRight, width));
	}
	
}
