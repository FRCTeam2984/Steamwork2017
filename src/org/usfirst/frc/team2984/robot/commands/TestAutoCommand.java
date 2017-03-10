package org.usfirst.frc.team2984.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TestAutoCommand extends CommandGroup {

    public TestAutoCommand() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
//    	addSequential(new DriveDistance(0, 10));
    	addSequential(new Rotate(10, 1000)); //TODO: Add this command
    	addSequential(new AlignToThePeg());
    	addSequential(new OpenGearGrabber());
    	addParallel(new KeepGearGrabberOpen());
//    	addSequential(new DriveDistance(0, -10));
    }
}
