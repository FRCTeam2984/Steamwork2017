package org.usfirst.frc.team2984.commands;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.commands.AlignToThePeg;
import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.util.Motion;
import org.usfirst.frc.team2984.robot.util.VisionTracker;
import org.usfirst.frc.team2984.util.DummyReporter;

import edu.wpi.first.wpilibj.HLUsageReporting;

public class AlignToThePegTest {
	private AlignToThePeg command;
	
	private VisionTracker tracker;
	private DriveTrain driveTrain;
	
	@Before
	public void before() {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
		
		tracker = mock(VisionTracker.class);
		driveTrain = mock(DriveTrain.class);
		command = new AlignToThePeg(tracker, driveTrain);
	}
	
	@Test
	public void testMotionGivenSteightOnAndFarAway() {
		when(tracker.hasTrack()).thenReturn(true);
		when(tracker.getDistance()).thenReturn(100D);
		when(tracker.getAngle()).thenReturn(0D);
		when(tracker.robotAngle()).thenReturn(0D);
		
		Motion compare = new Motion(0, 0.5, 0);
		command.execute();
		ArgumentCaptor<Motion> argument = ArgumentCaptor.forClass(Motion.class);
		verify(driveTrain).move(argument.capture());;
		assertEquals(compare, argument.getValue());
	}
	
	@Test
	public void testMotionGivenSteightOnAndTooClose() {
		when(tracker.hasTrack()).thenReturn(true);
		when(tracker.getDistance()).thenReturn(RobotMap.DOCKING_DISTANCE_THRESHOLD - 1);
		when(tracker.getAngle()).thenReturn(0D);
		when(tracker.robotAngle()).thenReturn(0D);
		Motion compare = new Motion(0, 0, 0);
		command.execute();
		ArgumentCaptor<Motion> argument = ArgumentCaptor.forClass(Motion.class);
		verify(driveTrain).move(argument.capture());;
		assertEquals(compare, argument.getValue());
	}
	
}