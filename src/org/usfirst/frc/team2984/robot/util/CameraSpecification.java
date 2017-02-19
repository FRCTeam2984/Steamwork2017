package org.usfirst.frc.team2984.robot.util;

public class CameraSpecification {
	public Dimension angularFieldOfView;
	public Dimension resolution;
	
	public CameraSpecification(Dimension angularFieldOfView, Dimension resolution) {
		this.angularFieldOfView = angularFieldOfView;
		this.resolution = resolution;
	}
	
	public Dimension getResolution() {
		return this.resolution;
	}
	
	public Dimension getAngularFieldOfView() {
		return this.angularFieldOfView;
	}
}
