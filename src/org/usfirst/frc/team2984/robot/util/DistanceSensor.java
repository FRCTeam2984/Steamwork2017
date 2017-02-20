package org.usfirst.frc.team2984.robot.util;

import org.usfirst.frc.team2984.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;

/**
 * A wrapper class for analog input that deals with a distance sensor allowing the values to be read as inches.
 * @author max
 *
 */
public class DistanceSensor {

	private AnalogInput sensor;
	
	/**
	 * Creates a new distances sensor at the following analog input port
	 * @param port the port which the sensor is plugged into.
	 */
	public DistanceSensor(int port){
		this.sensor = new AnalogInput(port);
	}
	
	/**
	 * Returns the value that the sensor is outputting converted to inches. It uses a calibration constant in Settings{Settings}
	 * @return
	 */
	public double getDistanceInInches(){
		return this.sensor.getVoltage()*RobotMap.SENSOR_V_TO_IN_CALIBRATION;
//		return this.sensor.getVoltage()*Settings.getInstance().getDouble("SensorVtoInCalibration");
	}
	
}
