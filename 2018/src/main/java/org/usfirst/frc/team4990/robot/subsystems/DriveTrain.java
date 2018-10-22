package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.Robot;
import org.usfirst.frc.team4990.robot.commands.TeleopDriveTrainController;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveTrain extends Subsystem implements PIDSource {
	public Gearbox left, right;
	public Command defaultCommand;
	public PIDSourceType pidSourceType = PIDSourceType.kDisplacement;

	/**
	 * Includes 4 driving motors and 2 encoders. All passed as gearbox constructors!
	 * 
	 * @param leftGearbox
	 *            left gearbox
	 * @param rightGearbox
	 *            right gearbox
	 * @author Class of '21 (created in 2018 season)
	 */
	public DriveTrain(Gearbox leftGearbox, Gearbox rightGearbox) {
		this.left = leftGearbox;
		this.right = rightGearbox;

		// The right gearbox is backwards
		this.right.fix_backwards = -1.0;
		// the bot swerves to the left, so slow down right side
		this.left.compensate = Robot.getConst("leftGearbox/compensate", 1.0);
		this.right.compensate = Robot.getConst("rightGearbox/compensate", 0.99);

		/**
		 * ramp down time in seconds.
		 */

		double rampDownTime = Robot.getConst("DriveTrain/rampDownTime", 0.3);

		this.left.frontMotor.configOpenloopRamp(rampDownTime, 0);
		this.left.rearMotor.configOpenloopRamp(rampDownTime, 0);
		this.right.frontMotor.configOpenloopRamp(rampDownTime, 0);
		this.right.rearMotor.configOpenloopRamp(rampDownTime, 0);
	}

	/**
	 * Sets speed of all drive train motors.
	 * 
	 * @param speed
	 *            speed to set, min -1, max 1 (stop is 0)
	 */

	public void setSpeed(double speed) {
		left.setSpeed(speed);
		right.setSpeed(speed);
	}

	/**
	 * Sets speed of all drive train motors.
	 * 
	 * @param leftSpeed
	 *            speed to set, min -1, max 1 (stop is 0)
	 * @param rightSpeed
	 *            speed to set, min -1, max 1 (stop is 0)
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
		// if (DriverStation.getInstance().isOperatorControl()) {
		this.setDefaultCommand(defaultCommand = new TeleopDriveTrainController());
		// }
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		pidSourceType = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return this.pidSourceType;
	}

	/**
	 * Returns raw average left/right encoder value, in unknown units. Use pidGet()
	 * to return distance in feet.
	 */

	public double getEncoderDistance() {
		return (/* left.encoder.getDistance() * */right.encoder.getDistance()); /// 2;
	}

	/**
	 * Returns right encoder value, in feet.
	 */
	public double pidGet() {
		// return (left.encoder.getDistance() * right.encoder.getDistance())/2;
		return right.encoder.getDistance();
	}

}
