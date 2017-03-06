package org.usfirst.frc.team2984.robot.util;

/**
 * A wall that has an angle in degrees and a distance in inches.
 * The distance is from the middle of the two distance sensors.
 * @author max
 *
 */
public class Wall {

	private double distance;
	private double angle;
	
	/**
	 * Makes a wall with the given distance and angle
	 * @param distance the distance from the wall centered between the two distance sensors
	 * @param angle The offset angle [-90, 90]
	 */
	public Wall(double distance, double angle){
		this.distance = distance;
		this.angle = angle;
	}
	
	/**
	 * Constructs a wall based on the distances from each distance sensor and the width apart. All values in inches.
	 * @param leftDistance The distance from the left distance sensor. [0, Infinity]
	 * @param rightDistance The distance from the right distance sensor. [0, Infinity]
	 * @param distanceApart The distance that the distance sensors are apart. [0, Infinity]
	 */
	public Wall(double leftDistance, double rightDistance, double distanceApart){
		this.distance = (leftDistance + rightDistance)/2;
		double angle = Math.atan((leftDistance - rightDistance)/distanceApart);
		this.angle = Math.toDegrees(angle);
	}
	
	/**
	 * Returns the distance from the wall in inches.
	 * @return the distance from the wall in inches.
	 */
	public double getDistance(){
		return this.distance;
	}
	
	/**
	 * Returns the angle of the wall in degrees.
	 * @return the angle of the wall in degrees. [-90, 90];
	 */
	public double getAngle(){
		return this.angle;
	}
	
	@Override

	public String toString(){
		return "Wall { Distance: " + this.distance + "in, Angle: " + this.angle + "°}";
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj == null) {
			return false;
		}
		
		if (!Wall.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		
		final Wall other = (Wall) obj;
		
		double distanceDiff = Math.abs(this.distance - other.distance);
		double angleDiff = Math.abs(this.angle - other.angle);
		
		if (distanceDiff > 0.0001 || angleDiff > 0.0001) {
			return false;
		}
		
		return true;
	}
	
}
