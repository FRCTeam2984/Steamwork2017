package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveDistance extends Command {

	private double distanceForward;
	private double distanceRight;
	private DriveTrain driveTrain;
	private boolean notMoving;
	
	private boolean reset;
	
	/**
	 * drives the given distance in inches
	 * @param distance
	 */
    public DriveDistance(double distanceRight, double distanceForward) {
    	this.distanceForward = distanceForward;
    	this.distanceRight = distanceRight;
    	this.driveTrain = DriveTrain.getInstance();
    	this.reset = true;
    	this.notMoving = true;
        requires(this.driveTrain);
    }

    protected void initialize() {

    }

    protected void execute() {
    	if(this.reset){
        	this.driveTrain.resetOrigin();
        	this.reset = false;
        	this.notMoving = true;
    	}
    	if(this.notMoving){
    		this.notMoving = !this.driveTrain.isThere(10);
    	}
    	this.driveTrain.moveDistance(this.distanceRight, this.distanceForward);
    }

    protected boolean isFinished() {
        return this.driveTrain.isThere(100) && !this.notMoving;
    }

    protected void end() {
    	this.reset = true;
    }

}
