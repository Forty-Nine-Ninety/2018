package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.Robot;
import org.usfirst.frc.team4990.robot.subsystems.F310Gamepad;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Press 'START' button on Drive or OP controller to see which is which
 * @author Benjamin and Class of '21 (created in 2018 season)
 *
 */

public class ControllerCheck extends Command {
	public ControllerCheck(F310Gamepad gamepad) {
    	if (gamepad.equals(Robot.driveGamepad)) {
    		System.out.println("Button 7 Pressed on DRIVE GAMEPAD");
    	} else if (gamepad.equals(Robot.opGamepad)) {
    		System.out.println("Button 7 Pressed on OP GAMEPAD");
    	}
	}
	
	protected boolean isFinished() {
		return true;
	}
}
