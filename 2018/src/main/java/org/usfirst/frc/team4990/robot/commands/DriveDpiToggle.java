package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.Robot;

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
		TeleopDriveTrainController.currentThrottleMultiplier = Robot.getConst("DriveDpiToggle/lowThrottleMultiplier",
				0.25);
		System.out.println("DriveTrain Multiplier: " + TeleopDriveTrainController.currentThrottleMultiplier);
	}
	
	public void end() {
		TeleopDriveTrainController.currentThrottleMultiplier = Robot.getConst("DriveDpiToggle/maxThrottleMultiplier",
				1.0);
		System.out.println("DriveTrain Multiplier: " + TeleopDriveTrainController.currentThrottleMultiplier);
	}
	
	public void interrupted() {
		end();
	}
	
	public boolean isFinished() {
		return false;
	}
}
