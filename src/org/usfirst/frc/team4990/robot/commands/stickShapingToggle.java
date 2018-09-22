package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Ported from old TeleopDriveController
 * @author benjamin and Class of '21 (created in 2018 season)
 *
 */

public class stickShapingToggle extends Command {
	public stickShapingToggle() {
	}
	
	public void initialize() {
		RobotMap.driveTrain.oldStickShapingMethod = false;
		System.out.println("StickShaping Method: NEW");
	}
	
	public void end() {
		RobotMap.driveTrain.oldStickShapingMethod = true;
		System.out.println("StickShaping Method: OLD");
	}
	
	public void interrupted() {
		end();
	}
	
	public boolean isFinished() {
		return false;
	}
}
