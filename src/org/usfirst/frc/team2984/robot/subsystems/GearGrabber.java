package org.usfirst.frc.team2984.robot.subsystems;

import org.usfirst.frc.team2984.robot.commands.KeepGearGrabberClosed;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

public class GearGrabber extends Subsystem {
	private CANTalon talon;
	
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
