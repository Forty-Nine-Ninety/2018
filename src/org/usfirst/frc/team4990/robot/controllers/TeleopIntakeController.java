package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.Intake;
import org.usfirst.frc.team4990.robot.subsystems.F310Gamepad;

public class TeleopIntakeController {
	private Intake intake;
	private F310Gamepad gpad;
	private int upController = 2;
	private int downController = 3;
	private double maxSpeed = 1.0;
	
	public TeleopIntakeController(Intake i, F310Gamepad pad) {
		intake = i;
		gpad = pad;
	}
	
	public void update() {
		
		if (gpad.getRawAxis(upController) > 0 && gpad.getRawAxis(downController) > 0) {
			intake.setSpeed(0.0);
		} else if (gpad.getRawAxis(upController) > 0) { //left bumper = elevator UP
			if (gpad.getRawAxis(upController) > maxSpeed) {
				intake.setSpeed(maxSpeed);
			} else { 
				intake.setSpeed(gpad.getRawAxis(upController)); 
			}
			return;
		} else if (gpad.getRawAxis(downController) > 0) { //right bumper = elevator DOWN
			if (gpad.getRawAxis(downController) > maxSpeed) {
				intake.setSpeed(-maxSpeed);
			} else { 
				intake.setSpeed(-gpad.getRawAxis(downController)); 
			}
			return;
		} else {
			intake.setSpeed(0.0);
			return;
		}
	}

}
