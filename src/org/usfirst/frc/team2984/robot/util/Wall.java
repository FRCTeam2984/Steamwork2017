package org.usfirst.frc.team2984.robot.util;

public class Wall {

	private double distance;
	private double angle;
	
	public Wall(double distance, double angle){
		this.distance = distance;
		this.angle = angle;
	}
	
	public double getDistance(){
		return this.distance;
	}
	
	public double getAngle(){
		return this.angle;
	}
	
}
