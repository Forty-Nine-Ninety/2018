package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.subsystems.motors.Motor;

public class Forklift {
	private Elevator elevator;
	private double elevatorSetPower;
	
	private Fork fork;
	private boolean forkState;
	
	public Forklift(Motor elevatorMotor, int forkPCMChannel, int topSwitchChannel, int topSwitchCounterSensitivity, int bottomSwitchChannel, int bottomSwitchCounterSensitivity, int encoderChannelA, 
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

	public void setElevatorPower(double power) {
		this.elevatorSetPower = power;
	}
	
	public double getElevatorSetPower() {
		return this.elevatorSetPower;
	}
	
	public void setForkToOpen() {
		this.forkState = true;
	}
	
	public void setForkToClosed() {
		this.forkState = false;
	}
	
	public void update() {
		this.elevator.checkSafety();
		
		this.elevator.setElevatorPower(this.elevatorSetPower);
		this.fork.setForkState(this.forkState);
	}
	
	public void reset() {
		this.elevator.reset();
	}
	
	public boolean isCarriageAbove() {
		return this.elevator.isAbove();
	}
	
	public int topSwitchCurrCount() {
		return this.elevator.topSwitchCurrCount();
	}
	
	public int topSwitchLastCount() {
		return this.elevator.topSwitchLastCount();
	}
	
	public boolean isCarriageBelow() {
		return this.elevator.isBelow();
	}
	
	public int bottomSwitchCurrCount() {
		return this.elevator.bottomSwitchCurrCount();
	}
	
	public int bottomSwitchLastCount() {
		return this.elevator.bottomSwitchLastCount();
	}
	
}
