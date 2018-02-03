package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.Elevator;
import org.usfirst.frc.team4990.robot.subsystems.F310Gamepad;

public class TeleopElevatorController {
	private Elevator elevator;
	private F310Gamepad gpad;
	private double maxSpeed;
	
	private int upController;
	private int downController;
	
	
	public TeleopElevatorController(Elevator elevatorInput, F310Gamepad gpadInput, double maxSpeedInput) {
		elevator = elevatorInput;
		gpad = gpadInput;
		maxSpeed = maxSpeedInput;
		//2 = LEFT Bumper on controller, 3 = RIGHT bumper on controller
		upController = 2;//LEFT Bumper
		downController = 3;//RIGHT Bumper
		//top speed of elevator motor (0.0 to 1.0)
		maxSpeed = 1.0;
	}
	
	public void update() {
		
		elevator.checkSafety();
		
		if (gpad.getRawAxis(upController) > 0 && gpad.getRawAxis(downController) > 0) {
			elevator.setElevatorPower(0.0);
		} else if (gpad.getRawAxis(upController) > 0) { //left bumper = elevator UP
			if (gpad.getRawAxis(upController) > maxSpeed) {
				elevator.setElevatorPower(maxSpeed);
			} else { 
				elevator.setElevatorPower(gpad.getRawAxis(upController)); 
			}
			return;
		} else if (gpad.getRawAxis(downController) > 0) { //right bumper = elevator DOWN
			if (gpad.getRawAxis(downController) > maxSpeed) {
				elevator.setElevatorPower(-maxSpeed);
			} else { 
				elevator.setElevatorPower(-gpad.getRawAxis(downController)); 
			}
			return;
		} else {
			elevator.setElevatorPower(0.0);
			return;
		}
			
		
		
	}

}
