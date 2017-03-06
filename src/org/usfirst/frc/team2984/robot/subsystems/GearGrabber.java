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
		talon.set(RobotMap.GEAR_GRABBER_CLOSE);

	}
	
	public void open(){
		talon.set(RobotMap.GEAR_GRABBER_OPEN);
	}
	
	public void clench(){
		talon.set(RobotMap.GEAR_GRABBER_CLOSE - RobotMap.GEAR_GRABBER_DELTA);

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
		
        //Set up the PID values
        talon.setProfile(0);
        talon.setF(f); // 0.1597
        talon.setP(p); // 0.42
        talon.setI(i); 
        talon.setD(d);
        talon.changeControlMode(TalonControlMode.Position);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new ClenchGearGrabber());
	}
}
