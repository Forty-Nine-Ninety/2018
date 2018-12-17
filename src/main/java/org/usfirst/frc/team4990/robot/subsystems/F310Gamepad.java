package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.OI.JoystickAnalogButton;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Button;
public class F310Gamepad extends Joystick {

	 /* the following methods provide semantic sugar over the raw
	 * axis and button inputs from the F310 Gamepad, as discovered
	 * by using the FRC Driver Station software.
	 */

	public Button a = new JoystickButton(this, 1);
	public Button b = new JoystickButton(this, 2);
	public Button x = new JoystickButton(this, 3);
	public Button y = new JoystickButton(this, 4);
	public Button leftBumper = new JoystickButton(this, 5);
	public Button rightBumper = new JoystickButton(this, 6);
	public Button start = new JoystickButton(this, 10);
	public Button back = new JoystickButton(this, 9);
	public Button rightJoystickButton = new JoystickButton(this, 12);
	public Button leftJoystickButton = new JoystickButton(this, 11);

	/*
	* Use [JoystickAnalogButton].get() to get boolean value.
	* Use [JoystickAnalogButton].getRawAxis() to get double value.
	*/
	public JoystickAnalogButton leftTrigger = new JoystickAnalogButton(this, 2, 0.95);
	public JoystickAnalogButton rightTrigger = new JoystickAnalogButton(this, 3, 0.95);
	public JoystickAnalogButton leftJoystickX = new JoystickAnalogButton(this, 0);
	public JoystickAnalogButton leftJoystickY = new JoystickAnalogButton(this, 1, 0.0078125, true);
	public JoystickAnalogButton rightJoystickX = new JoystickAnalogButton(this, 4, 0.0391);
	public JoystickAnalogButton rightJoystickY = new JoystickAnalogButton(this, 5);

	/**
	 * Initializes new F310 Gamepad
	 * @param joystickNumber From drive station port number. (0 to 3?) BASED ON WHEN PLUGGED IN, not specific ports.
	 */
	public F310Gamepad(int joystickNumber) {
		super(joystickNumber);
	}
}
