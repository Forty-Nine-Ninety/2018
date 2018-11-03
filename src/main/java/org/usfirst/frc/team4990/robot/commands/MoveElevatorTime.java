package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.RobotMap;
import edu.wpi.first.wpilibj.command.Command;

public class MoveElevatorTime extends Command {
	private double power;
	private double time;

	/**
	 * Moves elevator for duration.
	 * @param t in seconds
	 * @param power_input 0 to 1 for up, 0 to -1 for down
	 */
	
	public MoveElevatorTime(double t, double power_input) {
		this.time = t;
		//requires(RobotMap.elevator);
		this.power = power_input;
	}

	public void initialize() {
		
	}
	
	public void execute() {
		RobotMap.elevator.setElevatorPower(power);
		System.out.println("MoveElevatorTime. input power: " + power + " setpower: " + RobotMap.elevator.setSpeed + " time: " + this.timeSinceInitialized());
		RobotMap.elevator.periodic();
	}
	
	public void end() {
		RobotMap.elevator.setElevatorPower(0);
	}
	
	public void interrupted() {
		end();
	}

	@Override
	protected boolean isFinished() {
		return this.timeSinceInitialized() >= time;
	}
}
