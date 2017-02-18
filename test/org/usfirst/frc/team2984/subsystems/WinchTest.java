package org.usfirst.frc.team2984.subsystems;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team2984.robot.subsystems.Winch;
import org.usfirst.frc.team2984.util.DummyReporter;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.HLUsageReporting;

public class WinchTest {
	private Winch winch;
	private CANTalon talon;
	
	@Before
	public void before() {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
		
		talon = mock(CANTalon.class);
		winch = new Winch(talon);
	}
	
	@Test
	public void grabSetsTalon() {
		winch.grab();
		verify(talon).set(0.2);
	}

	@Test
	public void climbSetsTalon() {
		winch.climb();
		verify(talon).set(1);
	}
	
	@Test
	public void backDownSetsTalon() {
		winch.backDown();
		verify(talon).set(-1);
	}
}
