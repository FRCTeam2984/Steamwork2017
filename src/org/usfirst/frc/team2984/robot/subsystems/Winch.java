package org.usfirst.frc.team2984.robot.subsystems;

import org.usfirst.frc.team2984.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Winch extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void grab(){
    	RobotMap.winchMotor.set(0.2);
    }
    
    public void climb(){
    	RobotMap.winchMotor.set(1);
    }
    
    public void backDown(){
    	RobotMap.winchMotor.set(-1);
    }
}

