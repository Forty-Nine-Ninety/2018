package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.subsystems.motors.TalonMotorController;

public class Forklift {
	private Elevator elevator;
	private double elevatorSetPower;
	
	private Fork fork;
	private boolean forkState;
	
	/**
	 * Initialize Forklift
	 * @param elevatorMotor Talon PWM for elevator
	 * @param forkPCMChannel PCM port for solenoid
	 * @param topSwitchChannel DIO channel for top limit switch
	 * @param topSwitchCounterSensitivity sensitivity for top limit switch, default 4
	 * @param bottomSwitchChannel DIO channel for bottom limit switch
	 * @param bottomSwitchCounterSensitivity sensitivity for bottom limit switch, default 4
	 * @param encoderChannelA Encoder for elevator gearbox (Signal, Ground and 5v)
	 * @param encoderChannelB Encoder for elevator gearbox (just Signal)
	 */
	
	public Forklift(TalonMotorController elevatorMotor, int forkPCMChannel, int topSwitchChannel, int topSwitchCounterSensitivity, int bottomSwitchChannel, int bottomSwitchCounterSensitivity, int encoderChannelA, 
			int encoderChannelB) {
		this.elevator = new Elevator(elevatorMotor, 
				topSwitchChannel, 
				topSwitchCounterSensitivity, 
				bottomSwitchChannel, 
				bottomSwitchCounterSensitivity, 
				encoderChannelA, 
				encoderChannelB);
		this.fork = new Fork(forkPCMChannel);
	}
	
	/**
	 * Sets elevator power. Call update() to make it happen.
	 * @param power positive value (0 to 1) makes it go up, negative values (-1 to 0) makes it go down
	 */

	public void setElevatorPower(double power) {
		this.elevatorSetPower = power;
	}
	
	/**
	 * returns elevator power set using setElevatorPower()
	 * @return elevator power set using setElevatorPower()
	 */
	
	public double getElevatorSetPower() {
		return this.elevatorSetPower;
	}
	
	/**
	 * Opens fork. Call update() to make it happen.
	 * @param isOpen true = open, false = closed
	 */
	
	
	public void setForkToOpen() {
		this.forkState = true;
	}
	
	/**
	 * Closes fork. Call update() to make it happen.
	 * @param isOpen true = open, false = closed
	 */
	
	public void setForkToClosed() {
		this.forkState = false;
	}
	
	/**
	 * Sets elevator power to set power and opens/closes fork
	 */
	
	public void update() {
		this.elevator.setElevatorPower(this.elevatorSetPower);
		this.fork.setForkState(this.forkState);
		this.elevator.update();
	}
}
