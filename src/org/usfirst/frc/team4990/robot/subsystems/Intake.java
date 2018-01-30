package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.subsystems.motors.Motor;

//In & Out
// Constructor(motors)

public class Intake {
	private Motor motor;
	private double speed;
	private double rate = 0.9;
	
	public Intake(Motor m) {
		motor = m;
	}
	
	public void in() {
		speed = rate;
	}
	
	public void out() {
		speed = -rate;
	}
	
	public void stop() {
		speed = 0;
	}
	
	public void update() {
		motor.setPower(speed);
	}
}
