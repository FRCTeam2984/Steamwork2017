package org.usfirst.frc.team2984.robot.commands;

import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.util.RemoteJoystick;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class UpdatePIDsForDriveTrain extends InstantCommand {

	private DriveTrain driveTrain;
	private RemoteJoystick joystick;
	
    public UpdatePIDsForDriveTrain() {
        super();
        driveTrain = DriveTrain.getInstance();
    	joystick = RemoteJoystick.getInstance();
    	
    	requires(driveTrain);    
    }

    // Called once when the command executes
    protected void initialize() {
    	double[] pIDValues = joystick.getPIDValues();
    	if(pIDValues == null){
    		return;
    	}
    	DriveTrain.State state = driveTrain.getState();
    	if(state.equals(DriveTrain.State.DISTANCE_CONTROL)){
    		driveTrain.updatePID(pIDValues[4], pIDValues[5], pIDValues[6], pIDValues[7]);
    	}
    	if(state.equals(DriveTrain.State.SPEED_CONTROL)){
    		driveTrain.updatePID(pIDValues[0], pIDValues[1], pIDValues[2], pIDValues[3]);
    	}
    }

}
