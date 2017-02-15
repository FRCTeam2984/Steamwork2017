package org.usfirst.frc.team2984.robot.subsystems;

import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.commands.KeepGearGrabberClosed;

import edu.wpi.first.wpilibj.command.Subsystem;

public class GearGrabber extends Subsystem {

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new KeepGearGrabberClosed());
	}
	
	public void close(){
		RobotMap.gearGrabberController.set(-0.2);
	}
	
	public void open(){
		RobotMap.gearGrabberController.set(0.2);
	}
	
	public void keepClosed(){
		RobotMap.gearGrabberController.set(-0.2);
	}

}
