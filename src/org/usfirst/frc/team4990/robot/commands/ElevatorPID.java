package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
/**
 * Command that controls elevator in teleop
 * @author Class of '21 (created in 2018 season)
 */
public class ElevatorPID extends Command{
	
	double setpoint = 4;

	/**
	 * Constructor for TeleopElevatorController
	 * @author Class of '21 (created in 2018 season)
	 */
	public ElevatorPID() {
		requires(Robot.elevator);
	}
	
	public ElevatorPID(double setpoint) {
		this.setpoint = setpoint;
	}
	
	public void initalize() {
		Robot.elevator.elevatorPID.setSetpoint(setpoint);
		Robot.elevator.elevatorPID.enable();
	}
	
	/**
	 * Gets input from gamepad and sets motor power
	 * @author Class of '21 (created in 2018 season)
	 */
	
	public void execute() {
		System.out.println("Moving to " + Robot.elevator.elevatorPID.getSetpoint() + ", current speed: " + Robot.elevator.elevatorPID.get());

	}
	
	@Override
	protected void end() {
		Robot.elevator.elevatorPID.disable();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		Robot.elevator.elevatorPID.disable();
		Robot.elevator.setElevatorPower(Robot.elevator.stopFallingSpeed);
	}
	

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return Robot.elevator.elevatorPID.onTarget();
	}

}
