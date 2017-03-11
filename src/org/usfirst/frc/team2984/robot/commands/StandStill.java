package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.util.Motion;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class StandStill extends Command {

	private DriveTrain driveTrain;
	private long time;
	private long maxTime;
	private boolean reset;
	
	/**
	 * rotates to the given angle offset
	 * @param angle the angle in degrees 
	 */
    public StandStill(long maxTime) {
    	super("StandSTill");
    	this.driveTrain = DriveTrain.getInstance();
    	this.maxTime = maxTime;
    	this.reset = true;
        requires(this.driveTrain);
    }

    @Override
    protected void execute(){
    	if(this.reset){
    		this.time = System.currentTimeMillis();
    		this.reset = false;
    	}
    	this.driveTrain.move(new Motion(0, 0, 0));
    }
    
    protected boolean isFinished() {
        return (System.currentTimeMillis() - time) > this.maxTime;
    }
    
    protected void end(){
    	this.reset = true;
    }

}
