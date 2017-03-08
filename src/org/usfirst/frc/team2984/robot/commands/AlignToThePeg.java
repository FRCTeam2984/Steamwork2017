package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.subsystems.Gyroscope;
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
	private Gyroscope gyro;
	private boolean done;
	
    public AlignToThePeg() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
//    	requires(Robot.driveTrain);
    	driveTrain = DriveTrain.getInstance();
    	gyro = Gyroscope.getInstance();
    	this.done = false;
    	
    	requires(driveTrain);
    }
    
    public AlignToThePeg(VisionTracker tracker, DriveTrain driveTrain, Gyroscope gyro){
    	this.tracker = tracker;
    	this.driveTrain = driveTrain;
    	this.gyro = gyro;
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
    
    private double getYaw(VisionTarget target, double angleOffset) {
    	double robotAngle = this.gyro.getAngle();
    	double clockAngle = target.getClockAngle(RobotMap.CAMERA_SPECIFICATION, robotAngle, RobotMap.pegAngle);
    	
    	return clockAngle - angleOffset;
    }
    
    private void track(VisionTarget target) {
		double distance = target.getDistance(RobotMap.CAMERA_SPECIFICATION, RobotMap.TARGET_DIMENSION);
		double angleOffset = Math.toDegrees(Math.asin(RobotMap.CAMERA_OFFSET/distance));
		double targetRotation = target.getRotation(RobotMap.CAMERA_SPECIFICATION);
		double cameraAngle =  targetRotation + angleOffset;
		double heading = 0;
		double speed = 0;
		double rotation = 0;
		if(Math.abs(cameraAngle) > RobotMap.DOCKING_ROBOT_ANGLE_THRESHOLD){
			rotation = Math.min(Math.max(cameraAngle*RobotMap.ROBOT_ANGLE_PROPORIONAL_SCALAR, -1), 1);
			this.done = false;
		}
		
		double yaw = this.getYaw(target, angleOffset);
		
		if(Math.abs(yaw) > RobotMap.DOCKING_YAW_THRESHOLD){
			heading = RobotMap.pegAngle-90;
			speed = -Math.min(Math.max(yaw * RobotMap.ROBOT_ANGLE_PROPORIONAL_SCALAR, -1), 1);
			this.done = false;
		} else if(distance > RobotMap.DOCKING_DISTANCE_THRESHOLD){
			heading = RobotMap.pegAngle-180;
			speed = Math.min(Math.abs(distance * RobotMap.ROBOT_ANGLE_PROPORIONAL_SCALAR), 1);
			this.done = false;
		}
		driveTrain.moveAtAngle(heading, speed, rotation);
    }

    // Called repeatedly when this Command is scheduled to run
    public void execute() {
    	this.done = true;
    	VisionTarget target = this.tracker.getTarget();
    	
    	if(target == null){
    		driveTrain.move(new Motion(0, 0, 0));
    		return;
    	}
    	
    	if(this.tracker.hasTrack()){
    		track(target);
    	} else {
    		driveTrain.move(new Motion(0, 0, 0));
			this.done = false;
    	}
    }
    
    public boolean isDone() {
    	return this.done;
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
