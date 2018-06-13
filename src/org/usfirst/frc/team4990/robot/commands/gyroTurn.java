package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.Robot;
import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.command.Command;

public class gyroTurn extends Command {
	private double degrees;
	private boolean isFinished;
	private DriveTrain dt;
	private ADXRS450_Gyro gyro;

	/**
	 * Turns left or right
	 * @param inputDegrees Degrees to turn (Positive = right, negative = left)
	 */
	
	public gyroTurn(double classdegrees) {
		requires(Robot.driveTrain);
		// please note that the right encoder is backwards
		this.dt = Robot.driveTrain;
		this.degrees = classdegrees;
		this.isFinished = false;
		gyro = Robot.gyro;
	}

	public void initialize() {
		this.gyro.reset();
		System.out.println("gyroTurn(" + degrees + ")");
	}

	public void execute() {
		
		double currentDegreesTraveled = Math.abs(gyro.getAngle());
		System.out.println("Current angle: " + this.gyro.getAngle() + "  Stopping at: " + this.degrees);
		
		if (currentDegreesTraveled < this.degrees) {
			this.dt.setSpeed( //TODO change (1/90) to actual porportional value
					(degrees > 0) ? //turning right?
					(1/90) * (degrees - gyro.getAngle()) : -(1/90) * (degrees - gyro.getAngle()), 
					(degrees > 0) ? //turning right?
					-(1/90) * (degrees - gyro.getAngle()) : (1/90) * (degrees - gyro.getAngle())
					); // left needs to go forwards, right needs to go backwards to turn right	
		} else {
			this.dt.setSpeed(0, 0);
			this.isFinished = true;
		}
	}
	
	public boolean isFinished() {
		return this.isFinished;
	}
}
