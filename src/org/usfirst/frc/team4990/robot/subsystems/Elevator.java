package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.Constants;
import org.usfirst.frc.team4990.robot.subsystems.motors.Motor;

import edu.wpi.first.wpilibj.Encoder;

public class Elevator {
	private Motor elevatorMotor;
	
	private LimitSwitch topSwitch;
	
	private LimitSwitch bottomSwitch;
	
	private Encoder encoder;
	
	public Elevator(
			Motor elevatorMotor, 
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

	// positive power = up
	// negative power = down
	public void setElevatorPower(double power) {
		if ((this.topSwitch.getValue() && power > 0) || (this.bottomSwitch.getValue() && power < 0)) {
			this.elevatorMotor.setPower(0.0);
		} else {
			this.elevatorMotor.setPower(power);
		}
	}
	
	public void checkSafety() {
		if ((this.topSwitch.getValue() && this.elevatorMotor.getPower() > 0) || (this.bottomSwitch.getValue() && this.elevatorMotor.getPower() < 0)) {
			this.elevatorMotor.setPower(0.0);
			System.out.println("Elevator Safety Triggered");
		}
	}
	
	public boolean isTopSwitched() {
		return this.topSwitch.getValue();
	}
	
	public boolean isBottomSwitched() {
		return this.bottomSwitch.getValue();
	}
	
	public double getEncoderDistance() {
		return encoder.getDistance();
	}
	
	public void resetEncoderDistance() {
		this.encoder.reset();
	}
}
