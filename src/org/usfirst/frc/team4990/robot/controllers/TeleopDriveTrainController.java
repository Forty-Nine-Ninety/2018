package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.Constants;
import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4990.robot.subsystems.F310Gamepad;

import java.util.*;
/**
 * Class for controlling drivetrains.
 * @author Freshman Union
 * 
 */
public class TeleopDriveTrainController {
	private F310Gamepad gamepad;
	private DriveTrain driveTrain;
	
	private double lastThrottle = 0;
	private double lastTurnSteepness = 0;
	
	private Date lastUpdate;
	
	private boolean lastDpiToggleInput = false;
	private double currentThrottleMultiplier;
	
	private final double accelerationTime;
	private final double lowThrottleMultiplier;
	private final double maxThrottle;
	/**
	 * Constructor for TeleopDriveTrainController
	 * @author Freshman Union
	 * 
	 * @param gamepad Controller to read from
	 * @param driveTrain Drivetrain to control
	 * @param maxTurnRadius No idea yet.
	 * @param reverseTurningFlipped Whether turning is flipped; Not used.
	 * @param accelerationTime Time the robot has been accelerating for
	 * @param lowThrottleMultiplier Multiplier for when X button is pressed(puts robot into "turtle mode")
	 * @param maxThrottle Max throttle between 0 and 1
	 */
	public TeleopDriveTrainController(
			F310Gamepad gamepad, 
			DriveTrain driveTrain, 
			boolean reverseTurningFlipped,
			double accelerationTime,
			double lowThrottleMultiplier,
			double maxThrottle) {
		this.gamepad = gamepad;
		this.driveTrain = driveTrain;
		
		this.lastUpdate = new Date();
		
		this.currentThrottleMultiplier = maxThrottle;
		
		this.accelerationTime = accelerationTime;
		this.lowThrottleMultiplier = lowThrottleMultiplier;
		this.maxThrottle = maxThrottle;
	}
	
	/**
	 * Updates class variable values and sets motor speeds
	 * @author Freshman Union
	 */
	public void updateDriveTrainState() {
		boolean dpiTogglePressed = this.gamepad.getXButtonPressed();
		
		if (dpiTogglePressed && !this.lastDpiToggleInput) {
			if (this.currentThrottleMultiplier == this.maxThrottle) {
				this.currentThrottleMultiplier = this.lowThrottleMultiplier;
			} else {
				this.currentThrottleMultiplier = this.maxThrottle;
			}
		}
		
		this.lastDpiToggleInput = dpiTogglePressed;
		
		double throttleInput = this.gamepad.getLeftJoystickY();
		double turnSteepnessInput = this.gamepad.getRightJoystickX();
		
		//System.out.println("Throttle Input:" + throttleInput);
		
		//System.out.println("turnSteepnessInput:" + turnSteepnessInput);
		
		Date currentUpdate = new Date();
		
		double throttle = getNextThrottle(
				throttleInput * this.currentThrottleMultiplier, 
				this.lastThrottle, 
				this.lastUpdate, 
				currentUpdate, 
				this.accelerationTime);
		
		double turnSteepness = getNextThrottle(
				turnSteepnessInput * this.currentThrottleMultiplier,
				this.lastTurnSteepness,
				this.lastUpdate,
				currentUpdate,
				this.accelerationTime);
		
		if (throttle != 0 && turnSteepnessInput != 0) {
			setArcTrajectory(throttle, -turnSteepnessInput);
		} else if (throttle != 0 && turnSteepnessInput == 0) { 
			setStraightTrajectory(throttle);
		} else if (throttle == 0 && turnSteepness != 0) {
			setTurnInPlaceTrajectory(turnSteepness);
		} else {
			this.driveTrain.setSpeed(0.0, 0.0);
		}
		
		this.lastThrottle = throttle;
		this.lastTurnSteepness = turnSteepness;
		this.lastUpdate = currentUpdate;
	}
	
	/**
	 * Gets next throttle value
	 * @author Freshman Union
	 * @param throttleInput Current throttle value
	 * @param lastThrottle Last throttle value
	 * @param lastUpdate Last time function was called
	 * @param currentUpdate current time
	 * @param accelerationTime Time robot should take to accelerate
	 * @return Either 0 if the throttle is below Constants.zeroThrottleThreshold or the new throttle value
	 */
	public double getNextThrottle(double throttleInput, double lastThrottle, Date lastUpdate, Date currentUpdate, double accelerationTime) {
		double newThrottle = throttleInput;
		
		if (accelerationTime != 0) {
			double acceleration = (throttleInput - lastThrottle) / accelerationTime;
			double deltaTime = currentUpdate.getTime() - lastUpdate.getTime();
			
			double deltaThrottle = deltaTime * acceleration;
			
			newThrottle = lastThrottle + deltaThrottle;
		}
		
		return Math.abs(newThrottle) < Constants.zeroThrottleThreshold ? 0.0 : newThrottle;
	}
	/**
	 * Sets motor for arc turns
	 * @author Freshman Union
	 * @param throttle Value for left drivetrain
	 * @param turnSteepness How much the right drivetrain should turn compared to the left drivetrain
	 */
	public void setArcTrajectory(double throttle, double turnSteepness) {
		//without if statement; turns to the right
		
		double leftWheelSpeed = throttle;
		double rightWheelSpeed = calculateInsideWheelSpeed(throttle, turnSteepness);
		
		/* the robot should turn to the left, so left wheel is on the inside
		 * of the turn, and the right wheel is on the outside of the turn
		 */
		
		//goes forward and also runs this if reverse turning disabled
		boolean slowLeft;
		if (turnSteepness < 0) {
			if (throttle < 0) {
				slowLeft = true;
			} else {
				slowLeft = false;
			}
		}
		else {//What if it's 0 exact?  Or is that not possible? //NVM That's already considered above.
			if (throttle < 0) {
				slowLeft = false;
			} else {
				slowLeft = true;
			}
		}
		if (slowLeft) {
			leftWheelSpeed = calculateInsideWheelSpeed(throttle,-turnSteepness);
			rightWheelSpeed = throttle;
		}
		
		//System.out.println(leftWheelSpeed + "; " + rightWheelSpeed);
		
		this.driveTrain.setSpeed(leftWheelSpeed, rightWheelSpeed);
	}
	
	/**
	 * Calculates "inside" wheel motor speed based on the "outside" wheel speed and turnSteepness
	 * @author Freshman Union & Old Coders
	 * @param outsideWheelSpeed Speed of other wheel
	 * @param turnSteepness How much the inside wheels should turn compared to the outside wheels
	 * @return Calculated inside wheel speed
	 */
	private double calculateInsideWheelSpeed(double outsideWheelSpeed, double turnSteepness) {
		/*
		 * Basically, the larger the turnSteepness the smaller the power should be
		 * We also add a turnSensitivity constant to make the robot turn slower
		 */
		double turnSensitivity = 0.9;
		double newSteepness = turnSteepness * turnSensitivity;
		double turnValue = (turnSteepness > 0) ? 1 - newSteepness : 1 + newSteepness;
		return outsideWheelSpeed * turnValue;
	}
	
	/**
	 * Sets drivetrain to go straight
	 * @author Freshman Union
	 * @param throttle Speed to go straight at
	 */
	public void setStraightTrajectory(double throttle) {
		/* both motors should spin forward. */
		
		this.driveTrain.setSpeed(throttle, throttle);
	}
	
	/**
	 * Sets motor speed for stationary turns
	 * @author Freshman Union
	 * @param turningSpeed Speed to turn
	 */
	public void setTurnInPlaceTrajectory(double turningSpeed) {
		/* the right motor's velocity has the opposite sign of the the left motor's
		 * since the right motor will spin in the opposite direction from the left
		 */
		this.driveTrain.setSpeed(turningSpeed, -turningSpeed);
	}
}
