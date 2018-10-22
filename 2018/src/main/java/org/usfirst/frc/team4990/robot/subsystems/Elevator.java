package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.Robot;
import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.commands.TeleopElevatorController;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator extends Subsystem {
	
	public TalonMotorController elevatorMotor;
	
	public LimitSwitch topSwitch, bottomSwitch;
	
	public double maxSpeed = Robot.getConst("Elevator/maxSpeed", 1.0);
	
	public double stopFallingSpeed = Robot.getConst("Elevator/stopFallingSpeed", 0.05);
	
	public double setSpeed = 0;
	
	public String status = "Initializing"; 
	
	public Command defaultCommand = new TeleopElevatorController();

	/**
	 * Initializes elevator.
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
			status = topSwitch.getValue() ? "Safety Triggered in setElevatorPower, Top switch" : "Safety Triggered in setElevatorPower, Bottom switch";
		} else if (bottomSwitch.getValue() && power == 0 || bottomSwitch.getValue() && power == stopFallingSpeed || bottomSwitch.getValue() && power == 0.0578125) {
			this.setSpeed = 0;
			status = "At bottom, motor off";
		} else {
			if (power > stopFallingSpeed) { //right joystick positive = elevator UP
				if (power > maxSpeed) {
					this.setSpeed = maxSpeed;
					status = "going up, max speed";
				} else { 
					this.setSpeed = power;
					if (setSpeed == 0.0578125) { // stick deadband
						status = "holding position, motor on";
					} else {
						status = "going up";
					}
				}
			} else if (power < stopFallingSpeed) { //right joystick negative = elevator DOWN
				if (-power >= maxSpeed - stopFallingSpeed) {
					this.setSpeed = -maxSpeed + stopFallingSpeed;
					status = "going down, max speed";
				} else { 
					this.setSpeed = power; 
					status = "going down";
				}
			} else {
				this.setSpeed = stopFallingSpeed;
				status = "stopped";
			}
		}
	}
	
	/* (plz don't make this a javadoc because it will override the default javadoc for this method)
	 * Checks safety and (hopefully) stops elevator from falling if stopped
	 */
	
	public void periodic() {
		
		//check limit switches, stop motors if going toward danger
		if ((this.topSwitch.getValue() && this.setSpeed > stopFallingSpeed) || (this.bottomSwitch.getValue() && this.setSpeed < stopFallingSpeed)) {
			this.elevatorMotor.set(0);
			resetEncoderDistance();
			//System.out.println("Elevator Safety Triggered in update");
		} else {
			this.elevatorMotor.set(setSpeed);
		}
		
		SmartDashboard.putString("Elevator/Status", status);
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
		this.setDefaultCommand(defaultCommand);
	}
}
