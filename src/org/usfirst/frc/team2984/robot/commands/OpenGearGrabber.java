package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.subsystems.GearGrabber;

import edu.wpi.first.wpilibj.command.Command;

public class OpenGearGrabber extends Command {
	private GearGrabber grabber = GearGrabber.getInstance();
	private long time;
	private boolean reset = true;
	public OpenGearGrabber(){
		requires(grabber);
	}
	
	@Override
	protected void execute() {
		if(this.reset){
			time = System.currentTimeMillis();
			this.reset = false;
		}
		grabber.open();
	}
	
	protected void end(){
		this.reset = true;
	}
	
	@Override
	protected boolean isFinished() {
		return !reset && System.currentTimeMillis() - time > 1 && this.grabber.isOpen(40);
	}
}
