package org.usfirst.frc.team2984.robot.util;

import java.awt.geom.Point2D;

import org.opencv.core.Rect;

/**
 * A rectangular vision target, representing either an angled or head-on orientation. Use
 * this class to determine distance and internal/external angles to a physical target.
 */
public class VisionTarget {
	private double offset;
	private double width;
	private double height;
	
	/**
	 * A rectangular vision target, representing either an angled or head-on orientation.
	 * Units are pixels.
	 * 
	 * @param offset distance from horizontal center; may be positive, negative, or zero
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
	 * A rectangular vision target, representing either an angled or head-on orientation.
	 * Units are pixels.
	 * 
	 * @param rectA one of the two vision rectangles
	 * @param rectB the other vision rectangle
	 */
	public VisionTarget(Rect rectA, Rect rectB, CameraSpecification camera){
		Rect left = rectA;
		Rect right = rectB;
		if(rectA.x > rectB.x){
			left = right;
			right = rectA;
		}
		double width = camera.resolution.width;
		double averageX = (left.x + right.x + left.width + right.width)/2D;
		this.offset = averageX - width/2;
		this.height = (left.height + right.height)/2;
		this.width = right.x - left.x + right.width/2 + left.width/2;
	}
	
	/**
	 * A rectangular vision target, representing either an angled or head-on orientation.
	 * Units are pixels.
	 * 
	 * @param rectA one of the two vision rectangles
	 * @param rectB the other vision rectangle
	 */
	public VisionTarget(SingleTarget rectA, SingleTarget rectB, CameraSpecification camera){
		SingleTarget left = rectA;
		SingleTarget right = rectB;
		if(rectA.getX() > rectB.getX()){
			left = right;
			right = rectA;
		}
		double width = camera.resolution.width;
		double averageX = (left.getX() + right.getX() + left.getWidth() + right.getWidth())/2D;
		this.offset = averageX - width/2;
		this.height = (left.getHeight() + right.getHeight())/2;
		this.width = right.getX() - left.getX() + right.getWidth()/2 + left.getWidth()/2;
	}
	
	/**
	 * Points use Cartesian coordinate system.
	 * 
	 * @param topLeft
	 * @param topRight
	 * @param bottomLeft
	 * @param bottomRight
	 */
	public VisionTarget(Point2D topLeft, Point2D topRight, Point2D bottomLeft, Point2D bottomRight) {
		double averageLeft = (topLeft.getX() + bottomLeft.getX()) / 2;
		double averageRight = (topRight.getX() + bottomRight.getX()) / 2;
		double averageTop = (topLeft.getY() + topRight.getY()) / 2;
		double averageBottom = (bottomLeft.getY() + bottomRight.getY()) / 2;
		
		this.offset = averageLeft + ((averageRight - averageLeft) / 2);
		this.width = averageRight - averageLeft;
		this.height = averageTop - averageBottom;
	}
	
	/**
	 * See: http://wpilib.screenstepslive.com/s/4485/m/24194/l/288985-identifying-and-processing-the-targets
	 * 
	 * @param camera
	 * @param physicalTargetSize the size of the physical target, expressed in inches
	 * @return the distance to the target in inches
	 */
	public double getDistance(CameraSpecification camera, Dimension physicalTargetSize) {
		double theta = Math.toRadians(this.height/camera.resolution.height * camera.angularFieldOfView.height);
		
		return physicalTargetSize.height / Math.tan(theta);
	}
	
	/**
	 * Returns the apparent rotation of the camera, in degrees.
	 * 
	 * @param camera
	 * @return the apparent rotation of the camera about the target, in degrees
	 */
	public double getRotation(CameraSpecification camera) {
		double rawAngle = Math.toRadians((this.offset)/camera.resolution.width*camera.angularFieldOfView.width);
		double correctedAngle = MathUtil.yawFromRotatedCircle(Math.toRadians(camera.angle), rawAngle);
		return Math.toDegrees(correctedAngle);
	}
	
	/**
	 * Returns the apparent rotation of the camera about the target, in radians and
	 * within a ranges of -π/2 to π/2. Values from -π/2 to 0 indicate the camera has
	 * rotated counterclockwise. Values from 0 to π/2 indicate the camera has
	 * rotated clockwise. A value of 0 indicates that the camera lies on the axis of the
	 * target.
	 * 
	 * TODO: delete the following
	 * Returns the apparent rotation of the camera about the target, in radians and
	 * within the range 0 to π. Values from 0 to π/2 indicate the camera has
	 * rotated to the right. Values from π/2 to π indicate the target has rotated
	 * to the left.
	 * 
	 * @param camera
	 * @param physicalTargetSize the dimensions of the physical vision target, in inches
	 * @return the external rotation of the camera, in radians
	 * @throws RuntimeException if width exceeds expected width
	 */
	public double getClockAngle(CameraSpecification camera, Dimension physicalTargetSize) {
		double expectedWidth = this.height * (physicalTargetSize.width / physicalTargetSize.height);
		double cosine = this.width / expectedWidth;
		
		if (Math.abs(cosine) > 1) {
			throw new RuntimeException("Target width exceeds expected width.");
		}
		
		double angle = Math.acos(cosine); // from 0 to pi
		
		if (angle < Math.PI / 2) {
			return -angle;
		}
		
		return Math.PI - angle;
	}
	
	/**
	 * Gets the clock angle based on the camera specification, the current robot angle, and the peg's angle
	 * @param cameraSpecification the camera specification for the camera
	 * @param robotAngle the angle the robot is at
	 * @param pegAngle the angle the peg is at
	 * @return the clock angle
	 */
	public double getClockAngle(CameraSpecification cameraSpecification, double robotAngle, double pegAngle) {
		double rotation = this.getRotation(cameraSpecification);
		double antiTargetAngle = pegAngle-180;
		
		return antiTargetAngle - robotAngle - rotation;
	}
	
	/**
	 * 
	 * @param target
	 * @param camera
	 * @param targetSize
	 * @return
	 */
	public Motion getMotion(VisionTarget target, CameraSpecification camera, Dimension targetSize) {
		double cameraDistance = target.getDistance(camera, targetSize);
		double cameraRotation = target.getRotation(camera);
		double cameraClockAngle = target.getClockAngle(camera, targetSize);
		double distanceThreshold = 34;
		
		double x = Math.abs(cameraClockAngle) > 0 && cameraDistance > distanceThreshold ?
				-Math.abs(cameraClockAngle)/cameraClockAngle * 0.2 : 0.0;
		double y = cameraDistance > distanceThreshold ? 0.2 : 0;
		double rotation = Math.abs(cameraRotation) > 0 ?
				-Math.abs(cameraRotation)/cameraRotation * 0.2 : 0.0;
				
		y = x == 0 ? y : 0;
		
		return new Motion(x, y, rotation);
	}
	
	/**
	 * @return center of this VisionTarget, in pixels
	 */
	public double getCenter() {
		return this.offset;
	}
	
	/**
	 * 
	 * @return width of this VisionTarget, in pixels
	 */
	public double getWidth() {
		return this.width;
	}
	
	/**
	 * @return height of this VisionTarget, in pixels
	 */
	public double getHeight() {
		return this.height;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (!VisionTarget.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		
		final VisionTarget other = (VisionTarget) obj;
		
		double offsetDiff = Math.abs(this.offset - other.offset);
		double widthDiff = Math.abs(this.width - other.width);
		double heightDiff = Math.abs(this.height - other.height);
		
		if (offsetDiff > 0.0001 || widthDiff > 0.0001 || heightDiff > 0.0001) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return (int) this.offset + (int) this.width + (int) this.height;
	}

	public String toString(){
		return "Vision Target { Height: " + this.height + ", Width: " + this.width + ", Offset: " + this.offset + "}";
	}

}