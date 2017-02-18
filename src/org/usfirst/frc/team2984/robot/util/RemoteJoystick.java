package org.usfirst.frc.team2984.robot.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class RemoteJoystick {
	private static RemoteJoystick instance;
	
	private NetworkTable table;
	
	public static RemoteJoystick getInstance() {
		if (instance == null) {
			NetworkTable.setIPAddress("10.29.84.2");
			
			instance = new RemoteJoystick(NetworkTable.getTable("CustomData1"));
		}
		
		return instance;
	}
	
	public RemoteJoystick(NetworkTable table) {
		this.table = table;
	}
	
	public double getX() {
		testConnection();
		
		return table.getNumber("axis2", 0);
	}

	public double getY() {
		testConnection();
		
		return table.getNumber("axis1", 0);
	}

	public double getTwist() {
		testConnection();
		
		return table.getNumber("yaw", 0);
	}

	private void testConnection() {
		if (!table.isConnected()) {
			DriverStation.reportError("ArduinoReader is not running!", false);
		}
	}
}