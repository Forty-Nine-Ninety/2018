package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.subsystems.motors.Motor;

public class Scaler {
	private Motor scalermotor;
	private double currpower;
	
	public Scaler(Motor scalemot) {
		scalermotor = scalemot;
	}
	
	public void setSpeed(double power) {
		currpower = power;
	}
	
	public double getLastPower() {
		return currpower;
	}
	
	public void update() {
		scalermotor.setPower(currpower);
	}
}
