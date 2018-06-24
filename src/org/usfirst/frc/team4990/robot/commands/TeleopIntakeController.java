package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Class for intake in teleop period
 * @author Class of '21 (created in 2018 season)
 *
 */
public class TeleopIntakeController extends Command{
	
	public enum direction {IN, OUT};
	
	private direction dir;

	private double maxSpeed = 0.65;
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
		double tempInAxis = Robot.opGamepad.getLeftTrigger();
		double tempOutAxis = Robot.opGamepad.getRightTrigger();
		boolean override = Robot.opGamepad.getXButtonPressed();

		if (dir == direction.IN && ((Robot.intake.getAnalogInput() < 1.9 || override) || override)) { //left bumper = elevator UP
			if (tempInAxis > maxSpeed) {
				Robot.intake.setSpeed(maxSpeed);
			} else { 
				Robot.intake.setSpeed(tempInAxis);
			}
			return;
		} else if (dir == direction.OUT) { 
			if (tempOutAxis > maxSpeed) {
				Robot.intake.setSpeed(-maxSpeed);
			} else { 
				Robot.intake.setSpeed(-tempOutAxis); 
			}
			return;
		} else {
			Robot.intake.setSpeed(0.0);
			return;
		}
	}
	
	@Override
	protected void end() {
		Robot.intake.setSpeed(0.0);
	}
	
	@Override
	public void cancel() {
		Robot.intake.setSpeed(0.0);
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
