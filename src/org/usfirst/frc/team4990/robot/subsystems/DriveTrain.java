package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;

public class DriveTrain {
	public Gearbox leftGearbox;
	private double leftSetSpeed;
	
	public Gearbox rightGearbox;
	private double rightSetSpeed;
	/**
	 * Includes 4 driving motors and 2 encoders.
	 * @param leftMotor1 First Left Motor
	 * @param leftMotor2 Second Left Motor
	 * @param rightotor1 First Right Motor
	 * @param rightMotor2 Second Right Motor
	 * @param leftEncoderChannelA Encoder for Left gearbox (Signal, Ground and 5v)
	 * @param leftEncoderChannelB Encoder for Left gearbox (just Signal)
	 * @param rightEncoderChannelA Encoder for Right gearbox (Signal, Ground and 5v)
	 * @param rightEncoderChannelB Encoder for right gearbox (just Signal)
	 * @author Freshman Union
	 */
	
	public DriveTrain(Talon leftMotor1, Talon leftMotor2, Talon rightMotor1, Talon rightMotor2,
						int leftEncoderChannelA, int leftEncoderChannelB, 
						int rightEncoderChannelA, int rightEncoderChannelB) {
		this.leftGearbox = new Gearbox(leftMotor1, leftMotor2, leftEncoderChannelA, leftEncoderChannelB);
		this.rightGearbox = new Gearbox(rightMotor1, rightMotor2, rightEncoderChannelA, rightEncoderChannelB);
		
		// The gearbox is backwards
		this.rightGearbox.swapDirection();
	}
	
	/**
	 * Sets speed of Left AND Right sides
	 * @param leftSpeed Set speed for left side, min 0, max 1
	 * @param rightSpeed Set speed for right side, min 0, max 1
	 */
	
	public void setSpeed(double leftSpeed, double rightSpeed) {
		setLeftSpeed(leftSpeed);
		setRightSpeed(rightSpeed);
	}
	
	/**
	 * Sets robot's left side speed.
	 * @param leftSpeed Speed to set, min 0, max 1
	 */
	
	public void setLeftSpeed(double leftSpeed) {
		// the bot swerves to the right, so slow down left side
		double multiply_constant = 0.86;
		this.leftSetSpeed = leftSpeed * multiply_constant;
	}
	
	/**
	 * Sets robot's right side speed.
	 * @param rightSpeed Speed to set, min 0, max 1
	 */
	
	public void setRightSpeed(double rightSpeed) {
		this.rightSetSpeed = rightSpeed;
	}
	
	/**
	 * Acually sets the speeds of the motors.
	 */
	
	public void update() {
		this.leftGearbox.setSpeed(this.leftSetSpeed);
		this.rightGearbox.setSpeed(this.rightSetSpeed);
	}
	
	/**
	 * Returns left speed that will be set when update() is called
	 * @return left speed that will be set when update() is called
	 */
	
	//TODO: figure out how to scale PWM value to velocity
	public double getLeftSetSpeed() {
		return this.leftSetSpeed;
	}
	
	/**
	 * Returns right speed that will be set when update() is called
	 * @return right speed that will be set when update() is called
	 */
	
	public double getRightSetSpeed() {
		return this.rightSetSpeed;
	}
	
	/**
	 * Returns left gearbox's encoder distance
	 * @return left gearbox's encoder distance in feet (maybe)
	 */
	
	public double getLeftDistanceTraveled() {
		return Math.abs(this.leftGearbox.getDistanceTraveled());
	}
	
	/**
	 * Returns right gearbox's encoder distance
	 * @return right gearbox's encoder distance (multiply by 1.06517 to get feet)
	 */
	
	public double getRightDistanceTraveled() {
		return Math.abs(this.rightGearbox.getDistanceTraveled());
	}
	
	/**
	 * Returns left gearbox's encoder speed
	 * @return left gearbox's encoder speed
	 */
	
	public double getLeftVelocity() {
		return this.leftGearbox.getCurrentVelocity();
	}
	
	/**
	 * Returns right gearbox's encoder speed
	 * @return right gearbox's encoder speed
	 */
	
	public double getRightVelocity() {
		return this.rightGearbox.getCurrentVelocity();
	}
	
	/**
	 * Sets getRightDistance and getLeftDistance to 0. Also, it might reset the encoders. Nobody knows.
	 */
	
	public void resetDistanceTraveled() {
		this.leftGearbox.resetDistanceTraveled();
		this.rightGearbox.resetDistanceTraveled();
	}
}
