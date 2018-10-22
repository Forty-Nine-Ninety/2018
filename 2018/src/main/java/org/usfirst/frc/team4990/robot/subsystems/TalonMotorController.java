package org.usfirst.frc.team4990.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class TalonMotorController extends WPI_TalonSRX {

	/**
	 * Initialize Talon.
	 * 
	 * @param canID
	 *            CAN bus ID of Talon (0 to 63, set from the web dashboard)
	 */
	public TalonMotorController(int canID) {
		super(canID);
	}

	/**
	 * Sets power of motor.
	 * 
	 * @param power
	 *            Speed between -1 and 1
	 */

	public void setPower(double power) {
		this.set(Math.max(-1, Math.min(power, 1))); // constrains value to between -1 and 1
	}

	/**
	 * Gets speed last set to motor.
	 * 
	 * @return Speed between -1 and 1
	 */

	public double getPower() {
		return this.get();
	}
}
