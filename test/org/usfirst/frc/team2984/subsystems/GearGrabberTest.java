package org.usfirst.frc.team2984.subsystems;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
		when(talon.getEncPosition()).thenReturn((int) (RobotMap.GEAR_GRABBER_CLOSE));
		verify(talon).set(0.24); //becase ramping has started
	}
	
	@Test
	public void testCloseDrivesTalon() {
		when(talon.getEncPosition()).thenReturn((int) (RobotMap.GEAR_GRABBER_OPEN));
		grabber.close();
		verify(talon).set(0.3);
	}
	
	@Test
	public void testOpenDrivesTalon() {
		grabber.open();
		when(talon.getEncPosition()).thenReturn((int) (RobotMap.GEAR_GRABBER_CLOSE));
		verify(talon).set(-0.5);
	}
	
	@Test
	public void testGearGrabberOpenReturnsTrueWhenOpen() {
		when(talon.getEncPosition()).thenReturn((int) (RobotMap.GEAR_GRABBER_OPEN-80));
		assertTrue(grabber.isOpen(100));
	}
	
	@Test
	public void testGearGrabberOpenReturnsFalseWhenNotOpen() {
		when(talon.getEncPosition()).thenReturn((int) (RobotMap.GEAR_GRABBER_OPEN-120));
		assertFalse(grabber.isOpen(100));
	}
}
