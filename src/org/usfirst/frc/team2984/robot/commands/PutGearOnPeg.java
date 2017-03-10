package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.subsystems.Gyroscope;
import org.usfirst.frc.team2984.robot.util.MathUtil;
import org.usfirst.frc.team2984.robot.util.Motion;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class PutGearOnPeg extends Command {

	private Gyroscope gyro;
	private DriveTrain driveTrain;
	private boolean done;
	private boolean reset;
	private boolean isMoving;
	private long startTime;
	
    public PutGearOnPeg() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	driveTrain = DriveTrain.getInstance();
    	gyro = Gyroscope.getInstance();
    	this.done = false;
    	this.isMoving = false;
    	
    	requires(driveTrain);

    }
    
    public PutGearOnPeg(Gyroscope gyro, DriveTrain driveTrain){
    	this.gyro = gyro;
    	this.driveTrain = driveTrain;
    	this.done = false;
    	this.isMoving = false;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    public void execute() {
    	if(this.reset){
    		this.reset = false;
    		this.done = false;
    		this.driveTrain.resetOrigin();
    		this.startTime = System.currentTimeMillis();
    	}
		double angle = MathUtil.shortestDeltaAngle(this.gyro.getAngle(), RobotMap.pegAngle-180);
		if(this.isMoving && this.driveTrain.isThereAtAll(100)){
			this.driveTrain.move(new Motion(0,0,0));
			this.done = true;
			return;
		} else if(!this.isMoving){
			this.isMoving = !this.driveTrain.isThereAtAll(100);
		}
//		double deltaY = this.driveTrain.getDisplacementY();
//		double deltaX = this.driveTrain.getDisplacementX();
//		double wantedX = (RobotMap.ROBOT_PROPORTINAL_UNDULATING_FACTOR * deltaY)% (2*RobotMap.UNDULATING_AMPLITUDED) - RobotMap.UNDULATING_AMPLITUDED;
//		double x = Math.max(Math.min((wantedX - deltaX) * RobotMap.PEG_DROPOFF_OCCILATION_P, 0.3), -0.3);
		double angleOff = angle*RobotMap.PEG_DROPOFF_ROTATION_P;
		angleOff = Math.max(Math.min(angleOff, RobotMap.DOCKING_MAX_SPEED), -RobotMap.DOCKING_MAX_SPEED);
		this.driveTrain.move(new Motion(0, RobotMap.GEAR_DROPOFF_SPEED, angleOff));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return this.done || (System.currentTimeMillis() - this.startTime > RobotMap.GEAR_DROPOFF_MAX_TIME);
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
