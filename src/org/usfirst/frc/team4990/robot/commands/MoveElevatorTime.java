package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.Robot;
import org.usfirst.frc.team4990.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

public class MoveElevatorTime extends Command {
	private boolean isFinished;
	private long duration;
	private double power;
	private long startMillis;
	private Elevator elevator;

	/**
	 * Moves elevator for duration.
	 * @param time in milliseconds
	 * @param power 0 to 1 for up, 0 to -1 for down
	 */
	
	public MoveElevatorTime(double t, double power_input) {
		requires(Robot.elevator);
		this.duration = (long) t;
		this.power = power_input;
		this.isFinished = false;
		this.elevator = Robot.elevator;
	}
	/**
	 * Clears time
	 */
	public void initialize() {
		this.startMillis = System.currentTimeMillis();
		System.out.println("moveElevatorTime(" + duration + ")");
		elevator.setElevatorPower(power);
	}
	/**
	 * Sets isFinished to true if time is up
	 */
	public void execute() {
		if (startMillis + duration <= System.currentTimeMillis()) {
			//done waiting!
			elevator.setElevatorPower(0);
			this.isFinished = true;
		}
	}
	/**
	 * Returns whether the command is done or not
	 */
	public boolean isFinished() {
		return this.isFinished;
	}
}
