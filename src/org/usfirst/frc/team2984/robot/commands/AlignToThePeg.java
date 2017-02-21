package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.util.Motion;
import org.usfirst.frc.team2984.robot.util.VisionTracker;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AlignToThePeg extends Command {

	private VisionTracker tracker;
	private DriveTrain driveTrain;
	private boolean done;
	
    public AlignToThePeg() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
//    	requires(Robot.driveTrain);
    	driveTrain = DriveTrain.getInstance();
    	this.done = false;
    	
    	requires(driveTrain);
    }
    
    public AlignToThePeg(VisionTracker tracker, DriveTrain driveTrain){
    	this.tracker = tracker;
    	this.driveTrain = driveTrain;
    	this.done = false;
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
    public void execute() {
//		Robot.mecanumDriveTrain.move(0, 1, 0);
    	this.done = true;
    	if(this.tracker.hasTrack()){
    		double dist = this.tracker.getDistance();
    		double angle = this.tracker.getAngle();
    		angle = (!Double.isNaN(angle) && Math.abs(angle) > 0.45) ? -angle/2 : 0;
    		double robotAngle = this.tracker.robotAngle();
    		double forward = 0;
    		double right = 0;
    		double rotation = 0;
    		if(dist > RobotMap.DOCKING_DISTANCE_THRESHOLD){
    			forward = 0.5;
    			this.done = false;
    		}
    		if(Math.abs(angle) > RobotMap.DOCKING_ANGLE_THRESHOLD){
    			right += angle/Math.abs(angle)*0.5;
    			this.done = false;
    		}
    		if(Math.abs(robotAngle) > 0.1){
    			rotation = robotAngle;
    			this.done = false;
    		}
//    		SmartDashboard.putNumber("Distance", forward);
//    		SmartDashboard.putNumber("angle", angle);
//    		SmartDashboard.putNumber("robotAngle", robotAngle);

    		driveTrain.move(new Motion(right, forward, rotation));
    	} else {
//    		SmartDashboard.putNumber("Drive?", -1);

    		driveTrain.move(new Motion(0, 0, 0));
			this.done = false;

    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return this.done;
    }

    // Called once after isFinished returns true
    protected void end() {
    	this.done = false;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
