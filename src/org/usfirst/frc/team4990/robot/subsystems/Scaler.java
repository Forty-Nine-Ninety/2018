package org.usfirst.frc.team4990.robot.subsystems;

public class Scaler {
	private TalonMotorController scalerMotor;
	private double setSpeed;
	
	/**
	 * Initialize scaler
	 * @param talonMotorController motor used for scaler
	 */
	
	public Scaler(TalonMotorController talonMotorController) {
		scalerMotor = talonMotorController;
	}
	
	/**
	 * Set speed for scaler. Use update() to execute.
	 * @param power
	 */
	
	public void setSpeed(double speed) {
		setSpeed = speed;
	}
	
	/** 
	 * Returns set speed of scaler
	 * @return set speed of scaler
	 */
	
	public double getLastPower() {
		return setSpeed;
	}
	
	/**
	 * Used to execute speed changes.
	 */
	
	public void update() {
		scalerMotor.set(setSpeed);
	}
}
