package org.usfirst.frc.team2984.commands;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.commands.AlignToThePeg;
import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.subsystems.Gyroscope;
import org.usfirst.frc.team2984.robot.util.Motion;
import org.usfirst.frc.team2984.robot.util.Peg;
import org.usfirst.frc.team2984.robot.util.VisionTarget;
import org.usfirst.frc.team2984.robot.util.VisionTracker;
import org.usfirst.frc.team2984.util.DummyReporter;

import edu.wpi.first.wpilibj.HLUsageReporting;

public class AlignToThePegTest {
	private AlignToThePeg command;
	
	private VisionTracker tracker;
	private DriveTrain driveTrain;
	private Gyroscope gyro;
	
	@Before
	public void before() {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
		
		tracker = mock(VisionTracker.class);
		driveTrain = mock(DriveTrain.class);
		gyro = mock(Gyroscope.class);
		command = new AlignToThePeg(tracker, driveTrain, gyro);
	}
	
	@Test
	public void testMotionGivenThityDegreeseOffSteightOnAndFarAway() {
		when(tracker.hasTrack()).thenReturn(true);
		when(tracker.getTarget()).thenReturn(new VisionTarget(0,0,10));
		when(gyro.getAngle()).thenReturn(-30D);
		
		command.execute();
		assertMotionAtAngle(30, 1, 0);
	}
	
	@Test
	public void testMotionGivenSteightOnAndFarAway() {
		when(tracker.hasTrack()).thenReturn(true);
		when(tracker.getTarget()).thenReturn(new VisionTarget(0,0,10));
		when(gyro.getAngle()).thenReturn(-60D);
		
		command.execute();
		assertMotionAtAngle(-60, 1, 0);
	}
	
	@Test
	public void testMotionGivenSteightOnAndTooClose() {
		when(tracker.hasTrack()).thenReturn(true);
		when(tracker.getTarget()).thenReturn(new VisionTarget(0,0,200));
		when(gyro.getAngle()).thenReturn(-60D);
		
		command.execute();
		assertMotionAtAngle(0, 0, 0);
	}
	
	@Test
	public void testMotionGivenThityDegreeseOffAndTooClose() {
		when(tracker.hasTrack()).thenReturn(true);
		when(tracker.getTarget()).thenReturn(new VisionTarget(0,0,200));
		when(gyro.getAngle()).thenReturn(-30D);
		
		command.execute();
		assertMotionAtAngle(30, 1, 0);
	}
	
	@Test
	public void testMotionGivenThityDegreeseOffOverAndTooClose() {
		when(tracker.hasTrack()).thenReturn(true);
		when(tracker.getTarget()).thenReturn(new VisionTarget(0,0,200));
		when(gyro.getAngle()).thenReturn(-90D);
		
		command.execute();
		assertMotionAtAngle(30, -1, 0);
	}
	
	@Test
	public void testMotionGivenThityDegreeseOffOver() {
		when(tracker.hasTrack()).thenReturn(true);
		when(tracker.getTarget()).thenReturn(new VisionTarget(-100,0,20));
		when(gyro.getAngle()).thenReturn(-90D);
		
		command.execute();
		assertMotionAtAngle(30, -1, -0.875);
	}
	
	@Test
	public void testMotionGivenTwentyDegreeseOffUnder() {
		when(tracker.hasTrack()).thenReturn(true);
		when(tracker.getTarget()).thenReturn(new VisionTarget(100,0,20));
		when(gyro.getAngle()).thenReturn(-40D);
		
		command.execute();
		assertMotionAtAngle(30, 1, 0.875);
	}
	
	@Test
	public void testMotionGivenTwoDegreeseOffUnder() {
		when(tracker.hasTrack()).thenReturn(true);
		when(tracker.getTarget()).thenReturn(new VisionTarget(100,0,20));
		when(gyro.getAngle()).thenReturn(-83+RobotMap.DOCKING_YAW_THRESHOLD);
		
		command.execute();
		assertMotionAtAngle(-60, 1, 0.875);
	}
	
	private void assertMotionAtAngle(double angle, double speed, double rotation){
		ArgumentCaptor<Double> angleCaptor = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Double> speedCaptor = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Double> rotationCaptor = ArgumentCaptor.forClass(Double.class);
		verify(driveTrain).moveAtAngle(angleCaptor.capture(), speedCaptor.capture(), rotationCaptor.capture());
		assertEquals(angle, angleCaptor.getValue(), 0.00001);
		assertEquals(speed, speedCaptor.getValue(), 0.00001);
		assertEquals(rotation, rotationCaptor.getValue(), 0.00001);

	}
	
}