package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.F310Gamepad;
import org.usfirst.frc.team4990.robot.subsystems.Intake;

public class TeleopIntakeController {
	private Intake intake;
	private F310Gamepad gamepad;
	
	public TeleopIntakeController(F310Gamepad gm, Intake i) {
		intake = i;
		gamepad = gm;
	}
	
	public void update() {
		boolean inPressed = gamepad.getRightBumperPressed();
		boolean outPressed = gamepad.getLeftBumperPressed();
		
		if (inPressed) {
			intake.in();
			return;
		}
		if (outPressed) {
			intake.out();
			return;
		}
		
		intake.stop();
	}
}
