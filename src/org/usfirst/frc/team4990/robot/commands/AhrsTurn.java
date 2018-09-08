package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Use gyroTurn instead, depreciated.
 * @see gyroTurn
 * @depreciated
 * @author Class of '21
 *
 */

public class AhrsTurn extends Command implements PIDSource, PIDOutput {
	private AHRS ahrs;
	private DriveTrain dt;
	private double maxSpeed = 0.65;
	private double regSpeed = 0.35;
	private PIDController yawPIDController;
	private double heading = 0;
	private double kP = 0.03;
	private double kI = 0;
	private double kD = 0;
	
	private PIDSourceType pidSourceType = PIDSourceType.kRate;
	
	public AhrsTurn(double heading_input) {
		requires(RobotMap.driveTrain);
		this.ahrs = RobotMap.ahrs;
		this.dt = RobotMap.driveTrain;
		this.heading = heading_input;
	}
	
	public void initialize() {
		
		yawPIDController = new PIDController(kP, kI, kD, this, this);
		yawPIDController.setInputRange(-180f, 180f);
		//ahrs.setPIDSourceType(PIDSourceType.kRate);
		yawPIDController.setSetpoint(heading);
        yawPIDController.setOutputRange(-maxSpeed, maxSpeed);
        yawPIDController.setAbsoluteTolerance(3);
        yawPIDController.setContinuous(true);
        yawPIDController.enable();
        //ahrs.zeroYaw();
	}

	public void execute() {
		if (yawPIDController.onTarget()) { //go straight
			dt.setSpeed(0);
		} else if (yawPIDController.get() > 0) { //turn right?
			dt.setSpeed(regSpeed + yawPIDController.get(), regSpeed - yawPIDController.get()); 
		} else if (yawPIDController.get() < 0) {//turn left?
			dt.setSpeed(regSpeed - yawPIDController.get(), regSpeed + yawPIDController.get());
		}
	}

	public boolean isFinished() {
		return yawPIDController.onTarget();
	}

	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		pidSourceType = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return pidSourceType;
	}

	@Override
	public double pidGet() {
		return ahrs.getAngle();
	}
}
