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
public class Rotate extends Command {

	private double angle;
	private DriveTrain driveTrain;
	private Gyroscope gyro;
	private long time;
	private long maxTime;
	private boolean reset;
	
	/**
	 * rotates to the given angle offset
	 * @param angle the angle
	 */
    public Rotate(double angle, long maxTime) {
    	super("Rotate");
    	this.angle = angle;
    	this.driveTrain = DriveTrain.getInstance();
    	this.gyro = Gyroscope.getInstance();
    	this.reset = false;
    	this.maxTime = maxTime;
        requires(this.driveTrain);
    }

    @Override
    protected void execute(){
    	if(this.reset){
    		this.time = System.currentTimeMillis();
    		this.reset = false;
    	}
    	double angle = MathUtil.shortestDeltaAngle(this.gyro.getAngle(), this.angle);
    	double power = RobotMap.ROTATION_P * angle;
    	this.driveTrain.move(new Motion(0, 0, power));
    }
    
    protected boolean isFinished() {
        return Math.abs(MathUtil.shortestDeltaAngle(this.gyro.getAngle(), this.angle)) < RobotMap.ROTATION_THRESHOLD || (System.currentTimeMillis() - time) > this.maxTime;
    }
    
    protected void end(){
    	super.end();
    	this.reset = true;
    }

}
