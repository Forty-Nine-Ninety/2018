package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4990.robot.SmartDashboardController;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GyroTurn extends Command implements PIDOutput{
	AHRS ahrs = RobotMap.ahrs;
	DriveTrain dt = RobotMap.driveTrain;
	public final double DRIVETRAIN_WIDTH = (23 + 7 / 8) / 12;//In feet; move to Constants.java at some point maybe.

    /* The following PID Controller coefficients will need to be tuned 
     to match the dynamics of your drive system.  Note that the      
     SmartDashboard in Test mode has support for helping you tune    
     controllers by displaying a form where you can enter new P, I,  
     and D constants and test the mechanism.                         */
	
	PIDController turnController = new PIDController(SmartDashboardController.getConst("gyroTurn/tP", 0.02), 
	SmartDashboardController.getConst("gyroTurn/tI", 0.00002), 
	SmartDashboardController.getConst("gyroTurn/tD", 0.06), (PIDSource) ahrs, this);
	double maxSpeed;

	/**
	 * @param turnRadius The radius of turn in meters.
	 * @param isInPlace Whether to ignore turnRadius or not.
	 */
	public GyroTurn(double target, double maxSpeed, double timeout, double turnRadius, boolean isInPlace) {
		this.setTimeout(timeout);
		this.maxSpeed = SmartDashboardController.getConst("gyroTurn/speed", maxSpeed);
		SmartDashboardController.getConst("gyroTurn/turnRadius", turnRadius);
		SmartDashboardController.getBoolean("gyroTurn/turnInPlace", isInPlace);
		turnController.setSetpoint(target);
	}

	public GyroTurn(double target, double maxSpeed, double timeout) {
		this(target, maxSpeed, timeout, -1, true);
	}

	public void initialize() { 
		System.out.println("Initalizing GyroTurn");
		ahrs.setPIDSourceType(PIDSourceType.kDisplacement);
	    this.setName("DriveSystem", "GyroTurn");    
	    SmartDashboard.putData(this);

		ahrs.zeroYaw();
		turnController.setInputRange(-180, 180);
		turnController.setOutputRange(-maxSpeed, maxSpeed);
		turnController.setName("DriveSystem", "gyroTurn/turnController");
		SmartDashboard.putData(turnController);
	  
		dt.left.encoder.reset();
		dt.right.encoder.reset();
		
		turnController.setPercentTolerance(2);
		turnController.setContinuous(true);
		turnController.enable();
		turnController.setEnabled(true);
		
	}

	public void execute() {
		System.out.println("maxSpeed = " + maxSpeed + ", turnOutput = " + this.turnController.get() + ", ahrs = " + ahrs.pidGet() + ", isEnabled = "+turnController.isEnabled());
		if (SmartDashboardController.getBoolean("gyroTurn/turnInPlace", true)) {
			dt.left.setSpeed(this.turnController.get());
			dt.right.setSpeed(-this.turnController.get());
		}
		else {
			double radius = SmartDashboardController.getConst("gyroTurn/turnRadius", 0);
			double ratio;
			if (radius < 0) {
				radius += DRIVETRAIN_WIDTH / 2;
				ratio = radius / (radius + DRIVETRAIN_WIDTH);
				if (Math.abs(ratio) < 1) {
					dt.right.setSpeed(ratio * maxSpeed);
					dt.left.setSpeed(maxSpeed);
				}
				else {
					dt.right.setSpeed(maxSpeed);
					dt.left.setSpeed((1 / ratio) * maxSpeed);
				}
			}
			else if (radius > 0) {
				radius -= DRIVETRAIN_WIDTH / 2;
				ratio = radius / (radius + DRIVETRAIN_WIDTH);
				if (Math.abs(ratio) < 1) {
					dt.left.setSpeed(ratio * maxSpeed);
					dt.right.setSpeed(maxSpeed);
				}
				else {
					dt.left.setSpeed(maxSpeed);
					dt.right.setSpeed((1 / ratio) * maxSpeed);
				}
			}
			else {
				dt.left.setSpeed(this.turnController.get());
				dt.right.setSpeed(-this.turnController.get());
			}
		}
		dt.periodic();
		if(this.turnController.isEnabled() == false) turnController.setEnabled(true);
	}
	
	public void end() {
		turnController.disable();
	}
	
	public void interrupted() {
		end();
	}
	
	public boolean isFinished() {
		return this.isTimedOut();
	}

	@Override
	public void pidWrite(double output) {
		SmartDashboard.putNumber("gyroTurn/output", output);
		SmartDashboard.putNumber("gyroTurn/error", turnController.getError());
	}
}
