package org.usfirst.frc.team4990.robot;

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
	private int encoderChannel1L = 3;
	private int encoderChannel2L = 2;
	private int encoderChannel1R = 1;
	private int encoderChannel2R = 0;
	
	private double distance = 3, velocity = 0.25;//Distance in feet, velocity is motor power percentage, from 0-1, motor speed 12 ft/s
	private double distanceTraveled = 0;
	
	//Motors
	Sad motor1L, motor1R, motor2L, motor2R; 
	
	
	public void robotInit() {
		//What do I put here?
	}
	
	public void autonomousInit() {
		encoderLeft = new Encoder(encoderChannel1L, encoderChannel2L);
		encoderLeft.setDistancePerPulse(feetPerWheelRevolution / pulsesPerRevolution);
		encoderLeft.setMinRate(gearboxEncoderMinRate);
		encoderLeft.setSamplesToAverage(gearboxEncoderSamplesToAvg);
		
		encoderRight = new Encoder(encoderChannel1R, encoderChannel2R);
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
		motor1L = new Sad(3);
		motor2L = new Sad(2);
		motor1R = new Sad(1);
		motor2R = new Sad(0);
		
		encoderLeft.reset();
		encoderRight.reset();
	}
	
	public void autonomousPeriodic() {
		if (distanceTraveled < distance) {
			
			distanceTraveled = encoderLeft.getDistance();
			
			double distanceTraveledDifference = encoderLeft.getDistance() - encoderRight.getDistance();
			System.out.println(encoderLeft.getDistance() + " " + encoderRight.getDistance());
			double correctionalVelocity = velocity;
			
			if (distanceTraveledDifference < 0) {//Right is slower
				correctionalVelocity += (distanceTraveledDifference * -1) / 144;
			}
			else if (distanceTraveledDifference > 0) {//Right is faster
				correctionalVelocity -= (distanceTraveledDifference * -1) / 144;
			}
			
			motor1L.setSpeed(velocity);
			motor2L.setSpeed(velocity);
			correctionalVelocity *= -1;
			motor1R.setSpeed(correctionalVelocity);
			motor2R.setSpeed(correctionalVelocity);
			
			//System.out.println("Correctional: " + correctionalVelocity + " | Velocity: " + velocity + " | Distance Traveled: " + distanceTraveled + " | Distance Traveled Difference: " + distanceTraveledDifference);
		}
		else {
			motor1L.setSpeed(0);
			motor1R.setSpeed(0);
			motor2L.setSpeed(0);
			motor2R.setSpeed(0);
			System.out.println("HI!");
		}
	}
	
}
