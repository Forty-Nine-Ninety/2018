/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4990.robot;

import org.usfirst.frc.team4990.robot.commands.*;
import org.usfirst.frc.team4990.robot.subsystems.*;

import edu.wpi.first.wpilibj.buttons.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 * @author Class of '21 (created in 2018 season)
 */
public class OI{
	
	public F310Gamepad driveGamepad = RobotMap.driveGamepad;
	Button driveA = new JoystickButton(driveGamepad, 1);
	Button driveB = new JoystickButton(driveGamepad, 2);
	Button driveX = new JoystickButton(driveGamepad, 3);
	Button driveY = new JoystickButton(driveGamepad, 4);
	Button driveBumperLeft = new JoystickButton(driveGamepad, 5);
	Button driveBumperRight = new JoystickButton(driveGamepad, 6);
	Button driveStart = new JoystickButton(driveGamepad, 10);
	Button driveBack = new JoystickButton(driveGamepad, 9);
	Button driveJoystickPressRight = new JoystickButton(driveGamepad, 12);
	Button driveJoystickPressLeft = new JoystickButton(driveGamepad, 11);
	JoystickAnalogButton driveTriggerLeft = new JoystickAnalogButton(driveGamepad, 2);
	JoystickAnalogButton driveTriggerRight = new JoystickAnalogButton(driveGamepad, 3);
	JoystickAnalogButton driveJoystickLeftX = new JoystickAnalogButton(driveGamepad, 0);
	JoystickAnalogButton driveJoystickLeftY = new JoystickAnalogButton(driveGamepad, 1);
	JoystickAnalogButton driveJoystickRightX = new JoystickAnalogButton(driveGamepad, 4);
	JoystickAnalogButton driveJoystickRightY = new JoystickAnalogButton(driveGamepad, 5);
	
	public F310Gamepad opGamepad = RobotMap.opGamepad;
	Button opA = new JoystickButton(opGamepad, 1);
	Button opB = new JoystickButton(opGamepad, 2);
	Button opX = new JoystickButton(opGamepad, 3);
	Button opY = new JoystickButton(opGamepad, 4);
	Button opBumperLeft = new JoystickButton(opGamepad, 5);
	Button opBumperRight = new JoystickButton(opGamepad, 6);
	Button opStart = new JoystickButton(opGamepad, 10);
	Button opBack = new JoystickButton(opGamepad, 9);
	Button opJoystickPressRight = new JoystickButton(opGamepad, 12);
	Button opJoystickPressLeft = new JoystickButton(opGamepad, 11);
	JoystickAnalogButton opTriggerLeft = new JoystickAnalogButton(opGamepad, 2);
	JoystickAnalogButton opTriggerRight = new JoystickAnalogButton(opGamepad, 3);
	JoystickAnalogButton opJoystickLeftX = new JoystickAnalogButton(opGamepad, 0);
	JoystickAnalogButton opJoystickLeftY = new JoystickAnalogButton(opGamepad, 1);
	JoystickAnalogButton opJoystickRightX = new JoystickAnalogButton(opGamepad, 4);
	JoystickAnalogButton opJoystickRightY = new JoystickAnalogButton(opGamepad, 5);
	
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
	
	/*
		Drive Train: (drive controller)
		    Joysticks 1 and 2: forward/backward and turn left/right
		    X button: toggle slow mode
		
		Elevator: (OP controller)
		    RIGHT Joystick up/down: elevator up/down
		    Y button: Move elevator to preset height w/PID system
		
		Intake: (OP controller)
		    LOWER back bumpers/triggers: Left (in/out?), Right (in/out?)
		    X button: override sensor
		
		Scaler: (OP controller)
		    UPPER back button triggers: Left (in/out?), Right (in/out?)
		
		Check which controller is which: (both)
		    START key (RIGHT Middle): prints in console which controller it is being pressed on
	 */
	
	public OI() {
		//joystick config
		driveJoystickLeftY.setThreshold(0.0078125);
		driveJoystickRightX.setThreshold(0.0391);
		driveTriggerLeft.setThreshold(0.95);
		driveTriggerRight.setThreshold(0.95);
		
		opJoystickRightX.setThreshold(0.0391);
		opJoystickLeftY.setThreshold(0.0078125);
		opTriggerLeft.setThreshold(0.95);
		opTriggerRight.setThreshold(0.95);
		
		//intake
		JoystickButtonGroup intakeButtons = new JoystickButtonGroup(opTriggerLeft, opTriggerRight);
		opTriggerLeft.whileHeld(new TeleopIntakeController(TeleopIntakeController.direction.OUT));
		opTriggerRight.whileHeld(new TeleopIntakeController(TeleopIntakeController.direction.IN));
		intakeButtons.cancelWhenActive(new TeleopIntakeController());

		//elevator
		opJoystickRightY.whileHeld(new TeleopElevatorController());
		//opY.whenPressed(new ElevatorPID());
		
		//scaler
		JoystickButtonGroup scalerButtons = new JoystickButtonGroup(opBumperLeft, opBumperRight);
		opBumperLeft.whileHeld(new TeleopScalerController(TeleopScalerController.direction.IN));
		opBumperRight.whileHeld(new TeleopScalerController(TeleopScalerController.direction.OUT));
		scalerButtons.cancelWhenActive(new TeleopScalerController());
		
		//drivetrain
		driveX.whenPressed(new DriveDpiToggle());
		//default command is (standard) joystick drive
		
		//controller check
		driveStart.whileHeld(new ControllerCheck(RobotMap.driveGamepad));
		opStart.whileHeld(new ControllerCheck(RobotMap.opGamepad));
		
		//other
		
	}
	
	/**
	 * see https://gist.github.com/jcorcoran/5743806
	 * @author James
	 */
	
	public class JoystickAnalogButton extends Button {
		
		F310Gamepad m_gamepad;
		int m_axisNumber;
		private double THRESHOLD = 0;

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
		
		/**
		 * Returns boolean value of analog button.
		 * @return boolean value of button
		 */

		public boolean get() {
			if(THRESHOLD < 0){
				return m_gamepad.getRawAxis(m_axisNumber) < THRESHOLD;    //Return true if axis value is less than negative threshold
			} else {
				return m_gamepad.getRawAxis(m_axisNumber) > THRESHOLD;    //Return true if axis value is greater than positive threshold
			}
		}

		}
	
	/**
	 * based on https://gist.github.com/jcorcoran/5743806
	 * @author Benjamin
	 */
	
	public class JoystickButtonGroup extends Button {
		Button[] buttons;

		/**
		 * Create a group of buttons. Must have at least 2 buttons.
		 */
		public JoystickButtonGroup(Trigger... buttons) {
			if (buttons.length < 2) {
				this.free();
			} else {
				this.buttons = (Button[]) buttons;
			}
		}
		
		/**
		 * Create a group of buttons. Must have at least 2 buttons.
		 */
		public JoystickButtonGroup(JoystickAnalogButton... buttons) {
			if (buttons.length < 2) {
				this.free();
			} else {
				this.buttons = buttons;
			}
		}

		/**
		 * Returns boolean value of analog button.
		 * @return boolean value of button
		 */

		public boolean get() {
			int i = 0; 
			while(i < buttons.length) {
				if (!buttons[i].get()) {
					return false;
				}
				i++;
			}
			return true;
		}
	}
	
	
}
