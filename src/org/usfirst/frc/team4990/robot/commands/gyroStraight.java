package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class gyroStraight extends PIDCommand implements PIDOutput{
	AHRS ahrs = RobotMap.ahrs;
	DriveTrain dt = RobotMap.driveTrain;

    /* The following PID Controller coefficients will need to be tuned 
     to match the dynamics of your drive system.  Note that the      
     SmartDashboard in Test mode has support for helping you tune    
     controllers by displaying a form where you can enter new P, I,  
     and D constants and test the mechanism.                         */
    
    static final double T_kP = 0.03;
    static final double T_kI = 0.00;
    static final double T_kD = 0.00;
    static final double T_kF = 0.00;
    
    double kTargetAngleDegrees;
	double turnControllerOutput;
    
    /* The following PID Controller coefficients will need to be tuned 
    to match the dynamics of your drive system.  Note that the      
    SmartDashboard in Test mode has support for helping you tune    
    controllers by displaying a form where you can enter new P, I,  
    and D constants and test the mechanism.                         */
   
   static final double D_kP = 1;
   static final double D_kI = 0.00;
   static final double D_kD = 0.00;
   static final double D_kF = 0.00;
  
   PIDController distanceController = new PIDController(D_kP, D_kI, D_kD, D_kF, dt, this);
   double kTargetDistance = 6; //Feet? (check distance, should be distance to switch)
   double distanceControllerOutput;
	
	public gyroStraight(double distance) {
		super("StraightController", T_kP, T_kI, T_kD);
		kTargetDistance = distance;
		//requires(RobotMap.driveTrain);
	}

	public gyroStraight() {
		super("StraightController", T_kP, T_kI, T_kD);
		//requires(RobotMap.driveTrain);
	}

	public void initialize() {
		System.out.println("Initalizing gyroStraight with distance " + kTargetDistance);
	    this.setInputRange(-180.0f, 180.0f);
	    this.getPIDController().setOutputRange(-0.3, 0.3);
	    this.getPIDController().setAbsoluteTolerance(3);
	    this.setSetpoint(0);
	    this.getPIDController().setContinuous(true);
	    
	    this.setName("DriveSystem", "StraightController");    
	    SmartDashboard.putData(this);

		ahrs.zeroYaw();
		
		distanceController.setOutputRange(-0.7, 0.7);
		distanceController.setAbsoluteTolerance(0.2); //approximately 2.4 in
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
		return this.timeSinceInitialized() > 15;//this.getPIDController().onTarget();
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
