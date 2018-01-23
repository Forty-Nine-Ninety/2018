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
	private int encoderChannel1L = 2;
	private int encoderChannel2L = 3;
	private int encoderChannel1R = 0;
	private int encoderChannel2R = 1;
	
	//HOW FAR WE WANT THE ROBOT TO GO. WE MAY NEED TO CREATE MULTIPLE DISTANCES FOR EACH TURN JUST SAYING (:
	private double distance = 7, velocity = 0.15;//Distance in feet, velocity is motor power percentage, from 0-1, motor speed 12 ft/s
	private double distanceTraveled = 0;
	
	//AUTO config testing
	private boolean autoDone = false;
	
	//Motors
	TalonMotorController motor1L, motor1R, motor2L, motor2R; 
	
	
	public void robotInit() {
		//What do I put here?
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
			
			//Defines the motors
			motor1L = new TalonMotorController(0);
			motor2L = new TalonMotorController(1);
			motor1R = new TalonMotorController(2);
			motor2R = new TalonMotorController(3);
	}
	
	public void autonomousInit() { //Sets up the two encoders to measure output from two motors on each side
		
		try {//Sleep code; needs try-catch so don't delete
			TimeUnit.SECONDS.sleep(2);//Sleeps 2 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();//Print if there is error and then leave
			return;
		}
		
		//Resets the data on both encoders
		encoderLeft.reset();
		encoderRight.reset();
	}
	
	//Happens periodically throughout the robots driving(Once every 5ms)
	public void autonomousPeriodic() {
		if (distanceTraveled * 1.125 < distance && this.autoDone == false) {//Moves specified distance
			
			distanceTraveled = encoderLeft.getDistance(); /* Sets distanceTraveled to the distance traveled by
			encoderLeft;I chose left b/c I am left handed, but right can work too.  I just like left better. */
			
			/*
			motor1L.setSpeed(velocity * 0.727);
			motor2L.setSpeed(velocity * 0.727);
			motor1R.setSpeed(-velocity);
			motor2R.setSpeed(-velocity);
			*/
			
			System.out.println(this.distanceTraveled * 1.125 + " " + encoderLeft.getDistance() + " " + encoderRight.getDistance() * -1);//Prints "debug" info
			
			
		}
		else {
			//STOP
			System.out.println("DONE");
			distanceTraveled = 0;//idk what Wiley was doing
			autoDone = true;//Makes loop stop
			
		}
	}
	
}