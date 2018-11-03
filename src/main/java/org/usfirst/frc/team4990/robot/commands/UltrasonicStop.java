package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.SmartDashboardController;
import edu.wpi.first.wpilibj.command.Command;

public class UltrasonicStop extends Command {
	
	static double stopDistance = SmartDashboardController.getConst("UltrasonicStop/DefaultStopDistance", 20); // inches?
	Command command;

	/**
	 * Command for stopping when robot becomes in range of object.
	 * @param distance in inches
	 */
	
	public UltrasonicStop(double distance, Command command) {
		requires(RobotMap.driveTrain);
		stopDistance = distance;
		this.command = command;
	}
	
	/**
	 * Command for stopping when robot becomes within <b>20</b> inches of object.
	 */
	public UltrasonicStop() {
		requires(RobotMap.driveTrain);
	}
	
	/*public void execute() {
		System.out.println("current ultrasonic distance: " + RobotMap.ultrasonic.getRangeInches());
	}*/
	
	public boolean isFinished() {
		return RobotMap.ultrasonic.getRangeInches() < 20;
	}
	
	public void end() {
		command.cancel();
	}
	
	public void interrupted() {
		command.cancel();
	}
}
