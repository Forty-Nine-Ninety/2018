package org.usfirst.frc.team4990.robot.commands;

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
		TeleopDriveTrainController.oldStickShapingMethod = false;
		System.out.println("StickShaping Method: NEW");
	}
	
	public void end() {
		TeleopDriveTrainController.oldStickShapingMethod = true;
		System.out.println("StickShaping Method: OLD");
	}
	
	public void interrupted() {
		end();
	}
	
	public boolean isFinished() {
		return false;
	}
}
