package org.usfirst.frc.team2984.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Moments;
import org.usfirst.frc.team2984.robot.util.SingleTarget;

import edu.wpi.first.wpilibj.HLUsageReporting;

public class SingleTargetTest {
	
	@Before
	public void before() {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
	}
	
	@Test
	public void testConstructorSetsWidthOfUnrotatedRect() {
		RotatedRect rect = new RotatedRect(new double[]{0,0,20,0,0});
		Moments moments = new Moments();
		SingleTarget target = new SingleTarget(rect, moments);
		
		assertEquals(20, target.getWidth(), 2);
	}
	
	@Test
	public void testConstructorSetsWidthOfNintyDegreeRotatedRect() {
		RotatedRect rect = new RotatedRect(new double[]{0,0,0,20,90});
		Moments moments = new Moments();
		SingleTarget target = new SingleTarget(rect, moments);
		
		assertEquals(20, target.getWidth(), 2);
	}
	
	@Test
	public void testConstructorSetsWidthOfNegativeNintyDegreeRotatedRect() {
		RotatedRect rect = new RotatedRect(new double[]{0,0,0,20,-90});
		Moments moments = new Moments();
		SingleTarget target = new SingleTarget(rect, moments);
		
		assertEquals(20, target.getWidth(), 2);
	}
	
	@Test
	public void testConstructorSetsHeightOfUnrotatedRect() {
		RotatedRect rect = new RotatedRect(new double[]{0,0,0,20,0});
		Moments moments = new Moments();
		SingleTarget target = new SingleTarget(rect, moments);
		
		assertEquals(20, target.getHeight(), 2);
	}
	
	@Test
	public void testConstructorSetsHeightOfNintyDegreeRotatedRect() {
		RotatedRect rect = new RotatedRect(new double[]{0,0,20,0,90});
		Moments moments = new Moments();
		SingleTarget target = new SingleTarget(rect, moments);
		
		assertEquals(20, target.getHeight(), 2);
	}
	
	@Test
	public void testConstructorSetsX() {
		RotatedRect rect = new RotatedRect();
		Moments moments = new Moments(new double[]{1, 1337, 42});
		SingleTarget target = new SingleTarget(rect, moments);
		
		assertEquals(1337, target.getX(), 2);
	}

	@Test
	public void testConstructorSetsY() {
		RotatedRect rect = new RotatedRect();
		Moments moments = new Moments(new double[]{1, 0, 42});
		SingleTarget target = new SingleTarget(rect, moments);
		
		assertEquals(42, target.getY(), 2);
	}
}
