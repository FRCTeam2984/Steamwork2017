package org.usfirst.frc.team2984.util;

import static org.junit.Assert.assertEquals;

import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Rect;
import org.usfirst.frc.team2984.robot.util.CameraSpecification;
import org.usfirst.frc.team2984.robot.util.Dimension;
import org.usfirst.frc.team2984.robot.util.Motion;
import org.usfirst.frc.team2984.robot.util.VisionTarget;

import edu.wpi.first.wpilibj.HLUsageReporting;

public class VisionTargetTest {
	// from manual
	private Dimension targetSize = new Dimension(5, 10.25);
	private Dimension resolution = new Dimension(1000, 500);
	private Dimension fieldOfView = new Dimension(55, 45);
	private CameraSpecification camera;
	
	@Before
	public void before() {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
		camera = new CameraSpecification(fieldOfView, resolution, 0);
	}
	
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
		
		assertEquals(9.855184, target.getDistance(camera, targetSize), 0.0001);
	}
	
	@Test
	public void getDistanceReturnsDistanceGivenFarHeadOn() {
		VisionTarget target = new VisionTarget(0, 5 * targetSize.width, 5 * targetSize.height);
		
		assertEquals(127.04878, target.getDistance(camera, targetSize), 0.0001);
	}
	
	@Test
	public void getRotationReturnsZeroGivenNoOffset() {
		VisionTarget target = new VisionTarget(0, 50, 61.8642);
		
		assertEquals(0d, target.getRotation(camera), 0.0001);
	}
	
	@Test
	public void getRotationReturnsDegreesGivenPositiveOffset() {
		VisionTarget target = new VisionTarget(181.818181818, 50, 61.8642);
		
		assertEquals(10, target.getRotation(camera), 0.0001);
	}
	
	@Test
	public void getRotationReturnsDegreesGivenNegativeOffset() {
		VisionTarget target = new VisionTarget(-181.818181818, 50, 61.8642);
		
		assertEquals(-10, target.getRotation(camera), 0.0001);
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
	
	@Test
	public void getWidthReturnsWidthWhenGivenLeftThenRight() {
		Rect left = new Rect(0, 0, 50, 100);
		Rect right = new Rect(200, 0, 50, 100);
		VisionTarget target = new VisionTarget(left, right, camera);
		
		assertEquals(250, target.getWidth(), 0.00001);
	}
	
	@Test
	public void getWidthReturnsWidthWhenGivenRightThenLeft() {
		Rect left = new Rect(200, 0, 100, 100);
		Rect right = new Rect(0, 0, 50, 100);
		VisionTarget target = new VisionTarget(left, right, camera);
		
		assertEquals(275, target.getWidth(), 0.00001);
	}
	
	@Test
	public void getClockAngleReturnsZeroGivenHeadOnTarget() {
		VisionTarget target = new VisionTarget(0, 0, 10);
		assertEquals(0, target.getClockAngle(camera, 0, 180), 0.00001);
	}
	
	@Test
	public void getClockAngleReturnsThirtyFiveGivenZeroRobotAngleAndNegativeTwentyFiveCameraAngle() {
		VisionTarget target = new VisionTarget(-0.909090909*camera.resolution.width/2, 0, 10);
		assertEquals(-35, target.getClockAngle(camera, 0, 120), 0.00001);
	}
	
	@Test
	public void getClockAngleReturnsThirtyFiveGivenZeroRobotAngleAndTwentyFiveCameraAngle() {
		VisionTarget target = new VisionTarget(0.909090909*camera.resolution.width/2, 0, 10);
		assertEquals(35, target.getClockAngle(camera, 0, 240), 0.00001);
	}
	
	@Test
	public void getClockAngleReturnsZeroGivenZeroRobotAngleZeroCameraAngleAndNegativeSixtyGyroAngle() {
		VisionTarget target = new VisionTarget(0, 0, 10);
		assertEquals(0, target.getClockAngle(camera, -60, 120), 0.00001);
	}
	
	@Test
	public void testDistanceUnerOtherCameraSettings(){
		VisionTarget target = new VisionTarget(0, 0, 16.50395806);
		Dimension cameraResolution = new Dimension(320, 240);
		Dimension cameraFOV = new Dimension(56,41.625);
		CameraSpecification cameraSpec = new CameraSpecification(cameraFOV, cameraResolution, 0);
		double dist = target.getDistance(cameraSpec, new Dimension(10.25, 5));
		
		assertEquals(100, dist, 0.00001);
	}
	
	@Test
	public void testAngleUnerOtherCameraSettings(){
		VisionTarget target = new VisionTarget(-50, 0, 16.50395806);
		Dimension cameraResolution = new Dimension(100, 100);
		Dimension cameraFOV = new Dimension(90,0);
		CameraSpecification cameraSpec = new CameraSpecification(cameraFOV, cameraResolution, 10);
		double angle = target.getRotation(cameraSpec);
		
		assertEquals(-45.43854, angle, 0.0001);
	}
}
