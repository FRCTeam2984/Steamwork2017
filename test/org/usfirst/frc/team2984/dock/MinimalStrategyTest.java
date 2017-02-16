package org.usfirst.frc.team2984.dock;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.first.wpilibj.command.Command;

public class MinimalStrategyTest {
	private MinimalStrategy strategy;
	
	@Before
	public void before() {
		strategy = new MinimalStrategy();
	}
	
	@Test
	public void testDockReturnsDriveForwardCommandOnlyGivenHeadOnApproach() {
		VisionTarget target = new VisionTarget(0, 50, 61.8642);
		List<Command> commands = strategy.dock(target);
		
		assertEquals(1, commands.size());
		assertEquals("drive-forward-100", commands.get(0).toString());
	}
}
