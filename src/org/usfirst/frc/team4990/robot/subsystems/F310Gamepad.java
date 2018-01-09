package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;

public class F310Gamepad extends Joystick {
	public F310Gamepad(int joystickNumber) {
		super(joystickNumber);
	}
	
	/* the following methods provide semantic sugar over the raw
	 * axis and button inputs from the F310 Gamepad, as discovered
	 * by using the FRC Driver Station software.
	 */
	
	public double getLeftJoystickX() {
		return this.getRawAxis(0);
	}
	
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
	
	//According to Austin Chen, this method should have been named getMoneyFromLeftJ0y$tick
	public boolean getLeftJoystickPressed() {
		return this.getRawButton(11);
	}
	
	public double getRightJoystickX() {
		
		double rightRawAxis = this.getRawAxis(4);
		if (rightRawAxis < 0 && rightRawAxis >= 0.0391) {
			return 0;
		}
		return rightRawAxis;
	}
	
	public double getRightJoystickY() {
		return this.getRawAxis(5);
	}

	public boolean getRightJoystickPressed() {
		return this.getRawButton(12);
	}
	
	public boolean getAButtonPressed() {
		return this.getRawButton(1);
	}
	
	public boolean getBButtonPressed() {
		return this.getRawButton(2);
	}
	
	public boolean getYButtonPressed() {
		return this.getRawButton(4);
	}
	
	public boolean getXButtonPressed() {
		return this.getRawButton(3);
	}
	
	public boolean getLeftBumperPressed() {
		return this.getRawButton(5);
	}
	
	public boolean getRightBumperPressed() {
		return this.getRawButton(6);
	}
	
	public boolean getLeftTriggerPressed() {
		return this.getRawAxis(2) == 1.0;
	}
	
	public boolean getRightTriggerPressed() {
		return this.getRawAxis(3) == 1.0;
	}
}
