package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.Scaler;
import org.usfirst.frc.team4990.robot.subsystems.F310Gamepad;

public class TeleopScalerController {
	private Scaler scaler;
	private F310Gamepad gpad;
	private double speed;
	
	public TeleopScalerController(Scaler scale, F310Gamepad pad, double spd) {
		scaler = scale;
		gpad = pad;
		speed = spd;
	}
	
	public void update() {
		boolean lpressed = gpad.getLeftBumperPressed();
		boolean rpressed = gpad.getRightBumperPressed();
		
		if(lpressed) {
			scaler.setSpeed(-speed);
			return;
		}
		
		if(rpressed) {
			scaler.setSpeed(speed);
			return;
		}
		
		scaler.setSpeed(0);
	}

}
