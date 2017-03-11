package org.usfirst.frc.team2984.robot;

import org.usfirst.frc.team2984.robot.commands.AlignToThePeg;
import org.usfirst.frc.team2984.robot.commands.Climb;
import org.usfirst.frc.team2984.robot.commands.GearDelivery;
import org.usfirst.frc.team2984.robot.commands.IncrementDecrement;
import org.usfirst.frc.team2984.robot.commands.OpenGearGrabber;
import org.usfirst.frc.team2984.robot.commands.ReallySlowClimb;
import org.usfirst.frc.team2984.robot.commands.SlowClimb;
import org.usfirst.frc.team2984.robot.commands.TestAutoCommand;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
	
	public Joystick stick = new Joystick(0);
	
	/**
	 * the button that will make the robot drive forward for one second
	 */
	Button reallySlowClimb = new JoystickButton(stick, 1);
	Button climb = new JoystickButton(stick, 2);
	Button slowClimb = new JoystickButton(stick, 3);
	Button openGearGrabber = new JoystickButton(stick, 4);
	Button increment = new JoystickButton(stick, 5);
	Button decrement = new JoystickButton(stick, 7);
	Button alignToPeg = new JoystickButton(stick, 6);
	Button deliver = new JoystickButton(stick, 8);
	Button test = new JoystickButton(stick, 9);

	
	/**
	 * initializes the behaviors for each input
	 */
	public OI(){
//		updatePID.whenPressed(new UpdatePIDsForDriveTrain());
		climb.whileHeld(new Climb());
		slowClimb.whileHeld(new SlowClimb());
		reallySlowClimb.whileHeld(new ReallySlowClimb());
		openGearGrabber.whileHeld(new OpenGearGrabber());
		increment.whenPressed(new IncrementDecrement(0.0005));
		decrement.whenPressed(new IncrementDecrement(-0.0005));
//		driveForward.whenPressed(new DriveDistance(0, 18));
		AlignToThePeg alignToPegCommand = new AlignToThePeg();
		alignToPeg.whileHeld(alignToPegCommand);
		deliver.whileHeld(new GearDelivery());
		test.whenPressed(new TestAutoCommand());
	}
}
