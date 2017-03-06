package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.util.Motion;
import org.usfirst.frc.team2984.robot.util.VisionTarget;
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
    	VisionTarget target = this.tracker.getTarget();
    	if(target == null){
    		this.done = true;
    		return;
    	}
    	if(this.tracker.hasTrack()){
    		double dist = target.getDistance(RobotMap.CAMERA_SPECIFICATION, RobotMap.TARGET_DIMENSION);
    		double cameraAngle = target.getRotation(RobotMap.CAMERA_SPECIFICATION);
    		double[] robotAndClockAngle = this.getAngleAndRobotAngle(dist, cameraAngle, 0);
    		double angle = robotAndClockAngle[0];
    		double robotAngle = robotAndClockAngle[1];
    		double forward = 0;
    		double right = 0;
    		double rotation = 0;
    		if(dist > RobotMap.DOCKING_DISTANCE_THRESHOLD){
    			forward = 0.3;
    			this.done = false;
    		}
    		if(Math.abs(angle) > RobotMap.DOCKING_YAW_THRESHOLD){
    			right -= angle*2;
    			this.done = false;
    		}
    		if(Math.abs(robotAngle) > RobotMap.DOCKING_ROBOT_ANGLE_THRESHOLD){
    			rotation = robotAngle/Math.abs(robotAngle)*0.3;
    			this.done = false;
    		}
    		driveTrain.move(new Motion(right, forward, rotation));
    	} else {
//    		SmartDashboard.putNumber("Drive?", -1);

    		driveTrain.move(new Motion(0, 0, 0));
			this.done = false;

    	}
    }
    
    private double[] getAngleAndRobotAngle(double cameraAngle, double distance, double gyroAngle){
    	return new double[]{0,0};
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
