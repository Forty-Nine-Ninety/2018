package org.usfirst.frc.team4990.robot.commands;


import org.usfirst.frc.team4990.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;
/**
 * Command that controls elevator in teleop
 * @author Class of '21 (created in 2018 season)
 */
public class TeleopElevatorController extends Command{

	/**
	 * Constructor for TeleopElevatorController
	 * @author Class of '21 (created in 2018 season)
	 */
	public TeleopElevatorController() {
		requires(RobotMap.elevator);
	}
	
	/**
	 * Gets input from gamepad and sets motor power
	 * @author Class of '21 (created in 2018 season)
	 */
	public void execute() {

			//Elevator PID System still needs some work, disabled for now
			/*if (gpad.getYButtonPressed()) { 
				elevator.goToPosition(4);
				System.out.println("Moving to " + RobotMap.elevator.elevatorPID.getSetpoint() + ", current speed: " + RobotMap.elevator.elevatorPID.get());
		}
			} else { */
				RobotMap.elevator.setElevatorPower(Math.abs(RobotMap.opGamepad.getRightJoystickY()) + RobotMap.elevator.stopFallingSpeed);
			//}
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
