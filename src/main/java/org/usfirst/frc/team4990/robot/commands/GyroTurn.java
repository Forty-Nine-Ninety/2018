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

    /* The following PID Controller coefficients will need to be tuned 
     to match the dynamics of your drive system.  Note that the      
     SmartDashboard in Test mode has support for helping you tune    
     controllers by displaying a form where you can enter new P, I,  
     and D constants and test the mechanism.                         */
	
	PIDController turnController = new PIDController(SmartDashboardController.getConst("gyroTurn/tP", 0.2), 
	SmartDashboardController.getConst("gyroTurn/tI", 0), 
	SmartDashboardController.getConst("gyroTurn/tD", 0), (PIDSource) ahrs, this);
	double maxSpeed;

	public GyroTurn(double target, double maxSpeed, double timeout) {
		this.setTimeout(timeout);
		this.maxSpeed = SmartDashboardController.getConst("gyroTurn/speed", maxSpeed);
		turnController.setSetpoint(target);
	}

	public void initialize() {
		System.out.println("Initalizing GyroTurn");
		ahrs.setPIDSourceType(PIDSourceType.kDisplacement);
	    this.setName("DriveSystem", "GyroTurn");    
	    SmartDashboard.putData(this);

		ahrs.zeroYaw();
		turnController.setInputRange(-360, 360);
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
		dt.left.setSpeed(this.turnController.get());
		dt.right.setSpeed(-this.turnController.get());
		dt.periodic();
		if(this.turnController.isEnabled() == false) {
			turnController.setEnabled(true);
		}
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
