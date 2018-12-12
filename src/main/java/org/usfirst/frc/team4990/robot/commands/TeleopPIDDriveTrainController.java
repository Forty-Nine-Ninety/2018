package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.SmartDashboardController;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 * Class for controlling drivetrains.
 * @author Class of '21 (created in 2018 season)
 */
public class TeleopPIDDriveTrainController extends Command implements PIDOutput {
	
	public enum DriveMode { STRAIGHT, ARC, TURN, NONE }
	public DriveMode driveMode;	
	
	public static double currentThrottleMultiplier = 1;
	public static boolean oldStickShapingMethod = false;

	double throttle, turnSteepness;
	public PIDController turnController, straightController, arcController;
	/**
	 * Constructor for TeleopDriveTrainController
	 * @author Class of '21 (created in 2018 season)
	 */
	public TeleopPIDDriveTrainController() {
		requires(RobotMap.driveTrain);
	}
	
	public void initialize() {
	    this.setName("DriveSystem", "PIDTeleopDriveTrainController");    
	    SmartDashboard.putData(this);

		RobotMap.ahrs.setPIDSourceType(PIDSourceType.kDisplacement);
		RobotMap.ahrs.zeroYaw();
		
		RobotMap.driveTrain.left.encoder.reset();
		RobotMap.driveTrain.right.encoder.reset();

		straightController = new PIDController(0, 0, 0, (PIDSource) RobotMap.ahrs, this);
		straightController.setPID(SmartDashboardController.getConst("DriveSystem/PIDTeleopDriveTrainController/straight/p", 0.2),
			SmartDashboardController.getConst("DriveSystem/PIDTeleopDriveTrainController/straight/i", 0), 
			SmartDashboardController.getConst("DriveSystem/PIDTeleopDriveTrainController/straight/d",0)); 
		straightController.setContinuous(true);
		straightController.setInputRange(-360, 360);
		straightController.setOutputRange(-1, 1);
		straightController.setName("DriveSystem/PIDTeleopDriveTrainController", "straightController");
		SmartDashboard.putData(straightController);
		straightController.setPercentTolerance(2);
		straightController.setSetpoint(0);

		turnController = new PIDController(0, 0, 0, (PIDSource) RobotMap.ahrs, this);
		turnController.setPID(SmartDashboardController.getConst("DriveSystem/PIDTeleopDriveTrainController/turn/p", 0.2),
			SmartDashboardController.getConst("DriveSystem/PIDTeleopDriveTrainController/turn/i", 0), 
			SmartDashboardController.getConst("DriveSystem/PIDTeleopDriveTrainController/turn/d",0)); 
		turnController.setContinuous(true);
		turnController.setInputRange(-360, 360);
		turnController.setOutputRange(-1, 1);
		turnController.setName("DriveSystem/PIDTeleopDriveTrainController", "turnController");
		turnController.setPercentTolerance(2);
		turnController.setSetpoint(0);
		SmartDashboard.putData(turnController);

		arcController = new PIDController(0, 0, 0, (PIDSource) RobotMap.ahrs, this);
		arcController.setPID(SmartDashboardController.getConst("DriveSystem/PIDTeleopDriveTrainController/arc/p", 0.2),
			SmartDashboardController.getConst("DriveSystem/PIDTeleopDriveTrainController/arc/i", 0), 
			SmartDashboardController.getConst("DriveSystem/PIDTeleopDriveTrainController/arc/d",0)); 
		arcController.setContinuous(true);
		arcController.setInputRange(-360, 360);
		arcController.setOutputRange(-1, 1);
		arcController.setName("DriveSystem/PIDTeleopDriveTrainController", "arcController");
		arcController.setPercentTolerance(2);
		arcController.setSetpoint(0);
		SmartDashboard.putData(arcController);
	}

	/**
	 * Updates class variable values and sets motor speeds
	 * @author Class of '21 (created in 2018 season)
	 */
	public void execute() {

		throttle = getSquaredThrottle(RobotMap.driveGamepad.getLeftJoystickY());
		turnSteepness = getSquaredThrottle(RobotMap.driveGamepad.getRightJoystickX());
		
		if (throttle != 0 && turnSteepness != 0) { //arc turn
			if (driveMode != DriveMode.ARC) { //changed modes
				stateChangeReset();
			}
			driveMode = DriveMode.ARC;
			setArcTrajectory(throttle, -turnSteepness);
			
		} else if (throttle != 0 && turnSteepness == 0) { //go forward
			if (driveMode != DriveMode.STRAIGHT) { //changed modes
				stateChangeReset();
				straightController.enable();
			}
			driveMode = DriveMode.STRAIGHT;
			RobotMap.driveTrain.setSpeed(throttle + straightController.get(), 
				throttle - straightController.get());
			
		} else if (throttle == 0 && turnSteepness != 0) { //spin in place
			if (driveMode != DriveMode.TURN) { //changed modes
				stateChangeReset();
				//do we want PID for turning?
				//turnController.enable();
			}
			driveMode = DriveMode.TURN;
			/* the right motor's velocity has the opposite sign of the the left motor's
			 * since the right motor will spin in the opposite direction from the left */
			RobotMap.driveTrain.setSpeed(turnSteepness, -turnSteepness);
			
		} else {
			if (driveMode != DriveMode.NONE) { //changed modes
				stateChangeReset();
			}
			driveMode = DriveMode.NONE;
			RobotMap.driveTrain.setSpeed(0, 0);
		}
	}

	public void stateChangeReset() {
		RobotMap.ahrs.reset();

		RobotMap.driveTrain.left.encoder.reset();
		RobotMap.driveTrain.right.encoder.reset();

		straightController.disable();
		turnController.disable();
		arcController.disable();

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
		stateChangeReset();
	}

	@Override
	protected void interrupted() {
		end();
	}

	@Override
	public void pidWrite(double output) {
		//nothing
	}

}
