package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.RobotMap;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class MoveElevatorTime extends TimedCommand {
	private double power;

	/**
	 * Moves elevator for duration.
	 * @param time in seconds
	 * @param power 0 to 1 for up, 0 to -1 for down
	 */
	
	public MoveElevatorTime(double t, double power_input) {
		super("MoveElevatorTime",t);
		requires(RobotMap.elevator);
		this.power = power_input;
	}

	public void initialize() {
		RobotMap.elevator.setElevatorPower(power);
	}
	
	public void end() {
		RobotMap.elevator.setElevatorPower(0);
	}
	
	public void interrupted() {
		end();
	}
}
