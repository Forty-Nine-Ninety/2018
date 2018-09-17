package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.*;

//import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * Class for controlling drivetrains. NEEDS FURTHER DEVELOPMENT AND TESTING.
 * @author Class of '21 (created in 2018 season)
 * 
 */
public class TeleopArcadeDriveController extends Command implements PIDOutput{
	
	public enum DriveMode { STRAIGHT, ARC, TURN, NONE }
	
	private DriveMode driveMode;
	
	private PIDController turnController = new PIDController(0.03, 0, 0, 0, RobotMap.ahrs, this);
	
	private PIDController arcController = new PIDController(0.03, 0, 0, 0, RobotMap.ahrs, this);
	
    double leftMotorOutput;
    double rightMotorOutput;
    
    //private Counter turnSteepnessCounter = new Counter();
			
	
	/**
	 * Constructor for TeleopDriveTrainController
	 * @author Class of '21 (created in 2018 season)
	 */
	public TeleopArcadeDriveController() {
		requires(RobotMap.driveTrain);
		
		turnController.setSetpoint(0);
		turnController.setContinuous(true);
		turnController.setInputRange(-180, 180);
		turnController.setOutputRange(-0.5, 0.5);
		turnController.setName("TeleopDrive", "turnController");
		LiveWindow.add(turnController);
		
		arcController.setSetpoint(0);
		arcController.setContinuous(true);
		arcController.setInputRange(-180, 180);
		arcController.setOutputRange(-0.5, 0.5);
		arcController.setName("TeleopDrive", "arcController");
		LiveWindow.add(arcController);
	}
	
	/**
	 * Updates class variable values and sets motor speeds
	 * @author Class of '21 (created in 2018 season)
	 */
	public void execute() {

		double throttle = RobotMap.driveGamepad.getLeftJoystickY();
		
		double turnSteepness = RobotMap.driveGamepad.getRightJoystickY();
		
		if (throttle != 0 && turnSteepness != 0) { //arc turn
			if (driveMode != DriveMode.ARC) { //if mode changed
				RobotMap.ahrs.reset();
				arcController.enable();
			}
			driveMode = DriveMode.ARC;
			
			//arcController.setSetpoint(turnSteepnessCounter.get());
			this.arcDrive(getSquaredThrottle(throttle), -getSquaredThrottle(turnSteepness), false);
			RobotMap.driveTrain.setSpeed(leftMotorOutput /*+ arcController.get()*/, rightMotorOutput /*- arcController.get()*/);
			
		} else if (throttle != 0 && turnSteepness == 0) { //go forward
			if (driveMode != DriveMode.STRAIGHT) { //if mode changed
				RobotMap.ahrs.reset();
				turnController.enable();
			}
			
			driveMode = DriveMode.STRAIGHT;
			this.arcDrive(getSquaredThrottle(throttle), turnController.get(), true);
			
		} else if (throttle == 0 && turnSteepness != 0) { //spin in place
			
			/* the right motor's velocity has the opposite sign of the the left motor's
			 * since the right motor will spin in the opposite direction from the left
			 */
			driveMode = DriveMode.TURN;
			RobotMap.driveTrain.setSpeed(getSquaredThrottle(turnSteepness), -getSquaredThrottle(turnSteepness));
			
		} else {
			driveMode = DriveMode.NONE;
			RobotMap.driveTrain.setSpeed(0, 0);
		}
		
		if (driveMode != DriveMode.STRAIGHT && turnController.isEnabled()) {
			turnController.disable();
		}
		
		if (driveMode != DriveMode.ARC && arcController.isEnabled()) {
			arcController.disable();
		}

	}
	
	/**
	 * Adapted from WPILib class DifferentialDrive, method archadeDrive
	 * @param throttle      The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
	 * @param turnSteepness   The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
	 *                    positive.
	 * @param setSpeed Boolean, true will set motor speeds at end of function
	 */
	
	public void arcDrive(double throttle, double turnSteepness, boolean setSpeed) {
		throttle = limit(throttle);
	    turnSteepness = limit(turnSteepness);

	    double maxInput = Math.copySign(Math.max(Math.abs(throttle), Math.abs(turnSteepness)), throttle);

	    if (throttle >= 0.0) {
	      // First quadrant, else second quadrant
	      if (turnSteepness >= 0.0) {
	        leftMotorOutput = maxInput;
	        rightMotorOutput = throttle - turnSteepness;
	      } else {
	        leftMotorOutput = throttle + turnSteepness;
	        rightMotorOutput = maxInput;
	      }
	    } else {
	      // Third quadrant, else fourth quadrant
	      if (turnSteepness >= 0.0) {
	        leftMotorOutput = throttle + turnSteepness;
	        rightMotorOutput = maxInput;
	      } else {
	        leftMotorOutput = maxInput;
	        rightMotorOutput = throttle - turnSteepness;
	      }
	    }
	    
	    if (setSpeed) {
	    	RobotMap.driveTrain.setSpeed(limit(leftMotorOutput), -limit(rightMotorOutput));
	    }
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
	}
	
	/**
	 * Squares the number provided and keeps sign (+ or -)
	 * @param throttleInput number to square and keeps sign
	 * @return squared number provided with same sign
	 */
	public double getSquaredThrottle(double value) {
		return Math.copySign(value * value, value);
	}
	
	/**
	* Limit motor values to the -1.0 to +1.0 range.
	*/
	public double limit(double value) {
	    if (value > 1.0) {
	      return 1.0;
	    }
	    if (value < -1.0) {
	      return -1.0;
	    }
	    return value;
	}

}
