package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.Robot;
import org.usfirst.frc.team4990.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

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
		if (dir == direction.IN) {
			RobotMap.scaler.setSpeed(-Robot.getConst("TeleopScalerController/speed", 0.7));
			return;
		} if (dir == direction.OUT) {
			RobotMap.scaler.setSpeed(Robot.getConst("TeleopScalerController/speed", 0.7));
			return;
		} else {
			RobotMap.scaler.setSpeed(0);
		}
	}
	
	public void end() {
		RobotMap.scaler.setSpeed(0);
	}
	
	protected void interrupted() {
		end();
	}

	protected boolean isFinished() {
		return false;
	}

}
