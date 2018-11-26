package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.RobotMap;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
/**
 * Class for controlling drivetrains.
 * @author Class of '21 (created in 2018 season)
 */
public class TeleopDriveTrainController extends Command{
	
	public enum DriveMode { STRAIGHT, ARC, TURN, NONE }
	
	public DriveMode driveMode;	
	
	public static double currentThrottleMultiplier = 1;

	public static boolean oldStickShapingMethod = false;
	
	/**
	 * Constructor for TeleopDriveTrainController
	 * @author Class of '21 (created in 2018 season)
	 */
	public TeleopDriveTrainController() {
		requires(RobotMap.driveTrain);
	}
	
	/**
	 * Updates class variable values and sets motor speeds
	 * @author Class of '21 (created in 2018 season)
	 */
	public void execute() {
		/*double throttle = getNextThrottle(
				RobotMap.driveGamepad.getLeftJoystickY(), 
				this.lastThrottle);
		
		double turnSteepness = getNextThrottle(
				RobotMap.driveGamepad.getRightJoystickX(),
				this.lastTurnSteepness);
				*/
		double throttle = getSquaredThrottle(RobotMap.driveGamepad.getLeftJoystickY());
		
		double turnSteepness = getSquaredThrottle(RobotMap.driveGamepad.getRightJoystickX());
		
		if (throttle != 0 && turnSteepness != 0) { //arc turn
			driveMode = DriveMode.ARC;
			setArcTrajectory(throttle, -turnSteepness);
			
		} else if (throttle != 0 && turnSteepness == 0) { //go forward
			if (driveMode != DriveMode.STRAIGHT) { //changed modes
				RobotMap.ahrs.reset();
			}
			driveMode = DriveMode.STRAIGHT;
			RobotMap.driveTrain.setSpeed(throttle, throttle);
			
		} else if (throttle == 0 && turnSteepness != 0) { //spin in place
			/* the right motor's velocity has the opposite sign of the the left motor's
			 * since the right motor will spin in the opposite direction from the left
			 */
			driveMode = DriveMode.TURN;
			RobotMap.driveTrain.setSpeed(turnSteepness, -turnSteepness);
			
		} else {
			driveMode = DriveMode.NONE;
			RobotMap.driveTrain.setSpeed(0, 0);
		}
	}
	
	/**
	 * Squares the number provided and keeps sign (+ or -)
	 * @param throttleInput number to square and keeps sign
	 * @return squared number provided with same sign
	 */
	public double getSquaredThrottle(double throttleInput) {
		return throttleInput * throttleInput * Math.signum(throttleInput) * currentThrottleMultiplier;
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
		else {
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
		
		RobotMap.driveTrain.setSpeed(leftWheelSpeed, rightWheelSpeed);
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
		return !DriverStation.getInstance().isOperatorControl();
	}
	
	@Override
	protected void end() {
		RobotMap.driveTrain.setSpeed(0,0);
	}

}
