package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.subsystems.Gyroscope;
import org.usfirst.frc.team2984.robot.subsystems.WallFinder;
import org.usfirst.frc.team2984.robot.util.Motion;
import org.usfirst.frc.team2984.robot.util.Wall;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class PutGearOnPeg extends Command {

	private WallFinder wallFinder;
	private DriveTrain driveTrain;
	private Gyroscope gyro;
	private boolean done;
	private boolean reset;
	
    public PutGearOnPeg() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
//    	requires(Robot.driveTrain);
    	driveTrain = DriveTrain.getInstance();
    	gyro = Gyroscope.getInstance();
    	wallFinder = WallFinder.getInstance();
    	this.done = false;
    	
    	requires(driveTrain);
    	requires(wallFinder);

    }
    
    public PutGearOnPeg(WallFinder wallFinder, DriveTrain driveTrain, Gyroscope gyro){
    	this.wallFinder = wallFinder;
    	this.driveTrain = driveTrain;
    	this.gyro = gyro;
    	this.done = false;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    public void execute() {
    	if(this.reset){
    		this.reset = false;
    		this.driveTrain.resetOrigin();
    	}
    	this.done = true;
    	Wall wall = this.wallFinder.getWall();
    	if(wall == null){
    		driveTrain.move(new Motion(0, 0, 0));
    		return;
    	}
		double dist = wall.getDistance();
		double angle = wall.getAngle();
//		if(Math.abs(cameraAngle) > RobotMap.DOCKING_ROBOT_ANGLE_THRESHOLD){
//			rotation = Math.min(Math.max(-cameraAngle*RobotMap.ROBOT_ANGLE_PROPORIONAL_SCALAR, -1), 1);
//			this.done = false;
//		}
//		if(Math.abs(angle) > RobotMap.DOCKING_YAW_THRESHOLD){
//			angleToMove = RobotMap.pegAngle-90;
//			speed = Math.min(Math.abs(angle * RobotMap.ROBOT_ANGLE_PROPORIONAL_SCALAR), 1);
//			this.done = false;
//		} else if(dist > RobotMap.DOCKING_DISTANCE_THRESHOLD){
//			angleToMove = RobotMap.pegAngle-180;
//			speed = Math.min(Math.abs(dist * RobotMap.ROBOT_ANGLE_PROPORIONAL_SCALAR), 1);
//			this.done = false;
//		}
//		driveTrain.moveAtAngle(angleToMove, speed, rotation);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return this.done;
    }

    // Called once after isFinished returns true
    protected void end() {
    	this.done = false;
    	this.reset = true;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
