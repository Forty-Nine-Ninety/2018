package org.usfirst.frc.team4990.robot;
//This entire robot code is dedicated to Kyler Rosen, a friend, visionary, and a hero to the empire that is the Freshmen Union
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

import org.usfirst.frc.team4990.robot.controllers.*;
import org.usfirst.frc.team4990.robot.controllers.SimpleAutoDriveTrainScripter.StartingPosition;
import org.usfirst.frc.team4990.robot.subsystems.*;
import org.usfirst.frc.team4990.robot.subsystems.motors.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public SendableChooser<StartingPosition> autoChooser;
	public StartingPosition startPos = StartingPosition.FORWARD;

	private Preferences prefs;
	private F310Gamepad driveGamepad;
	private F310Gamepad opGamepad;
	private DriveTrain driveTrain;
	private Intake intake;
	private TeleopElevatorController teleopElevatorController;

	public UltrasonicSensor ultrasonicSensor;
	public ADXRS450_Gyro gyro;
	public AnalogInput ultrasonicInput;


	private SimpleAutoDriveTrainScripter autoScripter;

	private TeleopDriveTrainController teleopDriveTrainController;

	private TeleopIntakeController teleopIntakeController;

    public void robotInit() { //This function is run when the robot is first started up and should be used for any initialization code.

    	System.out.println("Version 1.29.2018.6.18");
    	this.prefs = Preferences.getInstance();

    	//~~~~ Driving/Operator Components ~~~~

    	this.driveGamepad = new F310Gamepad(1);
    	//this.opGamepad = new F310Gamepad(2);

    	this.driveTrain = new DriveTrain(
    		new TalonMotorController(0),
    		new TalonMotorController(1),
    		new TalonMotorController(2),
    		new TalonMotorController(3),
    		0, 1, 2, 3);

    	intake = new Intake(new TalonMotorController(5));

    	teleopIntakeController = new TeleopIntakeController(intake, driveGamepad);
    	
    	/*teleopElevatorController = new TeleopElevatorController(new Elevator(
    			new TalonMotorController(4), //Motor elevatorMotor
    			, //int topSwitchChannel (DIO)
    			4, //int topSwitchCounterSensitivity
    			, //int bottomSwitchChannel (DIO)
    			4), //int bottomSwitchCounterSensitivity
    			driveGamepad, //gamepad to control elevator
    			1.0);*/


    	//~~~~ Sensor Init & Details ~~~~

    	gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
    	//use gyro.getAngle() to return heading (returns number 0 to n)
    	//gyro details: http://first.wpi.edu/FRC/roborio/release/docs/java/edu/wpi/first/wpilibj/ADXRS450_Gyro.html

    	ultrasonicInput = new AnalogInput(0);
    	ultrasonicSensor = new UltrasonicSensor(ultrasonicInput);
    	//use ultrasonicSensor.getDistance() to get current distance
    	//see https://www.maxbotix.com/Ultrasonic_Sensors/MB1003.htm

    	updateDashboard();

    }
    
    public void robotPeriodic() {
    	//Put nothing here or else the robot might lag severely.
    }
    
    public void disabledInit() {
    	System.out.println("ROBOT DISABLED.");
    }

    public void disabledPeriodic() { //This function is run periodically when the robot is DISABLED. Be careful.
    		if (System.currentTimeMillis() % 1000 > 0 && System.currentTimeMillis() % 1000 < 50) { //runs around every 1 second
    			startPos = autoChooser.getSelected();
    			updateDashboard();
    			//System.out.println("refreshed SmartDashboard");
    		}
    }

    public void autonomousInit() { //This function is called at the start of autonomous
    	startPos = autoChooser.getSelected();
    	autoScripter = new SimpleAutoDriveTrainScripter(driveTrain, startPos, gyro);
    	System.out.println("Auto Init complete");
    }

    public void autonomousPeriodic() { //This function is called periodically during autonomous
    	autoScripter.update();
    	driveTrain.update();
    	updateDashboard();
    }

    public void teleopInit() { //This function is called at the start of teleop
    	this.teleopDriveTrainController = new TeleopDriveTrainController(
        		this.driveGamepad,
        		this.driveTrain,
        		this.prefs.getDouble("maxTurnRadius", Constants.defaultMaxTurnRadius),
        		this.prefs.getBoolean("reverseTurningFlipped", false),
        		this.prefs.getDouble("smoothDriveAccTime", Constants.defaultAccelerationTime),
        		this.prefs.getDouble("lowThrottleMultiplier", .25),
        		this.prefs.getDouble("maxThrottle", 1.0));
    }

    public void teleopPeriodic() { //This function is called periodically during teleop

	    this.teleopDriveTrainController.updateDriveTrainState();

	    //ever heard of the tale of last minute code
	    //I thought not, it is not a tale the chairman will tell to you

    	this.driveTrain.update();

    	teleopIntakeController.update();
    	intake.update();

    }

    public void updateDashboard() {
    	//Auto chooser
    	autoChooser = new SendableChooser<StartingPosition>();
    	autoChooser.addObject("Left", StartingPosition.LEFT);
    	autoChooser.addObject("Middle", StartingPosition.MID);
    	autoChooser.addObject("Right",  StartingPosition.RIGHT);
    	autoChooser.addObject("Stay", StartingPosition.STAY);
    	autoChooser.addDefault("Forward (cross line)", StartingPosition.FORWARD);
    	SmartDashboard.putData("Auto Location Chooser", autoChooser);
    	SmartDashboard.putString("Selected Starting Position", startPos.toString());
    	
    	//Other sensor gauges and data
    	SmartDashboard.putNumber("Ultrasonic Distance", ultrasonicSensor.getDistance());
    	SmartDashboard.putNumber("Gyro Heading", gyro.getAngle());
    	SmartDashboard.putNumber("Left Encoder", -this.driveTrain.getLeftDistanceTraveled());
    	SmartDashboard.putNumber("Right Encoder", this.driveTrain.getRightDistanceTraveled());
    	SmartDashboard.updateValues();
    }
}
