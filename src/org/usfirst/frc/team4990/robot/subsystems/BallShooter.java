package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.subsystems.motors.Motor;

public class BallShooter {
	private Motor motora;
	private Motor motorb;
	private double currpower;
	
	public BallShooter(Motor a, Motor b) {
		motora = a;
		motorb = b;
	}
	
	public void setPower(double power) {
		currpower = power;
	}
	
	public double getLastPower() {
		return currpower;
	}
	
	public void update() {
		motora.setPower(currpower);
		motorb.setPower(currpower);
	}
}
