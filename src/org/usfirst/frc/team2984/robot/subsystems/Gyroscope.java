package org.usfirst.frc.team2984.robot.subsystems;

import org.usfirst.frc.team2984.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class Gyroscope extends Subsystem {

	private static Gyroscope instance;
	
	private Gyro gyro;
	private double deltaAngle;
	
	public static Gyroscope getInstance() {
		if (instance == null) {
			
			instance = new Gyroscope(new ADXRS450_Gyro());
		}
		return instance;
	}
	
	public Gyroscope(Gyro gyro){
		this.gyro = gyro;
		this.calibrate(RobotMap.ROBOT_START_ANGLE);
	}
	
	public void calibrate(double angle){
		this.gyro.calibrate();
		this.deltaAngle = angle - this.gyro.getAngle();
	}
	
	public double getRate(){
		return this.gyro.getRate();
	}
	
	public double getAngle(){
		return this.gyro.getAngle() + this.deltaAngle;
	}
	
	@Override
	protected void initDefaultCommand() {

	}

}
