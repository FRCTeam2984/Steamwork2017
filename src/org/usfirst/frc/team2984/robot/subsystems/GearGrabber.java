package org.usfirst.frc.team2984.robot.subsystems;

import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.commands.ClenchGearGrabber;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GearGrabber extends Subsystem {
	private static GearGrabber instance;
	
	private CANTalon talon;
	private boolean isOver;
	private boolean isDisabled;
	private long overTime;
	private long disableTime;
	
	public static GearGrabber getInstance() {
		if (instance == null) {
			CANTalon talon = new CANTalon(RobotMap.GEAR_GRABBER_MOTOR_ID);
			setupEncoderAndPID(talon, true, 0, 0.0002, 0, 0);
			talon.setEncPosition(0);
			instance = new GearGrabber(talon);
		}
		
		return instance;
	}
	
	public GearGrabber(CANTalon talon) {
		this.talon = talon;
	}
	
	public void close(){
		this.set(RobotMap.GEAR_GRABBER_CLOSE);
	}
	
	public void open(){
		this.set(RobotMap.GEAR_GRABBER_OPEN);

	}
	
	public void clench(){
		this.set(RobotMap.GEAR_GRABBER_CLOSE - RobotMap.GEAR_GRABBER_DELTA);

	}
	
	public boolean isOpen(double epsilon){
		double talonPos = this.talon.getEncPosition();
		double delta = Math.abs(talonPos-RobotMap.GEAR_GRABBER_OPEN);
		return delta < epsilon;
	}
	
	private void set(double desiredPos){
		if((System.currentTimeMillis() - this.disableTime) < RobotMap.OVER_CURRENT_WAIT_TIME){
			this.talon.set(0);
		}
		int currentPos = talon.getEncPosition();
		double delta = desiredPos - currentPos;
		talon.set(Math.min(Math.max(delta*RobotMap.GEAR_GRABBER_P, -0.5), 0.3));
		if(talon.getOutputCurrent() > RobotMap.OVER_CURRENT_CURRENT && !this.isOver){
			this.isOver = true;
			this.overTime = System.currentTimeMillis();
		} else if(this.isOver && (System.currentTimeMillis() - this.overTime) > RobotMap.OVER_CURRENT_CUTOUT_TIME){
			this.isOver = false;
			this.overTime = System.currentTimeMillis();
		}
	}
	
	private static void setupEncoderAndPID(CANTalon talon, boolean reversed, double f, double p, double i, double d){
		//Setup Sensor
		talon.setFeedbackDevice(FeedbackDevice.QuadEncoder); //CRT Mag Encoder Relative if 1 turn
		talon.reverseSensor(reversed);
		talon.configEncoderCodesPerRev(1000); //number of revs per turn, 1000
		
		//Limit the max current, this case to [+12, -12]
		talon.configNominalOutputVoltage(+0.0f, -0.0f);
        talon.configPeakOutputVoltage(+6.0f, -2.0f);
        talon.setCloseLoopRampRate(3);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new ClenchGearGrabber());
	}
}
