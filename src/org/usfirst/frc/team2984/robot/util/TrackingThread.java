package org.usfirst.frc.team2984.robot.util;

import static org.opencv.core.Core.inRange;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team2984.robot.RobotMap;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TrackingThread extends Thread {

	private static final double NORMAL_WIDTH = 1.65;
	
	private Rect left;
	private Rect right;
	private Mat output;
	private Mat processingMat;
	private Mat tmp;
    private Scalar minc;
    private Scalar maxc;
	
	private volatile boolean shouldProcess;
	private volatile boolean hasTrack;
	private volatile double offsetAngle;
	private volatile double robotAngle;
	private volatile double distance;
	private volatile double leftRight;
	CvSource outputStream ;
	
	public TrackingThread(){
		minc = new Scalar(RobotMap.HUE_LOW, RobotMap.SATURATION_LOW, RobotMap.VALUE_LOW);
		maxc = new Scalar(RobotMap.HUE_HIGH, RobotMap.SATURATION_HIGH, RobotMap.VALUE_HIGH);
		this.shouldProcess = true;
		this.hasTrack = false;
		this.offsetAngle = -1;
		this.robotAngle = -1;
		this.distance = -1;
		this.output = new Mat();
		this.tmp = new Mat();
	}
	
	/**
	 * Starts the camera capture, sets resolution and exposure, and starts processing the video.
	 */
	@Override
	public void run() {
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(320, 240);
        camera.setExposureManual(1);
        CvSink cvSink = CameraServer.getInstance().getVideo();
        outputStream = CameraServer.getInstance().putVideo("Blur", 320, 240);

        Mat source = new Mat();
        processingMat = new Mat();

        while(true) {

        	try{

        		if(shouldProcess){
        			cvSink.grabFrame(source);
            		if(!source.empty())
            			process(source); 	

        		} else {
        			Thread.sleep(10);
        		}
        	} catch(Exception e){
        		SmartDashboard.putString("Error", e.getStackTrace()[0] +  "");
        	}
        }
	}
	
	/**
	 * Finds the rectangles and then calculates each of the measurements and updates them into the local variables.
	 * @param source The image to process
	 */
	public void process(Mat source){
		Rect[] rects = this.findRects(source);
		if(rects != null){
			SmartDashboard.putString("Rect1", rects[0].x + ", " + rects[0].y + ";" + rects[0].width + "," + rects[0].height);
			SmartDashboard.putString("Rect2", rects[1].x + ", " + rects[1].y + ";" + rects[1].width + "," + rects[1].height);
			if(rects[0].x < rects[1].x){
				this.left = rects[0];
				this.right = rects[1];
			} else {
				this.left = rects[1];
				this.right = rects[0];
			}
			double averageHight = this.left.height + this.right.height;
			averageHight /= 2.0;
			double distance = distance(averageHight);
			double angle = angle(Math.abs(this.right.x - this.left.x), averageHight);
			angle *= (this.left.height > this.right.height) ? -1 : 1;
			double robotAngle = robotAngle((this.right.x + this.left.x)/2.0);
			SmartDashboard.putNumber("Distance from board", distance);
			SmartDashboard.putNumber("Angle", angle);
			hasTrack = true;
			this.distance = distance;
			this.offsetAngle = angle;
			this.robotAngle = robotAngle;
			SmartDashboard.putBoolean("Track", true);

		} else {
			hasTrack = false;
			SmartDashboard.putBoolean("Track", false);
		}
	}
	
	private double robotAngle(double center){
		return center*2/320-1;
	}
	
	/**
	 * Finds the distance from the peg, Right not it uses the wrong algorithm, it should just be a linear regression.
	 * @param hight the height of the rectangles
	 * @return the distance from he camera to the rectangles.
	 */
	private double distance(double hight){
		//TODO Get Real Values
		double verticalAngle = hight/240*(35/180D*Math.PI);
		return 5/Math.tan(verticalAngle);
	}
	
	/**
	 * finds the angle based on the distance from the peg and the height of the rectangles.
	 * The height is used to find the distance that should be between the two rectangles.
	 * This height is then used along with the distance between the rectangles to find the angle offset.
	 * This is done with a triangle where one side is the distance between the two rectangles and the other is the appropriate width.
	 * @param dist The distance between the two rectangles in pixels
	 * @param hight The height of the rectangles in pixels
	 * @return the angle offset of the peg [0, 90]
	 */
	private double angle(double dist, double hight){
		double widthNomialNormalized = NORMAL_WIDTH*hight;
		double angle = Math.acos(Math.min(dist / widthNomialNormalized, 1));
		return angle;
	}
	
	/**
	 * Finds the rectangles in the given picture.
	 * It first filters out all other colors by searching for a color range, creating a binary image.
	 * Then it blurs the image. Then if runs a contour finder on the image. Then it makes sure that there are only two reults.
	 * If there are more or less it will return null.
	 * @param source The image to look in
	 * @return null (if not fount) or the two found rectangles
	 */
	private Rect[] findRects(Mat source){
        Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2HSV);
        inRange(output, minc, maxc, output);
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.blur(output, processingMat, new Size(3, 3));
        Imgproc.findContours(processingMat, contours, tmp, Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE);
        outputStream.putFrame(output);
        int i = 0;
        while(i < contours.size()){
        	if(Imgproc.contourArea(contours.get(i)) < 5){
        		contours.remove(i);
        		continue;
        	}
        	i++;
        }
        if(contours.size() == 2){
        	Rect rectA = Imgproc.boundingRect(contours.get(0));
            Rect rectB = Imgproc.boundingRect(contours.get(1));
            return new Rect[]{rectA, rectB};
        }
        return null;
	}
	
	/**
	 * Sets whether or not the tracker should be tracking, if not it sleeps for 10ms and then checks if it should check again.
	 * @param tracking whether or not to track
	 */
	public synchronized void setTracking(boolean tracking){
		this.shouldProcess = tracking;
	}
	
	/**
	 * Gets the angle offset from the peg's view, 0 is dead center, + is clockwise
	 * @return the angle [-90, 90]
	 */
	public synchronized double getAngle(){
		return this.offsetAngle;
	}

	/**
	 * Returns the distance in inches from the peg
	 * @return the distance [0, infinity]
	 */
	public synchronized double getDistance(){
		return this.distance;
	}
	
	public synchronized double robotAngle(){
		return this.robotAngle;
	}
	
	/**
	 * Returns whether or not the tracker has a track.
	 * @return whether or not the tracker has a track.
	 */
	public synchronized boolean hasTrack(){
		return this.hasTrack && this.shouldProcess;
	}
	
}
