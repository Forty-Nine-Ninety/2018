package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.*;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * Class for controlling drivetrains. DON'T USE THIS, WORK IN PROGRESS
 * @author Class of '21 (created in 2018 season)
 * @deprecated DON'T USE THIS, WORK IN PROGRESS
 */
public class TeleopArcadeDriveController extends Command implements PIDOutput{
	
	public enum DriveMode { STRAIGHT, ARC, TURN, NONE }
	
	private DriveMode driveMode;
	
	private PIDController turnController = new PIDController(0.03, 0, 0, 0, RobotMap.ahrs, this);
	private double turnControllerOutput = 0;
			
	
	/**
	 * Constructor for TeleopDriveTrainController
	 * @author Class of '21 (created in 2018 season)
	 */
	public TeleopArcadeDriveController() {
		requires(RobotMap.driveTrain);
		
		turnController.setSetpoint(0);
		turnController.setContinuous(false);
		turnController.setInputRange(-180, 180);
		turnController.setOutputRange(-0.5, 0.5);
		turnController.setName("TeleopDrive", "turnController");
		LiveWindow.add(turnController);
	}
	
	/**
	 * Updates class variable values and sets motor speeds
	 * @author Class of '21 (created in 2018 season)
	 */
	public void execute() {

		double throttle = RobotMap.driveGamepad.getLeftJoystickY();
		
		double turnSteepness = RobotMap.driveGamepad.getRightJoystickY();
		
		if (throttle != 0 && turnSteepness != 0) { //arc turn
			driveMode = DriveMode.ARC;
			RobotMap.differentialDrive.arcadeDrive(throttle, -turnSteepness);
			
		} else if (throttle != 0 && turnSteepness == 0) { //go forward
			if (driveMode != DriveMode.STRAIGHT) { //changed modes
				RobotMap.ahrs.reset();
				turnController.enable();
			}
			driveMode = DriveMode.STRAIGHT;
			RobotMap.driveTrain.setSpeed(throttle, turnControllerOutput);
			
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
		
		if (driveMode != DriveMode.STRAIGHT && turnController.isEnabled()) {
			turnController.disable();
			turnControllerOutput = 0;
		}

	}
	
	/**
	 * Squares the number provided and keeps sign (+ or -)
	 * @param throttleInput number to square and keeps sign
	 * @return squared number provided with same sign
	 */
	public double getSquaredThrottle(double throttleInput) {
		return throttleInput * throttleInput * Math.signum(throttleInput);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}
	
	@Override
	protected void end() {
		RobotMap.driveTrain.setSpeed(0,0);
	}
	
	protected void interrupted() {
		end();
	}

	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		output = turnControllerOutput;
	}

}
