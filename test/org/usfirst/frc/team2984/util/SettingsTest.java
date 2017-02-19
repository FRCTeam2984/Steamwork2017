package org.usfirst.frc.team2984.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team2984.robot.util.Motion;
import org.usfirst.frc.team2984.robot.util.RemoteJoystick;
import org.usfirst.frc.team2984.robot.util.Settings;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class SettingsTest {
	private Settings settings;
	
	@Before
	public void before() {
		settings = Settings.getInstance();
	}
	
	// TODO: test that getMotion reports error. This throws JNI error currently.
	// It's not possible to mock static methods with Mockito, but it can be done with
	// PowerMock.
	
	@Test
	public void settingsReturnsAValue() {
		
		
		assertEquals(0.12, settings.getDouble("SpeedF"), 0.01);
	}
	
}
