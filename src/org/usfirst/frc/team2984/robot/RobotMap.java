package org.usfirst.frc.team2984.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	public static int FRONT_LEFT_MOTOR_ID = 14;
	public static int FRONT_RIGHT_MOTOR_ID = 4;
	public static int REAR_LEFT_MOTOR_ID = 16;
	public static int REAR_RIGHT_MOTOR_ID = 1;
	public static int GEAR_GRABBER_MOTOR_ID = 30; // TODO: set real ID
	public static int WINCH_MOTOR_ID = 20; // TODO: set real ID
	public static int leftDistanceSensorPort;
	public static int rightDistanceSensorPort;
	public static double DOCKING_DISTANCE_THRESHOLD = 24D; // in inches
}
