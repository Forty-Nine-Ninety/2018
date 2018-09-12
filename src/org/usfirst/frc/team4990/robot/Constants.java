package org.usfirst.frc.team4990.robot;


/**
 * Class for constants (?)
 * @author Old Coder People
 *
 */
public class Constants {
	
	public static double zeroThrottleThreshold = 0.01;
	
	/**
	 * units: feet
	 */
	 public static double robotWidth = 2;
	/**
	 * units: feet
	 */
	 
	public static final double defaultMaxTurnRadius = 0.8;

	/**
	 * units: feet
	 */
	
	public static final double defaultAccelerationTime = 250;
	
	//TODO: figure out actual pulses per revolution and stop compensating by a factor of 2
	public static final int pulsesPerRevolution = 250;
	/**
	 * units: feet/s
	 */
	public static final double gearboxEncoderMinRate = 0.0;
	public static final int gearboxEncoderSamplesToAvg = 5;
	
	/**
	 * units: feet/revolution
	 */
	public static final double feetPerWheelRevolution = 4.0 / 12.0 * Math.PI;
	
	//drive train
	public static final double maxThrottle = 1;
	public static final double lowThrottleMultiplier = 0.25;
	public static final boolean reverseTurningFlipped = false;
}
