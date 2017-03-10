package org.usfirst.frc.team2984.subsystems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.subsystems.Gyroscope;
import org.usfirst.frc.team2984.robot.util.Motion;
import org.usfirst.frc.team2984.util.DummyReporter;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.HLUsageReporting;

public class DriveTrainTest {
	private DriveTrain drive;
	private CANTalon frontLeft;
	private CANTalon frontRight;
	private CANTalon backLeft;
	private CANTalon backRight;
	private Gyroscope gyroscope;
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
		gyroscope = mock(Gyroscope.class);
		drive = new DriveTrain(frontLeft, frontRight, backLeft, backRight, gyroscope);
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

		assertTrue(drive.isThere(4, 0, 0));
	}
	
	@Test
	public void isDoneReturnsTrueWhileMovingSlowlyBackward() {
		when(frontLeft.getEncPosition()).thenReturn(-1);
		when(frontRight.getEncPosition()).thenReturn(1);
		when(backLeft.getEncPosition()).thenReturn(-1);
		when(backRight.getEncPosition()).thenReturn(1);

		assertTrue(drive.isThere(4, 0, 0));
	}
	
	@Test
	public void isDoneReturnsTrueWhileMovingSlowlyLeft() {
		when(frontLeft.getEncPosition()).thenReturn(-1);
		when(frontRight.getEncPosition()).thenReturn(-1);
		when(backLeft.getEncPosition()).thenReturn(1);
		when(backRight.getEncPosition()).thenReturn(1);

		assertTrue(drive.isThere(4, 0, 0));
	}
	
	@Test
	public void isDoneReturnsTrueWhileMovingSlowlyRight() {
		when(frontLeft.getEncPosition()).thenReturn(1);
		when(frontRight.getEncPosition()).thenReturn(1);
		when(backLeft.getEncPosition()).thenReturn(-1);
		when(backRight.getEncPosition()).thenReturn(-1);

		assertTrue(drive.isThere(4, 0, 0));
	}
	
	@Test
	public void isDoneReturnsFalseWhileMovingQuicklyForward() {
		when(frontLeft.getEncVelocity()).thenReturn(100);
		when(frontRight.getEncVelocity()).thenReturn(-100);
		when(backLeft.getEncVelocity()).thenReturn(100);
		when(backRight.getEncVelocity()).thenReturn(-100);

		assertFalse(drive.isThere(4, 0, 0));
	}
	
	@Test
	public void isDoneReturnsFalseWhileMovingQuicklyBackward() {
		when(frontLeft.getEncVelocity()).thenReturn(-100);
		when(frontRight.getEncVelocity()).thenReturn(100);
		when(backLeft.getEncVelocity()).thenReturn(-100);
		when(backRight.getEncVelocity()).thenReturn(100);

		assertFalse(drive.isThere(4, 0, 0));
	}
	
	@Test
	public void isDoneReturnsFalseWhileMovingQuicklyLeft() {
		when(frontLeft.getEncVelocity()).thenReturn(-100);
		when(frontRight.getEncVelocity()).thenReturn(-100);
		when(backLeft.getEncVelocity()).thenReturn(100);
		when(backRight.getEncVelocity()).thenReturn(100);

		assertFalse(drive.isThere(4, 0, 0));
	}
	
	@Test
	public void isDoneReturnsFalseWhileMovingQuicklyRight() {
		when(frontLeft.getEncVelocity()).thenReturn(100);
		when(frontRight.getEncVelocity()).thenReturn(100);
		when(backLeft.getEncVelocity()).thenReturn(-100);
		when(backRight.getEncVelocity()).thenReturn(-100);

		assertFalse(drive.isThere(4, 0, 0));
	}
	
	@Test
	public void isDoneReturnsFalseWhileMovingDiagionaly() {
		when(frontLeft.getEncVelocity()).thenReturn(0);
		when(frontRight.getEncVelocity()).thenReturn(100);
		when(backLeft.getEncVelocity()).thenReturn(100);
		when(backRight.getEncVelocity()).thenReturn(0);

		assertFalse(drive.isThere(4, 0, 0));
	}
	
	@Test
	public void isDoneReturnsTrueWhileMovingSlowlyDiagionaly() {
		when(frontLeft.getEncVelocity()).thenReturn(0);
		when(frontRight.getEncVelocity()).thenReturn(1);
		when(backLeft.getEncVelocity()).thenReturn(1);
		when(backRight.getEncVelocity()).thenReturn(0);

		assertTrue(drive.isThere(4, 0, 0));
	}
	
	@Test
	public void isDoneReturnsFalseWhileMovingSlowlyAtZeroDisplacement() {
		when(frontLeft.getEncVelocity()).thenReturn(0);
		when(frontRight.getEncVelocity()).thenReturn(0);
		when(backLeft.getEncVelocity()).thenReturn(0);
		when(backRight.getEncVelocity()).thenReturn(0);

		assertFalse(drive.isThere(4, 0, 10));
	}
	
	@Test
	public void movesForwardSlowlyWhenGyroAngleIsZeroRobotAngleIsZeroAndRotationIsZero() {
		when(gyroscope.getAngle()).thenReturn(0D);
		drive.moveAtAngle(0, 0.3, 0);

		verifyTalons(0.3, 0.3, 0.3, 0.3);
	}
	
	@Test
	public void movesForwardQuicklyWhenGyroAngleIsZeroRobotAngleIsZeroAndRotationIsZero() {
		when(gyroscope.getAngle()).thenReturn(0D);
		drive.moveAtAngle(0, 1, 0);

		verifyTalons(1, 1, 1, 1);
	}
	
	
	@Test
	public void movesForwardSlowlyWhenGyroAngleIsNintyRobotAngleIsNintyAndRotationIsZero() {
		when(gyroscope.getAngle()).thenReturn(90D);
		drive.moveAtAngle(90, 0.3, 0);

		verifyTalons(0.3, 0.3, 0.3, 0.3);
	}
	
	@Test
	public void movesForwardQuicklyAndRotatesWhenGyroAngleIsNintyRobotAngleIsNintyAndRotationIsNinty() {
		when(gyroscope.getAngle()).thenReturn(90D);
		drive.moveAtAngle(90, 1, 1);

		verifyTalons(1, 0, 1, 0);
	}
	
	@Test
	public void movesRightSlowlyWhenGyroAngleIsZeroRobotAngleIsNintyAndRotationIsZero() {
		when(gyroscope.getAngle()).thenReturn(0D);
		drive.moveAtAngle(90, 0.3, 0);

		verifyTalons(0.3, -0.3, -0.3, 0.3);
	}
	
	@Test
	public void movesRightQuicklyWhenGyroAngleIsZeroRobotAngleIsNintyAndRotationIsZero() {
		when(gyroscope.getAngle()).thenReturn(0D);
		drive.moveAtAngle(90, 1, 0);

		verifyTalons(1, -1, -1, 1);
	}
	
	@Test
	public void rotatesClockwiesWhenGyroAngleIsZeroRobotAngleIsZeroAndRotationIsNinty() {
		when(gyroscope.getAngle()).thenReturn(0D);
		drive.moveAtAngle(0, 0, 90);

		verifyTalons(1, -1, 1, -1);
	}
	
	@Test
	public void rotatesClockwiesWhenGyroAngleIsNintyRobotAngleIsZeroAndRotationIsNinty() {
		when(gyroscope.getAngle()).thenReturn(90D);
		drive.moveAtAngle(0, 0, 90);

		verifyTalons(1, -1, 1, -1);
	}
	
	@Test
	public void doesNotMoveWhenGyroAngleIsThreeHunderedRobotAngleIsZeroAndRotationIsZero() {
		when(gyroscope.getAngle()).thenReturn(300D);
		drive.moveAtAngle(0, 0, 0);

		verifyTalons(0, 0, 0, 0);
	}
	
	@Test
	public void returnsZeroYDisplacementWhenEncodersAreAllZero(){
		when(frontLeft.getEncPosition()).thenReturn(0);
		when(frontRight.getEncPosition()).thenReturn(0);
		when(backLeft.getEncPosition()).thenReturn(0);
		when(backRight.getEncPosition()).thenReturn(0);

		assertEquals(0, drive.getDisplacementY(), 0.00001);
	}
	
	@Test
	public void returnsOneYDisplacementWhenEncodersAreAllPositiveOneInch(){
		int ticksY = (int) (RobotMap.DRIVE_TRAIN_TICK_TO_INCH_FORWARD);
		when(frontLeft.getEncPosition()).thenReturn(ticksY);
		when(frontRight.getEncPosition()).thenReturn(ticksY);
		when(backLeft.getEncPosition()).thenReturn(ticksY);
		when(backRight.getEncPosition()).thenReturn(ticksY);

		assertEquals(1, drive.getDisplacementY(), 0.00001);
	}
	
	@Test
	public void returnsOneHalfYDisplacementWhenHalfTheEncodersArePositiveOneInch(){
		int ticksY = (int) (RobotMap.DRIVE_TRAIN_TICK_TO_INCH_FORWARD);
		when(frontLeft.getEncPosition()).thenReturn(ticksY);
		when(frontRight.getEncPosition()).thenReturn(0);
		when(backLeft.getEncPosition()).thenReturn(0);
		when(backRight.getEncPosition()).thenReturn(ticksY);

		assertEquals(0.5, drive.getDisplacementY(), 0.00001);
	}
	
	@Test
	public void returnsThreeFourthsYDisplacementWhenHalfTheEncodersArePositiveOneInchAndTheOtherHalfAnInch(){
		int ticksY = (int) (RobotMap.DRIVE_TRAIN_TICK_TO_INCH_FORWARD);
		when(frontLeft.getEncPosition()).thenReturn(ticksY);
		when(frontRight.getEncPosition()).thenReturn(ticksY/2);
		when(backLeft.getEncPosition()).thenReturn(ticksY/2);
		when(backRight.getEncPosition()).thenReturn(ticksY);

		assertEquals(0.75, drive.getDisplacementY(), 0.0003);
	}
	
	@Test
	public void returnsZeroYDisplacementWhenTranslatedRight(){
		int ticksY = (int) (RobotMap.DRIVE_TRAIN_TICK_TO_INCH_FORWARD);
		when(frontLeft.getEncPosition()).thenReturn(ticksY);
		when(frontRight.getEncPosition()).thenReturn(-ticksY);
		when(backLeft.getEncPosition()).thenReturn(-ticksY);
		when(backRight.getEncPosition()).thenReturn(ticksY);

		assertEquals(0, drive.getDisplacementY(), 0.00001);
	}
	
	@Test
	public void returnsOneFourthYDisplacementWhenTranslatedRightAndUp(){
		int ticksY = (int) (RobotMap.DRIVE_TRAIN_TICK_TO_INCH_FORWARD);
		when(frontLeft.getEncPosition()).thenReturn(ticksY);
		when(frontRight.getEncPosition()).thenReturn(-ticksY/2);
		when(backLeft.getEncPosition()).thenReturn(-ticksY/2);
		when(backRight.getEncPosition()).thenReturn(ticksY);

		assertEquals(0.25, drive.getDisplacementY(), 0.0003);
	}
	
	@Test
	public void returnsZeroXDisplacementWhenEncodersAreAllZero(){
		when(frontLeft.getEncPosition()).thenReturn(0);
		when(frontRight.getEncPosition()).thenReturn(0);
		when(backLeft.getEncPosition()).thenReturn(0);
		when(backRight.getEncPosition()).thenReturn(0);

		assertEquals(0, drive.getDisplacementX(), 0.00001);
	}
	
	@Test
	public void returnsZeroXDisplacementWhenEncodersAreAllPositiveOneInch(){
		int ticksY = (int) (RobotMap.DRIVE_TRAIN_TICK_TO_INCH_FORWARD);
		when(frontLeft.getEncPosition()).thenReturn(ticksY);
		when(frontRight.getEncPosition()).thenReturn(ticksY);
		when(backLeft.getEncPosition()).thenReturn(ticksY);
		when(backRight.getEncPosition()).thenReturn(ticksY);

		assertEquals(0, drive.getDisplacementX(), 0.00001);
	}
	
	@Test
	public void returnsOneHalfXDisplacementWhenHalfTheEncodersArePositiveOneInch(){
		int ticksY = (int) (RobotMap.DRIVE_TRAIN_TICK_TO_INCH_FORWARD);
		when(frontLeft.getEncPosition()).thenReturn(ticksY);
		when(frontRight.getEncPosition()).thenReturn(0);
		when(backLeft.getEncPosition()).thenReturn(0);
		when(backRight.getEncPosition()).thenReturn(ticksY);

		assertEquals(0.5, drive.getDisplacementX(), 0.00001);
	}
	
	@Test
	public void returnsOneFourthXDisplacementWhenHalfTheEncodersArePositiveOneInchAndTheOtherHalfAnInch(){
		int ticksY = (int) (RobotMap.DRIVE_TRAIN_TICK_TO_INCH_FORWARD);
		when(frontLeft.getEncPosition()).thenReturn(ticksY);
		when(frontRight.getEncPosition()).thenReturn(ticksY/2);
		when(backLeft.getEncPosition()).thenReturn(ticksY/2);
		when(backRight.getEncPosition()).thenReturn(ticksY);

		assertEquals(0.25, drive.getDisplacementX(), 0.0003);
	}
	
	@Test
	public void returnsOneXDisplacementWhenTranslatedRight(){
		int ticksY = (int) (RobotMap.DRIVE_TRAIN_TICK_TO_INCH_FORWARD);
		when(frontLeft.getEncPosition()).thenReturn(ticksY);
		when(frontRight.getEncPosition()).thenReturn(-ticksY);
		when(backLeft.getEncPosition()).thenReturn(-ticksY);
		when(backRight.getEncPosition()).thenReturn(ticksY);

		assertEquals(1, drive.getDisplacementX(), 0.00001);
	}
	
	@Test
	public void returnsThreeFourthXDisplacementWhenTranslatedRightAndUp(){
		int ticksY = (int) (RobotMap.DRIVE_TRAIN_TICK_TO_INCH_FORWARD);
		when(frontLeft.getEncPosition()).thenReturn(ticksY);
		when(frontRight.getEncPosition()).thenReturn(-ticksY/2);
		when(backLeft.getEncPosition()).thenReturn(-ticksY/2);
		when(backRight.getEncPosition()).thenReturn(ticksY);

		assertEquals(0.75, drive.getDisplacementX(), 0.0003);
	}
	
	private void verifyTalons(double frontLeft, double frontRight, double backLeft, double backRight) {
		final ArgumentCaptor<Double> captor = ArgumentCaptor.forClass(Double.class);
		verify(this.frontLeft).set(captor.capture());
		assertEquals(frontLeft * speed, (double)captor.getValue(), 0.00001);
		verify(this.frontRight).set(captor.capture());
		assertEquals(frontRight * speed, (double)captor.getValue(), 0.00001);
		verify(this.backLeft).set(captor.capture());
		assertEquals(backLeft * speed, (double)captor.getValue(), 0.00001);
		verify(this.backRight).set(captor.capture());
		assertEquals(backRight * speed, (double)captor.getValue(), 0.00001);
	}
	
}
