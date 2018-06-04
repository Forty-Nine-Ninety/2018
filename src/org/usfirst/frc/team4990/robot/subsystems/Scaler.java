package org.usfirst.frc.team4990.robot.subsystems;

public class Scaler {
	private TalonMotorController scalermotor;
	private double currpower;
	
	/**
	 * Initialize scaler
	 * @param talonMotorController motor used for scaler
	 */
	
	public Scaler(TalonMotorController talonMotorController) {
		scalermotor = talonMotorController;
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
