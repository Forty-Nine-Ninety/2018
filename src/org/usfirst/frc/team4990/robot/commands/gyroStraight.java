package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.command.Command;

public class gyroStraight extends Command {
	
	private double distanceToGo;
	private double startingGyro;
	private boolean isFinished;
	private DriveTrain dt;
	private ADXRS450_Gyro gyro;
	private double baseMotorPower;
	private double currentDistanceTraveled;

	/**
	 * Command for going straight
	 * @param distance Distance to go straight for in feet
	 */
	
	public gyroStraight(double distance) {
		requires(RobotMap.driveTrain);
		//Remember that the right motor is the slow one
		this.isFinished = false;
		RobotMap.driveTrain = dt;
		RobotMap.gyro = gyro;
		this.distanceToGo = distance;
		this.startingGyro = 0;
		this.baseMotorPower = 0.3;
	}
	public void initialize() {
		System.out.println("gyroStraight(" + distanceToGo + ")");
		this.dt.resetDistanceTraveled();
		gyro.reset();
	}
	public void execute() {
		this.currentDistanceTraveled = (distanceToGo > 0) ? -this.dt.right.getDistanceTraveled() * 1.06517 : this.dt.right.getDistanceTraveled() * 1.06517;

		System.out.println("current distance: " + currentDistanceTraveled + " stopping at: " + this.distanceToGo + "r encoder: " + this.dt.right.getDistanceTraveled() + this.dt.left.getDistanceTraveled());
		if (currentDistanceTraveled < this.distanceToGo) { //not at goal yet
			this.dt.setSpeed(baseMotorPower + (0.1 * (startingGyro - gyro.getAngle())), baseMotorPower - (0.1 * (startingGyro - gyro.getAngle()))); //TODO: find proportional values (right now 0.1) for drive train
		} else {
			this.isFinished = true;
			this.dt.setSpeed(0, 0);
		}
	}
	
	public boolean isFinished() {
		return this.isFinished;
	}
}
