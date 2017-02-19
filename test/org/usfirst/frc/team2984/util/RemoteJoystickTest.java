package org.usfirst.frc.team2984.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team2984.robot.util.Motion;
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
	
	// TODO: test that getMotion reports error. This throws JNI error currently.
	// It's not possible to mock static methods with Mockito, but it can be done with
	// PowerMock.
	
	@Test
	public void getMotionReturnsMotionGivenConnected() {
		Motion expected = new Motion(1.23, 4.56, 7.89);
		
		when(table.isConnected()).thenReturn(true);
		when(table.getNumber("axis2", 0)).thenReturn(expected.getX());
		when(table.getNumber("axis1", 0)).thenReturn(expected.getY());
		when(table.getNumber("yaw", 0)).thenReturn(expected.getRotation());
		
		assertEquals(expected, joystick.getMotion());
	}
	
	@Test
	public void getPIDValuesReturnsPIDValuesGivenConnected() {
		double[] expected = new double[]{1.1,2.2,3.3,4.4,5.5,6,7.7,8.0};
		
		when(table.isConnected()).thenReturn(true);
		when(table.getString("Encoder Locations", "")).thenReturn("1.1,2.2,3.3,4.4,5.5,6,7.7,8.0,");
		
		double[] actual = joystick.getPIDValues();
		
		assertEquals(expected.length, actual.length);
		for(int i = 0; i<8; i++){
			assertEquals(expected[i], actual[i], 0.001);

		}
	}
	
	@Test
	public void getPIDValuesReturnsNullGivenTooFewArguments() {
		when(table.isConnected()).thenReturn(true);
		when(table.getString("Encoder Locations", "")).thenReturn("1.1,3.3,4.4,5.5,6,7.7,8.0");
		
		assertNull(joystick.getPIDValues());
	}
}
