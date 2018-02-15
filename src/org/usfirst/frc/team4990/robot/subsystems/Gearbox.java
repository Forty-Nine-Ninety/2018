package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.Constants;
import org.usfirst.frc.team4990.robot.subsystems.motors.Motor;

import edu.wpi.first.wpilibj.Encoder;

public class Gearbox {
	private Motor motor1;
	private Motor motor2;
	
	private Encoder encoder;
	
	private double compensate;
	private double fix_backwards;
	
	/**
	 * Used in ???
	 * @author Freshman Union
	 *
	 */
	
	public enum RobotSide {
		Left, Right
	}
	
	/**
	 * Initialize gearbox
	 * @param motor1 DIO port for 1st motor
	 * @param motor2 DIO port for 2nd motor
	 * @param encoderChannelA Encoder in gearbox (Signal, Ground and 5v)
	 * @param encoderChannelB Encoder in gearbox (just Signal)
	 */
	
	public Gearbox(Motor motor1, Motor motor2, int encoderChannelA, int encoderChannelB) {
		this.motor1 = motor1;
		this.motor2 = motor2;

		this.encoder = new Encoder(encoderChannelA, encoderChannelB);
		
		this.compensate = 1.0;
		this.fix_backwards = 1.0;
		
		this.encoder.setDistancePerPulse(Constants.feetPerWheelRevolution / Constants.pulsesPerRevolution);
		this.encoder.setMinRate(Constants.gearboxEncoderMinRate);
		this.encoder.setSamplesToAverage(Constants.gearboxEncoderSamplesToAvg);
	}
	
	public void swapDirection() {
		this.fix_backwards = (this.fix_backwards > 0.0) ? -1.0 : 1.0;
	}
	
	/**
	 * Set Speed of gearbox. Use update() to execute.
	 * @param speed speed from -1 to 1
	 */
	
	public void setSpeed(double speed) {
		this.motor1.setPower(speed * this.compensate * this.fix_backwards);
		this.motor2.setPower(speed * this.compensate * this.fix_backwards);
	}
	
	public void updateCompensate(double other_dist) {
		this.compensate = other_dist / this.getDistanceTraveled();
	}
	
	/*
	 * returned in ft
	 */

	/**
	 * Returns gearbox's encoder's distance in feet
	 * @return gearbox's encoder's distance in feet
	 */
	
	public double getDistanceTraveled() {
		return this.encoder.getDistance() * this.fix_backwards;
	}
	
	/*
	 * returned in ft/s
	 */
	
	/**
	 * Returns gearbox's encoder's speed in feet per second
	 * @return gearbox's encoder's speed in feet per second
	 */
	
	public double getCurrentVelocity() {
		return this.encoder.getRate() * this.fix_backwards;
	}
	
	/**
	 * Resets gearbox's encoder. Sets distance to 0.
	 */
	
	public void resetDistanceTraveled() {
		this.encoder.reset();
	}
}
