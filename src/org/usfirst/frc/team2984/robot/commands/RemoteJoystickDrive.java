package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.Robot;
import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.subsystems.MecanumDriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RemoteJoystickDrive extends Command {

	private MecanumDriveTrain driveTrain;
	
    public RemoteJoystickDrive() {
    	requires(Robot.mecanumDriveTrain);
    	this.driveTrain = Robot.mecanumDriveTrain;
    }

    protected void initialize() {
//    	this.driveTrain.move(0, 0, 0);
    }

    protected void execute() {
    	this.driveTrain.move(RobotMap.remoteJoystick.getX(),
    			RobotMap.remoteJoystick.getY(), RobotMap.remoteJoystick.getTwist());
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
