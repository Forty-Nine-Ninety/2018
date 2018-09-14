package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.Constants;
import org.usfirst.frc.team4990.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Ported from old TeleopDriveController
 * @author benjamin and Class of '21 (created in 2018 season)
 *
 */

public class DriveDpiToggle extends Command {
	public DriveDpiToggle() {
	}
	
	public void initialize() {
		RobotMap.driveTrain.currentThrottleMultiplier = Constants.lowThrottleMultiplier;
		System.out.println("DriveTrain Multiplier: " + RobotMap.driveTrain.currentThrottleMultiplier);
	}
	
	public void end() {
		RobotMap.driveTrain.currentThrottleMultiplier = Constants.maxThrottle;
		System.out.println("DriveTrain Multiplier: " + RobotMap.driveTrain.currentThrottleMultiplier);
	}
	
	public void interrupted() {
		end();
	}
	
	public boolean isFinished() {
		return false;
	}
}
