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

public class GyroStraight extends Command implements PIDOutput{
	AHRS ahrs = RobotMap.ahrs;
	DriveTrain dt = RobotMap.driveTrain;

    /* The following PID Controller coefficients will need to be tuned 
     to match the dynamics of your drive system.  Note that the      
     SmartDashboard in Test mode has support for helping you tune    
     controllers by displaying a form where you can enter new P, I,  
     and D constants and test the mechanism.                         */
	
	PIDController turnController = new PIDController(SmartDashboardController.getConst("GyroStraight/tP", 0.008), 
	SmartDashboardController.getConst("GyroStraight/tI", 0), 
	SmartDashboardController.getConst("GyroStraight/tD", 0.03), (PIDSource) ahrs, this);
	double speed;

	public GyroStraight(double speed, double timeout) {
		this.setTimeout(timeout);
		this.speed = speed;
	}

	public void initialize() {
		System.out.println("Initalizing GyroStraight");
		ahrs.setPIDSourceType(PIDSourceType.kDisplacement);
	    this.setName("DriveSystem", "GyroStraight");    
	    SmartDashboard.putData(this);

		turnController.setInputRange(-180, 180);
		turnController.setOutputRange(-1, 1);
		turnController.setName("DriveSystem", "turnController");
		SmartDashboard.putData(turnController);
	  
		ahrs.reset();
		dt.left.encoder.reset();
		dt.right.encoder.reset();
		
		turnController.setPercentTolerance(2);
		turnController.setSetpoint(0);
		turnController.enable();
		turnController.setEnabled(true);
		
	}

	public void execute() {
		System.out.println("speed = " + speed + ", turnOutput = " + this.turnController.get() + ", ahrs = " + ahrs.pidGet() + ", isEnabled = "+turnController.isEnabled());
		this.pidOutput(this.turnController.get(), speed);
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
	
	public void pidOutput(double turnOutput, double speed) {
		dt.left.setSpeed(speed + turnOutput);
		dt.right.setSpeed(speed - turnOutput);
		dt.periodic();
	}

	@Override
	public void pidWrite(double output) {
		SmartDashboard.putNumber("turnController-output", output);
		SmartDashboard.putNumber("turnController-error", turnController.getError());
	}
}
