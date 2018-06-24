package org.usfirst.frc.team4990.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team4990.robot.Robot;

/**
 * Class for Scaler
 * @author Class of '21 (created in 2018 season)
 */
public class TeleopScalerController extends Command{
	public enum direction {IN, OUT};
	private direction dir;
	/**
	 * Constructor for class
	 * @author Class of '21 (created in 2018 season)
	 */
	public TeleopScalerController(direction dir) {
		this.dir = dir;
	}
	
	public TeleopScalerController() {
		this.dir = direction.OUT;
	}
	
	/**
	 * Reads input and updates scaler
	 * @author Class of '21 (created in 2018 season)
	 */
	public void initalize() {		
		if(dir.equals(direction.IN)) {
			Robot.scaler.setSpeed(-0.7);
			return;
		} if(dir.equals(direction.OUT)) {
			Robot.scaler.setSpeed(0.7);
			return;
		} else {
			Robot.scaler.setSpeed(0);
		}
	}
	
	public void end() {
		Robot.scaler.setSpeed(0);
	}
	
	protected void interrupted() {
		Robot.scaler.setSpeed(0);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
