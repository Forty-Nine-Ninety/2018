package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * An Intake.
 * @author Class of '21 (created in 2018 season)
 * 
 */

public class Intake extends Subsystem{
	public TalonMotorController motorL;
	public TalonMotorController motorR;
	public AnalogInput infrared;
	private double speed;
	private double rate = 0.9;
	
	/**
	 * Initialize inkate.
	 * @param talonMotorController PWM port for LEFT talon
	 * @param talonMotorController2 PWM port for RIGHT talon
	 * @param infraredInput Analog Port for Sharp Distance Sensor
	 */
	
	public Intake(TalonMotorController talonMotorController, TalonMotorController talonMotorController2, AnalogInput infraredInput) {
		motorL = talonMotorController;
		motorR = talonMotorController2;
		infrared = infraredInput;
	}
	
	/**
	 * Enum describing ultrasonic visibility of box. MOVING indicates in between IN and OUT.
	 * @author Class of '21 (created in 2018 season)
	 */
	
	public enum BoxPosition {
		IN, MOVING, OUT
	}
	
	/**
	 * Intake cube. Does not auto-stop based on sensor. Execute by calling update().
	 */
	
	public void in() {
		//Set speed
		speed = rate;
	}
	
	/**
	 * Outake cube. Does not auto-stop based on sensor. Execute by calling update().
	 */
	public void out() {
		//Set speed
		speed = -rate;
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
		motorL.set(speed);
		motorR.set(-speed);
	}
	
	/**
	 * STOPS Intake. (speed 0) Execute by calling update().
	 */
	
	public void stop() {
		speed = 0;
	}
	
	/**
	 * returns distance in volts (0 to 5) read by distance sensor
	 * @return distance in volts (0 to 5) read by distance sensor
	 */
	
	public double getAnalogInput() {
		return infrared.getAverageVoltage();
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
