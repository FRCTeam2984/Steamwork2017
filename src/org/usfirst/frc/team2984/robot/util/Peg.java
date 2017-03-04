package org.usfirst.frc.team2984.robot.util;

import java.text.NumberFormat;

import org.opencv.core.Mat;

public class Peg {

	private double x;
	private double y;
	private double z;
	private double yaw;
	private double pitch;
	private double roll;
	
	public Peg(Mat tvec, Mat rvec){
		this.x = tvec.get(0, 0)[0];
		this.y = tvec.get(1, 0)[0];
		this.z = tvec.get(2, 0)[0];
		double[] rotations = this.toEuler(rvec);
		this.yaw = rotations[0];
		this.pitch = rotations[1];
		this.roll = rotations[2];
	}
	
	public Peg(double x, double y, double z, double yaw, double pitch, double roll){
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
	}
	
	/**
	 * conversion of the axis rotation to euler angles
	 * @see AngleToEuler{http://www.euclideanspace.com/maths/geometry/rotations/conversions/angleToEuler/index.htm}
	 * @param rvec the axis rotation vector to use
	 * @return the heading attitude and bank as an array of doubles
	 */
	private double[] toEuler(Mat rvec){
		double yaw;
		double pitch;
		double roll;
		double x = rvec.get(0, 0)[0];
		double y = rvec.get(1, 0)[0];
		double z = rvec.get(2, 0)[0];
		double angle = Math.sqrt(x * x + y * y + z * z); //Angle equals magnitude because of Opencv's compressed notation
		
		if (angle==0) return new double[]{Double.NaN, Double.NaN, Double.NaN};
		x /= angle;
		y /= angle;
		z /= angle;
		
		double s=Math.sin(angle);
		double c=Math.cos(angle);
		double t=1-c;

		if ((x*y*t + z*s) > 0.998) { // north pole singularity detected
			yaw = 2*Math.atan2(x*Math.sin(angle/2),Math.cos(angle/2));
			pitch = Math.PI/2;
			roll = 0;
			return new double[]{yaw, pitch, roll};
		}
		if ((x*y*t + z*s) < -0.998) { // south pole singularity detected
			yaw = -2*Math.atan2(x*Math.sin(angle/2),Math.cos(angle/2));
			pitch = -Math.PI/2;
			roll = 0;
			return new double[]{yaw, pitch, roll};
		}
		yaw = Math.atan2(y * s- x * z * t , 1 - (y*y+ z*z ) * t);
		pitch = Math.asin(x * y * t + z * s) ;
		roll = Math.atan2(x * s - y * z * t , 1 - (x*x + z*z) * t);
	    
		return new double[]{yaw, pitch, roll};
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double getYaw() {
		return yaw;
	}

	public double getPitch() {
		return pitch;
	}

	public double getRoll() {
		return roll;
	}
	
	public String toString(){
		NumberFormat format = NumberFormat.getInstance();
		format.setMinimumFractionDigits(2);
		format.setMinimumFractionDigits(2);
		return "Peg { Position: (" + format.format(this.x) + ", " + format.format(this.y) 
		+ ", " + format.format(this.z) + "), Rotation: (" + format.format(Math.toDegrees(this.yaw))
		+ ", " + format.format(Math.toDegrees(this.pitch)) + ", " + format.format(Math.toDegrees(this.roll)) + ")}";
	}
	
}