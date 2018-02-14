package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.subsystems.motors.Motor;

import edu.wpi.first.wpilibj.AnalogInput;

//In & Out
	// Constructor(motors)

public class Intake {
	private Motor motorL;
	private Motor motorR;
	private AnalogInput infrared;
	//private LimitSwitch ls;
	private double speed;
	private double rate = 0.9;
	
	
	public Intake(Motor mL, Motor mR, AnalogInput infraredInput) {
		motorL = mL;
		motorR = mR;
		infrared = infraredInput;
	}
	
	public enum BoxPosition {
		IN, MOVING, OUT
	}
	
	public void in() {
		//Set speed
		speed = rate;
	}
	
	public void out() {
		//Set speed
		speed = -rate;
	}
	
	public void setSpeed(double speedInput) {
		speed = speedInput;
	}
	
	public void update() {
		motorL.setPower(speed);
		motorR.setPower(-speed);
	}
	
	public void stop() {
		speed = 0;
	}
	
	public double getAnalogInput() {
		return infrared.getAverageVoltage();
	}
	
	public BoxPosition getBoxPosition() {
		double periodicAverageInfraredInput = getAnalogInput();
		if (periodicAverageInfraredInput >= 2 ) {
			return Intake.BoxPosition.IN;
		} else if (periodicAverageInfraredInput >= 0.4) {
			return Intake.BoxPosition.MOVING;
		} else {
			return Intake.BoxPosition.OUT;
		}
	}
	
	public boolean isBoxPosition(BoxPosition pos) {
		if (getBoxPosition() == pos) {
			return true;
		} else {
			return false;
		}
	}
	
}
