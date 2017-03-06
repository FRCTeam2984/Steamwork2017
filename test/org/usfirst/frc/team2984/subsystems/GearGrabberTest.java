package org.usfirst.frc.team2984.subsystems;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.subsystems.GearGrabber;
import org.usfirst.frc.team2984.util.DummyReporter;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.HLUsageReporting;

public class GearGrabberTest {
	private GearGrabber grabber;
	private CANTalon talon;
	
	@Before
	public void before() {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
		
		talon = mock(CANTalon.class);
		grabber = new GearGrabber(talon);
	}
	
	@Test
	public void testClenchDrivesTalon() {
		grabber.clench();
		verify(talon).set(RobotMap.GEAR_GRABBER_CLOSE - RobotMap.GEAR_GRABBER_DELTA);
	}
	
	@Test
	public void testCloseDrivesTalon() {
		grabber.close();
		verify(talon).set(RobotMap.GEAR_GRABBER_CLOSE);
	}
	
	@Test
	public void testOpenDrivesTalon() {
		grabber.open();
		verify(talon).set(RobotMap.GEAR_GRABBER_OPEN);
	}
}
