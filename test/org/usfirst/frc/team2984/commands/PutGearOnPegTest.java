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
import org.usfirst.frc.team2984.robot.commands.PutGearOnPeg;
import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.subsystems.WallFinder;
import org.usfirst.frc.team2984.robot.util.Motion;
import org.usfirst.frc.team2984.robot.util.Wall;
import org.usfirst.frc.team2984.util.DummyReporter;

import edu.wpi.first.wpilibj.HLUsageReporting;

public class PutGearOnPegTest {
	private PutGearOnPeg command;
	
	private WallFinder tracker;
	private DriveTrain driveTrain;
	
	@Before
	public void before() {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
		
		tracker = mock(WallFinder.class);
		driveTrain = mock(DriveTrain.class);
		command = new PutGearOnPeg(tracker, driveTrain);
	}
	
	@Test
	public void testMotionGivenFarAwayAndCenteredAtZeroDisplacement() {
		when(tracker.getWall()).thenReturn(new Wall(RobotMap.PEG_DROPOFF_DISTANCE + 10, 0));
		when(driveTrain.getDisplacementX()).thenReturn(0D);
		when(driveTrain.getDisplacementY()).thenReturn(0D);
		
		command.execute();
		assertMotion(new Motion(0, RobotMap.GEAR_DROPOFF_SPEED, 0));
	}
	
	@Test
	public void testMotionGivenFarAwayAndCenteredAtOneDisplacement() {
		when(tracker.getWall()).thenReturn(new Wall(RobotMap.PEG_DROPOFF_DISTANCE + 10, 0));
		when(driveTrain.getDisplacementX()).thenReturn(0D);
		when(driveTrain.getDisplacementY()).thenReturn(1/RobotMap.ROBOT_PROPORTINAL_UNDULATING_FACTOR);
		
		command.execute();
		assertMotion(new Motion(RobotMap.PEG_DROPOFF_OCCILATION_P*RobotMap.UNDULATING_AMPLITUDED, RobotMap.GEAR_DROPOFF_SPEED, 0));
	}
	
	@Test
	public void testMotionGivenFarAwayAndCenteredAtThreeDisplacement() {
		when(tracker.getWall()).thenReturn(new Wall(RobotMap.PEG_DROPOFF_DISTANCE + 10, 0));
		when(driveTrain.getDisplacementX()).thenReturn(0D);
		when(driveTrain.getDisplacementY()).thenReturn(3/RobotMap.ROBOT_PROPORTINAL_UNDULATING_FACTOR);
		
		command.execute();
		assertMotion(new Motion(-RobotMap.PEG_DROPOFF_OCCILATION_P*RobotMap.UNDULATING_AMPLITUDED, RobotMap.GEAR_DROPOFF_SPEED, 0));
	}
	
	@Test
	public void testMotionGivenFarAwayAndAtPositiveMaxAtOneDisplacement() {
		when(tracker.getWall()).thenReturn(new Wall(RobotMap.PEG_DROPOFF_DISTANCE + 10, 0));
		when(driveTrain.getDisplacementX()).thenReturn(RobotMap.UNDULATING_AMPLITUDED);
		when(driveTrain.getDisplacementY()).thenReturn(1/RobotMap.ROBOT_PROPORTINAL_UNDULATING_FACTOR);
		
		command.execute();
		assertMotion(new Motion(0, RobotMap.GEAR_DROPOFF_SPEED, 0));
	}
	
	@Test
	public void testMotionGivenFarAwayAndAtNegativeMaxAtThreeDisplacement() {
		when(tracker.getWall()).thenReturn(new Wall(RobotMap.PEG_DROPOFF_DISTANCE + 10, 0));
		when(driveTrain.getDisplacementX()).thenReturn(-RobotMap.UNDULATING_AMPLITUDED);
		when(driveTrain.getDisplacementY()).thenReturn(3/RobotMap.ROBOT_PROPORTINAL_UNDULATING_FACTOR);
		
		command.execute();
		assertMotion(new Motion(0, RobotMap.GEAR_DROPOFF_SPEED, 0));
	}
	
	@Ignore
	@Test
	public void testMotionGivenFarAwayAtTwoDegreseAndAtPositiveMaxAtOneDisplacement() {
		when(tracker.getWall()).thenReturn(new Wall(RobotMap.PEG_DROPOFF_DISTANCE + 10, 2));
		when(driveTrain.getDisplacementX()).thenReturn(RobotMap.UNDULATING_AMPLITUDED);
		when(driveTrain.getDisplacementY()).thenReturn(1/RobotMap.ROBOT_PROPORTINAL_UNDULATING_FACTOR);
		
		command.execute();
		assertMotion(new Motion(0, RobotMap.GEAR_DROPOFF_SPEED, -2*RobotMap.PEG_DROPOFF_ROTATION_P));
	}
	
	private void assertMotion(Motion motion){
		ArgumentCaptor<Motion> motionCaptor = ArgumentCaptor.forClass(Motion.class);
		verify(driveTrain).move(motionCaptor.capture());
		assertEquals(motion, motionCaptor.getValue());

	}
	
}