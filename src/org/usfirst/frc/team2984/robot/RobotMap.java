package org.usfirst.frc.team2984.robot;

import org.usfirst.frc.team2984.robot.util.CameraSpecification;
import org.usfirst.frc.team2984.robot.util.Dimension;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	public static final int FRONT_LEFT_MOTOR_ID = 14;
	public static final int FRONT_RIGHT_MOTOR_ID = 4;
	public static final int REAR_LEFT_MOTOR_ID = 16;
	public static final int REAR_RIGHT_MOTOR_ID = 1;
	public static final int GEAR_GRABBER_MOTOR_ID = 30;
	public static final int WINCH_MOTOR_ID = 20; 
	
	public static final Dimension CAMERA_RESOLUTION = new Dimension(320, 240);
	public static final Dimension CAMERA_FOV = new Dimension(56,41.625);
	public static final double CAMERA_ANGLE = 10;
	public static final CameraSpecification CAMERA_SPECIFICATION = new CameraSpecification(CAMERA_FOV, CAMERA_RESOLUTION, CAMERA_ANGLE);
	public static final double CAMERA_OFFSET = 2.5;
	public static final Dimension TARGET_DIMENSION = new Dimension(10.25, 5);

	public static int leftDistanceSensorPort = 3; // TODO: set value
	public static int rightDistanceSensorPort = 1; // TODO: set value
	
	public static final double DOCKING_DISTANCE_THRESHOLD = 30D; // in inches
	public static final double DOCKING_YAW_THRESHOLD = 3;
	public static final double DOCKING_ROBOT_ANGLE_THRESHOLD = 5;
	public static final double PEG_DROPOFF_DISTANCE = 10D;
	
	public static final double PEG_DROPOFF_OCCILATION_P = 0.1;
	public static final double PEG_DROPOFF_ROTATION_P = 0.004;
	public static final double ROBOT_DISTANCE_PROPORIONAL_SCALAR = 0.005;
	public static double ROBOT_YAW_PROPORIONAL_SCALAR = 0.021;
	public static final double DOCKING_MAX_SPEED = 0.5;
	public static final double ROBOT_PROPORTINAL_UNDULATING_FACTOR = 0.4;
	public static final double UNDULATING_AMPLITUDED = 1;
	public static final double GEAR_DROPOFF_SPEED = 0.3;
	public static final double ROBOT_SCALAR_FOR_OTHER_SCALARS_VIA_DISTANCE = 1/60D;
	public static final long GEAR_DROPOFF_MAX_TIME = 1000;

	public static final double DRIVE_TRAIN_MAX_SPEED = 1600.0;
	public static final double SENSOR_WIDTH = 18.75D;//in inches
	public static final double DRIVE_TRAIN_TICK_TO_INCH_FORWARD = 1215D; //Ticks Per Inch Forward
	public static final double DRIVE_TRAIN_TICK_TO_INCH_RIGHT = 1215D; //Ticks Per Inch Right
	public static final double DRIVE_TRAIN_TICK_TO_RADIAN = 1000D; //Ticks Per Radian
//	public static final double SENSOR_V_TO_IN_CALIBRATION_RIGHT = 8.21D;
//	public static final double SENSOR_START_DISTANCE_RIGHT = -9.83D;
	public static final double SENSOR_V_TO_IN_CALIBRATION_RIGHT = 1D;
	public static final double SENSOR_START_DISTANCE_RIGHT = 0D;
	public static final double SENSOR_V_TO_IN_CALIBRATION_LEFT =1D;
	public static final double SENSOR_START_DISTANCE_LEFT = 0D;
	
	public static final int VALUE_LOW = 41; //TODO: set to 41
	
	public static final double SPEED_F = 0.12;
	public static final double SPEED_P = 0.12;
	public static final double SPEED_I = 0.0;
	public static final double SPEED_D = 0.0;
	
	public static final double DISTANCE_F = 000032;
	public static final double DISTANCE_P = 0.000032;
	public static final double DISTANCE_I = 0.0;
	public static final double DISTANCE_D = 0.0;
	public static final float MAX_SPEED_DITANCE = 6; // 0-12
	
	public static final double ROTATION_P = 0.018;
	public static final double ROTATION_I = 0.0;
	public static final double ROTATION_D = 0.0;
	public static final double ROTATION_THRESHOLD = 0.5;
	
	public static final double GEAR_GRABBER_CLOSE = 0;
	public static final double GEAR_GRABBER_OPEN = -1000;
	public static final double GEAR_GRABBER_DELTA = -100;
	public static final double GEAR_GRABBER_P = 0.003;
	
	public static final double ROBOT_START_ANGLE = 0;
	public static final double ROBOT_ANGLE_PROPORIONAL_SCALAR = 0.03;
	public static final long OVER_CURRENT_WAIT_TIME = 1000;
	public static final long OVER_CURRENT_CUTOUT_TIME = 1000;
	public static final double OVER_CURRENT_CURRENT = 1000;
	
	public static double pegAngle = 180; //TODO FOR THE LOVE OF GOD CHANGE IT BACK TO 240

}