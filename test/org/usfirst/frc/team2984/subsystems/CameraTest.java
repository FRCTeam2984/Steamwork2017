package org.usfirst.frc.team2984.subsystems;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
import org.usfirst.frc.team2984.robot.subsystems.Camera;
import org.usfirst.frc.team2984.robot.util.Dimension;
import org.usfirst.frc.team2984.robot.util.VisionTarget;
import org.usfirst.frc.team2984.util.DummyReporter;

import edu.wpi.cscore.CvSink;
import edu.wpi.first.wpilibj.HLUsageReporting;

public class CameraTest {
	private Dimension targetSize = new Dimension(5, 10.25);
	private Camera camera;
	private CvSink sink;
	
	static {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	@Before
	public void before() {
		sink = mock(CvSink.class);
		camera = new Camera(sink);
	}
	
	@Test
	public void getVisionTargetReturnsFooGivenBar() {
		VisionTarget expected = new VisionTarget(0, 50 * targetSize.width, 50 * targetSize.height);
		
		assertEquals(expected, camera.getVisionTarget());
	}
}
