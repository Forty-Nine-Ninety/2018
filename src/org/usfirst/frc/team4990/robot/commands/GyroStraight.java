package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.Robot;
import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GyroStraight extends PIDCommand implements PIDOutput{
	AHRS ahrs = RobotMap.ahrs;
	DriveTrain dt = RobotMap.driveTrain;

    /* The following PID Controller coefficients will need to be tuned 
     to match the dynamics of your drive system.  Note that the      
     SmartDashboard in Test mode has support for helping you tune    
     controllers by displaying a form where you can enter new P, I,  
     and D constants and test the mechanism.                         */
  
	PIDController distanceController = new PIDController(SmartDashboardController.getConst("GyroStraight/distance_kP", 1),
			SmartDashboardController.getConst("GyroStraight/distance_kI", 0), SmartDashboardController.getConst("GyroStraight/distance_kD", 0), 0, dt, this);
	double kTargetDistance = SmartDashboardController.getConst("GyroStraight/defaultDistance", 6); // Feet? (check distance, should be
																				// distance to switch)
	double distanceControllerOutput, turnControllerOutput;
	
	public GyroStraight(double distance) {
		super("StraightController", SmartDashboardController.getConst("GyroStraight/turn_kP", 0.03),
				SmartDashboardController.getConst("GyroStraight/turn_kP", 0), SmartDashboardController.getConst("GyroStraight/turn_kP", 0));
		kTargetDistance = distance;
		//requires(RobotMap.driveTrain);
	}

	public GyroStraight() {
		super("StraightController", SmartDashboardController.getConst("GyroStraight/turn_kP", 0.03),
				SmartDashboardController.getConst("GyroStraight/turn_kP", 0), SmartDashboardController.getConst("GyroStraight/turn_kP", 0));
		//requires(RobotMap.driveTrain);
	}

	public void initialize() {
		System.out.println("Initalizing GyroStraight with distance " + kTargetDistance);
		this.setInputRange(-SmartDashboardController.getConst("GyroStraight/straight_inputRange", 180.0f),
				SmartDashboardController.getConst("GyroStraight/straight_inputRange", 180.0f));
		this.getPIDController().setOutputRange(-SmartDashboardController.getConst("GyroStraight/straight_outputRange", 0.3),
				SmartDashboardController.getConst("GyroStraight/straight_outputRange", 0.3));
		this.getPIDController().setAbsoluteTolerance(SmartDashboardController.getConst("GyroStraight/straight_absoluteTolerance", 3));
	    this.setSetpoint(0);
	    this.getPIDController().setContinuous(true);
	    
	    this.setName("DriveSystem", "StraightController");    
	    SmartDashboard.putData(this);

		ahrs.zeroYaw();
		
		distanceController.setOutputRange(-SmartDashboardController.getConst("GyroStraight/distance_outputRange", 0.7),
				SmartDashboardController.getConst("GyroStraight/distance_outputRange", 0.7));
		distanceController.setAbsoluteTolerance(SmartDashboardController.getConst("GyroStraight/distance_absoluteTolerance", 0.2)); // approximately
																													// 2.4
																													// in
		distanceController.setContinuous(false);
	    distanceController.setSetpoint(this.kTargetDistance);
		
		distanceController.setName("DriveSystem", "DistanceController");    
		SmartDashboard.putData(distanceController);
	  
		dt.left.encoder.reset();
		dt.right.encoder.reset();
		
		distanceController.enable();
	}

	public void execute() {
		System.out.println("This should be running in execute() of gyroStreight");
	}
	
	public void end() {
		//PIDCommands automatically disable internal PID controller.
		distanceController.disable();
		this.getPIDController().disable();
	}
	
	public void interrupted() {
		end();
	}
	
	public boolean isFinished() {
		return this.timeSinceInitialized() > SmartDashboardController.getConst("GyroStraight/timeout", 15);// this.getPIDController().onTarget();
	}


	@Override
	protected double returnPIDInput() {
		return ahrs.getAngle();
	}

	@Override
	protected void usePIDOutput(double output) {
		// used with TURN controller
		turnControllerOutput = output;
		pidOutput(output, distanceControllerOutput);
		
	}

	@Override
	public void pidWrite(double output) {
		// used with DISTANCE controller
		distanceControllerOutput = output;
		pidOutput(turnControllerOutput, output);
	}
	
	public void pidOutput(double turnOutput, double distanceOutput) {
		dt.left.setSpeed(distanceOutput + turnOutput);
		dt.right.setSpeed(distanceOutput - turnOutput);
	}
}
