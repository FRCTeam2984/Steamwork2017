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
	
	public Peg getPeg(){
		return this.tracker.getPeg();
	}
	
	public boolean hasTrack(){
		return this.tracker.hasTrack();
	}
	
	public static void init(){
		visionTracker = new VisionTracker();
	}
	
}
