package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.Intake;
import org.usfirst.frc.team4990.robot.subsystems.F310Gamepad;

public class TeleopIntakeController {
	private Intake intake;
	private F310Gamepad gpad;
	private int upController = 2;
	private double tempUpAxis;
	private int downController = 3;
	private double tempDownAxis;
	private double maxSpeed = 1.0;
	
	public TeleopIntakeController(Intake i, F310Gamepad pad) {
		intake = i;
		gpad = pad;
	}
	
	public void update() {
		tempUpAxis = gpad.getRawAxis(upController);
		tempDownAxis = gpad.getRawAxis(downController);
		
		if (tempUpAxis > 0 && tempDownAxis > 0) {
			intake.setSpeed(0.0);
		} else if (tempUpAxis > 0) { //left bumper = elevator UP
			if (tempUpAxis > maxSpeed) {
				intake.setSpeed(maxSpeed);
			} else { 
				intake.setSpeed(tempUpAxis); 
			}
			return;
		} else if (tempDownAxis > 0) { //right bumper = elevator DOWN
			if (tempDownAxis > maxSpeed) {
				intake.setSpeed(-maxSpeed);
			} else { 
				intake.setSpeed(-tempDownAxis); 
			}
			return;
		} else {
			intake.setSpeed(0.0);
			return;
		}
	}

}
