package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class gyroTurn extends PIDCommand {
	AHRS ahrs = RobotMap.ahrs;
	DriveTrain dt = RobotMap.driveTrain;
    double kTargetAngleDegrees;
    
    /* The following PID Controller coefficients will need to be tuned 
     to match the dynamics of your drive system.  Note that the      
     SmartDashboard in Test mode has support for helping you tune    
     controllers by displaying a form where you can enter new P, I,  
     and D constants and test the mechanism.                         */
    
    static final double kP = 0.03;
    static final double kI = 0.00;
    static final double kD = 0.00;
    static final double kF = 0.00;

	/**
	 * Turns left or right
	 * @param inputDegrees Degrees to turn (Positive = right, negative = left)
	 */
	
	public gyroTurn(double degrees) {
		super("TurnController", kP, kI, kD);
		kTargetAngleDegrees = degrees;
		requires(RobotMap.driveTrain);
	}

	public void initialize() {
	    this.setInputRange(-180.0f, 180.0f);
	    this.getPIDController().setOutputRange(-1.0, 1.0);
	    this.getPIDController().setAbsoluteTolerance(3);
	    
	    this.getPIDController().setContinuous(true);
	    this.setName("DriveSystem", "RotateController");
	    LiveWindow.add(this);
	    
	    this.setSetpoint(kTargetAngleDegrees);
		ahrs.zeroYaw();
	}

	public void execute() {
		
	}
	
	public void end() {
		//PIDCommands automatically disable PID loops.
	}
	
	public void interrupted() {
		end();
	}
	
	public boolean isFinished() {
		return this.getPIDController().onTarget();
	}


	@Override
	protected double returnPIDInput() {
		// TODO Auto-generated method stub
		return ahrs.getAngle();
	}

	@Override
	protected void usePIDOutput(double output) {
		dt.left.setSpeed(output);
		dt.right.setSpeed(-output);
		
	}
}
