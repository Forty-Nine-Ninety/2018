package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.subsystems.motors.Motor;

import edu.wpi.first.wpilibj.Ultrasonic;

//In & Out
	// Constructor(motors)

public class Intake {
	private Motor motorL;
	private Motor motorR;
	private Ultrasonic ultrasonic;
	//private LimitSwitch ls;
	private double speed;
	private double rate = 0.9;
	
	
	public Intake(Motor mL, Motor mR/*, LimitSwitch switch*/) {
		motorL = mL;
		motorR = mR;
		//ls = switch;
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
		motorR.setPower(speed);
	}
	
	public void stop() {
		speed = 0;
	}
	
	public double getUltrasonicDistance() {
		return -1;
	}
}
