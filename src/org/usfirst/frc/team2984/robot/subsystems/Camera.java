package org.usfirst.frc.team2984.robot.subsystems;

import org.usfirst.frc.team2984.robot.util.Dimension;
import org.usfirst.frc.team2984.robot.util.VisionTarget;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Camera extends Subsystem {
	public Camera() {
		
	}
	
	public VisionTarget getVisionTarget() {
		// TODO: stop cheating!
		Dimension targetSize = new Dimension(5, 10.25);
		return new VisionTarget(0, 50 * targetSize.width, 50 * targetSize.height);
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}
}
