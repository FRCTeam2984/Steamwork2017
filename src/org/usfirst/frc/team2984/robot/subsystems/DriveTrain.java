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
	
	private double speed = Settings.getInstance().getDouble("DriveTrainMaxSpeed");
	private double ticksPerInch = Settings.getInstance().getDouble("DriveTrainTickToInch");
	private CANTalon frontLeft;
	private CANTalon frontRight;
	private CANTalon backLeft;
	private CANTalon backRight;
	
	private State driveState;
	
	public static enum State {
		SPEED_CONTROL,
		DISTANCE_CONTROL,
		VOLTAGE_CONTROL
	}
	
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
		this.switchState(State.SPEED_CONTROL);
	}
	
	public void move(Motion motion) {
		this.switchState(State.SPEED_CONTROL);
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
	
	/**
	 * moves the given distance in x and y
	 * @param x
	 * @param y
	 */
	public void moveDistance(double x, double y){
		this.switchState(State.DISTANCE_CONTROL);
		double xTicks = x*this.ticksPerInch;
		double yTicks = y*this.ticksPerInch;
		double fl = xTicks + yTicks;
		double fr = -xTicks + yTicks;
		double bl = -xTicks + yTicks;
		double br = xTicks + yTicks;
		this.frontLeft.set(fl);
		this.frontRight.set(fr);
		this.backRight.set(br);
		this.backLeft.set(bl);
	}
	
	public void resetOrigin(){
		this.frontLeft.setEncPosition(0);
		this.frontRight.setEncPosition(0);
		this.backLeft.setEncPosition(0);
		this.backRight.setEncPosition(0);
	}
	
	public boolean isThere(double x, double y, double epsilon){
		int fl = this.frontLeft.getEncPosition();
		int fr = this.frontLeft.getEncPosition();
		int bl = this.frontLeft.getEncPosition();
		int br = this.frontLeft.getEncPosition();
		double xTicks = x*this.ticksPerInch;
		double yTicks = y*this.ticksPerInch;
		fl -= xTicks + yTicks;
		fr -= -xTicks + yTicks;
		bl -= -xTicks + yTicks;
		br -= xTicks + yTicks;
		fl = Math.abs(fl);
		fr = Math.abs(fr);
		bl = Math.abs(bl);
		br = Math.abs(br);
		int max = Math.max(Math.max(fl, fr), Math.max(bl, br));
		return max < epsilon;
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new RemoteJoystickDrive());
	}
	
	private double getMaximumValue(double first, double second, double third, double fourth) {
		return Math.max(Math.max(first, second), Math.max(third, fourth));
	}
	
	private void configureTalonsSpeed(){
		Settings settings = Settings.getInstance();
		double f = settings.getDouble("SpeedF");
		double p = settings.getDouble("SpeedP");
		double i = settings.getDouble("SpeedI");
		double d = settings.getDouble("SpeedD");
		updatePID(0.12, 0.12, i, d);
		this.frontLeft.changeControlMode(TalonControlMode.Speed);
		this.frontRight.changeControlMode(TalonControlMode.Speed);
		this.backLeft.changeControlMode(TalonControlMode.Speed);
		this.backRight.changeControlMode(TalonControlMode.Speed);

	}
	
	private void configureTalonsVoltage(){
		this.configureTalonVoltage(this.frontLeft);
		this.configureTalonVoltage(this.frontRight);
		this.configureTalonVoltage(this.backLeft);
		this.configureTalonVoltage(this.backRight);
	}
	
	private void configureTalonVoltage(CANTalon talon){
		//Limit the max current, this case to [+12, -12]
		talon.configNominalOutputVoltage(+0.0f, -0.0f);
        talon.configPeakOutputVoltage(+12.0f, -12.0f);
        talon.changeControlMode(TalonControlMode.PercentVbus);
	}
	
	private void configureTalonsDistance(){
		Settings settings = Settings.getInstance();
		double f = settings.getDouble("DistanceF");
		double p = settings.getDouble("DistanceP");
		double i = settings.getDouble("DistanceI");
		double d = settings.getDouble("DistanceD");
		updatePID(f, p, i, d);
		this.frontLeft.changeControlMode(TalonControlMode.Position);
		this.frontRight.changeControlMode(TalonControlMode.Position);
		this.backLeft.changeControlMode(TalonControlMode.Position);
		this.backRight.changeControlMode(TalonControlMode.Position);
	}
	
	private void setupEncoderAndPID(CANTalon talon, boolean reversed, double f, double p, double i, double d){
		//Setup Sensor
		talon.setFeedbackDevice(FeedbackDevice.QuadEncoder); //CRT Mag Encoder Relative if 1 turn
		talon.reverseSensor(reversed);
		talon.configEncoderCodesPerRev(1000); //number of revs per turn, 1000
		
		//Limit the max current, this case to [+12, -12]
		talon.configNominalOutputVoltage(+0.0f, -0.0f);
        talon.configPeakOutputVoltage(+12.0f, -12.0f);
		
        //Set up the PID values
        talon.setProfile(0);
        talon.setF(f); // 0.1597
        talon.setP(p); // 0.42
        talon.setI(i); 
        talon.setD(d);
	}
	
	public void switchState(State state){
		if(state == this.driveState){
			return;
		}
		if(state == null){
			state = this.driveState;
		}
		switch(state){
			case VOLTAGE_CONTROL:
				this.configureTalonsVoltage();
				break;
			case SPEED_CONTROL:
				this.configureTalonsSpeed();
				break;
			case DISTANCE_CONTROL:
				this.configureTalonsDistance();
		}
		this.driveState = state;
	}
	
	public void updatePID(double f, double p, double i, double d){
		this.setupEncoderAndPID(this.frontLeft, false, f, p, i, d);
		this.setupEncoderAndPID(this.frontRight, true, f, p, i, d);
		this.setupEncoderAndPID(this.backLeft, false, f, p, i, d);
		this.setupEncoderAndPID(this.backRight, true, f, p, i, d);
	}
	
	public State getState(){
		return this.driveState;
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