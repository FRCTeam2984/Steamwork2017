package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.subsystems.Winch;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SlowClimb extends Command {

    private Winch winch;

	public SlowClimb() {
    	this.winch = Winch.getInstance();
    	requires(this.winch);
    }

    protected void initialize() {
    }

    protected void execute() {
    	this.winch.hold(0.1);
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
