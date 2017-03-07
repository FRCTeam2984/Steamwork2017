package org.usfirst.frc.team2984.robot.util;

public class MathUtil {

	/**
	 * Will return the smallest angle needed to add to angle to get to toMoveTo/
	 * @param angle the angle to start at
	 * @param toMoveTo the angle to finish at
	 * @return the smallest angle to add to angle to get to toMoveTo
	 */
	public static double shortestDeltaAngle(double angle, double toMoveTo){
		if(angle < 0 && toMoveTo < 0){
			return -shortestDeltaAngle(-angle, -toMoveTo);
		}
		if(angle < 0){
			return shortestDeltaAngle(360 + angle % 360, toMoveTo);
		} else if(toMoveTo < 0){
			return shortestDeltaAngle(angle, 360 + toMoveTo % 360);
		}
		double difference = toMoveTo - angle;
		double otherWay = -Math.copySign(360 - Math.abs(difference), difference);
		if(Math.abs(difference) < Math.abs(otherWay)){
			return difference%360;
		} else {
			return otherWay%360;
		}
	}
	
}
