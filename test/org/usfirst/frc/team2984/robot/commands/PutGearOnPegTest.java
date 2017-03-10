package org.usfirst.frc.team2984.robot.commands;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.subsystems.Gyroscope;
import org.usfirst.frc.team2984.robot.util.Motion;
import org.usfirst.frc.team2984.util.DummyReporter;

import edu.wpi.first.wpilibj.HLUsageReporting;

public class PutGearOnPegTest {
	private PutGearOnPeg command;
	
	private Gyroscope gyro;
	private DriveTrain driveTrain;
	
	@Before
	public void before() {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
		
		gyro = mock(Gyroscope.class);
		driveTrain = mock(DriveTrain.class);
		command = new PutGearOnPeg(gyro, driveTrain);
		RobotMap.pegAngle = 180;
	}
	
	@Test
	public void testMotionGivenFarAwayAndCenteredAtZeroDisplacement() {
		when(gyro.getAngle()).thenReturn(0D);
		when(driveTrain.getDisplacementX()).thenReturn(0D);
		when(driveTrain.getDisplacementY()).thenReturn(0D);
		when(driveTrain.isThereAtAll(100)).thenReturn(false);
		
		command.execute();
		assertMotion(new Motion(0, RobotMap.GEAR_DROPOFF_SPEED, 0));
	}
	
	@Ignore //For this test was when we still moved sideways
	@Test
	public void testMotionGivenFarAwayAndCenteredAtOneDisplacement() {
		when(gyro.getAngle()).thenReturn(0D);
		when(driveTrain.getDisplacementX()).thenReturn(0D);
		when(driveTrain.getDisplacementY()).thenReturn(1/RobotMap.ROBOT_PROPORTINAL_UNDULATING_FACTOR);
		when(driveTrain.isThereAtAll(100)).thenReturn(false);

		command.execute();
		assertMotion(new Motion(RobotMap.PEG_DROPOFF_OCCILATION_P*RobotMap.UNDULATING_AMPLITUDED, RobotMap.GEAR_DROPOFF_SPEED, 0));
	}
	
	@Ignore //For this test was when we still moved sideways
	@Test
	public void testMotionGivenFarAwayAndCenteredAtThreeDisplacement() {
		when(gyro.getAngle()).thenReturn(0D);
		when(driveTrain.getDisplacementX()).thenReturn(0D);
		when(driveTrain.getDisplacementY()).thenReturn(3/RobotMap.ROBOT_PROPORTINAL_UNDULATING_FACTOR);
		when(driveTrain.isThereAtAll(100)).thenReturn(false);

		command.execute();
		assertMotion(new Motion(-RobotMap.PEG_DROPOFF_OCCILATION_P*RobotMap.UNDULATING_AMPLITUDED, RobotMap.GEAR_DROPOFF_SPEED, 0));
	}
	
	@Test
	public void testMotionGivenFarAwayAndAtPositiveMaxAtOneDisplacement() {
		when(gyro.getAngle()).thenReturn(0D);
		when(driveTrain.getDisplacementX()).thenReturn(RobotMap.UNDULATING_AMPLITUDED);
		when(driveTrain.getDisplacementY()).thenReturn(1/RobotMap.ROBOT_PROPORTINAL_UNDULATING_FACTOR);
		when(driveTrain.isThereAtAll(100)).thenReturn(false);
		
		command.execute();
		assertMotion(new Motion(0, RobotMap.GEAR_DROPOFF_SPEED, 0));
	}
	
	@Test
	public void testMotionGivenFarAwayAndAtNegativeMaxAtThreeDisplacement() {
		when(gyro.getAngle()).thenReturn(0D);
		when(driveTrain.getDisplacementX()).thenReturn(-RobotMap.UNDULATING_AMPLITUDED);
		when(driveTrain.getDisplacementY()).thenReturn(3/RobotMap.ROBOT_PROPORTINAL_UNDULATING_FACTOR);
		when(driveTrain.isThereAtAll(100)).thenReturn(false);
		
		command.execute();
		assertMotion(new Motion(0, RobotMap.GEAR_DROPOFF_SPEED, 0));
	}
	
	@Test
	public void testMotionGivenFarAwayAtTwoDegreseAndAtPositiveMaxAtOneDisplacement() {
		when(gyro.getAngle()).thenReturn(2D);
		when(driveTrain.getDisplacementX()).thenReturn(RobotMap.UNDULATING_AMPLITUDED);
		when(driveTrain.getDisplacementY()).thenReturn(1/RobotMap.ROBOT_PROPORTINAL_UNDULATING_FACTOR);
		when(driveTrain.isThereAtAll(100)).thenReturn(false);
		
		command.execute();
		assertMotion(new Motion(0, RobotMap.GEAR_DROPOFF_SPEED, -2*RobotMap.PEG_DROPOFF_ROTATION_P));
	}
	
	@Test
	public void testMotionGivenFarAwayAtLargeDegreseAndAtPositiveMaxAtOneDisplacement() {
		when(gyro.getAngle()).thenReturn(1/RobotMap.PEG_DROPOFF_ROTATION_P + 1);
		when(driveTrain.getDisplacementX()).thenReturn(RobotMap.UNDULATING_AMPLITUDED);
		when(driveTrain.getDisplacementY()).thenReturn(1/RobotMap.ROBOT_PROPORTINAL_UNDULATING_FACTOR);
		when(driveTrain.isThereAtAll(100)).thenReturn(false);
		
		command.execute();
		assertMotion(new Motion(0, RobotMap.GEAR_DROPOFF_SPEED, -RobotMap.DOCKING_MAX_SPEED));
	}
	
	private void assertMotion(Motion motion){
		ArgumentCaptor<Motion> motionCaptor = ArgumentCaptor.forClass(Motion.class);
		verify(driveTrain).move(motionCaptor.capture());
		assertEquals(motion, motionCaptor.getValue());

	}
	
}