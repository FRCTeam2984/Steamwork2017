package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.util.VisionTracker;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AlignToThePeg extends Command {

	private VisionTracker tracker;
	private DriveTrain driveTrain;
	
    public AlignToThePeg() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
//    	requires(Robot.driveTrain);
    	driveTrain = DriveTrain.getInstance();
    	
    	requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
//    	Robot.mecanumDriveTrain.move(0, 0, 0);
    	if(this.tracker == null){
    		SmartDashboard.putString("Wrong", "Wrong");
        	this.tracker = VisionTracker.getInstance();
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//		Robot.mecanumDriveTrain.move(0, 1, 0);

    	if(this.tracker.hasTrack()){
    		double dist = this.tracker.getDistance();
    		double leftRight = this.tracker.getLeftRightDistance();
//    		double angle = this.tracker.getAngle();
//    		angle = (!Double.isNaN(angle) && Math.abs(angle) > 0.45) ? -angle/2 : 0;
    		double forward = (dist > 40) ? 0.45 : 0;
    		double right =(Math.abs(leftRight) > 0.1) ? leftRight*2.5 : 0;
    		SmartDashboard.putNumber("Distance?", forward);
    		SmartDashboard.putNumber("right", right);

    		driveTrain.move(right, forward, 0);
    	} else {
    		SmartDashboard.putNumber("Drive?", -1);

    		driveTrain.move(0, 0, 0);
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
