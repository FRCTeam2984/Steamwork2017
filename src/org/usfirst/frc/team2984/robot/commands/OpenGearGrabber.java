package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OpenGearGrabber extends Command {

	public OpenGearGrabber(){
		requires(Robot.gearGrabber);
	}
	
	
	
	@Override
	protected void execute() {
		Robot.gearGrabber.open();
	}



	@Override
	protected boolean isFinished() {
		return false;
	}

}
