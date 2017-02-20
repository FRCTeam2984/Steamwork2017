package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.subsystems.Winch;
import org.usfirst.frc.team2984.robot.util.Motion;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;

/**
 *
 */
public class Climb extends Command {

    private Winch winch;
    private DriveTrain driveTrain;
    private Accelerometer accel;

	public Climb() {
    	this.winch = Winch.getInstance();
    	this.driveTrain = DriveTrain.getInstance();
    	this.accel = new BuiltInAccelerometer();
    	
    	requires(this.winch);
    	requires(this.driveTrain);
    }

    protected void initialize() {
    }

    protected void execute() {
    	double angle = this.getAngle();
    	if(angle < 0.1){
    		this.driveTrain.move(new Motion(0, 0.3, 0));
    		this.winch.idle();
    	} else if(angle < 0.2){
    		this.winch.climb();
    		this.driveTrain.move(new Motion(0, 0, 0));
    	} else if(angle < 0.4){
    		this.winch.climb();
    		this.driveTrain.move(new Motion(0, -0.2, 0));
    	} else {
    		this.winch.climb();
    		this.driveTrain.move(new Motion(0, 0, 0));
    	}
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	this.winch.idle();
    }
    protected void interrupted() {
    }
    
    private double getAngle(){
    	return Math.PI/2 - Math.abs(Math.atan(this.accel.getX()/this.accel.getZ()));
    }
}
