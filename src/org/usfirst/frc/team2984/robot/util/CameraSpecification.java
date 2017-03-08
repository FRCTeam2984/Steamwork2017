package org.usfirst.frc.team2984.robot.util;

public class CameraSpecification {
	public Dimension angularFieldOfView;
	public Dimension resolution;
	public double angle;
	
	public CameraSpecification(Dimension angularFieldOfView, Dimension resolution, double angle) {
		this.angularFieldOfView = angularFieldOfView;
		this.resolution = resolution;
		this.angle = angle;
	}
	
	public double getAngle(){
		return this.angle;
	}
	
	public Dimension getResolution() {
		return this.resolution;
	}
	
	public Dimension getAngularFieldOfView() {
		return this.angularFieldOfView;
	}
}
