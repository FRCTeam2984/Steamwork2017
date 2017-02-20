package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Rotate extends Command {

	private double angle;
	private DriveTrain driveTrain;
	
	private boolean reset;
	
	/**
	 * rotates to the given angle offset
	 * @param angle the angle
	 */
    public Rotate(double angle) {
    	this.angle = angle;
    	this.driveTrain = DriveTrain.getInstance();
    	this.reset = true;
        requires(this.driveTrain);
    }

    protected void execute() {
    	if(this.reset){
        	this.driveTrain.resetOrigin();
        	this.reset = false;
    	}
    	this.driveTrain.rotate(this.angle);
    }

    protected boolean isFinished() {
        return this.driveTrain.isThere(100);
    }

    protected void end() {
    	this.reset = true;
    }

}
