package org.usfirst.frc.team2984.robot.subsystems;

import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.util.DistanceSensor;
import org.usfirst.frc.team2984.robot.util.Wall;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class WallFinder extends Subsystem {

	private static WallFinder wallFinder;
	
	private DistanceSensor leftSensor;
	private DistanceSensor rightSensor;
	private double sensorWidth;
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	public static WallFinder getInstance(){
		if(wallFinder != null){
			return wallFinder;
		}
		DistanceSensor left = new DistanceSensor(RobotMap.leftDistanceSensorPort, RobotMap.SENSOR_V_TO_IN_CALIBRATION_RIGHT, RobotMap.SENSOR_START_DISTANCE_RIGHT);
		DistanceSensor right = new DistanceSensor(RobotMap.rightDistanceSensorPort, RobotMap.SENSOR_V_TO_IN_CALIBRATION_LEFT, RobotMap.SENSOR_START_DISTANCE_LEFT);
		wallFinder = new WallFinder(left, right, RobotMap.SENSOR_WIDTH);
		return wallFinder;
	}
	
	public WallFinder(DistanceSensor leftSensor, DistanceSensor rightSensor, double sensorWidth){
		this.leftSensor = leftSensor;
		this.rightSensor = rightSensor;
		this.sensorWidth = sensorWidth;
	}
	
    public void initDefaultCommand() {
    }
    
    public DistanceSensor getLeft(){
    	return this.leftSensor;
    }
    
    public DistanceSensor getRight(){
    	return this.rightSensor;
    }
    
    public Wall getWall(){
    	double left = this.leftSensor.getDistanceInInches();
    	double right = this.rightSensor.getDistanceInInches();
    	return new Wall(left, right, this.sensorWidth);
    }
}

