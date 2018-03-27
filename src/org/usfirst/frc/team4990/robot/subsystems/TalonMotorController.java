package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;

public class TalonMotorController extends Talon{
	
	/**
	 * Initialize Talon.
	 * @param pwmPort PWM port of Talon
	 */
	public TalonMotorController(int pwmPort) {
		super(pwmPort);
	}
	
	/**
	 * Sets power of motor.
	 * @param power Speed between -1 and 1
	 */
	
	public void setPower(double power) {
		this.set(Math.max(-1, Math.min(power, 1))); //constrains value to between -1 and 1
	}
	
	/**
	 * Gets speed last set to motor.
	 * @return Speed between -1 and 1
	 */
	
	public double getPower() {
		return this.get();
	}
}
