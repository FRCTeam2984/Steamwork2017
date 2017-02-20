package org.usfirst.frc.team2984.robot.subsystems;

import org.opencv.core.Mat;
import org.usfirst.frc.team2984.robot.util.Dimension;
import org.usfirst.frc.team2984.robot.util.VisionTarget;

import edu.wpi.cscore.CvSink;
//import edu.wpi.cscore.CvSink;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Camera extends Subsystem {
	private CvSink sink;
	
	public Camera(CvSink sink) {
		this.sink = sink;
	}
	
	public VisionTarget getVisionTarget() {
		Mat image = new Mat();
		
		sink.grabFrame(image);
		// TODO: stop cheating!
		Dimension targetSize = new Dimension(5, 10.25);
		return new VisionTarget(0, 50 * targetSize.width, 50 * targetSize.height);
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}
	
//    new Thread(() -> {
//        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
//        camera.setResolution(640, 480);
//        
//        CvSink cvSink = CameraServer.getInstance().getVideo();
//        CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
//        
//        Mat source = new Mat();
//        Mat output = new Mat();
//        
//        while(!Thread.interrupted()) {
//            cvSink.grabFrame(source);
//            Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
//            outputStream.putFrame(output);
//        }
//    }).start();
}
