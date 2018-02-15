package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.Constants;
import org.usfirst.frc.team4990.robot.subsystems.motors.TalonMotorController;
import edu.wpi.first.wpilibj.Encoder;

public class Elevator {
	private TalonMotorController elevatorMotor;
	
	private LimitSwitch topSwitch;
	
	private LimitSwitch bottomSwitch;
	
	private Encoder encoder;
	
	private double stopFallingSpeed = 0.05;
	
	private boolean stopped = false;
	
	/**
	 * Initializes elevator.
	 * @param elevatorMotor Motor used to drive elevator (vex pro 775)
	 * @param topSwitchChannel DIO channel for top limit switch
	 * @param topSwitchCounterSensitivity sensitivity for top limit switch, default 4
	 * @param bottomSwitchChannel DIO channel for bottom limit switch
	 * @param bottomSwitchCounterSensitivity sensitivity for bottom limit switch, default 4
	 * @param encoderChannelA Encoder for elevator gearbox (Signal, Ground and 5v)
	 * @param encoderChannelB Encoder for elevator gearbox (just Signal)
	 */
	
	public Elevator(
			TalonMotorController elevatorMotor, 
			int topSwitchChannel, 
			int topSwitchCounterSensitivity, 
			int bottomSwitchChannel, 
			int bottomSwitchCounterSensitivity,
			int encoderChannelA, 
			int encoderChannelB) {
		this.elevatorMotor = elevatorMotor;
		
		this.topSwitch = new LimitSwitch(topSwitchChannel, topSwitchCounterSensitivity);
		this.bottomSwitch = new LimitSwitch(bottomSwitchChannel, bottomSwitchCounterSensitivity);
		
		encoder = new Encoder(encoderChannelA, encoderChannelB);
		this.encoder.setDistancePerPulse(1.16 * Math.PI / Constants.pulsesPerRevolution); //diameter of elevator chain gear * PI
		this.encoder.setMinRate(Constants.gearboxEncoderMinRate);
		this.encoder.setSamplesToAverage(Constants.gearboxEncoderSamplesToAvg);
	}
	
	/**
	 * Sets elevator power and checks limit switches. If moving would hurt elevator, does not move.
	 * @param power positive value (0 to 1) makes it go up, negative values (-1 to 0) makes it go down
	 */
	
	public void setElevatorPower(double power) {
		if ((this.topSwitch.getValue() && power > 0) || (this.bottomSwitch.getValue() && power < 0)) {
			this.elevatorMotor.setPower(0.0);
			resetEncoderDistance();
			stopped = true;
		} else {
			this.elevatorMotor.setPower(power);
			if (power == 0 && ! stopped) {
				resetEncoderDistance();
				stopped = true;
			} else if (! stopped) {
				stopped = false;
			}
		}
	}
	
	/**
	 * Checks safety and (hopefully) stops it from falling if stopped
	 */
	
	public void update() {
		
		//check limit switches, stop motors if going toward danger
		if ((this.topSwitch.getValue() && this.elevatorMotor.getPower() > 0) || (this.bottomSwitch.getValue() && this.elevatorMotor.getPower() < 0)) {
			this.elevatorMotor.setPower(0.0);
			resetEncoderDistance();
			stopped = true;
			System.out.println("Elevator Safety Triggered");
		}
		
		//if stopped, use encoders to run motors to stop intake from falling
		if (this.elevatorMotor.getPower() == 0)
			if (Math.abs(getEncoderDistance()) > 0.01) {
				setElevatorPower(stopFallingSpeed);
			} else {
				setElevatorPower(0.0);
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
		return encoder.getDistance();
	}
	
	/**
	 * resets encoder's distance.
	 */
	
	public void resetEncoderDistance() {
		this.encoder.reset();
	}
}
