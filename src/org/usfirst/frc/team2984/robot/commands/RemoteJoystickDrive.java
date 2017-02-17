package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.util.RemoteJoystick;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RemoteJoystickDrive extends Command {
	private DriveTrain driveTrain;
	private RemoteJoystick joystick;
	
    public RemoteJoystickDrive() {
    	driveTrain = DriveTrain.getInstance();
    	joystick = RemoteJoystick.getInstance();
    	
    	requires(driveTrain);
    }

    protected void initialize() {
    	
    }

    protected void execute() {
    	driveTrain.move(joystick.getX(),
    			joystick.getY(), joystick.getTwist());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
