package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.subsystems.motors.Motor;

//In & Out
	// Constructor(motors)

public class Intake {
	private Motor motor;
	//private LimitSwitch ls;
	private double speed;
	private double rate = 0.9;
	
	
	public Intake(Motor m/*, LimitSwitch switch*/) {
		motor = m;
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
	
	public void update() {
		motor.setPower(speed);
	}
	
	public void stop() {
		speed = 0;
	}
	
	public boolean getLimitSwitch() {
		//return ls.isSwitched();
		return true;
	}
}
