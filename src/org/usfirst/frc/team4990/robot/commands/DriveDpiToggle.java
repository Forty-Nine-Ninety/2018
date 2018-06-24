package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.Constants;
import org.usfirst.frc.team4990.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Ported from old TeleopDriveController
 * @author benjamin and Class of '21 (created in 2018 season)
 *
 */

public class DriveDpiToggle extends Command {
	public DriveDpiToggle() {
		if (Robot.driveTrain.currentThrottleMultiplier == Constants.maxThrottle) {
			Robot.driveTrain.currentThrottleMultiplier = Constants.lowThrottleMultiplier;
		} else {
			Robot.driveTrain.currentThrottleMultiplier = Constants.maxThrottle;
		}
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}
}
