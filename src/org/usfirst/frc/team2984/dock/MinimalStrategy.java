package org.usfirst.frc.team2984.dock;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team2984.robot.commands.DriveForward;

import edu.wpi.first.wpilibj.command.Command;

public class MinimalStrategy implements DockingStrategy {

	@Override
	public List<Command> dock(VisionTarget target) {
		List<Command> result = new ArrayList<Command>();
		DriveForward forward = new DriveForward();
		
		result.add(forward);
		
		return result;
	}
	
}
