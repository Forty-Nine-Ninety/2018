package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.subsystems.motors.Motor;

public class DriveTrain {
	private Gearbox leftGearbox;
	private double leftSetSpeed;
	
	private Gearbox rightGearbox;
	private double rightSetSpeed;
	
	public DriveTrain(Motor leftMotor1, Motor leftMotor2, Motor rightMotor1, Motor rightMotor2,
						int leftEncoderChannelA, int leftEncoderChannelB, 
						int rightEncoderChannelA, int rightEncoderChannelB) {
		this.leftGearbox = new Gearbox(leftMotor1, leftMotor2, leftEncoderChannelA, leftEncoderChannelB, Gearbox.RobotSide.Left);
		this.rightGearbox = new Gearbox(rightMotor1, rightMotor2, rightEncoderChannelA, rightEncoderChannelB, Gearbox.RobotSide.Right);
		
		// The gearbox is backwards
		this.rightGearbox.swapDirection();
	}
	
	public void setSpeed(double leftSpeed, double rightSpeed) {		
		setLeftSpeed(leftSpeed);
		setRightSpeed(rightSpeed);
	}
	
	public void setLeftSpeed(double leftSpeed) {
		this.leftSetSpeed = leftSpeed ;//slower?
	}
	
	public void setRightSpeed(double rightSpeed) {
		this.rightSetSpeed = rightSpeed;
	}
	
	public void update() {
		this.leftGearbox.setSpeed(this.leftSetSpeed);
		this.rightGearbox.setSpeed(this.rightSetSpeed);
	}
	
	//TODO: figure out how to scale PWM value to velocity
	public double getLeftSetSpeed() {
		return this.leftSetSpeed;
	}
	
	public double getRightSetSpeed() {
		return this.rightSetSpeed;
	}
	
	public double getLeftDistanceTraveled() {
		return this.leftGearbox.getDistanceTraveled();
	}
	
	public double getRightDistanceTraveled() {
		return this.rightGearbox.getDistanceTraveled();
	}
	
	public double getLeftVelocity() {
		return this.leftGearbox.getCurrentVelocity();
	}
	
	public double getRightVelocity() {
		return this.rightGearbox.getCurrentVelocity();
	}
	
	public void resetDistanceTraveled() {
		this.leftGearbox.resetDistanceTraveled();
		this.rightGearbox.resetDistanceTraveled();
	}
}
