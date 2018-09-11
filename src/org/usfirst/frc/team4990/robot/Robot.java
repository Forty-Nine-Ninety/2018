package org.usfirst.frc.team4990.robot;
//This entire robot code is dedicated to Kyler Rosen, a friend, visionary, and a hero to the empire that is the Freshmen Union
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.*;
import edu.wpi.first.wpilibj.smartdashboard.*;

import org.usfirst.frc.team4990.robot.commands.*;
import org.usfirst.frc.team4990.robot.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory. 
 */
public class Robot extends TimedRobot {
	
	/**
	 * An enum that describes the starting position of the robot
	 * @author Class of '21 (created in 2018 season)
	 *
	 */
	public enum StartingPosition {
		LEFT, 
		MID, 
		RIGHT, 
		ERROR,
		STAY, 
		FORWARD
	};

	public SendableChooser<StartingPosition> autoChooser;
	public StartingPosition startPos = StartingPosition.FORWARD;
	
	public AutonomusCommand autonomusCommand;

	public static OI oi;
	public static RobotMap robotMap;


    public void robotInit() { //This function is run when the robot is first started up and should be used for any initialization code.

    	System.out.println("Version 1.29.2018.6.18");

    	
    	robotMap = new RobotMap();
    	oi = new OI();
    	updateAutoDashboard();
    	
    	simpleDashboardPeriodic();

    	resetSensors();
    	
    	liveWindowInit();
    	
    }
    
    public void robotPeriodic() {
    	//Don't put anything here or else the robot might lag severely.
    }
    
    public void disabledInit() {
    		System.out.println("ROBOT DISABLED.");
    }

    public void disabledPeriodic() { //This function is run periodically when the robot is DISABLED. Be careful.
    		if (System.currentTimeMillis() % 200 > 0 && System.currentTimeMillis() % 500 < 50) { //runs around every 1 second
    			startPos = autoChooser.getSelected();
    			simpleDashboardPeriodic();
    			updateAutoDashboard();
    		}
    		
    }

    public void autonomousInit() { //This function is called at the start of autonomous
	    	startPos = autoChooser.getSelected();
			if (autonomusCommand != null) {
				autonomusCommand.start();
			}
	    	System.out.println("Auto Init complete");
    }

    public void autonomousPeriodic() { //This function is called periodically during autonomous
	    	Scheduler.getInstance().run();
	    	RobotMap.driveTrain.update();
	    	RobotMap.elevator.update();
	    	simpleDashboardPeriodic();
    }

    public void teleopInit() { //This function is called at the start of teleop
		if (autonomusCommand != null) {
			autonomusCommand.cancel();
		}

    }

    
    
    public void teleopPeriodic() { //This function is called periodically during teleop
    		Scheduler.getInstance().run();
	    	simpleDashboardPeriodic();
	    	RobotMap.driveTrain.update();
	    	RobotMap.elevator.update();
	    	RobotMap.intake.update();
	    	RobotMap.scaler.update();
    } 
    
    public void testInit() { //TODO add commands for testing
    		liveWindowInit();
	    	startPos = autoChooser.getSelected();
			if (autonomusCommand != null) {
				autonomusCommand.start();
			}
    		//teleopInit();
    }
    
    public void testPeriodic() {
    		Scheduler.getInstance().run();
    		//teleopPeriodic();
    		//System.out.println(ahrs.getAngle());
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
	    	SmartDashboard.updateValues(); //always run at END of updateAutoDashboard
    }
    
    	public void simpleDashboardPeriodic() {
	    	SmartDashboard.putBoolean("Box", RobotMap.intake.isBoxPosition(Intake.BoxPosition.OUT));
	    	SmartDashboard.putBoolean("Elevator Top Limit Switch", RobotMap.elevator.isTopSwitched());
	    	SmartDashboard.putBoolean("Elevator Bottom Limit Switch", RobotMap.elevator.isBottomSwitched());
	    	
	    	SmartDashboard.updateValues(); //always run at END of simpleDashboardPeriodic
    	}
    
    	public void complexDashboardPeriodic() {
	    	
	    	//Other sensor gauges and data
	    	SmartDashboard.putNumber("Gyro Heading", RobotMap.gyro.getAngle());
	    	SmartDashboard.putNumber("Analog Infrared Voltage", RobotMap.intake.getAnalogInput());
	    	SmartDashboard.putNumber("Left Encoder", RobotMap.driveTrain.left.getDistanceTraveled());
	    	SmartDashboard.putNumber("Right Encoder", RobotMap.driveTrain.right.getDistanceTraveled());
	    	
	    	SmartDashboard.putBoolean("Box In", RobotMap.intake.isBoxPosition(Intake.BoxPosition.IN));
	    	SmartDashboard.putBoolean("Box Out", RobotMap.intake.isBoxPosition(Intake.BoxPosition.OUT));
	    	SmartDashboard.putBoolean("Box In and Out At The Same Time", RobotMap.intake.isBoxPosition(Intake.BoxPosition.MOVING));
	    	
	    	SmartDashboard.putNumber("Throttle Input", RobotMap.driveGamepad.getLeftJoystickY());
	    	SmartDashboard.putNumber("Turn Steepness Input", RobotMap.driveGamepad.getRightJoystickX());

	    	SmartDashboard.putBoolean("Elevator Top Limit Switch", RobotMap.elevator.isTopSwitched());
	    	SmartDashboard.putBoolean("Elevator Bottom Limit Switch", RobotMap.elevator.isBottomSwitched());
	    	
	    	
	    	SmartDashboard.putData("Left Drive Encoder",RobotMap.driveTrain.left.encoder);
	    	SmartDashboard.putData("Right Drive Encoder",RobotMap.driveTrain.right.encoder);
	    	
	    	SmartDashboard.updateValues(); //always run at END of dashboardPeriodic
    }

	public void resetSensors() {
    		System.out.print("Starting gyro calibration. DON'T MOVE THE ROBOT...");
    		RobotMap.gyro.calibrate();
    		RobotMap.ahrs.reset();
    		System.out.print("Gyro calibration done. Resetting encoders...");
    		RobotMap.driveTrain.resetDistanceTraveled();
    		System.out.print("Sensor reset complete.");
	}
	
	public void liveWindowInit() {
		//Elevator
		RobotMap.elevatorTalon.setName("Elevator","Motor");
		
		//Intake
		RobotMap.intakeTalonA.setName("Intake", "LeftMotor");
		RobotMap.intakeTalonB.setName("Intake", "RightMotor");
		RobotMap.intakeDistanceAnalogInput.setName("Intake", "Infrared");
		
		//DriveTrain
		RobotMap.driveTrain.left.motorGroup.setName("DriveTrain","LeftMotors");
		RobotMap.driveTrain.right.motorGroup.setName("DriveTrain","RightMotors");
		RobotMap.driveTrain.left.encoder.setName("DriveTrain","LeftEncoder");
		RobotMap.driveTrain.right.encoder.setName("DriveTrain","RightEncoder");
		
		//General
		RobotMap.gyro.setName("General", "Gyro");
		RobotMap.ahrs.setName("General", "Gyro");
	}
	
	//ever heard of the tale of last minute code
    //I thought not, it is not a tale the chairman will tell to you
	//(Keep below last function of Robot.java)

}
