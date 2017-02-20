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

    protected void initialize() {
    	double[] pIDValues = joystick.getPIDValues();
    	if(pIDValues == null){
    		return;
    	}
    	DriveTrain.State state = driveTrain.getState();
    	if(state.equals(DriveTrain.State.DISTANCE_CONTROL)){
    		driveTrain.updatePID(pIDValues[4]/1000, pIDValues[5]/1000, pIDValues[6]/1000, pIDValues[7]/1000);
    	}
    	if(state.equals(DriveTrain.State.SPEED_CONTROL)){
    		driveTrain.updatePID(pIDValues[0]/1000, pIDValues[1]/1000, pIDValues[2]/1000, pIDValues[3]/1000);
    	}
    }

}
