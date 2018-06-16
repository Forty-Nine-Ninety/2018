package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.Intake;
import org.usfirst.frc.team4990.robot.subsystems.F310Gamepad;

/**
 * Class for intake in teleop period
 * @author Freshman Union
 *
 */
public class TeleopIntakeController {
	
	private Intake intake;
	private F310Gamepad gpad;
	private int inController = 2;
	private double tempInAxis;
	private int outController = 3;
	private double tempOutAxis;
	private double maxSpeed = 0.65;
	/**
	 * Constructor for class
	 * @author Freshman Union
	 * @param i Intake to control
	 * @param pad Gamepad to read input from
	 */
	public TeleopIntakeController(Intake i, F310Gamepad pad) {
		intake = i;
		gpad = pad;
	}
	/**
	 * Read input and updates motor speeds
	 * @author Freshman Union
	 */
	public void update() {
		tempInAxis = gpad.getRawAxis(inController);
		tempOutAxis = gpad.getRawAxis(outController);
		boolean override = gpad.getRawButton(7);
		if (tempInAxis > 0 && tempOutAxis > 0) {
			intake.setSpeed(0.0);
		} else if (tempInAxis > 0 && ((intake.getAnalogInput() < 1.9 || override) || override)) { //left bumper = elevator UP
			if (tempInAxis > maxSpeed) {
				intake.setSpeed(maxSpeed);
			} else { 
				intake.setSpeed(tempInAxis);
			}
			return;
		} else if (tempOutAxis > 0) { 
			if (tempOutAxis > maxSpeed) {
				intake.setSpeed(-maxSpeed);
			} else { 
				intake.setSpeed(-tempOutAxis); 
			}
			return;
		} else {
			intake.setSpeed(0.0);
			return;
		}
	}

}
