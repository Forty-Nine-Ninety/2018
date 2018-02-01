package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;

public class UltrasonicSensor {
	private AnalogInput input;
	private double defaultVoltage;
	private double voltagePerInch;
	
	public UltrasonicSensor(AnalogInput inputDevice) {
		input = inputDevice;
		voltagePerInch = 5.0 / 512.0;//Found these values at https://github.com/Sparx-Robotics-1126/2015-Season/blob/master/src/org/gosparx/team1126/robot/sensors/MaxSonarEZ1.java
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
