package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.Constants;
import org.usfirst.frc.team4990.robot.RobotMap;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * Ported from old TeleopDriveController
 * @author benjamin and Class of '21 (created in 2018 season)
 *
 */

public class DriveDpiToggle extends InstantCommand {
	public DriveDpiToggle() {
		if (RobotMap.driveTrain.currentThrottleMultiplier == Constants.maxThrottle) {
			RobotMap.driveTrain.currentThrottleMultiplier = Constants.lowThrottleMultiplier;
		} else {
			RobotMap.driveTrain.currentThrottleMultiplier = Constants.maxThrottle;
		}
	}
}
