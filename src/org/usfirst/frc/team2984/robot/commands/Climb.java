package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.subsystems.Winch;
import org.usfirst.frc.team2984.robot.util.Motion;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Climb extends Command {

	double xaverage;
	double zAverage;
	double yAverage;
	
    private Winch winch;
    private DriveTrain driveTrain;
    private Accelerometer accel;

	public Climb() {
    	this.winch = Winch.getInstance();
    	requires(this.winch);
    }

    protected void initialize() {
    }

    protected void execute() {
    	this.winch.climb();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	this.winch.idle();
    }
    protected void interrupted() {
    }
    
}
