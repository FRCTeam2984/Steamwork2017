package org.usfirst.frc.team2984.robot.subsystems;

import org.usfirst.frc.team2984.dock.VisionTarget;
import org.usfirst.frc.team2984.robot.Camera;
import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.commands.RemoteJoystickDrive;
import org.usfirst.frc.team2984.robot.util.Dimension;
import org.usfirst.frc.team2984.robot.util.Settings;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

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
	
	/**
	 * Values are interpreted in terms of maximum motor drive rate.
	 * 
	 * @param x
	 * @param y
	 * @param rotation
	 * @see <a href="https://www.researchgate.net/publication/268326364_A_Design_Of_Omni-Directional_For_Mobile_Robot">A Design Of Omni-Directional For Mobile Robot</a>
	 */
	public void move(double x, double y, double rotation){
		double fl = x + y + rotation;
		double fr = -x + y - rotation;
		double bl = -x + y + rotation;
		double br = x + y - rotation;
		double max = Math.max(Math.max(fl, bl), Math.max(fr, br));
		
		if (max > 1){
			fl *= this.speed / max;
			fr *= this.speed / max;
			bl *= this.speed / max;
			br *= this.speed / max;	
		}
		
		this.frontLeft.set(fl);
		this.frontRight.set(fr);
		this.backLeft.set(bl);
		this.backRight.set(br);
	}
	
	public void dock(VisionTarget target, Camera camera, Dimension targetSize) {
		// TODO: stop cheating!
		this.frontRight.set(1);
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new RemoteJoystickDrive());
	}
}
