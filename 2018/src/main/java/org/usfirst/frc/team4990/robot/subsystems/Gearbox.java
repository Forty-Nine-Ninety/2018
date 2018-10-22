package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class Gearbox {
	public TalonMotorController frontMotor, rearMotor;
	public SpeedControllerGroup motorGroup;
	
	public Encoder encoder;
	
	public double compensate, fix_backwards, setSpeed;
	
	public Gearbox(TalonMotorController fMotor, TalonMotorController rMotor, Encoder encoder) {
		this.frontMotor = fMotor;
		this.rearMotor = rMotor;
		this.motorGroup = new SpeedControllerGroup(frontMotor, rearMotor);
		
		this.encoder = encoder;
		
		this.compensate = 1.0;
		this.fix_backwards = 1.0;
		
		this.encoder.setDistancePerPulse(
				/* feetPerWheelRevolution */ (4.0 / 12.0 * Math.PI) / /* pulsesPerRevolution */ 250);
		this.encoder.setMinRate(0);
		this.encoder.setSamplesToAverage(/* gearboxEncoderSamplesToAvg */ 5);
	}
	
	public void update() {
		motorGroup.set(setSpeed * compensate * fix_backwards);
	}
	
	public void swapDirection() {
		this.fix_backwards = (this.fix_backwards > 0.0) ? -1.0 : 1.0;
	}
	
	/**
	 * Sets speed of robot side
	 * @param speed Set speed for side, min -1, max 1
	 */
	
	public void setSpeed(double speed) {
		this.setSpeed = speed;
	}
	
	public void updateCompensate(double new_dist) {
		this.compensate = new_dist / getDistanceTraveled();
	}
	
	/**
	 * Returns speed that will be set when update() is called
	 * @return speed that will be set when update() is called
	 */
	
	public double getSetSpeed() {
		return this.setSpeed;
	}
	
	/**
	 * Returns current speed (not changed until new speed is send via update())
	 * @return current speed (not changed until new speed is send via update())
	 */
	
	public double getSpeed() {
		return this.motorGroup.get();
	}
	
	public void clearStickyFaults() {
		frontMotor.clearStickyFaults(0);
		rearMotor.clearStickyFaults(0);
	}
	
	/**
	 * Returns encoder distance in ft?
	 * @return encoder distance (multiply by 1.06517 to get feet?)
	 */
	
	public double getDistanceTraveled() {
		return Math.abs(this.encoder.getDistance());
	}
	
	/**
	 * Returns left gearbox's encoder speed in feet per sec
	 * @return left gearbox's encoder speed
	 */
	
	public double getVelocity() {
		return Math.abs(this.encoder.getRate());
	}
	
	public void resetDistanceTraveled() {
		this.encoder.reset();
	}
}
