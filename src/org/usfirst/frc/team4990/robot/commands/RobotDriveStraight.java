package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.Robot;
import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;


public class RobotDriveStraight extends Command {
	DriveTrain dt = RobotMap.driveTrain;

	public static double targetTime = SmartDashboardController.getConst("RobotDriveStraight/defaultTargetTime", 2.8);
	double speed = SmartDashboardController.getConst("RobotDriveStraight/defaultSpeed", 0.3);
	
	public RobotDriveStraight(double time) {
		requires(RobotMap.driveTrain);
		RobotDriveStraight.targetTime = time;
		//requires(RobotMap.driveTrain);
	}
	
	public RobotDriveStraight(double time, double speed) {
		requires(RobotMap.driveTrain);
		RobotDriveStraight.targetTime = time;
		this.speed = speed;
		//requires(RobotMap.driveTrain);
	}

	public RobotDriveStraight() {
		requires(RobotMap.driveTrain);
		//requires(RobotMap.driveTrain);
	}

	public void initialize() {
		System.out.println("Initalizing GyroStraight with time " + RobotDriveStraight.targetTime);
		dt.right.encoder.reset();
		dt.setSpeed(speed);
	}

	public void execute() {
		System.out.println("RobotDriveStraight. Time: " + this.timeSinceInitialized() + " stopping at: " + RobotDriveStraight.targetTime);
	}
	
	public void end() {
		dt.setSpeed(0);
	}
	
	public void interrupted() {
		end();
	}
	
	public boolean isFinished() {
		return this.timeSinceInitialized() >= RobotDriveStraight.targetTime;
	}

}
