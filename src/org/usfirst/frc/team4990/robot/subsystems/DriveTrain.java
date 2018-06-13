package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveTrain extends Subsystem implements PIDOutput  {
	public Gearbox left, right;
	
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
	 * @author Freshman Union
	 */
	public DriveTrain(Gearbox gearbox, Gearbox gearbox2) {
		this.left = gearbox;
		this.right = gearbox2;
		
		// The right gearbox is backwards
		this.right.fix_backwards = -1.0;
		// the bot swerves to the right, so slow down left side
		this.left.compensate = 0.9;
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
	
	
	/**
	 * Actually sets the speeds of all drive train motors.
	 */
	
	public void update() {
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

	public void pidWrite(double output) {
		//setSpeed(output);
		//speed set somewhere else!
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
}
