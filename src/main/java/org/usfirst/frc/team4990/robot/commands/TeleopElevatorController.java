package org.usfirst.frc.team4990.robot.commands;


import org.usfirst.frc.team4990.robot.RobotMap;
import edu.wpi.first.wpilibj.DriverStation;
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
		RobotMap.elevator.setElevatorPower(-1 * RobotMap.opGamepad.getRightJoystickY() + RobotMap.elevator.stopFallingSpeed);
	}

	@Override
	protected boolean isFinished() {
		return !DriverStation.getInstance().isOperatorControl();
	}
	
	public void end() {
		RobotMap.elevator.setElevatorPower(0);
	}
	
	public void interupted() {
		end();
	}

}
