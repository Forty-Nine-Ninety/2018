package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.Intake;
import org.usfirst.frc.team4990.robot.subsystems.F310Gamepad;


public class TeleopIntakeController {
	
	public enum BoxPosition {
		IN, MOVING, OUT
	}
	
	private Intake intake;
	private F310Gamepad gpad;
	private int inController = 2;
	private double tempInAxis;
	private int outController = 3;
	private double tempOutAxis;
	private double maxSpeed = 1.0;
	
	public TeleopIntakeController(Intake i, F310Gamepad pad) {
		intake = i;
		gpad = pad;
	}
	
	public void update() {
		tempInAxis = gpad.getRawAxis(inController);
		tempOutAxis = gpad.getRawAxis(outController);
		boolean override = gpad.getRawButton(7);
		
		if (override) {
			if (tempInAxis > 0 && tempOutAxis > 0) {
				intake.setSpeed(0.0);
			} else if (tempInAxis > 0) { //left bumper = elevator UP
				if (tempInAxis > maxSpeed) {
					intake.setSpeed(maxSpeed);
				} else { 
					intake.setSpeed(tempInAxis);
				}
				return;
			} else if (tempOutAxis > 0) { //right bumper = elevator DOWN
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
		else {
			if (tempInAxis > 0 && tempOutAxis > 0) {
				intake.setSpeed(0.0);
			} else if (tempInAxis > 0 && intake.getAnalogInput() < 2) { //left bumper = elevator UP
				if (tempInAxis > maxSpeed) {
					intake.setSpeed(maxSpeed);
				} else { 
					intake.setSpeed(tempInAxis);
				}
				return;
			} else if (tempOutAxis > 0) { //right bumper = elevator DOWN
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

}
