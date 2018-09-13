package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.RobotMap;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * An Intake.
 * @author Class of '21 (created in 2018 season)
 * 
 */

public class Intake extends Subsystem{
	private double speed;
	
	/**
	 * Initialize inkate.
	 * @param talonMotorController PWM port for LEFT talon
	 * @param talonMotorController2 PWM port for RIGHT talon
	 * @param infraredInput Analog Port for Sharp Distance Sensor
	 */
	
	public Intake() {
		
	}
	
	/**
	 * Enum describing ultrasonic visibility of box. MOVING indicates in between IN and OUT.
	 * @author Class of '21 (created in 2018 season)
	 */
	
	public enum BoxPosition {
		IN, MOVING, OUT
	}
	
	/**
	 * Sets speed of intake. Does not auto-stop based on sensor. Execute by calling update().
	 * @param speedInput
	 */
	
	public void setSpeed(double speedInput) {
		speed = speedInput;
	}
	
	/**
	 * Executes new speed of inkate.
	 */
	
	public void update() {
		RobotMap.intakeTalonA.set(speed);
		RobotMap.intakeTalonB.set(-speed);
	}
	
	/**
	 * returns distance in volts (0 to 5) read by distance sensor
	 * @return distance in volts (0 to 5) read by distance sensor
	 */
	
	public double getAnalogInput() {
		return RobotMap.intakeDistanceAnalogInput.getAverageVoltage();
		
	}
	
	/**
	 * returns enum for where distance sensor sees box
	 * @return enum for where distance sensor sees box
	 */
	
	public BoxPosition getBoxPosition() {
		if (getAnalogInput() >= 2 ) {
			return Intake.BoxPosition.IN;
		} else if (getAnalogInput() >= 0.4) {
			return Intake.BoxPosition.MOVING;
		} else {
			return Intake.BoxPosition.OUT;
		}
	}
	
	/**
	 * boolean check for box location
	 * @param pos position to check for
	 * @return boolean if distance sensor sees box in specified position
	 */
	
	public boolean isBoxPosition(BoxPosition pos) {
		if (getBoxPosition() == pos) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void initDefaultCommand() {

	}
	
}
