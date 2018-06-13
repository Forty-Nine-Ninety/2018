/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4990.robot;

import org.usfirst.frc.team4990.robot.subsystems.F310Gamepad;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	
	public F310Gamepad driveGamepad = Robot.driveGamepad;
	Button driveJoystickRight = new JoystickButton(driveGamepad, 12);
	Button driveJoystickLeft = new JoystickButton(driveGamepad, 11);
	Button driveA = new JoystickButton(driveGamepad, 1);
	Button driveB = new JoystickButton(driveGamepad, 2);
	Button driveX = new JoystickButton(driveGamepad, 3);
	Button driveY = new JoystickButton(driveGamepad, 4);
	Button driveBumperLeft = new JoystickButton(driveGamepad, 5);
	Button driveBumperRight = new JoystickButton(driveGamepad, 6);
	Button driveStart = new JoystickButton(driveGamepad, 10);
	Button driveBack = new JoystickButton(driveGamepad, 9);
	
	Button driveTriggerLeft = new JoystickAnalogButton(driveGamepad, 2);
	Button driveTriggerRight = new JoystickAnalogButton(driveGamepad, 3);
	
	public F310Gamepad opGamepad = Robot.opGamepad;
	Button opJoystickRight = new JoystickButton(opGamepad, 12);
	Button opJoystickLeft = new JoystickButton(opGamepad, 11);
	Button opA = new JoystickButton(opGamepad, 1);
	Button opB = new JoystickButton(opGamepad, 2);
	Button opX = new JoystickButton(opGamepad, 3);
	Button opY = new JoystickButton(opGamepad, 4);
	Button opBumperLeft = new JoystickButton(opGamepad, 5);
	Button opBumperRight = new JoystickButton(opGamepad, 6);
	Button opStart = new JoystickButton(opGamepad, 10);
	Button opBack = new JoystickButton(opGamepad, 9);
	
	Button opTriggerLeft = new JoystickAnalogButton(opGamepad, 2);
	Button opeTriggerRight = new JoystickAnalogButton(opGamepad, 3);
	
	/* CREATING BUTTONS
	One type of button is a joystick button which is any button on a
	joystick.
	You create one by telling it which joystick it's on and which button
	number it is.
	Joystick stick = new Joystick(port);
	Button button = new JoystickButton(stick, buttonNumber);

	There are a few additional built in buttons you can use. Additionally,
	by subclassing Button you can create custom triggers and bind those to
	commands the same as any other Button.

	//TRIGGERING COMMANDS WITH BUTTONS
	Once you have a button, it's trivial to bind it to a button in one of
	three ways:

	Start the command when the button is pressed and let it run the command
	until it is finished as determined by it's isFinished method.
	button.whenPressed(new ExampleCommand());

	Run the command while the button is being held down and interrupt it once
	the button is released.
	button.whileHeld(new ExampleCommand());

	Start the command when the button is released and let it run the command
	until it is finished as determined by it's isFinished method.
	button.whenReleased(new ExampleCommand());
	*/
	
	public OI() {
		//drive gamepad
		
	}
	
	public class JoystickAnalogButton extends Button {
		
		F310Gamepad m_gamepad;
		int m_axisNumber;
		private double THRESHOLD = 0.9;

		/**
		 * Create a button for triggering commands off a joystick's analog axis
		 * 
		 * @param joystick The GenericHID object that has the button (e.g. Joystick, KinectStick, etc)
		 * @param axisNumber The axis number
		 */
		public JoystickAnalogButton(F310Gamepad gamepad, int axisNumber) {
			m_gamepad = gamepad;
			m_axisNumber = axisNumber;
		}

		/**
		 * Create a button for triggering commands off a joystick's analog axis
		 * 
		 * @param joystick The GenericHID object that has the button (e.g. Joystick, KinectStick, etc)
		 * @param axisNumber The axis number
		 * @param threshold The threshold to trigger above (positive) or below (negative)
		 */
		public JoystickAnalogButton(F310Gamepad joystick, int axisNumber, double threshold) {
			m_gamepad = joystick;
			m_axisNumber = axisNumber;
			THRESHOLD = threshold;
		}

		/**
		 * Set the value above which triggers should occur (for positive thresholds)
		 *  or below which triggers should occur (for negative thresholds)
		 * The default threshold value is 0.5
		 *  
		 * @param threshold the threshold value (1 to -1)
		 */
		public void setThreshold(double threshold){
			THRESHOLD = threshold;
		}
		 
		/**
		 * Get the defined threshold value.
		 * @return the threshold value
		 */
		public double getThreshold(){
			return THRESHOLD;
		}
		

		public boolean get() {
			if(THRESHOLD < 0){
				return m_gamepad.getRawAxis(m_axisNumber) < THRESHOLD;    //Return true if axis value is less than negative threshold
			} else {
				return m_gamepad.getRawAxis(m_axisNumber) > THRESHOLD;    //Return true if axis value is greater than positive threshold
			}
		}

		}
	
	
}
