package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class CloseGearGrabber extends Command {

	public CloseGearGrabber(){
		requires(Robot.gearGrabber);
	}
	
	
	
	@Override
	protected void execute() {
		Robot.gearGrabber.close();
	}



	@Override
	protected boolean isFinished() {
		return false;
	}

}
