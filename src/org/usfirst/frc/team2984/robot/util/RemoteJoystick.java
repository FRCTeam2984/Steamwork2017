package org.usfirst.frc.team2984.robot.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class RemoteJoystick {

	private NetworkTable table;

	public RemoteJoystick(String name, boolean server) {

		if (server) {
			NetworkTable.setIPAddress("10.29.84.2");
			table = NetworkTable.getTable(name);
		} else {
			table = NetworkTable.getTable(name);
		}
	}


	public String getString(String key){
		if(this.isConnected()){
			if(table.containsKey(key)){
				return table.getString(key, "");
			}
		}
		return "";
	}
	
	//@TODO: implement whatever this does
	public int getInteger(String name){
		if (this.isConnected()){
			return (int) table.getNumber(name, 0);
		}
		return 0;
	}
	
	public double getX() {
		if (this.isConnected() && this.table.containsKey("axis2")){
			return table.getNumber("axis2", 0);
		}
		DriverStation.reportError("ArduinoReader is not running!", false);
		return 0;
	}

	public double getY() {
		if (this.isConnected() && this.table.containsKey("axis1")){
			return table.getNumber("axis1", 0);
		}
		DriverStation.reportError("ArduinoReader is not running!", false);
		return 0;
	}

	public double getTwist() {
		if (this.isConnected() && this.table.containsKey("yaw")){
			return table.getNumber("yaw", 0);
		}
		DriverStation.reportError("ArduinoReader is not running!", false);
		return 0;
	}

	public boolean getButton(int num) {
		if (this.isConnected() && this.table.containsKey("button" + num)){
			return table.getBoolean("button" + num, false);
		}
		DriverStation.reportError("ArduinoReader is not running!", false);
		return false;
	}

	public double getAxis(int num) {
		if (this.isConnected() && this.table.containsKey("axis" + num)){
			return table.getNumber("axis" + num, 0);
		}
		DriverStation.reportError("ArduinoReader is not running!", false);
		return 0;
	}

	public double getNumber(String name) {
		if (this.isConnected() && this.table.containsKey(name)){
			return table.getNumber(name, 0);
		}
		DriverStation.reportError("ArduinoReader is not running!", false);
		return 0;
	}

	public double getMode() {
		if (this.isConnected() && this.table.containsKey("grabber")){
			return table.getNumber("grabber", 0);
		}
		DriverStation.reportError("ArduinoReader is not running!", false);
		return 0;
	}

	public boolean isConnected() {
		return table.isConnected();
	}

	public void setButton(int num, boolean value) {
		table.putBoolean("button", value);
	}

	public long getTimeTaken() {
		long time = (long) table.getNumber("TimeStarted", 0);
		table.putNumber("TimeStartedServer", System.currentTimeMillis());
		return System.currentTimeMillis() - time;
	}

	public void setAxis(int num, double value) {
		table.putNumber("axis" + num, value);
	}

	public void setNumber(String name, double value) {
		table.putNumber(name, value);
	}
	
	public void setBoolean(String name, boolean value){
		table.putBoolean(name, value);
	}

}