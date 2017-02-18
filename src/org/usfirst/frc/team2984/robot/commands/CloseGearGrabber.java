package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.subsystems.GearGrabber;

import edu.wpi.first.wpilibj.command.Command;

public class CloseGearGrabber extends Command {
	private GearGrabber grabber;

	public CloseGearGrabber(){
		grabber = GearGrabber.getInstance();
		
		requires(grabber);
	}
	
	@Override
	protected void execute() {
		grabber.close();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}
}
