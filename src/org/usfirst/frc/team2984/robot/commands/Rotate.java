package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.subsystems.Gyroscope;
import org.usfirst.frc.team2984.robot.util.MathUtil;
import org.usfirst.frc.team2984.robot.util.Motion;

import edu.wpi.first.wpilibj.command.PIDCommand;

/**
 *
 */
public class Rotate extends PIDCommand {

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
    	super(RobotMap.ROTATION_P, RobotMap.ROTATION_I, RobotMap.ROTATION_D);
    	this.angle = angle;
    	this.driveTrain = DriveTrain.getInstance();
    	this.gyro = Gyroscope.getInstance();
    	this.reset = false;
    	this.maxTime = maxTime;
        requires(this.driveTrain);
    }

    @Override
    protected void execute(){
    	super.execute();
    	if(this.reset){
    		this.time = System.currentTimeMillis();
    		this.reset = false;
    	}
    }
    
    protected boolean isFinished() {
        return Math.abs(MathUtil.shortestDeltaAngle(this.gyro.getAngle(), this.angle)) < RobotMap.ROTATION_THRESHOLD || (System.currentTimeMillis() - time) > this.maxTime;
    }
    
    protected void end(){
    	super.end();
    	this.reset = true;
    }

	@Override
	protected double returnPIDInput() {
		return MathUtil.shortestDeltaAngle(this.gyro.getAngle(), this.angle);
	}

	@Override
	protected void usePIDOutput(double output) {
		this.driveTrain.move(new Motion(0, 0, output));
	}

}
