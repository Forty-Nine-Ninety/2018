package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;

/**
 * We don't like this sensor, but it came in the 2018 Kit of Parts, so we tried it. It was not accurate enough for 2018's applications.
 * If someone ever wants to use it, spend some time looking at the sensor curve and write a better interface than this one!
 * @deprecated
 * @author Class of '21 (created in 2018 season)
 *
 */

public class MaxBoticsUltrasonicSensor {
	private AnalogInput input;
	private double defaultVoltage;
	private double voltagePerInch;
	
	/**
	 * Initialize MaxBotics Ultrasonic.
	 * @param inputDevice Analog IO port that sensor is plugged into
	 */
	
	public MaxBoticsUltrasonicSensor(AnalogInput inputDevice) {
		input = inputDevice;
		voltagePerInch = 0.01431;//Found these values at from testing the sensor
		resetDefaultVoltage();
	}
	
	/**
	 * Sets zero/background value of sensor to current reading.
	 */
	
	public void setDefaultVoltage() {
		defaultVoltage = input.getVoltage();
	}
	
	/**
	 * Sets zero/background value of sensor to 0.
	 */
	
	public void resetDefaultVoltage() {
		defaultVoltage = 0;
	}
	
	/**
	 * Returns distance in inches.
	 * @return distance in inches (theoretically)
	 */
	
	public double getDistance() {
		return (input.getVoltage() - defaultVoltage) / voltagePerInch;
	}
}
