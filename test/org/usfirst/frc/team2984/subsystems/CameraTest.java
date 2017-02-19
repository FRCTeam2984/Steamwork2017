package org.usfirst.frc.team2984.subsystems;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team2984.robot.subsystems.Camera;
import org.usfirst.frc.team2984.robot.util.Dimension;
import org.usfirst.frc.team2984.robot.util.VisionTarget;
import org.usfirst.frc.team2984.util.DummyReporter;

import edu.wpi.first.wpilibj.HLUsageReporting;

public class CameraTest {
	private Dimension targetSize = new Dimension(5, 10.25);
	private Camera camera;
	
	@Before
	public void before() {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
		
		camera = new Camera();
	}
	
	@Test
	public void getVisionTargetReturnsFooGivenBar() {
		VisionTarget expected = new VisionTarget(0, 50 * targetSize.width, 50 * targetSize.height);
		
		assertEquals(expected, camera.getVisionTarget());
	}
}
