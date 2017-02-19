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
        requires(this.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(this.reset){
        	this.driveTrain.resetOrigin();
        	this.reset = false;
    	}
    	this.driveTrain.moveDistance(this.distanceRight, this.distanceForward);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return this.driveTrain.isThere(distanceRight, distanceForward, 100);
    }

    // Called once after isFinished returns true
    protected void end() {
    	this.reset = true;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
