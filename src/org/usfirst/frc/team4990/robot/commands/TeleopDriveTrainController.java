package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.*;

import edu.wpi.first.wpilibj.command.Command;

import java.util.*;
/**
 * Class for controlling drivetrains.
 * @author Class of '21 (created in 2018 season)
 */
public class TeleopDriveTrainController extends Command {
	
	private double lastThrottle = 0;
	private double lastTurnSteepness = 0;
	
	private Date lastUpdate = new Date();
	
	/**
	 * Constructor for TeleopDriveTrainController
	 * @author Class of '21 (created in 2018 season)
	 */
	public TeleopDriveTrainController() {
		requires(Robot.driveTrain);
	}
	
	/**
	 * Updates class variable values and sets motor speeds
	 * @author Class of '21 (created in 2018 season)
	 */
	public void execute() {
		double throttle = getNextThrottle(
				Robot.driveGamepad.getLeftJoystickY(), 
				this.lastThrottle);
		
		double turnSteepness = getNextThrottle(
				Robot.driveGamepad.getRightJoystickX(),
				this.lastTurnSteepness);
		
		if (throttle != 0 && turnSteepness != 0) { //arc turn
			setArcTrajectory(throttle, -turnSteepness);
			
		} else if (throttle != 0 && turnSteepness == 0) { //go forward
			Robot.driveTrain.setSpeed(throttle, throttle);
			
		} else if (throttle == 0 && turnSteepness != 0) { //spin in place
			/* the right motor's velocity has the opposite sign of the the left motor's
			 * since the right motor will spin in the opposite direction from the left
			 */
			Robot.driveTrain.setSpeed(turnSteepness, -turnSteepness);
			
		} else {
			Robot.driveTrain.setSpeed(0, 0);
		}
		
		this.lastThrottle = throttle;
		this.lastTurnSteepness = turnSteepness;
		this.lastUpdate = new Date();
	}
	
	/**
	 * Gets next throttle value
	 * @author Class of '21 (created in 2018 season)
	 * @param throttleInput Current throttle value
	 * @param lastThrottle Last throttle value
	 * @return Either 0 if the throttle is below Constants.zeroThrottleThreshold or the new throttle value
	 */
	public double getNextThrottle(double throttleInput, double lastThrottle) {
		double acceleration = ((throttleInput * Robot.driveTrain.currentThrottleMultiplier) - lastThrottle) / Constants.defaultAccelerationTime;
		double newThrottle = lastThrottle + ((new Date().getTime() - this.lastUpdate.getTime()) * acceleration);
		
		return Math.abs(newThrottle) < Constants.zeroThrottleThreshold ? 0.0 : newThrottle;
	}
	/**
	 * Sets motor for arc turns
	 * @author Class of '21 (created in 2018 season)
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
		
		Robot.driveTrain.setSpeed(leftWheelSpeed, rightWheelSpeed);
	}
	
	/**
	 * Calculates "inside" wheel motor speed based on the "outside" wheel speed and turnSteepness
	 * @author Class of '21 (created in 2018 season) & Old Coders
	 * @param outsideWheelSpeed Speed of other wheel
	 * @param turnSteepness How much the inside wheels should turn compared to the outside wheels
	 * @return Calculated inside wheel speed
	 */
	private double calculateInsideWheelSpeed(double outsideWheelSpeed, double turnSteepness) {
		/*
		 * Basically, the larger the turnSteepness the smaller the power should be
		 * We also add a turnSensitivity constant to make the robot turn slower
		 */
		double newSteepness = turnSteepness * 0.9;
		double turnValue = (turnSteepness > 0) ? 1 - newSteepness : 1 + newSteepness;
		return outsideWheelSpeed * turnValue;
	}

	@Override
	protected boolean isFinished() {
		return false;
	}
	
	@Override
	protected void end() {
		Robot.driveTrain.setSpeed(0,0);
	}

}
