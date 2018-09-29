package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;


public class RobotDriveStraight extends Command {
	DriveTrain dt = RobotMap.driveTrain;

	double kTargetTime = 2.8; 
	double speed = 0.3;
	boolean useEncoders = false;
	double targetDistance = 4.7; //feet?
	
	public RobotDriveStraight(double time) {
		requires(RobotMap.driveTrain);
		kTargetTime = time;
		//requires(RobotMap.driveTrain);
	}
	
	public RobotDriveStraight(double time, double speed) {
		requires(RobotMap.driveTrain);
		kTargetTime = time;
		this.speed = speed;
		//requires(RobotMap.driveTrain);
	}
	
	public RobotDriveStraight(double time, double speed, double distance) {
		requires(RobotMap.driveTrain);
		kTargetTime = time;
		this.speed = speed;
		this.targetDistance = distance;
		this.useEncoders = true;
		//requires(RobotMap.driveTrain);
	}

	public RobotDriveStraight() {
		requires(RobotMap.driveTrain);
		//requires(RobotMap.driveTrain);
	}

	public void initialize() {
		System.out.println("Initalizing gyroStraight with time " + kTargetTime);
		dt.right.encoder.reset();
		dt.setSpeed(speed);
	}

	public void execute() {
		System.out.println("RobotDriveStraight. Time: " + this.timeSinceInitialized() + "Distance: " + Math.abs(dt.right.encoder.getDistance()));
	}
	
	public void end() {
		dt.setSpeed(0);
	}
	
	public void interrupted() {
		end();
	}
	
	public boolean isFinished() {
		if (this.timeSinceInitialized() >= this.kTargetTime) {
			return true;
		} else if (useEncoders) {
			return Math.abs(dt.right.encoder.getDistance()) > targetDistance;
		} else {
			return false;
		}
	}

}
