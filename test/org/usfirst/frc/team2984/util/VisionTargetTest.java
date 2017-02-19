package org.usfirst.frc.team2984.util;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.Test;
import org.usfirst.frc.team2984.robot.Camera;
import org.usfirst.frc.team2984.robot.util.Dimension;
import org.usfirst.frc.team2984.robot.util.Motion;
import org.usfirst.frc.team2984.robot.util.VisionTarget;

public class VisionTargetTest {
	// from manual
	private Dimension targetSize = new Dimension(5, 10.25);
	private Dimension resolution = new Dimension(1000, 500);
	private Dimension fieldOfView = new Dimension(55, 45);
	private Camera camera = new Camera(resolution, fieldOfView);
	
	@Test
	public void testPointsConstructorSetsCenter() {
		Point2D topLeft = new Point2D.Double(-15, 10);
		Point2D topRight = new Point2D.Double(5, 10);
		Point2D bottomLeft = new Point2D.Double(-15, -10);
		Point2D bottomRight = new Point2D.Double(5, -10);
		VisionTarget target = new VisionTarget(topLeft, topRight, bottomLeft, bottomRight);
		
		assertEquals(-5.0, target.getCenter(), 0.0001);
	}
	
	@Test
	public void testPointConstructorSetsWidth() {
		Point2D topLeft = new Point2D.Double(-15, 10);
		Point2D topRight = new Point2D.Double(5, 10);
		Point2D bottomLeft = new Point2D.Double(-15, -10);
		Point2D bottomRight = new Point2D.Double(5, -10);
		VisionTarget target = new VisionTarget(topLeft, topRight, bottomLeft, bottomRight);
		
		assertEquals(20d, target.getWidth(), 0.0001);
	}
	
	@Test
	public void testPointConstructorSetsHeight() {
		Point2D topLeft = new Point2D.Double(-15, 10);
		Point2D topRight = new Point2D.Double(5, 10);
		Point2D bottomLeft = new Point2D.Double(-15, -10);
		Point2D bottomRight = new Point2D.Double(5, -10);
		VisionTarget target = new VisionTarget(topLeft, topRight, bottomLeft, bottomRight);
		
		assertEquals(20d, target.getHeight(), 0.0001);
	}
	
	@Test
	public void getDistanceReturnsDistanceGivenNearHeadOn() {
		VisionTarget target = new VisionTarget(0, 50 * targetSize.width, 50 * targetSize.height);
		
		assertEquals(12.0711, target.getDistance(camera, targetSize), 0.0001);
	}
	
	@Test
	public void getDistanceReturnsDistanceGivenFarHeadOn() {
		VisionTarget target = new VisionTarget(0, 5 * targetSize.width, 5 * targetSize.height);
		
		assertEquals(120.7107, target.getDistance(camera, targetSize), 0.0001);
	}
	
	@Test
	public void getRotationReturnsZeroGivenNoOffset() {
		VisionTarget target = new VisionTarget(0, 50, 61.8642);
		
		assertEquals(0d, target.getRotation(camera, targetSize), 0.0001);
	}
	
	@Test
	public void getRotationReturnsDegreesGivenPositiveOffset() {
		VisionTarget target = new VisionTarget(38.2683, 50, 61.8642);
		
		assertEquals(Math.PI / 8, target.getRotation(camera, targetSize), 0.0001);
	}
	
	@Test
	public void getRotationReturnsDegreesGivenNegativeOffset() {
		VisionTarget target = new VisionTarget(-38.2683, 50, 61.8642);
		
		assertEquals(-Math.PI / 8, target.getRotation(camera, targetSize), 0.0001);
	}
	
	@Test(expected=RuntimeException.class)
	public void getClockAngleThrowsGivenImpossibleApparentWidth() {
		VisionTarget target = new VisionTarget(0, -40, 61.8642);
		
		target.getClockAngle(camera, targetSize);
	}
	
	@Test
	public void getClockAngleReturnsZeroGivenNoCompression() {
		VisionTarget target = new VisionTarget(0, 50, 102.5);
		
		assertEquals(0d, target.getClockAngle(camera, targetSize), 0.0001);
	}
	
	@Test
	public void getClockAngleReturnsCounterclockwiseAngleGivenSmallPositiveCompression() {
		VisionTarget target = new VisionTarget(0, 49, 102.5);
		
		assertEquals(-0.2003, target.getClockAngle(camera, targetSize), 0.0001);
	}
	
	@Test
	public void getClockAngleReturnsClockwiseAngleGivenSmallNegativeCompression() {
		VisionTarget target = new VisionTarget(0, -49, 102.5);
		
		assertEquals(0.2003, target.getClockAngle(camera, targetSize), 0.0001);
	}
	
	@Test
	public void getClockAngleReturnsMinus45DegreesGivenPositiveCompression() {
		VisionTarget target = new VisionTarget(0, 50*Math.cos(Math.PI/4), 102.5);
		
		assertEquals(-Math.PI / 4, target.getClockAngle(camera, targetSize), 0.0001);
	}
	
	@Test
	public void getClockAngleReturn45DegreesGivenNegativeCompression() {
		VisionTarget target = new VisionTarget(0, -50*Math.cos(Math.PI/4), 102.5);
		
		assertEquals(Math.PI / 4, target.getClockAngle(camera, targetSize), 0.0001);
	}
	
	@Test
	public void getMotionReturnsStopGivenHeadOnBelowDistanceThreshold() {
		// 12 inches
		VisionTarget target = new VisionTarget(0, 50 * targetSize.width, 50 * targetSize.height);
		Motion expected = new Motion(0, 0, 0);
		
		assertEquals(expected, target.getMotion(target, camera, targetSize));
	}
	
	@Test
	public void getMotionReturnsForwardGivenHeadOnAboveDistanceThreshold() {
		// 36 inches
		VisionTarget target = new VisionTarget(0, (50/3) * targetSize.width, (50/3) * targetSize.height);
		Motion expected = new Motion(0, 0.2, 0);
		
		assertEquals(expected, target.getMotion(target, camera, targetSize));
	}
	
	@Test
	public void getMotionReturnsLeftwardRotationGivenTurnedRightBelowDistanceThreshold() {
		VisionTarget target = new VisionTarget(0.5, 50 * targetSize.width, 50 * targetSize.height);
		Motion expected = new Motion(0, 0, -0.2);
		
		assertEquals(expected, target.getMotion(target, camera, targetSize));
	}
	
	@Test
	public void getMotionReturnsRightwardRotationGivenTurnedLeftBelowDistanceThreshold() {
		VisionTarget target = new VisionTarget(-0.5, 50 * targetSize.width, 50 * targetSize.height);
		Motion expected = new Motion(0, 0, 0.2);
		
		assertEquals(expected, target.getMotion(target, camera, targetSize));
	}
	
	@Test
	public void getMotionReturnsStopGivenRightwardClockAngleBelowDistanceThreshold() {
		VisionTarget target = new VisionTarget(0, 0.9 * 50 * targetSize.width, 50 * targetSize.height);
		Motion expected = new Motion(0, 0, 0);
		
		assertEquals(expected, target.getMotion(target, camera, targetSize));
	}
	
	@Test
	public void getMotionReturnsLeftStrafeGivenRightwardClockAngleAboveDistanceThreshold() {
		VisionTarget target = new VisionTarget(0, -0.9 * (50/3) * targetSize.width, (50/3) * targetSize.height);
		Motion expected = new Motion(-0.2, 0, 0);
		
		assertEquals(expected, target.getMotion(target, camera, targetSize));
	}
	
	@Test
	public void getMotionReturnsRightStrafeGivenLeftwardClockAngleAboveDistanceThreshold() {
		VisionTarget target = new VisionTarget(0, 0.9 * (50/3) * targetSize.width, (50/3) * targetSize.height);
		Motion expected = new Motion(0.2, 0, 0);
		
		assertEquals(expected, target.getMotion(target, camera, targetSize));
	}
	
	@Test
	public void getMotionReturnsStrafeAndRotateGivenRightwardClockAngleAndTurnedRight() {
		VisionTarget target = new VisionTarget(0.5, 0.9 * (50/3) * targetSize.width, (50/3) * targetSize.height);
		Motion expected = new Motion(0.2, 0, -0.2);
		
		assertEquals(expected, target.getMotion(target, camera, targetSize));
	}
}
