package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;

public class MoveElevator extends Command {
	private Elevator elevator;
	private boolean isFinished;
	private PIDController elevatorPID;
	
	/**
	 * Moves elevator a set distance
	 * @param distance Distance to move; positive is up, negative is down I think
	 * TODO: Needs to be modified for actual encoder
	 */
	
	public MoveElevator(double dist) {
		requires(RobotMap.elevator);
		this.elevator = RobotMap.elevator;
		this.isFinished = false;
		this.elevatorPID = elevator.elevatorPID;
	}
	
	public void initialize() {
		elevator.resetEncoderDistance();
		elevatorPID.enable();
	}
	
	public void execute() {
		elevator.setElevatorPower(elevatorPID.get());
			
		if (elevatorPID.onTarget()){ //isFinished
			isFinished = true;
			elevatorPID.disable();
		}
		
		if (this.isFinished) {
			elevator.setElevatorPower(0);
		}
		
	}
	
	public boolean isFinished() {
		return this.isFinished;
	}
}
