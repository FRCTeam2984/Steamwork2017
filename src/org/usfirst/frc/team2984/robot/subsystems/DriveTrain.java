package org.usfirst.frc.team2984.robot.subsystems;

import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.commands.RemoteJoystickDrive;
import org.usfirst.frc.team2984.robot.util.Motion;
import org.usfirst.frc.team2984.robot.util.Settings;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Wheel orientation:
 * 
 *  grabber
 *  
 * \\\   ///
 * 
 * 
 * ///   \\\
 *
 *   winch
 */
public class DriveTrain extends Subsystem {
	private static DriveTrain instance;
	
	private double speed = Settings.getInstance().getDouble("DriveMotorRate");
	private CANTalon frontLeft;
	private CANTalon frontRight;
	private CANTalon backLeft;
	private CANTalon backRight;
	
	public static DriveTrain getInstance() {
		if (instance == null) {
			CANTalon frontLeft = new CANTalon(RobotMap.FRONT_LEFT_MOTOR_ID);
			CANTalon frontRight = new CANTalon(RobotMap.FRONT_RIGHT_MOTOR_ID);
			CANTalon rearLeft = new CANTalon(RobotMap.REAR_LEFT_MOTOR_ID);
			CANTalon rearRight = new CANTalon(RobotMap.REAR_RIGHT_MOTOR_ID);
			
			configureTalon(frontLeft);
			configureTalon(frontRight);
			configureTalon(rearLeft);
			configureTalon(rearRight);
			
			instance = new DriveTrain(frontLeft, frontRight, rearLeft, rearRight);
		}
		
		return instance;
	}
	
	/**
	 * @param frontLeft
	 * @param frontRight
	 * @param backLeft
	 * @param backRight
	 */
	public DriveTrain(CANTalon frontLeft, CANTalon frontRight, CANTalon backLeft, CANTalon backRight) {
		super("drive-train");
		
		this.frontLeft = frontLeft;
		this.frontRight = frontRight;
		this.backLeft = backLeft;
		this.backRight = backRight;
	}
	
	public void move(Motion motion) {
		double fl = motion.getX() + motion.getY() + motion.getRotation();
		double fr = -motion.getX() + motion.getY() - motion.getRotation();
		double bl = -motion.getX() + motion.getY() + motion.getRotation();
		double br = motion.getX() + motion.getY() - motion.getRotation();
		double max = getMaximumValue(fl, fr, bl, br);
		
		if (max > 1) {
			fl = fl / max;
			fr = fr / max;
			bl = bl / max;
			br = br / max;
		}
		
		this.frontLeft.set(fl * this.speed);
		this.frontRight.set(fr * this.speed);
		this.backRight.set(br * this.speed);
		this.backLeft.set(bl * this.speed);
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new RemoteJoystickDrive());
	}
	
	private double getMaximumValue(double first, double second, double third, double fourth) {
		return Math.max(Math.max(first, second), Math.max(third, fourth));
	}
	
	/**
	 * sets up the Talon to be used during driving
	 * @param talon the Talon to configure
	 */
	private static void configureTalon(CANTalon talon){
		//Setup Sensor
		talon.setFeedbackDevice(FeedbackDevice.QuadEncoder); //CRT Mag Encoder Relative if 1 turn
		talon.reverseSensor(false);
		talon.configEncoderCodesPerRev(1000); //number of revs per turn, 1000
		
		//Limit the max current, this case to [+12, -12]
		talon.configNominalOutputVoltage(+0.0f, -0.0f);
        talon.configPeakOutputVoltage(+12.0f, -12.0f);
		
        //Set up the PID values
        talon.setProfile(0);
        talon.setF(0.1597); // 0.1597
        talon.setP(0.42); // 0.42
        talon.setI(0); 
        talon.setD(0);
        talon.changeControlMode(TalonControlMode.Speed);
	}
	
	// from Google Drive
//	private static void configureTalon(CANTalon talon, boolean reverse){
//		//Setup Sensor
//		talon.setFeedbackDevice(FeedbackDevice.QuadEncoder); //CRT Mag Encoder Relative if 1 turn
//		talon.reverseSensor(reverse);
//		talon.configEncoderCodesPerRev(1000); //number of revs per turn, 1000
//		
//		//Limit the max current, this case to [+12, -12]
//		talon.configNominalOutputVoltage(+0.0f, -0.0f);
//        talon.configPeakOutputVoltage(+12.0f, -12.0f);
//		
//        //Set up the PID values
//        talon.setProfile(0);
//        talon.setF(0.1097); // 0.1597
//        talon.setP(0.12); // 0.42
//        talon.setI(0); 
//        talon.setD(0);
//        talon.changeControlMode(TalonControlMode.Speed);
//	}
}