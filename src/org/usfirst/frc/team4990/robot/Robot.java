package org.usfirst.frc.team4990.robot;
//This entire robot code is dedicated to Kyler Rosen, a friend, visionary, and a hero to the empire that is the Freshmen Union(Le Dab Gang)
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4990.robot.controllers.*;
import org.usfirst.frc.team4990.robot.controllers.SimpleAutoDriveTrainScripter.StartingPosition;
import org.usfirst.frc.team4990.robot.subsystems.*;

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

	public Preferences prefs;
	
	private F310Gamepad driveGamepad;
	private F310Gamepad opGamepad;
	
	private DriveTrain driveTrain;
	private TeleopDriveTrainController teleopDriveTrainController;
	private Intake intake;
	private TeleopIntakeController teleopIntakeController;
	public Elevator elevator;
	private TeleopElevatorController teleopElevatorController;


	public ADXRS450_Gyro gyro;

	private SimpleAutoDriveTrainScripter autoScripter;
	private SimpleAutoDriveTrainScripter testScripter;

    public void robotInit() { //This function is run when the robot is first started up and should be used for any initialization code.

    	System.out.println("Version 1.29.2018.6.18");
    	this.prefs = Preferences.getInstance();

    	//~~~~ Driving/Operator Components ~~~~
    	this.driveGamepad = new F310Gamepad(this.prefs.getInt("Drive Gamepad Port", 0));
    	this.opGamepad = new F310Gamepad(this.prefs.getInt("Op Gamepad Port", 1));

    	this.driveTrain = new DriveTrain(
    		new TalonMotorController(0),
    		new TalonMotorController(1),
    		new TalonMotorController(2),
    		new TalonMotorController(3),
    		0, 1, 2, 3);

    	intake = new Intake(new TalonMotorController(5), new TalonMotorController(4), new AnalogInput(0)); //Ultrasonic DIOs  are 8 and 9
    	
    	teleopIntakeController = new TeleopIntakeController(intake, opGamepad);
    	
    	elevator = new Elevator(
    			new TalonMotorController(7), //Motor elevatorMotorA
    			new TalonMotorController(6), //Motor elevatorMotorB
    			6, //int topSwitchChannel (DIO)
    			4, //int topSwitchCounterSensitivity
    			7, //int bottomSwitchChannel (DIO)
    			4, //int bottomSwitchCounterSensitivity
    			4, //int Encoder DIO port A
    			5); //int Encoder DIO port B
    	
    	teleopElevatorController = new TeleopElevatorController(elevator,
    			opGamepad, //gamepad to control elevator
    			1.0); // max speed (0.1 to 1.0) 
    			

    	//~~~~ Sensor Init & Details ~~~~

    	gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
    	//use gyro.getAngle() to return heading (returns number 0 to n)
    	//gyro details: http://first.wpi.edu/FRC/roborio/release/docs/java/edu/wpi/first/wpilibj/ADXRS450_Gyro.html
    	
    	
    	updateAutoDashboard();
    	
    	dashboardPeriodic();

    	resetSensors();
    	
    }
    
    public void robotPeriodic() {
    	//Put nothing here or else the robot might lag severely.
    }
    
    public void disabledInit() {
    	System.out.println("ROBOT DISABLED.");
    }

    public void disabledPeriodic() { //This function is run periodically when the robot is DISABLED. Be careful.
    		if (System.currentTimeMillis() % 200 > 0 && System.currentTimeMillis() % 1000 < 50) { //runs around every 1 second
    			startPos = autoChooser.getSelected();
    			dashboardPeriodic();
    			updateAutoDashboard();
    			//System.out.println("refreshed SmartDashboard");
    		}
    }

    public void autonomousInit() { //This function is called at the start of autonomous
	    	startPos = autoChooser.getSelected();
	    	autoScripter = new SimpleAutoDriveTrainScripter(driveTrain, startPos, gyro, intake, this.elevator);
	    	System.out.println("Auto Init complete");
    }

    public void autonomousPeriodic() { //This function is called periodically during autonomous
	    	autoScripter.update();
	    	driveTrain.update();
	    	dashboardPeriodic();
    }

    public void teleopInit() { //This function is called at the start of teleop
    	this.teleopDriveTrainController = new TeleopDriveTrainController(
        		this.driveGamepad,
        		this.driveTrain,
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
	    	teleopElevatorController.update();
	    	teleopIntakeController.update();
	    	intake.update();
	    	dashboardPeriodic();
	    	if (driveGamepad.getRawButton(8)) {
	    		System.out.println("Button 7 Pressed on DRIVE GAMEPAD");
	    	} else if (opGamepad.getRawButton(8)) {
	    		System.out.println("Button 7 Pressed on OP GAMEPAD");
	    	}
    	
    } 
    
    public void testInit() { //TODO add commands for testing
    		liveWindowInit();
    		testScripter = new SimpleAutoDriveTrainScripter(driveTrain, startPos, gyro, intake, elevator);
    		testScripter.init();
    }
    
    public void testPeriodic() {
    		testScripter.update();
    }
    
    /**
     * Adds SendableChooser to SmartDashboard for Auto route choosing.
     */

    public void updateAutoDashboard() {
	    	//Auto chooser
	    	autoChooser = new SendableChooser<StartingPosition>();
	    	autoChooser.addObject("Left", StartingPosition.LEFT);
	    	autoChooser.addObject("Middle", StartingPosition.MID);
	    	autoChooser.addObject("Right",  StartingPosition.RIGHT);
	    	autoChooser.addObject("Stay", StartingPosition.STAY);
	    	autoChooser.addDefault("Forward (cross line)", StartingPosition.FORWARD);
	    	SmartDashboard.putData(autoChooser);
	    	SmartDashboard.putString("Selected Starting Position", startPos.toString());
    }
    
    	public void dashboardPeriodic() {
	    	
	    	//Other sensor gauges and data
	    	SmartDashboard.putNumber("Gyro Heading", gyro.getAngle());
	    	SmartDashboard.putNumber("Analog Infrared Voltage", intake.getAnalogInput());
	    	SmartDashboard.putNumber("Left Encoder", -this.driveTrain.getLeftDistanceTraveled());
	    	SmartDashboard.putNumber("Right Encoder", this.driveTrain.getRightDistanceTraveled());
	    	
	    	SmartDashboard.putString("The Infrared Sensor Tri Value", intake.getBoxPosition().toString());
	    	SmartDashboard.putBoolean("Box In", intake.isBoxPosition(Intake.BoxPosition.IN));
	    	SmartDashboard.putBoolean("Box Out", intake.isBoxPosition(Intake.BoxPosition.OUT));
	    	SmartDashboard.putBoolean("Box In and Out At The Same Time", intake.isBoxPosition(Intake.BoxPosition.MOVING));
	    	
	    	SmartDashboard.putBoolean("Elevator Top Limit Switch", this.elevator.isTopSwitched());
	    	SmartDashboard.putBoolean("Elevator Bottom Limit Switch", this.elevator.isBottomSwitched());
	    	
	    	SmartDashboard.updateValues(); //always run at END of dashboardPeriodic
    }

	public void resetSensors() {
    		System.out.print("Starting gyro calibration. DON'T MOVE THE ROBOT...");
    		gyro.calibrate();
    		System.out.print("Gyro calibration done. Resetting encoders...");
    		this.driveTrain.resetDistanceTraveled();
    		System.out.print("Sensor reset complete.");
	}
	
	public void liveWindowInit() {
		//Elevator
		elevator.encoder.setName("Elevator","Encoder");
		elevator.elevatorMotorA.setName("Elevator","MotorA");
		elevator.elevatorMotorB.setName("Elevator","MotorB");
		
		//Intake
		intake.motorL.setName("Intake", "LeftMotor");
		intake.motorR.setName("Intake", "RightMotor");
		intake.infrared.setName("Intake", "Infrared");
		
		//DriveTrain
		driveTrain.leftGearbox.motor1.setName("DriveTrain","LeftMotor1");
		driveTrain.leftGearbox.motor2.setName("DriveTrain","LeftMotor2");
		driveTrain.rightGearbox.motor1.setName("DriveTrain","RightMotor1");
		driveTrain.rightGearbox.motor2.setName("DriveTrain","RightMotor2");
		
		driveTrain.leftGearbox.encoder.setName("DriveTrain","LeftEncoder");
		driveTrain.rightGearbox.encoder.setName("DriveTrain","RightEncoder");
	}

}
