package org.usfirst.frc.team2984.robot;

import org.usfirst.frc.team2984.robot.util.RemoteJoystick;

import com.ctre.CANTalon;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
	
	public static final RemoteJoystick remoteJoystick = new RemoteJoystick("CustomData1", false);
	
	public static  CANTalon frontLeftMotor;
	public static  CANTalon frontRightMotor;
	public static  CANTalon backLeftMotor;
	public static  CANTalon backRightMotor;
	
	public static int leftDistanceSensorPort;
	public static int rightDistanceSensorPort;
	
	public static CANTalon gearGrabberController;
	
	public static CANTalon winchMotor;
	
	public static void init(){
		frontLeftMotor = new CANTalon(14);
		frontRightMotor = new CANTalon(4);
		backLeftMotor = new CANTalon(16);
		backRightMotor = new CANTalon(1); 
		
		gearGrabberController = new CANTalon(0/* TODO: SET ID */);
		winchMotor = new CANTalon(2/* TODO: SET ID */);
	}
}
