package org.usfirst.frc.team2984.robot.util;

import static org.opencv.core.Core.inRange;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.usfirst.frc.team2984.robot.RobotMap;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TrackingThread extends Thread {

	private CameraSpecification spec;
	private Mat processingMat;
	private Mat tmp;
    private Scalar minc;
    private Scalar maxc;
    
	private volatile boolean shouldProcess;
	private volatile boolean hasTrack;
	private volatile VisionTarget target;

	
	public TrackingThread(){
		minc = new Scalar(0, 0, RobotMap.VALUE_LOW);
		maxc = new Scalar(180, 255, 255);
		this.shouldProcess = true;
		this.hasTrack = false;
		this.tmp = new Mat();
		this.processingMat = new Mat();
		this.spec = RobotMap.CAMERA_SPECIFICATION;
		this.target = new VisionTarget(0,0,0);
	}
	
	/**
	 * Starts the camera capture, sets resolution and exposure, and starts processing the video.
	 */
	@Override
	public void run() {
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution((int)(this.spec.resolution.width + 0.5), (int)(this.spec.resolution.height + 0.5));
        camera.setExposureManual(0);
        CvSink cvSink = CameraServer.getInstance().getVideo();
        CvSource outputStream = CameraServer.getInstance().putVideo("Blur", (int)(this.spec.resolution.width + 0.5), (int)(this.spec.resolution.height + 0.5));

        Mat source = new Mat();

        while(true) {

        	try{

        		if(shouldProcess){
        			cvSink.grabFrame(source);
            		if(!source.empty())
            			process(source); 	
            		outputStream.putFrame(source);
        		} else {
        			Thread.sleep(10);
        		}
        	} catch(Exception e){
        		DriverStation.reportError(e.toString(), false);
        		SmartDashboard.putString("Error", e.getStackTrace()[0] +  "");
        		SmartDashboard.putString("Error String", e +  "");
        	}
        }
	}
	
	/**
	 * Finds the rectangles and then calculates each of the measurements and updates them into the local variables.
	 * @param source The image to process
	 */
	public void process(Mat source){
		SingleTarget[] rects = this.findRects(source);
		if(rects != null){
			this.hasTrack = true;
			this.target = new VisionTarget(rects[0], rects[1], this.spec);
		} else {
			hasTrack = false;
		}
	}
	
	/**
	 * Finds the rectangles in the given picture.
	 * It first filters out all other colors by searching for a color range, creating a binary image.
	 * Then it blurs the image. Then if runs a contour finder on the image. Then it makes sure that there are only two reults.
	 * If there are more or less it will return null.
	 * @param source The image to look in
	 * @return null (if not fount) or the two found rectangles
	 */
	private SingleTarget[] findRects(Mat source){
        Imgproc.cvtColor(source, source, Imgproc.COLOR_BGR2HSV);
        inRange(source, minc, maxc, source);
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.blur(source, processingMat, new Size(3, 3));
        Imgproc.findContours(processingMat, contours, tmp, Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE);
        int i = 0;
        while(i < contours.size()){
        	if(Imgproc.contourArea(contours.get(i)) < 30){
        		contours.remove(i);
        		continue;
        	}
        	i++;
        }
        if(contours.size() == 2){
        	MatOfPoint2f contoursA = new MatOfPoint2f( contours.get(0).toArray() );
        	MatOfPoint2f contoursB = new MatOfPoint2f( contours.get(1).toArray() );
        	RotatedRect rectA = Imgproc.minAreaRect(contoursA);
            RotatedRect rectB = Imgproc.minAreaRect(contoursB);
            Moments muA = Imgproc.moments(contours.get(0), false);
			Moments muB = Imgproc.moments(contours.get(1), false);
			contoursA.release();
			contoursB.release();
            return new SingleTarget[]{new SingleTarget(rectA, muA), new SingleTarget(rectB, muB)};
        } else if(contours.size() == 3){
        	Rect rectA = Imgproc.boundingRect(contours.get(0));
        	Rect rectB = Imgproc.boundingRect(contours.get(1));
        	Rect rectC = Imgproc.boundingRect(contours.get(2));
        	int deltaAB = Math.abs(rectA.x - rectB.x);
        	int deltaAC = Math.abs(rectA.x - rectC.x);
        	int deltaBC = Math.abs(rectB.x - rectC.x);
        	if(deltaAB < deltaAC && deltaAB < deltaBC){
        		Point tl = min(rectA.tl(),rectB.tl());
        		Point br = max(rectA.br(),rectB.br());
        		Point center = average(tl, br);
        		Size size = delta(tl, br);
        		RotatedRect merged = new RotatedRect(center, size, 0);
            	MatOfPoint2f contoursC = new MatOfPoint2f( contours.get(2).toArray() );
            	RotatedRect rotatedRectC = Imgproc.minAreaRect(contoursC);
                Moments muC = Imgproc.moments(contours.get(2), false);
                Moments muAB = new Moments(new double[]{1, center.x, center.y});
            	contoursC.release();
                return new SingleTarget[]{new SingleTarget(merged, muAB), new SingleTarget(rotatedRectC, muC)};
        	} else if(deltaAC < deltaBC){
        		Point tl = min(rectA.tl(),rectC.tl());
        		Point br = max(rectA.br(),rectC.br());
        		Point center = average(tl, br);
        		Size size = delta(tl, br);
        		RotatedRect merged = new RotatedRect(center, size, 0);
            	MatOfPoint2f contoursB = new MatOfPoint2f( contours.get(1).toArray() );
            	RotatedRect rotatedRectB = Imgproc.minAreaRect(contoursB);
                Moments muB = Imgproc.moments(contours.get(1), false);
                Moments muAC = new Moments(new double[]{1, center.x, center.y});
            	contoursB.release();
                return new SingleTarget[]{new SingleTarget(merged, muAC), new SingleTarget(rotatedRectB, muB)};
        	} else{
        		Point tl = min(rectB.tl(),rectC.tl());
        		Point br = max(rectB.br(),rectC.br());
        		Point center = average(tl, br);
        		Size size = delta(tl, br);
        		RotatedRect merged = new RotatedRect(center, size, 0);
            	MatOfPoint2f contoursA = new MatOfPoint2f( contours.get(0).toArray() );
            	RotatedRect rotatedRectA = Imgproc.minAreaRect(contoursA);
                Moments muA = Imgproc.moments(contours.get(1), false);
                Moments muBC = new Moments(new double[]{1, center.x, center.y});
            	contoursA.release();
                return new SingleTarget[]{new SingleTarget(merged, muBC), new SingleTarget(rotatedRectA, muA)};
        	}
        }
        return null;
	}
	
	private Point min(Point a, Point b){
		return new Point(Math.min(a.x, b.x), Math.min(a.y, b.y));
	}
	
	private Point max(Point a, Point b){
		return new Point(Math.max(a.x, b.x), Math.max(a.y, b.y));
	}
	
	private Point average(Point a, Point b){
		return new Point((a.x + b.x)/2, (a.y + b.y)/2);
	}
	
	private Size delta(Point a, Point b){
		return new Size(Math.abs(a.x - b.x), Math.abs(a.y - b.y));
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
	public synchronized VisionTarget getTarget(){
		return this.target;
	}

	
	/**
	 * Returns whether or not the tracker has a track.
	 * @return whether or not the tracker has a track.
	 */
	public synchronized boolean hasTrack(){
		return this.hasTrack && this.shouldProcess;
	}
	
}
