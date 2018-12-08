/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4990.robot;

import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4990.robot.subsystems.Elevator;
import org.usfirst.frc.team4990.robot.subsystems.F310Gamepad;
import org.usfirst.frc.team4990.robot.subsystems.Gearbox;
import org.usfirst.frc.team4990.robot.subsystems.Intake;
import org.usfirst.frc.team4990.robot.subsystems.LimitSwitch;
import org.usfirst.frc.team4990.robot.subsystems.Scaler;
import org.usfirst.frc.team4990.robot.subsystems.TalonMotorController;
import com.kauailabs.navx.frc.AHRS;

//import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Ultrasonic;
//import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */

public class RobotMap { 
	
	public static PowerDistributionPanel pdp;
	
	public static F310Gamepad driveGamepad;
	public static F310Gamepad opGamepad;

	public static TalonMotorController leftFrontDriveTalon;
	public static TalonMotorController leftRearDriveTalon;
	public static TalonMotorController rightFrontDriveTalon;
	public static TalonMotorController rightRearDriveTalon;
	public static TalonMotorController intakeTalonA;
	public static TalonMotorController intakeTalonB;
	public static TalonMotorController elevatorTalon;
	public static TalonMotorController scalerTalon;
	
	public static DigitalOutput ultrasonicDigitalOutput;
	public static DigitalInput ultrasonicEchoDigitalInput;
	public static DigitalInput leftEncoderPortA;
	public static DigitalInput leftEncoderPortB;
	public static DigitalInput rightEncoderPortA;
	public static DigitalInput rightEncoderPortB;
	public static DigitalInput elevatorLimitSwitchTopInput;
	public static DigitalInput elevatorLimitSwitchBottomInput;
	public static AnalogInput intakeDistanceAnalogInput;

	public static Encoder leftEncoder;
	public static Encoder rightEncoder;

	public static LimitSwitch elevatorLimitSwitchTop;
	public static LimitSwitch elevatorLimitSwitchBottom;
	
	public static Gearbox leftGearbox;
	public static Gearbox rightGearbox;
	
	public static SpeedControllerGroup leftMotorGroup;
	public static SpeedControllerGroup rightMotorGroup;
	
	public static DriveTrain driveTrain;
	public static Intake intake;
	public static Elevator elevator;
	public static Scaler scaler;
	
	public static Ultrasonic ultrasonic;
	public static AHRS ahrs;

	//public static ADXRS450_Gyro gyro;
	
	public RobotMap() {
		
		pdp = new PowerDistributionPanel();
		
		driveGamepad = new F310Gamepad(0);
		opGamepad = new F310Gamepad(1);
		
		leftFrontDriveTalon = new TalonMotorController(1);
		leftRearDriveTalon = new TalonMotorController(2);
		rightFrontDriveTalon = new TalonMotorController(3);
		rightRearDriveTalon = new TalonMotorController(4);
		intakeTalonA = new TalonMotorController(8);
		intakeTalonB = new TalonMotorController(7);
		elevatorTalon = new TalonMotorController(6);
		scalerTalon = new TalonMotorController(9);
		
		ultrasonicDigitalOutput = new DigitalOutput(4); //PING
		ultrasonicEchoDigitalInput = new DigitalInput(5); //ECHO
		leftEncoderPortA = new DigitalInput(0);
		leftEncoderPortB = new DigitalInput(1);
		rightEncoderPortA = new DigitalInput(2);
		rightEncoderPortB = new DigitalInput(3);
		elevatorLimitSwitchTopInput = new DigitalInput(6);
		elevatorLimitSwitchBottomInput = new DigitalInput(7);
		intakeDistanceAnalogInput = new AnalogInput(0);
		
		leftEncoder = new Encoder(leftEncoderPortA, leftEncoderPortB);
		rightEncoder = new Encoder(rightEncoderPortA, rightEncoderPortB);

		elevatorLimitSwitchTop = new LimitSwitch(elevatorLimitSwitchTopInput);
		elevatorLimitSwitchBottom = new LimitSwitch(elevatorLimitSwitchBottomInput);
		
		leftGearbox = new Gearbox(leftFrontDriveTalon, leftRearDriveTalon, leftEncoder);
		rightGearbox = new Gearbox(rightFrontDriveTalon, rightRearDriveTalon, rightEncoder);
		
		leftMotorGroup = new SpeedControllerGroup(leftFrontDriveTalon, leftRearDriveTalon);
		rightMotorGroup = new SpeedControllerGroup(rightFrontDriveTalon, rightRearDriveTalon);
		//differentialDrive = new DifferentialDrive(leftMotorGroup, rightMotorGroup);
					
		driveTrain = new DriveTrain(leftGearbox, rightGearbox);
		intake = new Intake();
		elevator = new Elevator();
		scaler = new Scaler(scalerTalon);

		ultrasonic = new Ultrasonic(ultrasonicDigitalOutput, ultrasonicEchoDigitalInput, Ultrasonic.Unit.kInches);		
		ahrs = new AHRS(SPI.Port.kMXP); 
		//navX-MXP RoboRIO extension and 9-axis gyro thingy
		//for simple gyro angles: use ahrs.getAngle() to get heading (returns number -n to n) and reset() to reset angle (and drift)

		/*
		gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
		//use gyro.getAngle() to return heading (returns number -n to n) and reset() to reset angle
		//gyro details: http://first.wpi.edu/FRC/roborio/release/docs/java/edu/wpi/first/wpilibj/ADXRS450_Gyro.html
		*/
	} 
}
