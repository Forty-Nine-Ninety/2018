package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.Robot;
import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;


public class RobotDriveUltrasonicStop extends Command {
	DriveTrain dt = RobotMap.driveTrain;

	double targetDistance = SmartDashboardController.getConst("RobotDriveStraight/defaultTargetDistance", 12); // inches?
	double speed = SmartDashboardController.getConst("RobotDriveStraight/defaultSpeed", 0.3);
	double timeout = SmartDashboardController.getConst("RobotDriveStraight/defaultTimeout", 4); // seconds
	
	public RobotDriveUltrasonicStop(double distance, double speed, double timeout) {
		requires(RobotMap.driveTrain);
		targetDistance = distance;
		this.speed = speed;
		this.timeout = timeout;
		//requires(RobotMap.driveTrain);
	}

	public void initialize() {
		System.out.println("Initalizing RobotDriveUltrasonicStop with stopping distance " + targetDistance);
		dt.right.encoder.reset();
		dt.setSpeed(speed);
	}

	public void execute() {
		System.out.println("RobotDriveUltrasonicStop. Time: " + this.timeSinceInitialized() + "Distance: "
				+ RobotMap.ultrasonic.getRangeInches());
	}
	
	public void end() {
		dt.setSpeed(0);
	}
	
	public void interrupted() {
		end();
	}
	
	public boolean isFinished() { // if timeout, return true, if not timeout return true if in range
		return this.timeSinceInitialized() >= this.timeout ? true
				: this.targetDistance <= RobotMap.ultrasonic.getRangeInches();
	}

}
