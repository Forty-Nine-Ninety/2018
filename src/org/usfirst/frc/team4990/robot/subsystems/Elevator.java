package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem {
	
	public TalonSRX elevatorMotor;
	
	public LimitSwitch topSwitch, bottomSwitch;
	
	public double maxSpeed = 1.0;
	
	public double stopFallingSpeed = 0.05;
	
	private double setSpeed = 0;
	
	/**
	 * Initializes elevator.
	 * @param elevatorMotor Talon for motor used to drive elevator
	 * @param topSwitchChannel DIO channel for top limit switch
	 * @param bottomSwitchChannel DIO channel for bottom limit switch
	 * @author Class of '21 (created in 2018 season)
	 */
	
	public Elevator() {
		
		this.elevatorMotor = RobotMap.elevatorTalon;
		
		this.topSwitch = RobotMap.elevatorLimitSwitchTop;
		this.bottomSwitch = RobotMap.elevatorLimitSwitchBottom;
		
	}
	
	/**
	 * Sets elevator power and checks limit switches. If moving would hurt elevator, does not move.
	 * @param power positive value (0 to 1) makes it go up, negative values (-1 to 0) makes it go down
	 */
	
	public void setElevatorPower(double power) {	
		if ((topSwitch.getValue() && power > 0) || (bottomSwitch.getValue() && power < 0)) {
			this.setSpeed = 0;
			System.out.println("Elevator Safety Triggered in setElevatorPower");
		} else {
			if (power > stopFallingSpeed) { //right joystick positive = elevator UP
				if (power > maxSpeed) {
					this.setSpeed = maxSpeed;
				} else { 
					this.setSpeed = power; 
				}
			} else if (power < stopFallingSpeed) { //right joystick negative = elevator DOWN
				if (-power > maxSpeed) {
					this.setSpeed = maxSpeed;
				} else { 
					this.setSpeed = power; 
				}
			} else {
				this.setSpeed = stopFallingSpeed;
			}
		}
	}
	
	/**
	 * Checks safety and (hopefully) stops it from falling if stopped
	 */
	
	public void update() {
		
		if (setSpeed > stopFallingSpeed || setSpeed < stopFallingSpeed) {
			
		}
		
		//check limit switches, stop motors if going toward danger
		if ((this.topSwitch.getValue() && this.setSpeed > stopFallingSpeed) || (this.bottomSwitch.getValue() && this.setSpeed < stopFallingSpeed)) {
			this.elevatorMotor.set(ControlMode.PercentOutput, 0);
			resetEncoderDistance();
			System.out.println("Elevator Safety Triggered in update");
		} else {
			this.elevatorMotor.set(ControlMode.PercentOutput, setSpeed);
		}
		
	}
	
	/**
	 * returns boolean whether top is switched.
	 * @return boolean whether top is switched.
	 */
	
	public boolean isTopSwitched() {
		return this.topSwitch.getValue();
	}
	
	/**
	 * returns boolean whether bottom is switched.
	 * @return boolean whether bottom is switched.
	 */
	
	public boolean isBottomSwitched() {
		return this.bottomSwitch.getValue();
	}
	
	/**
	 * returns elevator gearbox's distance in unknown units
	 * @return elevator gearbox's distance in unknown units
	 */
	
	public double getEncoderDistance() {
		return elevatorMotor.getSelectedSensorPosition(0);
	}
	
	/**
	 * resets encoder's distance.
	 */
	
	public void resetEncoderDistance() {
		elevatorMotor.setSelectedSensorPosition(0, 0, 25);
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
}
