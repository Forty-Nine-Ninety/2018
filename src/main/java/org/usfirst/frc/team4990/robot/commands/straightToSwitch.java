package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4990.robot.SmartDashboardController;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;

/**
 * @deprecated
 */

public class straightToSwitch extends Command {
	private double startingGyro;
	private boolean isFinished;
	private DriveTrain dt;
	private ADXRS450_Gyro gyro;
	private Ultrasonic ultrasonic;
	private double baseMotorPower;
	private double currentGyroData;
	private double leftMotorAdjust;

	/**
	 * Command for going straight and stopping when robot reaches switch
	 */
	
	public straightToSwitch(DriveTrain dt, ADXRS450_Gyro gyro, Ultrasonic ultrasonic) {
		requires(dt);
		//Remember that the right motor is the slow one
		this.isFinished = false;
		this.dt = dt;
		this.gyro = gyro;
		this.ultrasonic = ultrasonic;
		this.startingGyro = 0;
		this.baseMotorPower = 0.3;
	}
	
	public void initialize() {
		System.out.println("straightToSwitch()");
		this.dt.resetDistanceTraveled();
		gyro.reset();
	}
	public void execute() {
		this.currentGyroData = gyro.getAngle();
		System.out.println("current ultrasonic distance: " + ultrasonic.getRangeInches());
		
		if (ultrasonic.getRangeInches() < 20) { //THIS LINE TELLS ROBOT WHEN TO STOP
			
			if (this.currentGyroData > this.startingGyro) {
				this.leftMotorAdjust = this.baseMotorPower - 0.064023; //add to number to go more LEFT
			} else if (this.currentGyroData < this.startingGyro) {
				this.leftMotorAdjust = this.baseMotorPower + 0.05;

			}
			this.dt.setSpeed(this.leftMotorAdjust, this.baseMotorPower);
		} else {
			this.isFinished = true;
			this.dt.setSpeed(0, 0);
		}
	}
	
	public boolean isFinished() {
		return this.isFinished;
	}
}
