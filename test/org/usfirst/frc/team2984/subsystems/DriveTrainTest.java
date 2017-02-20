package org.usfirst.frc.team2984.subsystems;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import edu.wpi.first.wpilibj.HLUsageReporting;

import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.util.Motion;
import org.usfirst.frc.team2984.util.DummyReporter;

import com.ctre.CANTalon;

public class DriveTrainTest {
	private DriveTrain drive;
	private CANTalon frontLeft;
	private CANTalon frontRight;
	private CANTalon backLeft;
	private CANTalon backRight;
	private double speed = RobotMap.DRIVE_TRAIN_MAX_SPEED;
	
	static {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
	}
	
	@Before
	public void before() {
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
	
	@Test
	public void isDoneReturnsTrueWhileMovingSlowlyForward() {
		when(frontLeft.getEncVelocity()).thenReturn(1);
		when(frontRight.getEncVelocity()).thenReturn(-1);
		when(backLeft.getEncVelocity()).thenReturn(1);
		when(backRight.getEncVelocity()).thenReturn(-1);

		assertTrue(drive.isThere(4));
	}
	
	@Test
	public void isDoneReturnsTrueWhileMovingSlowlyBackward() {
		when(frontLeft.getEncPosition()).thenReturn(-1);
		when(frontRight.getEncPosition()).thenReturn(1);
		when(backLeft.getEncPosition()).thenReturn(-1);
		when(backRight.getEncPosition()).thenReturn(1);

		assertTrue(drive.isThere(4));
	}
	
	@Test
	public void isDoneReturnsTrueWhileMovingSlowlyLeft() {
		when(frontLeft.getEncPosition()).thenReturn(-1);
		when(frontRight.getEncPosition()).thenReturn(-1);
		when(backLeft.getEncPosition()).thenReturn(1);
		when(backRight.getEncPosition()).thenReturn(1);

		assertTrue(drive.isThere(4));
	}
	
	@Test
	public void isDoneReturnsTrueWhileMovingSlowlyRight() {
		when(frontLeft.getEncPosition()).thenReturn(1);
		when(frontRight.getEncPosition()).thenReturn(1);
		when(backLeft.getEncPosition()).thenReturn(-1);
		when(backRight.getEncPosition()).thenReturn(-1);

		assertTrue(drive.isThere(4));
	}
	
	@Test
	public void isDoneReturnsFalseWhileMovingQuicklyForward() {
		when(frontLeft.getEncVelocity()).thenReturn(100);
		when(frontRight.getEncVelocity()).thenReturn(-100);
		when(backLeft.getEncVelocity()).thenReturn(100);
		when(backRight.getEncVelocity()).thenReturn(-100);

		assertFalse(drive.isThere(4));
	}
	
	@Test
	public void isDoneReturnsFalseWhileMovingQuicklyBackward() {
		when(frontLeft.getEncVelocity()).thenReturn(-100);
		when(frontRight.getEncVelocity()).thenReturn(100);
		when(backLeft.getEncVelocity()).thenReturn(-100);
		when(backRight.getEncVelocity()).thenReturn(100);

		assertFalse(drive.isThere(4));
	}
	
	@Test
	public void isDoneReturnsFalseWhileMovingQuicklyLeft() {
		when(frontLeft.getEncVelocity()).thenReturn(-100);
		when(frontRight.getEncVelocity()).thenReturn(-100);
		when(backLeft.getEncVelocity()).thenReturn(100);
		when(backRight.getEncVelocity()).thenReturn(100);

		assertFalse(drive.isThere(4));
	}
	
	@Test
	public void isDoneReturnsFalseWhileMovingQuicklyRight() {
		when(frontLeft.getEncVelocity()).thenReturn(100);
		when(frontRight.getEncVelocity()).thenReturn(100);
		when(backLeft.getEncVelocity()).thenReturn(-100);
		when(backRight.getEncVelocity()).thenReturn(-100);

		assertFalse(drive.isThere(4));
	}
	
	@Test
	public void isDoneReturnsFalseWhileMovingDiagionaly() {
		when(frontLeft.getEncVelocity()).thenReturn(0);
		when(frontRight.getEncVelocity()).thenReturn(100);
		when(backLeft.getEncVelocity()).thenReturn(100);
		when(backRight.getEncVelocity()).thenReturn(0);

		assertFalse(drive.isThere(4));
	}
	
	@Test
	public void isDoneReturnsTrueWhileMovingSlowlyDiagionaly() {
		when(frontLeft.getEncVelocity()).thenReturn(0);
		when(frontRight.getEncVelocity()).thenReturn(1);
		when(backLeft.getEncVelocity()).thenReturn(1);
		when(backRight.getEncVelocity()).thenReturn(0);

		assertTrue(drive.isThere(4));
	}
	
	private void verifyTalons(double frontLeft, double frontRight, double backLeft, double backRight) {
		verify(this.frontLeft).set(frontLeft * speed);
		verify(this.frontRight).set(frontRight * speed);
		verify(this.backLeft).set(backLeft * speed);
		verify(this.backRight).set(backRight * speed);
	}
}
