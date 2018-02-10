package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.Elevator;
import org.usfirst.frc.team4990.robot.subsystems.F310Gamepad;

public class TeleopElevatorController {
	private Elevator elevator;
	private F310Gamepad gpad;
	private double maxSpeed;
	
	private int controller;
	
	
	public TeleopElevatorController(Elevator elevatorInput, F310Gamepad gpadInput, double maxSpeedInput) {
		elevator = elevatorInput;
		gpad = gpadInput;
		maxSpeed = maxSpeedInput;
		//2 = LEFT Bumper on controller, 3 = RIGHT bumper on controller
		controller = 4;//RIGHT joystick
		//top speed of elevator motor (0.0 to 1.0)
		maxSpeed = 1.0;
	}
	
	public void update() {
		
		elevator.update();
		
		elevator.checkSafety();
		
		if (gpad.getRawAxis(controller) > 0 && gpad.getRawAxis(controller) < 0) {
			elevator.setElevatorPower(0.0);
		} else if (gpad.getRawAxis(controller) > 0) { //right joystick positive = elevator UP
			if (gpad.getRawAxis(controller) > maxSpeed) {
				elevator.setElevatorPower(maxSpeed);
			} else { 
				elevator.setElevatorPower(gpad.getRawAxis(controller)); 
			}
			return;
		} else if (gpad.getRawAxis(controller) < 0) { //right joystick negative = elevator DOWN
			if (-gpad.getRawAxis(controller) > maxSpeed) {
				elevator.setElevatorPower(-maxSpeed);
			} else { 
				elevator.setElevatorPower(gpad.getRawAxis(controller)); 
			}
			return;
		} else {
			elevator.setElevatorPower(0.0);
			return;
		}
			
		
		
	}

}
