package org.usfirst.frc.team4990.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class Elevator implements PIDSource, PIDOutput{
	
	public TalonSRX elevatorMotor;
	
	public LimitSwitch topSwitch, bottomSwitch;
	
	public double stopFallingSpeed = 0.05;
	
	private boolean stopped = false;
	
	//for Elevator goToPostion
	private PIDSourceType pidSource = PIDSourceType.kDisplacement;
	public double doneTolerance = 3; //percent
	public boolean goToPostionActive = false;
	double kP = 0.2;
	double kI = 0;
	double kD = 0;
	
	public PIDController elevatorPID = new PIDController(kP, kI, kD, this, this);
	
	/**
	 * Initializes elevator.
	 * @param elevatorMotor Talon for motor used to drive elevator
	 * @param topSwitchChannel DIO channel for top limit switch
	 * @param bottomSwitchChannel DIO channel for bottom limit switch
	 */
	
	public Elevator(TalonSRX elevatorMotor, int topSwitchChannel, int bottomSwitchChannel) {
		
		this.elevatorMotor = elevatorMotor;
		
		this.topSwitch = new LimitSwitch(topSwitchChannel);
		this.bottomSwitch = new LimitSwitch(bottomSwitchChannel);
		
		this.elevatorPID.setInputRange(-180f, 180f);
		this.elevatorPID.setContinuous(true);; //minimumInput, maximumInput
		this.elevatorPID.setOutputRange(-1, 1); //minimumOutput, maximumoutput (motor constraints)
		this.elevatorPID.setAbsoluteTolerance(doneTolerance);
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
			if (!this.goToPostionActive) {
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
			} else {
				this.setSpeed = power; 
			}
		}
	}
	
	/**
	 * Checks safety and (hopefully) stops it from falling if stopped
	 */
	
	public void update() {
		
		//updates elevator PID for goToPostion
		if (goToPostionActive) {
			if (this.elevatorPID.onTarget()){ //done
				this.elevatorPID.disable();
				goToPostionActive = false;
			} else {
				setElevatorPower(this.elevatorPID.get());
			} 
		}
		
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
	
	public void goToPosition(double postionInput) {
		resetEncoderDistance();
		this.elevatorPID.setSetpoint(postionInput);
		this.elevatorPID.enable();
		setElevatorPower(this.elevatorPID.get());
		goToPostionActive = true;
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
		elevatorMotor.setSelectedSensorPosition(0, 0, 0);
	}

	@Override
	public void pidWrite(double output) {
		setElevatorPower(output);
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		this.pidSource = pidSource;
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return pidSource;
	}

	@Override
	public double pidGet() {
		return getEncoderDistance();
	}
}
