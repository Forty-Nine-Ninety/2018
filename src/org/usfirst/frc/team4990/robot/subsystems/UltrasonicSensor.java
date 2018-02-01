package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;

public class UltrasonicSensor {
	private AnalogInput input;
	private double defaultVoltage;
	private double voltagePerInch;
	
	public UltrasonicSensor(AnalogInput inputDevice) {
		input = inputDevice;
		voltagePerInch = 0.01431;//Found these values at from testing the sensor
		resetDefaultVoltage();
	}
	
	public void setDefaultVoltage() {
		defaultVoltage = input.getVoltage();
	}
	
	public void resetDefaultVoltage() {
		defaultVoltage = 0;
	}
	
	public double getDistance() {
		return (input.getVoltage() - defaultVoltage) / voltagePerInch;
	}
}
