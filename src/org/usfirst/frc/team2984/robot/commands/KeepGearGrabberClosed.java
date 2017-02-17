package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class KeepGearGrabberClosed extends Command {

	public KeepGearGrabberClosed(){
		requires(Robot.gearGrabber);
	}
	
	
	
	@Override
	protected void execute() {
		SmartDashboard.putBoolean("Maybe?", true);
		Robot.gearGrabber.clench();
	}



	@Override
	protected boolean isFinished() {
		return false;
	}

}
