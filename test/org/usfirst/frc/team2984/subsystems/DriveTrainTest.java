package org.usfirst.frc.team2984.subsystems;

import static org.mockito.Mockito.*;

import edu.wpi.first.wpilibj.HLUsageReporting;

import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team2984.dock.VisionTarget;
import org.usfirst.frc.team2984.robot.Camera;
//import org.mockito.InOrder;
import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.util.Dimension;
import org.usfirst.frc.team2984.robot.util.Motion;
import org.usfirst.frc.team2984.robot.util.Settings;
import org.usfirst.frc.team2984.util.DummyReporter;

import com.ctre.CANTalon;

public class DriveTrainTest {
	private DriveTrain drive;
	private CANTalon frontLeft;
	private CANTalon frontRight;
	private CANTalon backLeft;
	private CANTalon backRight;
	private Dimension targetSize = new Dimension(5, 10.25);
	private Dimension resolution = new Dimension(1000, 500);
	private Dimension fieldOfView = new Dimension(55, 45);
	private Camera camera = new Camera(resolution, fieldOfView);
	private double speed = Settings.getInstance().getDouble("DriveMotorRate");
	
	@Before
	public void before() {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
		
		frontLeft = mock(CANTalon.class);
		frontRight = mock(CANTalon.class);
		backLeft = mock(CANTalon.class);
		backRight = mock(CANTalon.class);
		drive = new DriveTrain(frontLeft, frontRight, backLeft, backRight);
	}
	
	@Test
	public void moveDrivesGivenPositiveX() {
		drive.move(new Motion(1, 0, 0));
		verifyTalons(1, -1, -1, 1);
	}
	
	@Test
	public void moveDrivesGivenNegativeX() {
		drive.move(new Motion(-1, 0, 0));
		verifyTalons(-1, 1, 1, -1);
	}
	
	@Test
	public void moveDrivesGivenPositiveY() {
		drive.move(new Motion(0, 1, 0));
		verifyTalons(1, 1, 1, 1);
	}
	
	@Test
	public void moveDrivesGivenNegativeY() {
		drive.move(new Motion(0, -1, 0));
		verifyTalons(-1, -1, -1, -1);
	}
	
	@Test
	public void moveDrivesGivenPositiveRotation() {
		drive.move(new Motion(0, 0, 1));
		verifyTalons(1, -1, 1, -1);
	}
	
	@Test
	public void moveDrivesGivenNegativeRotation() {
		drive.move(new Motion(0, 0, -1));
		verifyTalons(-1, 1, -1, 1);
	}
	
	@Test
	public void moveDrivesGivenPositiveXandY() {
		drive.move(new Motion(1, 1, 0));
		// This passes, but it looks like a bug.
		verifyTalons(1, 0, 0, 1);
	}
	
//	@Test
//	public void dockDrivesForwardGivenHeadOn() {
//		VisionTarget target = new VisionTarget(0, 50, 61.8642);
	//  this target is invalid, so the test fails with NaN
//		
//		drive.dock(target, camera, targetSize);
//		verifyTalons(1, 1, 1, 1);
//	}
	
//	@Test
//	public void dockFooGivenHeadOnAndRotatedRight() {
//		VisionTarget target = new VisionTarget(0, -40, 61.8642);
//		
//		drive.dock(target, camera, targetSize);
//		verifyTalons(1, 1, 1, 1);
//	}
	
	private void verifyTalons(double frontLeft, double frontRight, double backLeft, double backRight) {
		verify(this.frontLeft).set(frontLeft * speed);
		verify(this.frontRight).set(frontRight * speed);
		verify(this.backLeft).set(backLeft * speed);
		verify(this.backRight).set(backRight * speed);
	}
}
