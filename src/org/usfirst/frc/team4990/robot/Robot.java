package org.usfirst.frc.team4990.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

import java.util.concurrent.TimeUnit;

import org.usfirst.frc.team4990.robot.subsystems.motors.TalonMotorController;

import edu.wpi.first.wpilibj.Encoder;

public class Robot extends IterativeRobot {
	
	//Constants
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
	
	//HOW FAR WE WANT THE ROBOT TO GO. WE MAY NEED TO CREATE MULTIPLE DISTANCES FOR EACH TURN JUST SAYING (:
	private double distance = 3, velocity = 0.10;//Distance in feet, velocity is motor power percentage, from 0-1, motor speed 12 ft/s
	private double distanceTraveled = 0;
	
	//Motors
	TalonMotorController motor1L, motor1R, motor2L, motor2R; 
	
	private double lastLeftDistance = 0, lastRightDistance = 0, distanceDifferenceLeft, distanceDifferenceRight;
	
	//I made a function that stops the robot (:
	public void StopTheRobot() {
		motor1L.setSpeed(0);
		motor1R.setSpeed(0);
		motor2L.setSpeed(0);
		motor2R.setSpeed(0);
	}
	
	public void robotInit() {
		//What do I put here?
	}
	
	public void autonomousInit() { //Sets up the two encoders to measure output from two motors on each side
		//Left Encoder
		encoderLeft = new Encoder(encoderChannel1L, encoderChannel2L);
		encoderLeft.setDistancePerPulse(feetPerWheelRevolution / pulsesPerRevolution);
		encoderLeft.setMinRate(gearboxEncoderMinRate);
		encoderLeft.setSamplesToAverage(gearboxEncoderSamplesToAvg);
		//Right Encoder
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
		//Defines the motors
		motor1L = new TalonMotorController(encoderChannel1L);
		motor2L = new TalonMotorController(encoderChannel2L);
		motor1R = new TalonMotorController(encoderChannel1R);
		motor2R = new TalonMotorController(encoderChannel2R);
		
		//Resets the data on both encoders
		encoderLeft.reset();
		encoderRight.reset();
	}
	
	//Happens periodically throughout the robots driving
	public void autonomousPeriodic() {
		if (distanceTraveled < distance || distanceTraveled > distance) {
			updateDistances();
			distanceTraveled = encoderLeft.getDistance();
			
			double difference = (distanceDifferenceLeft - distanceDifferenceRight) / 2;
			
			motor1L.setSpeed(velocity - difference);
			motor2L.setSpeed(velocity - difference);
			motor1L.setSpeed(velocity + difference);
			motor2L.setSpeed(velocity + difference);
			
			/*
			distanceTraveled = encoderLeft.getDistance();
			
			double distanceTraveledDifference = encoderLeft.getDistance() + encoderRight.getDistance(); //The distance variation between the left and right sides
			System.out.print("ThiqqBoi\t\t\t\t");
			System.out.println(encoderLeft.getDistance() + " " + encoderRight.getDistance() * -1); //Prints data to console
			double correctionalVelocity = velocity; //Correctional Velocity Starts off As Normal Velocity
			
			
			if (distanceTraveledDifference < 0) {//if Right is slower
				correctionalVelocity += (distanceTraveledDifference * -1) / 12; //Speeds up
			}
			else if (distanceTraveledDifference > 0) {//if Right is faster
				correctionalVelocity -= (distanceTraveledDifference * -1) / 12; //Slows down
			}
			System.out.println(correctionalVelocity);
			
			//Right will correct itself based on the correctional velocity needed. Left never corrects.
			motor1L.setSpeed(velocity);
			motor2L.setSpeed(velocity);
			correctionalVelocity *= -1;
			motor1R.setSpeed(correctionalVelocity); 
			motor2R.setSpeed(correctionalVelocity);
			*/
			
		}
		else {//If it has reached distance, stop all the motors
			StopTheRobot();
		}
	}
	
	
	//Random functions for myself
	
	private void updateDistances() {
		distanceDifferenceLeft = encoderLeft.getDistance() - lastLeftDistance;
		distanceDifferenceRight = encoderRight.getDistance() * -1 - lastRightDistance;
		lastLeftDistance = encoderLeft.getDistance();
		lastRightDistance = encoderRight.getDistance() * -1;
	}
	
}

//NEW ERA CODE

