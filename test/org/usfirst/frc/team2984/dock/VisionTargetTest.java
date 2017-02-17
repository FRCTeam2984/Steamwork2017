package org.usfirst.frc.team2984.dock;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.Test;
import org.usfirst.frc.team2984.dock.VisionTarget;
import org.usfirst.frc.team2984.robot.Camera;
import org.usfirst.frc.team2984.robot.util.Dimension;

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
	public void testGetDistanceReturnsDistanceGivenTenPercentHeightAndHeadOn() {
		VisionTarget target = new VisionTarget(0, 50, 61.8642);
		
		assertEquals(100.0000, target.getDistance(camera, targetSize), 0.0001);
	}
	
	@Test
	public void testGetInternalRotationReturnsZeroGivenNoOffset() {
		VisionTarget target = new VisionTarget(0, 50, 61.8642);
		
		assertEquals(0d, target.getInternalRotation(camera, targetSize), 0.0001);
	}
	
	@Test
	public void testGetInternalRotationReturnsDegreesGivenPositiveOffset() {
		VisionTarget target = new VisionTarget(38.2683, 50, 61.8642);
		
		assertEquals(Math.PI / 8, target.getInternalRotation(camera, targetSize), 0.0001);
	}
	
	@Test
	public void testGetInternalRotationReturnsDegreesGivenNegativeOffset() {
		VisionTarget target = new VisionTarget(-38.2683, 50, 61.8642);
		
		assertEquals(-Math.PI / 8, target.getInternalRotation(camera, targetSize), 0.0001);
	}
	
	@Test
	public void testGetExternalRotationReturnsZeroGivenNoCompression() {
		VisionTarget target = new VisionTarget(0, 50, 102.5);
		
		assertEquals(0d, target.getExternalRotation(camera, targetSize), 0.0001);
	}
	
	@Test
	public void testGetExternalRotationReturnsValueGivenSmallPositiveCompression() {
		VisionTarget target = new VisionTarget(0, 49, 102.5);
		
		assertEquals(0.2003, target.getExternalRotation(camera, targetSize), 0.0001);
	}
	
	@Test
	public void testGetExternalRotationREturnsValueGivenSmallNegativeCompression() {
		VisionTarget target = new VisionTarget(0, -49, 102.5);
		
		assertEquals(Math.PI - 0.2003, target.getExternalRotation(camera, targetSize), 0.0001);
	}
	
	@Test
	public void testGetExternalRotationReturns45DegreesGivenScaledWidth() {
		VisionTarget target = new VisionTarget(0, 50*Math.cos(Math.PI/4), 102.5);
		
		assertEquals(Math.PI / 4, target.getExternalRotation(camera, targetSize), 0.0001);
	}
	
	@Test
	public void testGetExternalRotationReturnsMinus45DegreesGivenNegativeScaledWidth() {
		VisionTarget target = new VisionTarget(0, -50*Math.cos(Math.PI/4), 102.5);
		
		assertEquals(3*Math.PI / 4, target.getExternalRotation(camera, targetSize), 0.0001);
	}
}
