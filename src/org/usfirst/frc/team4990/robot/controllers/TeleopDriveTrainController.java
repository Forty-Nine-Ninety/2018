package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.Constants;
import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4990.robot.subsystems.F310Gamepad;

import java.util.*;

public class TeleopDriveTrainController {
	private F310Gamepad gamepad;
	private DriveTrain driveTrain;
	
	private double lastThrottle = 0;
	private double lastTurnSteepness = 0;
	
	private Date lastUpdate;
	
	private boolean lastDpiToggleInput = false;
	private double currentThrottleMultiplier;
	
	private final double maxTurnRadius;
	private final double accelerationTime;
	private final double lowThrottleMultiplier;
	private final double maxThrottle;
	
	public TeleopDriveTrainController(
			F310Gamepad gamepad, 
			DriveTrain driveTrain, 
			double maxTurnRadius, 
			boolean reverseTurningFlipped,
			double accelerationTime,
			double lowThrottleMultiplier,
			double maxThrottle) {
		this.gamepad = gamepad;
		this.driveTrain = driveTrain;
		
		this.lastUpdate = new Date();
		
		this.currentThrottleMultiplier = maxThrottle;
		
		this.maxTurnRadius = maxTurnRadius;
		this.accelerationTime = accelerationTime;
		this.lowThrottleMultiplier = lowThrottleMultiplier;
		this.maxThrottle = maxThrottle;
	}
	
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
		
		System.out.println("Throttle Input:" + throttleInput);
		
		System.out.println("turnSteepnessInput:" + turnSteepnessInput);
		
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
		
		System.out.println("throttle:" + throttle);
		System.out.println("turnSteepness" + turnSteepness);
		
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
	
	public void setArcTrajectory(double throttle, double turnSteepness) {
		System.out.println("setAT throttle:" + throttle);
		System.out.println("setAT turnSteepness: " + turnSteepness);
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
		
		System.out.println(leftWheelSpeed + "; " + rightWheelSpeed);
		
		this.driveTrain.setSpeed(leftWheelSpeed, rightWheelSpeed);
	}
	
	// TAKE ANOTHER LOOK LATER!!!  INNER WHEEL IS FLIPPED 
	
	private double calculateInsideWheelSpeed(double outsideWheelSpeed, double turnSteepness) {
		System.out.println("MEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEP ||| OWS: " + outsideWheelSpeed + " | turnSteepness: " + turnSteepness);
		double turnRadius = (1 - turnSteepness) * this.maxTurnRadius;
		
		return outsideWheelSpeed * (turnRadius / (turnRadius + Constants.robotWidth));
	}
	
	public void setStraightTrajectory(double throttle) {
		/* both motors should spin forward. */
		this.driveTrain.setSpeed(throttle, throttle);
	}
	
	public void setTurnInPlaceTrajectory(double turningSpeed) {
		/* the right motor's velocity has the opposite sign of the the left motor's
		 * since the right motor will spin in the opposite direction from the left
		 */
		this.driveTrain.setSpeed(turningSpeed, -turningSpeed);
	}
}
