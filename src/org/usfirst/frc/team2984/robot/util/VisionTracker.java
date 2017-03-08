package org.usfirst.frc.team2984.robot.util;

public class VisionTracker{

	private static VisionTracker visionTracker;
	
	private TrackingThread thread;
	
	private VisionTracker(){
		this.thread = new TrackingThread();
		this.thread.start();
	}
	
	public static VisionTracker getInstance(){
		if(visionTracker == null){
			init();
		}
		return visionTracker;
	}
	
	public void shouldTrack(boolean should){
		this.thread.setTracking(should);
	}
	
	public VisionTarget getTarget(){
		return this.thread.getTarget();
	}
	
	public boolean hasTrack(){
		return this.thread.hasTrack();
	}
	
	public static void init(){
		visionTracker = new VisionTracker();
	}
	
}
