package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.Robot;
import org.usfirst.frc.team2984.robot.util.VisionTracker;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AlignToThePeg extends Command {

	private VisionTracker tracker;
	
    public AlignToThePeg() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.mecanumDriveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.mecanumDriveTrain.move(0, 0, 0);
    	if(this.tracker == null){
    		SmartDashboard.putString("Wrong", "Wrong");
        	this.tracker = VisionTracker.getInstance();
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(this.tracker.hasTrack()){
    		double dist = this.tracker.getDistance();
    		double offsetAngle = this.tracker.getAngle();
    		double robotAngle = this.tracker.robotAngle();
    		double rotation = Math.min(Math.max(-robotAngle, -1), 1);
    		double forward = (dist > 10) ? 0.3 : 0;
    		double right = (offsetAngle > 0.2) ? 0.2 : ((offsetAngle < -0.2) ? -0.2 : 0);
    		Robot.mecanumDriveTrain.move(forward, right, rotation);
    	} else {
    		Robot.mecanumDriveTrain.move(0, 0, 0);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
