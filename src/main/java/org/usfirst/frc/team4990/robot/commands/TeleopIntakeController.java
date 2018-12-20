package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.OI;
import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.SmartDashboardController;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Class for intake in teleop period
 * @author Class of '21 (created in 2018 season)
 *
 */
public class TeleopIntakeController extends Command{
	
	public enum direction {IN, OUT};
	
	private direction dir;

	private double maxSpeed = SmartDashboardController.getConst("TeleopIntakeController/maxSpeed", 0.65);
	/**
	 * Constructor for class
	 * @author Class of '21 (created in 2018 season)
	 */
	public TeleopIntakeController(direction dir) {
		this.dir = dir;
	}
	
	public TeleopIntakeController() {
	}
	/**
	 * Read input and updates motor speeds
	 * @author Class of '21 (created in 2018 season)
	 */
	public void execute() {
		double tempInAxis = RobotMap.opGamepad.leftTrigger.getRawAxis();
		double tempOutAxis = RobotMap.opGamepad.rightTrigger.getRawAxis();
		//boolean override = RobotMap.opGamepad.getXButtonPressed();

		if (dir == direction.IN /*&& ((RobotMap.intake.getAnalogInput() < 1.9 || override) || override)*/) { //left bumper = elevator UP
			if (tempInAxis > maxSpeed) {
				RobotMap.intake.setSpeed(maxSpeed);
			} else { 
				RobotMap.intake.setSpeed(tempInAxis);
			}
		} else if (dir == direction.OUT) { 
			if (tempOutAxis > maxSpeed) {
				RobotMap.intake.setSpeed(-maxSpeed);
			} else { 
				RobotMap.intake.setSpeed(-tempOutAxis); 
			}
		} else {
			RobotMap.intake.setSpeed(0.0);
		}
	}
	
	protected void end() {
		RobotMap.intake.setSpeed(0.0);
	}
	
	public void interupted() {
		end();
	}
	
	protected boolean isFinished() {
		if (dir == direction.IN) {
			return OI.intakeINAnalogButton.getRawAxis() < SmartDashboardController.getConst("TeleopIntakeController/triggerDeadband", 0.05);
		} else {
			return OI.intakeOUTAnalogButton.getRawAxis() < SmartDashboardController.getConst("TeleopIntakeController/triggerDeadband", 0.05);
		}
	}

}
