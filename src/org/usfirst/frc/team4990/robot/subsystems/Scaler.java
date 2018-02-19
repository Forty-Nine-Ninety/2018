package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;

public class Scaler {
	private Talon scalermotor;
	private double currpower;
	
	/**
	 * Initialize scaler
	 * @param scalemot motor used for scaler
	 */
	
	public Scaler(Talon scalemot) {
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
		scalermotor.set(currpower);
	}
}
