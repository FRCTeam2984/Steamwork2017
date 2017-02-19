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
		DistanceSensor left = new DistanceSensor(RobotMap.leftDistanceSensorPort);
		DistanceSensor right = new DistanceSensor(RobotMap.rightDistanceSensorPort);
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
    
    public Wall getWall(){
    	double distance = this.leftSensor.getDistanceInInches();
    	distance += this.rightSensor.getDistanceInInches();
    	distance /= 2;
    	double sensorDelta = this.leftSensor.getDistanceInInches() - this.rightSensor.getDistanceInInches();
    	double angle = Math.atan(sensorDelta/this.sensorWidth);
    	return new Wall(distance,angle);
    }
}

