package org.usfirst.frc.team2984.robot.subsystems;

import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.commands.KeepGearGrabberClosed;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

public class GearGrabber extends Subsystem {
	private static GearGrabber instance;
	
	private CANTalon talon;
	
	public static GearGrabber getInstance() {
		if (instance == null) {
			instance = new GearGrabber(RobotMap.gearGrabberController);
		}
		
		return instance;
	}
	
	public GearGrabber(CANTalon talon) {
		this.talon = talon;
	}
	
	public void close(){
		talon.set(-0.2);
	}
	
	public void open(){
		talon.set(0.2);
	}
	
	public void clench(){
		talon.set(-0.2);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new KeepGearGrabberClosed());
	}
}
