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
import org.usfirst.frc.team2984.robot.util.Peg;
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
		when(tracker.getPeg()).thenReturn(new Peg(0,0,50,0,0,0));
		
		Motion compare = new Motion(0, 0.5, 0);
		command.execute();
		ArgumentCaptor<Motion> argument = ArgumentCaptor.forClass(Motion.class);
		verify(driveTrain).move(argument.capture());;
		assertEquals(compare, argument.getValue());
	}
	
	@Test
	public void testMotionGivenSteightOnAndTooClose() {
		when(tracker.hasTrack()).thenReturn(true);
		when(tracker.getPeg()).thenReturn(new Peg(0,0,RobotMap.DOCKING_DISTANCE_THRESHOLD-1,0,0,0));
		Motion compare = new Motion(0, 0, 0);
		command.execute();
		ArgumentCaptor<Motion> argument = ArgumentCaptor.forClass(Motion.class);
		verify(driveTrain).move(argument.capture());;
		assertEquals(compare, argument.getValue());
	}
	
	@Test
	public void testMotionGiven15DegreeAngleAndFarAway() {
		when(tracker.hasTrack()).thenReturn(true);
		when(tracker.getPeg()).thenReturn(new Peg(32.784983332,0,126.67144846034347,0,0,0));
		Motion compare = new Motion(0, 0.5, 0.5);
		command.execute();
		ArgumentCaptor<Motion> argument = ArgumentCaptor.forClass(Motion.class);
		verify(driveTrain).move(argument.capture());;
		assertEquals(compare, argument.getValue());
	}
	
	@Test
	public void testMotionGiven15DegreeAngleAndClose() {
		when(tracker.hasTrack()).thenReturn(true);
		when(tracker.getPeg()).thenReturn(new Peg(13.516963489,0,52.22553650759857,0,0,0));
		Motion compare = new Motion(0, 0.5, 0.5);
		command.execute();
		ArgumentCaptor<Motion> argument = ArgumentCaptor.forClass(Motion.class);
		verify(driveTrain).move(argument.capture());;
		assertEquals(compare, argument.getValue());
	}
	
	
	
}