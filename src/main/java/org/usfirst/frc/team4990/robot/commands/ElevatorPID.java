package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.SmartDashboardController;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * Command that controls elevator in teleop
 * 
 * @author Class of '21 (created in 2018 season)
 */
public class ElevatorPID extends PIDCommand {

	/*
	 * The following PID Controller coefficients will need to be tuned to match the
	 * dynamics of your elevator. Note that the SmartDashboard in Test mode has
	 * support for helping you tune controllers by displaying a form where you can
	 * enter new P, I, and D constants and test the mechanism.
	 */

	/**
	 * Constructor for ElevatorPID
	 * 
	 * @param setpoint
	 *            setpoint for PID system (in feet?)
	 * @author Class of '21 (created in 2018 season)
	 */

	public ElevatorPID(double setpoint) {
		super("ElevatorPID", SmartDashboardController.getConst("ElevatorPID/kP", 0.2), SmartDashboardController.getConst("ElevatorPID/kI", 0),
				SmartDashboardController.getConst("ElevatorPID/kD", 0), 0);
		requires(RobotMap.elevator);
		this.setSetpoint(setpoint);
	}

	public ElevatorPID() {
		super("ElevatorPID", SmartDashboardController.getConst("ElevatorPID/kP", 0.2), SmartDashboardController.getConst("ElevatorPID/kI", 0),
				SmartDashboardController.getConst("ElevatorPID/kD", 0), 0);
		requires(RobotMap.elevator);
		this.setSetpoint(SmartDashboardController.getConst("ElevatorPID/defaultSetpoint", 4));
	}

	public void initalize() {
		this.setInputRange(0, SmartDashboardController.getConst("ElevatorPID/maxInput", 5)); // minimumInput, maximumInput (in feet?) (fix
																			// maximum input)
		this.getPIDController().setContinuous(false);
		this.getPIDController().setOutputRange(-SmartDashboardController.getConst("ElevatorPID/outputRange", 0.7),
				SmartDashboardController.getConst("ElevatorPID/outputRange", 0.7)); // minimumOutput, maximumOutput (motor constraints)
		this.getPIDController().setPercentTolerance(SmartDashboardController.getConst("ElevatorPID/percentTolerance", 5));

		this.setName("Elevator", "ElevatorPIDController");
		LiveWindow.add(this);
	}

	/**
	 * @author Class of '21 (created in 2018 season)
	 */

	public void execute() {
		System.out.println("Moving to " + this.getSetpoint() + ", current speed: " + this.getPIDController().get());

	}

	@Override
	protected void end() {
		RobotMap.elevator.setElevatorPower(RobotMap.elevator.stopFallingSpeed);
	}

	@Override
	protected void interrupted() {
		end();

	}

	@Override
	protected boolean isFinished() {
		return this.getPIDController().onTarget();
	}

	@Override
	protected double returnPIDInput() {
		return RobotMap.elevator.getEncoderDistance(); // add factor to make this return feet from bottom of elevator
														// (or intake to ground?)
	}

	@Override
	protected void usePIDOutput(double output) {
		RobotMap.elevator.setElevatorPower(output);
	}

}
