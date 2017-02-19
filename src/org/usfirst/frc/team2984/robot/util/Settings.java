package org.usfirst.frc.team2984.robot.util;

import java.util.HashMap;

public class Settings {

	private static Settings settings;
	
	public static Settings getInstance(){
		if(settings == null){
			settings = new Settings();
		}
		return settings;
	}
	
	private HashMap<String, Object> data;
	
	private Settings(){
		this.data = new HashMap<String, Object>();
		init();
	}
	
	private void init(){
		this.data.put("HueLow", 65);
		this.data.put("HueHigh", 115);
		this.data.put("SaturationLow", 57);
		this.data.put("SaturationHigh", 207);
		this.data.put("ValueLow", 34);
		this.data.put("ValueHigh", 179);
		this.data.put("DriveTrainMaxSpeed", 1000.0);
		this.data.put("DriveTrainMaxVoltage", 12.0);
		this.data.put("DriveTrainTickToInch", 0.001 /*TODO: FIND THIS*/);
		this.data.put("SensorVtoInCalibration", 1D);
		this.data.put("SensorWidth", 1D);
	}
	
	public boolean getBoolean(String key){
		if(this.data.containsKey(key)){
			if(this.data.get(key).getClass().equals(Boolean.class))
				return (Boolean) this.data.get(key);
			else
				return false;
		} else {
			this.data.put(key, false);
			return false;
		}
	}
	
	public String getString(String key){
		if(this.data.containsKey(key)){
			if(this.data.get(key).getClass().equals(String.class))
				return (String) this.data.get(key);
			else
				return ""; 
		} else {
			this.data.put(key, "");
			return "";
		}
	}
	
	public int getInt(String key){
		if(this.data.containsKey(key)){
			if(this.data.get(key).getClass().equals(Integer.class))
				return (Integer) this.data.get(key);
			else
				return 0;
		} else {
			this.data.put(key, 0);
			return 0;
		}
	}
	
	public double getDouble(String key){
		if(this.data.containsKey(key)){
			if(this.data.get(key).getClass().equals(Double.class))
				return (Double) this.data.get(key);
			else
				return 0;
		} else {
			this.data.put(key, 0.0);
			return 0;
		}
	}
	
}
