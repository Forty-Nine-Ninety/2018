package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.Robot;
import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeOut extends Command {
	private Intake intake;
	private boolean isFinished;
	private double timeout = 0;
	
	/**
	 * Makes intake throw whatever is inside out
	 */
	
	public IntakeOut() {
		requires(RobotMap.intake);
		this.intake = RobotMap.intake;
		this.isFinished = false;
	}
	
	public IntakeOut(double time) {
		requires(RobotMap.intake);
		this.intake = RobotMap.intake;
		this.isFinished = false;
		this.timeout = time;
	}
	
	public void initialize() {
		System.out.println("IntakeOut Starting");
	}
	
	public void execute() {
		/*BoxPosition boxPos = intake.getBoxPosition();
		if (boxPos.equals(BoxPosition.OUT)) {
			isFinished = true;
		} else if (boxPos.equals(BoxPosition.MOVING) || boxPos.equals(BoxPosition.IN)) {
			intake.setSpeed(speed);

		}*/
		intake.setSpeed(SmartDashboardController.getConst("IntakeOut/speed", -0.6));
		System.out.println("IntakeOut. time: " + this.timeSinceInitialized());
		intake.periodic();
	}
	
	public boolean isFinished() {
		return timeout > 0 ? this.timeSinceInitialized() >= timeout : isFinished;
	}
	
	public void end() {
		intake.setSpeed(0);
		intake.periodic();
	}
	
	public void interupted() {
		end();
	}
}
