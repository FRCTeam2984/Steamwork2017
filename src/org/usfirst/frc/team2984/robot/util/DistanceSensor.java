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
	private double vToIn;
	private double inStart;
	
	/**
	 * Creates a new distances sensor at the following analog input port
	 * @param port the port which the sensor is plugged into.
	 */
	public DistanceSensor(int port, double vToIn, double inStart){
		this.sensor = new AnalogInput(port);
		this.sensor.setAverageBits(4);
		this.vToIn = vToIn;
		this.inStart = inStart;
	}
	
	/**
	 * Returns the value that the sensor is outputting converted to inches. It uses a calibration constant in Settings{Settings}
	 * @return
	 */
	public double getDistanceInInches(){
		return (5-this.sensor.getVoltage())*vToIn + inStart;
//		return this.sensor.getVoltage()*Settings.getInstance().getDouble("SensorVtoInCalibration");
	}
	
}
