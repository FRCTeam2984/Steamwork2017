package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.RobotMap;

import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IncrementDecrement extends InstantCommand {
	private double delta;
	
    public IncrementDecrement(double delta) {
        super();
    	this.delta = delta;
    }

    protected void initialize() {
    	RobotMap.ROBOT_YAW_PROPORIONAL_SCALAR += delta;
    	SmartDashboard.putNumber("YAWPRO", RobotMap.ROBOT_YAW_PROPORIONAL_SCALAR);
    }
}
