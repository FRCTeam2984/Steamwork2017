package org.usfirst.frc.team2984.robot.util;

import static org.opencv.core.Core.inRange;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team2984.robot.RobotMap;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TrackingThread extends Thread {

	private static final double NORMAL_WIDTH = 1.43;
	
	private Rect left;
	private Rect right;
	private Mat output;
	private Mat processingMat;
	private Mat tmp;
    private Scalar minc;
    private Scalar maxc;
	
	private volatile boolean shouldProcess;
	private volatile boolean hasTrack;
	private volatile Peg peg;
	CvSource outputStream;
	private MatOfPoint3f objectPoints;
	
	public TrackingThread(){
		minc = new Scalar(RobotMap.HUE_LOW, RobotMap.SATURATION_LOW, RobotMap.VALUE_LOW);
		maxc = new Scalar(RobotMap.HUE_HIGH, RobotMap.SATURATION_HIGH, RobotMap.VALUE_HIGH);
		ArrayList<Point3> objectPoints = new ArrayList<Point3>(8);
		
		//Top Left -> Top Right -> Bottom Left -> Bottom Right
		objectPoints.add(new Point3(-5.125, 2.5, 0));
		objectPoints.add(new Point3(-3.125, 2.5, 0));
		objectPoints.add(new Point3(3.125, 2.5, 0));
		objectPoints.add(new Point3(5.125, 2.5, 0));
//		objectPoints.add(new Point3(-4.125, 0, 0));
//		objectPoints.add(new Point3(4.125, 0, 0));
		objectPoints.add(new Point3(-5.125, -2.5, 0));
		objectPoints.add(new Point3(-3.125, -2.5, 0));
		objectPoints.add(new Point3(3.125, -2.5, 0));
		objectPoints.add(new Point3(5.125, -2.5, 0));

		Point3[] points = new Point3[4];
		
		this.objectPoints = new MatOfPoint3f(objectPoints.toArray(points));
		this.shouldProcess = true;
		this.hasTrack = false;
		this.peg = new Peg(0,0,0,0,0,0);
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
        camera.setExposureManual(0);
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
	 * Finds the peg and updates the member variables
	 * @param source The image to process
	 */
	public void process(Mat source){
		List<Point> points = this.findRectanglePoints(source);
		if(points.size() != 8){
			this.hasTrack = false;
		}
		this.hasTrack = true;
		points.sort(new Comparator<Point>(){

			@Override
			public int compare(Point arg0, Point arg1) {
				return (int) ((arg0.x-arg0.y*10)-(arg1.x-arg1.y*10));
			}
			
		});
		
		Point[] pointArray = new Point[points.size()];
		this.peg = solvePnP(points.toArray(pointArray));
		
	}
	
	/**
	 * Solves the PnP problem for the given points.
	 * Aka using the points that were found with {@link #findRectanglePoints(Mat) findRectanglePoints}
	 * the method finds the corresponding peg object.
	 * 
	 * Contained within are the camera values, they should be moved out.
	 * @param points The points to use for the PnP
	 * @return the peg corresponding to the projected points.
	 */
	public Peg solvePnP(Point[] points){
		Mat intrinsics = Mat.eye(3, 3, CvType.CV_32F);
		intrinsics.put(0, 0, 386.3);
		intrinsics.put(1, 1, 515.066666667);
		intrinsics.put(0, 2, 320 / 2);
		intrinsics.put(1, 2, 240 / 2);
		MatOfPoint2f points2dMat = new MatOfPoint2f( points);

		Mat rvec = new Mat();
		Mat tvec = new Mat();
		Calib3d.solvePnP(objectPoints, points2dMat, intrinsics, new MatOfDouble(), rvec, tvec, false, Calib3d.CV_EPNP);
		Mat cameraAngle = Mat.eye(3, 3, CvType.CV_64F);
		cameraAngle.put(0, 0, 0.9848);
		cameraAngle.put(0, 2, -0.1737);
		cameraAngle.put(2, 0, 0.1737);
		cameraAngle.put(2, 2, 0.9848);
		Mat rotatedT = new Mat();
		Core.gemm(cameraAngle, tvec, 1, new Mat(0, 0, CvType.CV_64F), 0, rotatedT);
		tvec.release();
		Peg p = new Peg(rotatedT, rvec);
		rotatedT.release();
		rvec.release();
		return p;

	}
	
	/**
	 * Finds the rectangles in the given picture.
	 * 1. First it finds the areas that are above a threshold in
	 * value and makes the rest of the image black leaving the
	 * Monochrome value picture.
	 * 2. It computes the corner Eigen Values which equates to
	 * finding how much a region looks like an edge in either direction.
	 * 3. Filters out all of the Eigen values that don't look like corners.
	 * 4. Finds the connected components and their centroids (fancy word for
	 * weighted centers). This finds them based on connected components which
	 * means the background gets found too, therefore we must filter them.
	 * Done in step 7.
	 * 5. Converts the centroids to points, hack to make JNI work.
	 * 6. Finds the sub-pixle corner of each of the points using built in methods.
	 * 7. Filters out the points which don't have Eigen values backing them up.
	 * @param source The image to look in
	 * @return the points which it has found, if two full rectangles were found then 8 points are returned
	 */
	private List<Point> findRectanglePoints(Mat source){
		//1.
        Imgproc.cvtColor(source, source, Imgproc.COLOR_BGR2HSV);
        List<Mat> hsv = new ArrayList<Mat>();
        Core.split(source, hsv);
        Mat value = hsv.get(2);
        Mat binaryValue = new Mat();
        Imgproc.threshold(value, binaryValue, minc.val[2], 255, Imgproc.THRESH_BINARY);
        this.dialateAndMask(value, binaryValue, 10);
        //2.
        Mat cornersMat = new Mat();
        Imgproc.cornerMinEigenVal(value, cornersMat, 4, 3);
        //3.
		MinMaxLocResult minMax = Core.minMaxLoc(cornersMat);
        Imgproc.threshold(cornersMat, cornersMat, minMax.maxVal/4, 255, Imgproc.THRESH_BINARY);
        //4.
        Mat labels = new Mat();
        Mat stats = new Mat();
        Mat centroids = new Mat();
        Mat corersProcessed = new Mat();
        cornersMat.convertTo(corersProcessed, CvType.CV_8U);
        Imgproc.connectedComponentsWithStats(corersProcessed, labels, stats, centroids);
        //5.
        Point[] centroidsArray = new Point[centroids.rows()];
        double[] centroidInfo = new double[2];
        for(int i = 0; i < centroids.rows(); i++) {
        	centroids.row(i).get(0, 0, centroidInfo);
            Point centroid = new Point(centroidInfo[0], centroidInfo[1]);
            centroidsArray[i] = centroid;
        }
        MatOfPoint2f corners = new MatOfPoint2f(centroidsArray);
        //6.
        TermCriteria criteria = new TermCriteria(TermCriteria.EPS | TermCriteria.MAX_ITER, 30, 0.1);
        Imgproc.cornerSubPix(value, corners, new Size(5,5), new Size(-1,-1), criteria);
        //7.
        Point[] points = corners.toArray();
        List<Point> finalPoints = new ArrayList<Point>();
        for(Point point : points){
        	if(point.x == 0 || point.y == 0)
        		continue;
        	if(isValZero(point, 0, 0, labels) && isValZero(point, 1, 0, labels) && isValZero(point, 0, 1, labels)
        			&& isValZero(point, -1, 0, labels) && isValZero(point, 0, -1, labels)&&
        			isValZero(point, 2, 0, labels) && isValZero(point, 0, 2, labels) 
        			&& isValZero(point, -2, 0, labels) && isValZero(point, 0, -2, labels)){
        		continue;
        	}
        	finalPoints.add(point);
        }
        return finalPoints;
	}
	
	/**
	 * returns whether or not the value in the matrix at p plus the offset is zero.
	 * @param p The point of which the coordinates will be used
	 * @param xOff The offset to search in the x direction
	 * @param yOff The offset to search in the y direction
	 * @param toCheckIn The matrix to check the value of
	 * @return whether or not the value at (p.x + xOff, p.y + yOff) is 0
	 */
	private boolean isValZero(Point p, int xOff, int yOff, Mat toCheckIn){
		int x = (int)(p.x+0.5) + xOff;
		int y = (int)(p.y+0.5) + yOff;
		if(x < 0 || y < 0){
			return true;
		}
		double val = toCheckIn.get(y, x)[0];
		return val <= 0.001;
	}
	
	/**
	 * Dilates the binary image by the given size and then masks the
	 * specified image with the dilated one.
	 * @param toMask The image to mask
	 * @param binary The binary image to dilate and use as a mask
	 * @param dialateSize The size of the square used to dilate.
	 */
	private void dialateAndMask(Mat toMask, Mat binary, int dialateSize){
		Mat kernel = Imgproc.getStructuringElement(0, new Size(dialateSize,dialateSize));
		Mat dialted = new Mat();
		Imgproc.dilate(binary, dialted, kernel);
        Imgproc.threshold(dialted, dialted, 250, 255, Imgproc.THRESH_BINARY);
		MinMaxLocResult minMax = Core.minMaxLoc(toMask, dialted);
		Mat newMasked = new Mat(toMask.size(), toMask.type());
		newMasked.setTo(new Scalar(minMax.minVal));
		toMask.copyTo(newMasked, dialted);
		newMasked.copyTo(toMask);
		newMasked.release();
		kernel.release();
	}
	
	/**
	 * Sets whether or not the tracker should be tracking, if not it sleeps for 10ms and then checks if it should check again.
	 * @param tracking whether or not to track
	 */
	public synchronized void setTracking(boolean tracking){
		this.shouldProcess = tracking;
	}
	
	/**
	 * Gets the last known peg
	 * @return the last know peg
	 */
	public synchronized Peg getPeg(){
		return this.peg;
	}
	
	/**
	 * Returns whether or not the tracker has a track.
	 * @return whether or not the tracker has a track.
	 */
	public synchronized boolean hasTrack(){
		return this.hasTrack && this.shouldProcess;
	}
	
}
