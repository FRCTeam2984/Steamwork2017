package org.usfirst.frc.team2984.robot;

import org.usfirst.frc.team2984.robot.util.Dimension;

public class Camera {
	public Dimension angleOfView;
	public Dimension resolution;
	
	/**
	 * 
	 * @param resolution
	 * @param fieldOfView
	 */
	public Camera(Dimension resolution, Dimension angleOfView) {
		this.resolution = resolution;
		this.angleOfView = angleOfView;
	}
}