package org.usfirst.frc.team4990.robot.subsystems.motors;

public interface Motor {
	/**
	 * Sets power of motor.
	 * @param power Speed between -1 and 1
	 */
	void setPower(double power); //It's over 9000!
	
	/**
	 * Gets speed last set to motor.
	 * @return Speed between -1 and 1
	 */
	double getPower();
}
