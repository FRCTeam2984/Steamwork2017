package org.usfirst.frc.team2984.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team2984.robot.util.RemoteJoystick;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class RemoteJoystickTest {
	private RemoteJoystick joystick;
	private NetworkTable table;
	
	@Before
	public void before() {
		table = mock(NetworkTable.class);
		joystick = new RemoteJoystick(table);
	}
	
	// TODO: text that getX, getY, getTwist report error, but this throws JNI error currently
	// It's not possible to mock static methods with Mockito, but it can be done with
	// PowerMock.
	
	@Test
	public void getXReturnsXGivenConnected() {
		when(table.isConnected()).thenReturn(true);
		when(table.getNumber("axis2", 0)).thenReturn(1.23);
		assertEquals(1.23, joystick.getX(), 0.01);
	}
	
	@Test
	public void getYReturnsYGivenConnected() {
		when(table.isConnected()).thenReturn(true);
		when(table.getNumber("axis1", 0)).thenReturn(1.23);
		assertEquals(1.23, joystick.getY(), 0.01);
	}
	
	@Test
	public void getTwistReturnsTwistGivenConnected() {
		when(table.isConnected()).thenReturn(true);
		when(table.getNumber("yaw", 0)).thenReturn(1.23);
		assertEquals(1.23, joystick.getTwist(), 0.01);
	}
}
