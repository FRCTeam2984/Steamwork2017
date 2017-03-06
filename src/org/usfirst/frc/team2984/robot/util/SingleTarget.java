package org.usfirst.frc.team2984.robot.util;

import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Moments;

public class SingleTarget {

	private double x;
	private double y;
	private double width;
	private double height;
	
	public SingleTarget(RotatedRect boundingRect, Moments mu){
		this.x = (mu.get_m10() / mu.get_m00());
		this.y = (mu.get_m01() / mu.get_m00());
		if(Math.abs(boundingRect.angle%180) > 45){
			this.height = boundingRect.size.width;
			this.width = boundingRect.size.height;
		} else {
			this.width = boundingRect.size.width;
			this.height = boundingRect.size.height;
		}
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	
}
