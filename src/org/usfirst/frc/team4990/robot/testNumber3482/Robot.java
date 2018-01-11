package org.usfirst.frc.team4990.robot.testNumber3482;

import edu.wpi.first.wpilibj.IterativeRobot;

import java.util.concurrent.TimeUnit;

import edu.wpi.first.wpilibj.Encoder;

public class Robot extends IterativeRobot {
	
	//Variables
	private int pulsesPerRevolution = 250;
	private double feetPerWheelRevolution = 4.0 / 12.0 * Math.PI;
	private double gearboxEncoderMinRate = 0.0;
	private int gearboxEncoderSamplesToAvg = 5;
	
	//Encoders
	private Encoder encoderRight, encoderLeft;
	
	//Digital IO Pins
	private int encoderChannel1L = 2;
	private int encoderChannel2L = 3;
	private int encoderChannel1R = 0;
	private int encoderChannel2R = 1;
	
	private double distance = 3, velocity = 0.25;//Distance in feet, velocity is motor power percentage, from 0-1, motor speed 12 ft/s
	private double distanceTraveled = 0;
	
	//Motors
	Sad motor1L, motor1R, motor2L, motor2R; 
	
	
	public void robotInit() {
		//What do I put here?
	}
	
	public void autonomousInit() {
		encoderLeft.setDistancePerPulse(feetPerWheelRevolution / pulsesPerRevolution);
		encoderLeft.setMinRate(gearboxEncoderMinRate);
		encoderLeft.setSamplesToAverage(gearboxEncoderSamplesToAvg);
		
		encoderRight.setDistancePerPulse(feetPerWheelRevolution / pulsesPerRevolution);
		encoderRight.setMinRate(gearboxEncoderMinRate);
		encoderRight.setSamplesToAverage(gearboxEncoderSamplesToAvg);
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		motor1L = new Sad(2);
		motor2L = new Sad(3);
		motor1R = new Sad(0);
		motor2R = new Sad(1);
		
		encoderLeft.reset();
		encoderRight.reset();
	}
	
	public void autonomousPeriodic() {
		if (distanceTraveled < distance) {
			motor1L.setSpeed(velocity);
			motor1R.setSpeed(velocity);
			motor2L.setSpeed(velocity);
			motor2R.setSpeed(velocity);
			
			distanceTraveled = encoderLeft.getDistance();
		}
		
		motor1L.setSpeed(0);
		motor1R.setSpeed(0);
		motor2L.setSpeed(0);
		motor2R.setSpeed(0);
	}
	
}
