package org.usfirst.frc.team2984.robot.util;

public class VisionTracker{

	private static VisionTracker visionTracker;
	
	private TrackingThread tracker;
	
	private VisionTracker(){
		this.tracker = new TrackingThread();
		this.tracker.start();
	}
	
	public static VisionTracker getInstance(){
		if(visionTracker == null){
			init();
		}
		return visionTracker;
	}
	
	public void shouldTrack(boolean should){
		this.tracker.setTracking(should);
	}
	
	public double getAngle(){
		return this.tracker.getAngle();
	}
	
	public double getDistance(){
		return this.tracker.getDistance();
	}
	
	public double robotAngle(){
		return this.tracker.robotAngle();
	}
	
	public boolean hasTrack(){
		return this.tracker.hasTrack();
	}
	
	public static void init(){
		visionTracker = new VisionTracker();
	}
	
}
