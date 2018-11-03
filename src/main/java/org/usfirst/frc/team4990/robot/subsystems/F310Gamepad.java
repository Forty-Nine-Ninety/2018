package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import org.usfirst.frc.team4990.robot.SmartDashboardController;
public class F310Gamepad extends Joystick {
	
	/**
	 * Initializes new F310 Gamepad
	 * @param joystickNumber Port of drive station. (0 to 3?) BASED ON WHEN PLUGGED IN, not specific ports.
	 */
	public F310Gamepad(int joystickNumber) {
		super(joystickNumber);
	}
	
	/* the following methods provide semantic sugar over the raw
	 * axis and button inputs from the F310 Gamepad, as discovered
	 * by using the FRC Driver Station software.
	 */
	
	/**
	 * returns LEFT josystick's X value. 
	 * @return LEFT josystick's X value, between 1 and -1
	 */
	
	public double getLeftJoystickX() {
		return this.getRawAxis(0);
	}
	
	/**
	 * returns LEFT josystick's Y value
	 * @return LEFT josystick's Y value, between 1 and -1
	 */
	
	public double getLeftJoystickY() {
		/*
		 * For some reason, the F310 gamepad's left joystick y axis outputs negative
		 * values for upward joystick movement--not very natural. To make positive 
		 * output values correspond to 'positive," upward movement of the joystick,
		 * the value of getRawAxis is multiplied by -1.
		 */
		double rawInput = -this.getRawAxis(1);
		
		/*this if statement compensates for the value of 
		* 0.0078125 read as rawInput (not 0 as expected) when the 
		* left joystick is in its resting position
		*/
		if (rawInput > 0 && rawInput <= 0.0078125) {
			return 0;
		}
		
		return rawInput;
	}
	
	/**
	 * returns boolean of LEFT josystick's button pressed
	 * @return boolean of LEFT josystick's button pressed
	 */
	
	//According to Austin Chen, this method should have been named getMoneyFromLeftJ0y$tick
	public boolean getLeftJoystickPressed() {
		return this.getRawButton(11);
	}
	
	/**
	 * returns RIGHT josystick's X value. 
	 * @return RIGHT josystick's X value, between 1 and -1
	 */
	
	public double getRightJoystickX() {
		
		double rightRawAxis = this.getRawAxis(4);
		if (rightRawAxis < 0 && rightRawAxis >= 0.0391) {
			return 0;
		}
		return rightRawAxis;
	}
	
	/**
	 * returns RIGHT josystick's Y value. 
	 * @return RIGHT josystick's X value, between 1 and -1
	 */
	
	public double getRightJoystickY() {
		return this.getRawAxis(5);
	}
	
	/**
	 * returns boolean of RIGHT josystick's button pressed
	 * @return boolean of RIGHT josystick's button pressed
	 */

	public boolean getRightJoystickPressed() {
		return this.getRawButton(12);
	}
	
	/**
	 * Returns boolean value of whether A Button is pressed
	 * @return boolean value of whether A Button is pressed
	 */
	
	public boolean getAButtonPressed() {
		return this.getRawButton(1);
	}
	
	/**
	 * Returns boolean value of whether B Button is pressed
	 * @return boolean value of whether B Button is pressed
	 */
	
	public boolean getBButtonPressed() {
		return this.getRawButton(2);
	}
	
	/**
	 * Returns boolean value of whether Y Button is pressed
	 * @return boolean value of whether Y Button is pressed
	 */
	
	public boolean getYButtonPressed() {
		return this.getRawButton(4);
	}
	
	/**
	 * Returns boolean value of whether X Button is pressed
	 * @return boolean value of whether X Button is pressed
	 */
	
	public boolean getXButtonPressed() {
		return this.getRawButton(3);
	}
	
	/**
	 * Returns boolean value of whether left bumper Button (lower) is pressed
	 * @return boolean value of whether left bumper Button (lower) is pressed
	 */
	
	public boolean getLeftBumperPressed() {
		return this.getRawButton(5);
	}
	
	/**
	 * Returns boolean value of whether right bumper Button (lower) is pressed
	 * @return boolean value of whether right bumper Button (lower) is pressed
	 */
	
	public boolean getRightBumperPressed() {
		return this.getRawButton(6);
	}
	
	/**
	 * Returns boolean value of whether left trigger Button (upper) is pressed
	 * @return boolean value of whether left trigger Button (upper) is pressed
	 */
	
	public boolean getLeftTriggerPressed() {
		return this.getRawAxis(2) == 1.0;
	}
	
	/**
	 * Returns boolean value of whether right trigger Button (upper) is pressed
	 * @return boolean value of whether right trigger Button (upper) is pressed
	 */
	
	public boolean getRightTriggerPressed() {
		return this.getRawAxis(3) == 1.0;
	}
	
	/**
	 * Returns double value of left trigger Button (upper)
	 * @return boolean value of left trigger Button (upper)
	 */
	
	public double getLeftTrigger() {
		return this.getRawAxis(2);
	}
	
	/**
	 * Returns double value of right trigger Button (upper)
	 * @return boolean value of right trigger Button (upper)
	 */
	
	public double getRightTrigger() {
		return this.getRawAxis(3);
	}
}
