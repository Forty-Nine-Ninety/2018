package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.subsystems.motors.Motor;

public class Scaler {
	private Motor scalermotor;
	private double currpower;
	
	/**
	 * Initialize scaler
	 * @param scalemot motor used for scaler
	 */
	
	public Scaler(Motor scalemot) {
		scalermotor = scalemot;
	}
	
	/**
	 * Set speed for scaler. Use update() to execute.
	 * @param power
	 */
	
	public void setSpeed(double power) {
		currpower = power;
	}
	
	/** 
	 * Returns set speed of scaler
	 * @return set speed of scaler
	 */
	
	public double getLastPower() {
		return currpower;
	}
	
	/**
	 * Used to execute speed changes.
	 */
	
	public void update() {
		scalermotor.setPower(currpower);
	}
}
