package org.usfirst.frc.team4990.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Ultrasonic;

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
	private DriveTrain driveTrain;
	private Intake intake;
	
	public Ultrasonic ultrasonicSensor;
	public ADXRS450_Gyro gyro; //use gyro.
	
	
	private SimpleAutoDriveTrainScripter autoScripter;
	
	private TeleopDriveTrainController teleopDriveTrainController;
	
	private TeleopIntakeController teleopIntakeController;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    	System.out.println("Version 1.29.2018.6.18");
    	this.prefs = Preferences.getInstance();
    	
    	//~~~~ Driving Components ~~~~
    	
    	this.driveGamepad = new F310Gamepad(1);
    	
    	this.driveTrain = new DriveTrain( 
    		new TalonMotorController(0),
    		new TalonMotorController(1),
    		new TalonMotorController(2),
    		new TalonMotorController(3),
    		0, 1, 2, 3);
    	
    	intake = new Intake(new TalonMotorController(5));
    	
    	teleopIntakeController = new TeleopIntakeController(intake, driveGamepad);
    	
    	//~~~~ Sensor Init & Details ~~~~
    	
    	gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0); 
    	//use gyro.getAngle() to return heading (returns number 0 to n)
    	//gyro details: http://first.wpi.edu/FRC/roborio/release/docs/java/edu/wpi/first/wpilibj/ADXRS450_Gyro.html
    	
    	//	(we don't use the) ping digital io channel   echo digital io channel
    	//											|   |
    	Ultrasonic ultrasonicSensor = new Ultrasonic(9, 6);
    	ultrasonicSensor.setEnabled(true);
    	//use ultrasonicSensor.getRangeInches() to get current distance
    	//NEVER try to use ultrasonicSensor.ping() (It might break everything since there is no ping wire)
    	//see https://www.maxbotix.com/Ultrasonic_Sensors/MB1003.htm

    	updateDashboard();
    	
    
    }

    public void disabledPeriodic() { //just an idea, @wiley, what do you think for updating SmartDashboard?
    		if (System.currentTimeMillis() % 1000 > 0 && System.currentTimeMillis() % 1000 < 50) { //runs around every 1 second
    			startPos = autoChooser.getSelected();
    			updateDashboard();
    			//System.out.println("refreshed SmartDashboard");
    		}
    }
    
    public void autonomousInit() {
    	startPos = autoChooser.getSelected();
    	autoScripter = new SimpleAutoDriveTrainScripter(driveTrain, startPos, gyro);
    	System.out.println("Auto Init");
    }
    
    public void autonomousPeriodic() { //This function is called periodically during autonomous
    	autoScripter.update();
    	driveTrain.update();
    	updateDashboard();
    }
    
    public void teleopInit() {
    	this.teleopDriveTrainController = new TeleopDriveTrainController(
        		this.driveGamepad, 
        		this.driveTrain, 
        		this.prefs.getDouble("maxTurnRadius", Constants.defaultMaxTurnRadius),
        		this.prefs.getBoolean("reverseTurningFlipped", false),
        		this.prefs.getDouble("smoothDriveAccTime", Constants.defaultAccelerationTime),
        		this.prefs.getDouble("lowThrottleMultiplier", .25),
        		this.prefs.getDouble("maxThrottle", 1.0));
    }
     
    public void teleopPeriodic() { //This function is called periodically during operator control
    	
	    this.teleopDriveTrainController.updateDriveTrainState();
	    
	    //ever heard of the tale of last minute code
	    //I thought not, it is not a tale the chairman will tell to you

    	this.driveTrain.update();
    	
    	teleopIntakeController.update();
    	intake.update();

    }
    
    public void updateDashboard() {
    	//~~~~ Smart Dashboard ~~~~ 
    	//Auto chooser
    	autoChooser = new SendableChooser<StartingPosition>();
    	autoChooser.addObject("Left", StartingPosition.LEFT);
    	autoChooser.addObject("Middle", StartingPosition.MID);
    	autoChooser.addObject("Right",  StartingPosition.RIGHT);
    	autoChooser.addObject("Stay", StartingPosition.STAY);
    	autoChooser.addDefault("Cross Line", StartingPosition.FORWARD);
    	SmartDashboard.putData("Auto Location Chooser", autoChooser);
    	SmartDashboard.putString("Selected Starting Position", startPos.toString());
    	//Other gauges and data
    	//SmartDashboard.putNumber("Ultrasonic distance", ultrasonicSensor.getRangeInches());
    	SmartDashboard.putNumber("gyro heading", gyro.getAngle());
    	SmartDashboard.updateValues();
    }
}
