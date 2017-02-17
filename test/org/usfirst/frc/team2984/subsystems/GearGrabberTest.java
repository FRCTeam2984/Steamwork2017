package org.usfirst.frc.team2984.subsystems;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
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
		verify(talon).set(-0.2);
	}
	
	@Test
	public void testCloseDrivesTalon() {
		grabber.close();
		verify(talon).set(-0.2);
	}
	
	@Test
	public void testOpenDrivesTalon() {
		grabber.open();
		verify(talon).set(0.2);
	}
}
