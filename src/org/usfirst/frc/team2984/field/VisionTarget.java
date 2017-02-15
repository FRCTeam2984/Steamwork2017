package org.usfirst.frc.team2984.field;

import org.usfirst.frc.team2984.robot.Camera;
import org.usfirst.frc.team2984.robot.util.Dimension;

public class VisionTarget {
	private double offset;
	private double width;
	private double height;
	
	/**
	 * A rectangular vision target. Constructor parameters are measured in pixels.
	 * 
	 * @param offset may be positive, negative, or zero
	 * @param width positive value indicates left side larger than right, negative
	 * value indicates right side larger than left
	 * @param height positive value only
	 */
	public VisionTarget(double offset, double width, double height) {
		this.offset = offset;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * See: http://wpilib.screenstepslive.com/s/4485/m/24194/l/288985-identifying-and-processing-the-targets
	 * 
	 * @param camera
	 * @param physicalTargetSize the size of the physical target, expressed in inches
	 * @return the distance to the target in inches
	 */
	public double getDistance(Camera camera, Dimension physicalTargetSize) {
		double theta = Math.toRadians(camera.angleOfView.height) / 2;
		
		return this.getVerticalFieldOfView(camera, physicalTargetSize) / (2 * Math.tan(theta));
	}
	
	/**
	 * Returns the apparent rotation of the camera about the target, in radians.
	 * 
	 * @param camera
	 * @param physicalTargetSize the dimensions of the physical target, in inches
	 * @return the apparent rotation of the camera about the target, in radians
	 */
	public double getInternalRotation(Camera camera, Dimension physicalTargetSize) {
		double distance = this.getDistance(camera,  physicalTargetSize);
		
		return Math.asin(this.offset / distance);
	}
	
	/**
	 * Returns the apparent rotation of the target, in radians and within the range
	 * 0 to π. Values from 0 to π/2 indicate the target has turned right.
	 * Values from 0 to π indicate the target has turned left.
	 * 
	 * @param camera
	 * @param physicalTargetSize the dimensions of the physical vision target, in inches
	 * @return the external rotation of the camera, in radians
	 */
	public double getExternalRotation(Camera camera, Dimension physicalTargetSize) {
		double expectedWidth = this.height * (physicalTargetSize.width / physicalTargetSize.height);
		double cosine = this.width / expectedWidth;
		
		return Math.acos(cosine);
	}
	
	/**
	 *
	 * @return vertical field of view, in inches
	 */
	private double getVerticalFieldOfView(Camera camera, Dimension objectSize) {
		return camera.resolution.height * objectSize.height / this.height;
	}
}
