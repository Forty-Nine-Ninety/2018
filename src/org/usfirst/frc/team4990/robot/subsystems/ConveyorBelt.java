package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.subsystems.motors.Motor;

public class ConveyorBelt {
	private Motor motora;
	private double currpower;
	
	public ConveyorBelt(Motor a) {
		motora = a;
	}
	
	public void setPower(double power) {
		currpower = power;
	}
	
	public double getLastPower() {
		return currpower;
	}
	
	public void update() {
		motora.setPower(currpower);
	}
}
