package org.usfirst.frc.team2984.dock;

import java.util.List;

import edu.wpi.first.wpilibj.command.Command;

public interface DockingStrategy {
	public List<Command> dock(VisionTarget target);
}
