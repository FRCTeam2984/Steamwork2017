package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.subsystems.Winch;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class IdleWinch extends Command {

	private Winch winch;
	
    public IdleWinch() {
    	this.winch = Winch.getInstance();
    }

    protected void execute() {
    	this.winch.idle();
    }

    protected boolean isFinished() {
        return false;
    }
}
