package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.Elevator;
import org.usfirst.frc.team4990.robot.subsystems.F310Gamepad;
/**
 * Class that controls elevator in teleop
 * @author Freshman Union
 *
 */
public class TeleopElevatorController {
	private Elevator elevator;
	private F310Gamepad gpad;
	private double maxSpeed;
	private double tempAxis;
	
	private int controller;
	
	/**
	 * Constructor for TeleopElevatorController
	 * @author Freshman Union
	 * @param elevatorInput Elevator to be controlled
	 * @param gpadInput Gamepad to read input from
	 * @param maxSpeedInput Maximum speed for elevator to go up/down at
	 */
	public TeleopElevatorController(Elevator elevatorInput, F310Gamepad gpadInput, double maxSpeedInput) {
		elevator = elevatorInput;
		gpad = gpadInput;
		maxSpeed = maxSpeedInput;
		controller = 5;//RIGHT joystick
		//top speed of elevator motor (0.0 to 1.0)
		maxSpeed = 1.0;
	}
	
	/**
	 * Gets input from gamepad and sets motor power
	 * @author Freshman Union
	 */
	public void update() {
		tempAxis = gpad.getRawAxis(controller);
		
		if (tempAxis > 0) { //right joystick positive = elevator UP
			if (tempAxis > maxSpeed) {
				elevator.setElevatorPower(maxSpeed);
			} else { 
				elevator.setElevatorPower(tempAxis); 
			}
		} else if (tempAxis < 0) { //right joystick negative = elevator DOWN
			if (-tempAxis > maxSpeed) {
				elevator.setElevatorPower(maxSpeed);
			} else { 
				elevator.setElevatorPower(tempAxis); 
			}
		} else {
			elevator.setElevatorPower(0.0);
		}
			
		elevator.update(); //always run at END of update function
	}

}
