package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.Constants;
import org.usfirst.frc.team4990.robot.subsystems.motors.Motor;

import edu.wpi.first.wpilibj.Encoder;

public class Gearbox {
	private Motor motor1;
	private Motor motor2;
	
	private RobotSide robotSide;
	
	private Encoder encoder;
	
	private double compensate;
	private double fix_backwards;
	
	public enum RobotSide {
		Left, Right
	}
	
	public Gearbox(Motor motor1, Motor motor2, int encoderChannelA, int encoderChannelB, RobotSide robotSide) {
		this.motor1 = motor1;
		this.motor2 = motor2;
		
		this.robotSide = robotSide;
		
		this.encoder = new Encoder(encoderChannelA, encoderChannelB, robotSide == RobotSide.Right, Encoder.EncodingType.k2X);
		
		this.compensate = 1.0;
		this.fix_backwards = 1.0;
		
		this.encoder.setDistancePerPulse(Constants.feetPerWheelRevolution / Constants.pulsesPerRevolution);
		this.encoder.setMinRate(Constants.gearboxEncoderMinRate);
		this.encoder.setSamplesToAverage(Constants.gearboxEncoderSamplesToAvg);
	}
	
	public void swapDirection() {
		this.fix_backwards = (this.fix_backwards > 0.0) ? -1.0 : 1.0;
	}
	
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
	public double getDistanceTraveled() {
		return this.encoder.getDistance() * this.fix_backwards;
	}
	
	/*
	 * returned in ft/s
	 */
	public double getCurrentVelocity() {
		return this.encoder.getRate() * this.fix_backwards;
	}
	
	public void resetDistanceTraveled() {
		this.encoder.reset();
	}
}
