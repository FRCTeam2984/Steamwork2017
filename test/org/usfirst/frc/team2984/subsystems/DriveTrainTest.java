package org.usfirst.frc.team2984.subsystems;

import static org.mockito.Mockito.*;

import edu.wpi.first.wpilibj.HLUsageReporting;

import org.junit.Before;
import org.junit.Test;
//import org.mockito.InOrder;
import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.util.DummyReporter;

import com.ctre.CANTalon;

public class DriveTrainTest {
	private DriveTrain drive;
	private CANTalon frontLeft;
	private CANTalon frontRight;
	private CANTalon backLeft;
	private CANTalon backRight;
	
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
	public void testMoveDrivesFrontLeftMotorGivenPositiveX() {
		drive.move(1, 0, 0);
		verify(frontLeft).set(1.0);
	}
	
	@Test
	public void testMoveDrivesFrontRightMotorGivenPostiiveX() {
		drive.move(1, 0,  0);
		verify(frontRight).set(-1.0);
	}
	
	@Test
	public void testMoveDrivesBackLeftMotorGivenPostiiveX() {
		drive.move(1, 0, 0);
		verify(backLeft).set(-1.0);
	}
	
	@Test
	public void testMoveDrivesBackRightMotorGivenPositiveX() {
		drive.move(1, 0, 0);
		verify(backRight).set(1.0);
	}
	
	@Test
	public void testMoveDrivesFrontLeftMotorGivenNegativeX() {
		drive.move(-1, 0, 0);
		verify(frontLeft).set(-1.0);
	}
	
	@Test
	public void testMoveDrivesFrontRightMotorGivenNegativeX() {
		drive.move(-1, 0,  0);
		verify(frontRight).set(1.0);
	}
	
	@Test
	public void testMoveDrivesBackLeftMotorGivenNegativeX() {
		drive.move(-1, 0, 0);
		verify(backLeft).set(1.0);
	}
	
	@Test
	public void testMoveDrivesBackRightMotorGivenNegativeX() {
		drive.move(-1, 0, 0);
		verify(backLeft).set(1.0);
	}
	
	@Test
	public void testMoveDrivesFrontLeftMotorGivenPositiveY() {
		drive.move(0, 1, 0);
		verify(frontLeft).set(1.0);
	}
	
	@Test
	public void testMoveDrivesFrontRightMotorGivenPositiveY() {
		drive.move(0, 1, 0);
		verify(frontRight).set(1.0);
	}
	
	@Test
	public void testMoveDrivesBackLeftMotorGivenPositiveY() {
		drive.move(0, 1, 0);
		verify(backLeft).set(1.0);
	}
	
	@Test
	public void testMoveDrivesBackRightMotorGivenPositiveY() {
		drive.move(0, 1, 0);
		verify(backRight).set(1.0);
	}
	
	@Test
	public void testMoveDrivesFrontLeftMotorGivenNegativeY() {
		drive.move(0, -1,  0);
		verify(frontLeft).set(-1.0);
	}
	
	@Test
	public void testMoveDrivesFrontRightMotorGivenNegativeY() {
		drive.move(0, -1, 0);
		verify(frontRight).set(-1.0);
	}
	
	@Test
	public void testMoveDrivesBackLeftMotorGivenNegativeY() {
		drive.move(0, -1, 0);
		verify(backLeft).set(-1.0);
	}
	
	@Test
	public void testMoveDrivesBackRightMotorGivenNegativeY() {
		drive.move(0, -1, 0);
		verify(backRight).set(-1.0);
	}
	
	@Test
	public void testMoveDrivesFrontLeftMotorGivenPositiveRotation() {
		drive.move(0, 0, 1);
		verify(frontLeft).set(1.0);
	}
	
	@Test
	public void testMoveDrivesFrontRightMotorGivenPositiveRotation() {
		drive.move(0, 0, 1);
		verify(frontRight).set(-1.0);
	}
	
	@Test
	public void testMoveDrivesBackLeftMotorGivenPositiveRotation() {
		drive.move(0, 0, 1);
		verify(backLeft).set(1.0);
	}
	
	@Test
	public void testMoveDrivesBackRightMotorGivenPositiveRotation() {
		drive.move(0, 0, 1);
		verify(backRight).set(-1.0);
	}
	
	@Test
	public void testMoveDrivesFrontLeftMotorGivenNegativeRotation() {
		drive.move(0, 0, -1);
		verify(frontLeft).set(-1);
	}
	
	@Test
	public void testMoveDrivesFrontRightMotorGivenNegativeRotation() {
		drive.move(0, 0, -1);
		verify(frontRight).set(1);
	}
	
	@Test
	public void testMoveDrivesBackLeftMotorGivenNegativeRotation() {
		drive.move(0, 0, -1);
		verify(backLeft).set(-1);
	}
	
	@Test
	public void testMoveDrivesBackRightMotorGivenNegativeRotation() {
		drive.move(0, 0, -1);
		verify(backRight).set(1);
	}
}
