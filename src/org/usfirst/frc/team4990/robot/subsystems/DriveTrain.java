package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.commands.TeleopDriveTrainController;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveTrain extends Subsystem implements PIDSource {
	public Gearbox left, right;
	public double currentThrottleMultiplier = 1.0;
	
	public boolean oldStickShapingMethod = true;
	
	public TeleopDriveTrainController teleopDriveTrainController;
	
	/**
	 * Includes 4 driving motors and 2 encoders. All passed as gearbox constructors!
	 * @param talonMotorController First Left Motor
	 * @param talonMotorController2 Second Left Motor
	 * @param rightotor1 First Right Motor
	 * @param talonMotorController4 Second Right Motor
	 * @param leftEncoderChannelA Encoder for Left gearbox (Signal, Ground and 5v)
	 * @param leftEncoderChannelB Encoder for Left gearbox (just Signal)
	 * @param rightEncoderChannelA Encoder for Right gearbox (Signal, Ground and 5v)
	 * @param rightEncoderChannelB Encoder for right gearbox (just Signal)
	 * @author Class of '21 (created in 2018 season)
	 */
	public DriveTrain(Gearbox gearbox, Gearbox gearbox2) {
		this.left = gearbox;
		this.right = gearbox2;
		
		// The right gearbox is backwards
		this.right.fix_backwards = -1.0;
		// the bot swerves to the right, so slow down left side
		this.left.compensate = 1.0;
		this.right.compensate = 1.0;
		
		/**
		 * ramp down time in seconds.
		 */
		
		double rampDownTime = 0.2; 
		
		this.left.frontMotor.configOpenloopRamp(rampDownTime, 0);
		this.left.rearMotor.configOpenloopRamp(rampDownTime, 0);
		this.right.frontMotor.configOpenloopRamp(rampDownTime, 0);
		this.right.rearMotor.configOpenloopRamp(rampDownTime, 0);
	}
	
	/**
	 * Sets speed of all drive train motors.
	 * @param speed speed to set, min -1, max 1 (stop is 0)
	 */
	
	public void setSpeed(double speed) {
		left.setSpeed(speed);
		right.setSpeed(speed);
	}
	
	/**
	 * Sets speed of all drive train motors.
	 * @param leftSpeed speed to set, min -1, max 1 (stop is 0)
	 * @param rightSpeed speed to set, min -1, max 1 (stop is 0)
	 */
	
	public void setSpeed(double leftSpeed, double rightSpeed) {
		left.setSpeed(leftSpeed);
		right.setSpeed(rightSpeed);
	}
	
	@Override
	public void periodic() {
		left.motorGroup.set(left.setSpeed * left.compensate * left.fix_backwards);
		right.motorGroup.set(right.setSpeed * right.compensate * right.fix_backwards);
	}
	
	/**
	 * Resets left and right encoder distances.
	 */

	public void resetDistanceTraveled() {
		left.encoder.reset();
		right.encoder.reset();
	}

	@Override
	protected void initDefaultCommand() {
		if (DriverStation.getInstance().isOperatorControl()) {
		teleopDriveTrainController = new TeleopDriveTrainController();
		super.setDefaultCommand(teleopDriveTrainController);
		}
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}
	/**
	 * Returns raw average left/right encoder value, in unknown units.
	 * Use pidGet() to return distance in feet.
	 */
	
	public double getEncoderDistance() {
		return (left.encoder.getDistance() * right.encoder.getDistance())/2;
	}

	/**
	 * Returns average left/right encoder value, in feet.
	 */
	public double pidGet() {
		return (left.encoder.getDistance() * right.encoder.getDistance())/2;
	}
		
}
