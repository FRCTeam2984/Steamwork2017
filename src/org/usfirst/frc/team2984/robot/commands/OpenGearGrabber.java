package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.subsystems.GearGrabber;

import edu.wpi.first.wpilibj.command.Command;

public class OpenGearGrabber extends Command {
	private GearGrabber grabber = GearGrabber.getInstance();
	
	public OpenGearGrabber(){
		requires(grabber);
	}
	
	@Override
	protected void execute() {
		grabber.open();
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
}
