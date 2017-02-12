package org.usfirst.frc.team2984.robot.subsystems;

import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.commands.RemoteJoystickDrive;
import org.usfirst.frc.team2984.robot.util.Settings;

import edu.wpi.first.wpilibj.command.Subsystem;

public class MecanumDriveTrain extends Subsystem {

	
	private double maxRate = 12;
	
	/**
	 * creates a new mecanum drive system with the following Talon IDs
	 * @param frontLeft frontLeft Talon ID
	 * @param frontRight frontRight Talon ID
	 * @param backLeft backLeft Talon ID
	 * @param backRight backRight Talon ID
	 */
	public MecanumDriveTrain(){
//		this.setupTalon(this.frontLeft);
//		this.setupTalon(this.frontRight);
//		this.setupTalon(this.backLeft);
//		this.setupTalon(this.backRight);
		maxRate = Settings.getInstance().getInt("DriveMotorRate");
	}
	
	/**
	 * sets up the Talon to be used during driving
	 * @param talon the Talon to configure
	 */
//	private void setupTalon(CANTalon talon){
//		//Setup Sensor
//		talon.setFeedbackDevice(FeedbackDevice.QuadEncoder); //CRT Mag Encoder Relative if 1 turn
//		talon.reverseSensor(false);
//		talon.configEncoderCodesPerRev(256); //number of revs per turn
//		
//		//Limit the max current, this case to [+12, -12]
//		talon.configNominalOutputVoltage(+0.0f, -0.0f);
//        talon.configPeakOutputVoltage(+12.0f, -12.0f);
//		
//        //Set up the PID values
//        talon.setProfile(0);
//        talon.setF(0.1097);
//        talon.setP(0.22);
//        talon.setI(0); 
//        talon.setD(0);
//        talon.changeControlMode(TalonControlMode.Speed);
//	}
	
	public void move(double x, double y, double rotation){
		double fl = x + y + rotation;
		double fr = -x + y + rotation;
		double bl = -x + y - rotation;
		double br = x + y - rotation;

		double max = Math.max(Math.max(fl, bl), Math.max(fr, br));
		if(max > 1){
			fl *= this.maxRate / max;
			fr *= this.maxRate / max;
			bl *= this.maxRate / max;
			br *= this.maxRate / max;	
		}
		RobotMap.frontLeftMotor.set(fl);
		RobotMap.frontRightMotor.set(fr);
		RobotMap.backLeftMotor.set(bl);
		RobotMap.backRightMotor.set(br);
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new RemoteJoystickDrive());
	}

}
