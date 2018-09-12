package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.subsystems.Intake;
import org.usfirst.frc.team4990.robot.subsystems.Intake.BoxPosition;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeIn extends Command {
	private Intake intake;
	private boolean isFinished = false;
	private double speed = 0.6;

	/**
	 * Makes intake take in whatever is in front of it (people included)
	 */
	
	public IntakeIn() {
		requires(RobotMap.intake);
		this.intake = RobotMap.intake;

	}
	
	public void initialize() {
		//nothing.
	}
	
	public void execute() {
		BoxPosition boxPos = intake.getBoxPosition();
		if (boxPos.equals(BoxPosition.IN)) {
			isFinished = true;
		} else if (boxPos.equals(BoxPosition.MOVING) || boxPos.equals(BoxPosition.OUT)) {
			intake.setSpeed(speed);
		}
		
		intake.update();
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public void end() {
		intake.setSpeed(0);
		intake.update();
	}
	
	public void interupted() {
		end();
	}
}
