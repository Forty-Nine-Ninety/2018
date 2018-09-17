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
	
	/**
	 * COMPLETE credit goes to Deep Blue Robotics. Link: https://github.com/DeepBlueRobotics/RobotCode2018/blob/master/Robot2018/src/main/java/org/usfirst/frc/team199/Robot2018/Robot.java
	 * Benjamin has received permission to use this code. Thank you team 199!
	 */
	
	
	public static SmartDashboardInterface sd = new SmartDashboardInterface() {
		
		/**
		 * Retrieves a numerical constant from SmartDashbaord/Shuffleboard.
		 * @param key string key to identify value
		 * @param def number to return if no value is retrieved
		 * @author Deep Blue Robotics (Team 199)
		 */
		
		@Override
		public double getConst(String key, double def) {
			Preferences pref = Preferences.getInstance();
			if (!pref.containsKey("Const/" + key)) {
				pref.putDouble("Const/" + key, def);
				if (pref.getDouble("Const/ + key", def) != def) {
					System.err.println("pref Key" + "Const/" + key + "already taken by a different type");
					return def;
				}
			}
			return pref.getDouble("Const/" + key, def);
		}
		
		/**
		 * Retrieves a string from SmartDashbaord/Shuffleboard.
		 * @param key string key to identify value
		 * @param def string to return if no value is retrieved
		 * @author Deep Blue Robotics (Team 199)
		 */

		@Override
		public String getString(String key, String def) {
			Preferences pref = Preferences.getInstance();
			if (!pref.containsKey("String/" + key)) {
				pref.putString("String/" + key, def);
				if (pref.getString("String/ + key", def) != def) {
					System.err.println("pref Key" + "String/" + key + "already taken by a different type");
					return def;
				}
			}
			return pref.getString("String/" + key, def);
		}

		/**
		 * Adds a constant to SmartDashbaord/Shuffleboard.
		 * @param key string key to identify value
		 * @param def value to be stored
		 * @author Deep Blue Robotics (Team 199)
		 */
		
		@Override
		public void putConst(String key, double def) {
			Preferences pref = Preferences.getInstance();
			pref.putDouble("Const/" + key, def);
			if (pref.getDouble("Const/ + key", def) != def) {
				System.err.println("pref Key" + "Const/" + key + "already taken by a different type");
			}
		}
		
		/**
		 * Adds a Sendable to SmartDashbaord/Shuffleboard.
		 * @param key string key to identify value
		 * @param def data to be stored
		 * @author Deep Blue Robotics (Team 199)
		 */

		@Override
		public void putData(String string, Sendable data) {
			SmartDashboard.putData(string, data);
		}
		
		/**
		 * Adds a number to SmartDashbaord/Shuffleboard.
		 * @param key string key to identify value
		 * @param def number to be stored
		 * @author Deep Blue Robotics (Team 199)
		 */

		@Override
		public void putNumber(String string, double d) {
			SmartDashboard.putNumber(string, d);
		}
		
		/**
		 * Adds a Boolean to SmartDashbaord/Shuffleboard.
		 * @param key string key to identify value
		 * @param def string to be stored
		 * @author Deep Blue Robotics (Team 199)
		 */

		@Override
		public void putBoolean(String string, boolean b) {
			SmartDashboard.putBoolean(string, b);
		}

		/*
		 * if (!SmartDashboard.containsKey("Const/" + key)) { if
		 * (!SmartDashboard.putNumber("Const/" + key, def)) {
		 * System.err.println("SmartDashboard Key" + "Const/" + key +
		 * "already taken by a different type"); return def; } } return
		 * SmartDashboard.getNumber("Const/" + key, def);
		 */
	};
	
	/**
	 * Retrieves a numerical constant from SmartDashbaord/Shuffleboard.
	 * @param key string key to identify value
	 * @param def number to return if no value is retrieved
	 * @author Deep Blue Robotics (Team 199)
	 */

	public static double getConst(String key, double def) {
		return sd.getConst(key, def);
	}
	
	/**
	 * Retrieves a string from SmartDashbaord/Shuffleboard.
	 * @param key string key to identify value
	 * @param def string to return if no value is retrieved
	 * @author Deep Blue Robotics (Team 199)
	 */

	public static String getString(String key, String def) {
		return sd.getString(key, def);
	}
	
	

	public static void putConst(String key, double def) {
		sd.putConst(key, def);
	}

	/**
	 * Retrieves a boolean from SmartDashbaord/Shuffleboard.
	 * @param key string key to identify value
	 * @param def boolean to return if no value is retrieved
	 * @author Deep Blue Robotics (Team 199)
	 */
	
	public static boolean getBool(String key, boolean def) {
		Preferences pref = Preferences.getInstance();
		if (!pref.containsKey("Bool/" + key)) {
			pref.putBoolean("Bool/" + key, def);
			if (pref.getBoolean("Bool/" + key, def) == def) {
				System.err.println("pref Key" + "Bool/" + key + "already taken by a different type");
				return def;
			}
		}
		return pref.getBoolean("Bool/" + key, def);
	}
	
	/**
	 * @author Team 4990
	 */

    public void robotInit() { //This function is run when the robot is first started up and should be used for any initialization code.

    	System.out.println("Version 1.29.2018.6.18");
    	
    	robotMap = new RobotMap();
    	oi = new OI();
    	
    	//CameraServer.getInstance().startAutomaticCapture();
    	
    	updateAutoDashboard();
    	
    	simpleDashboardPeriodic();
    	
    	complexDashboardPeriodic();

    	resetSensors();
    		
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
	    	Scheduler.getInstance().run(); //runs execute() of current commands and period() of subsystems.
	    	simpleDashboardPeriodic();
    }

    public void teleopInit() { //This function is called at the start of teleop
		if (autonomusCommand != null) {
			autonomusCommand.cancel();
		}

    }

    
    
    public void teleopPeriodic() { //This function is called periodically during teleop
    		Scheduler.getInstance().run(); //runs execute() of current commands and period() of subsystems.
	    	simpleDashboardPeriodic();
    } 
    
    public void testInit() { 
    		complexDashboardPeriodic();
	    	startPos = autoChooser.getSelected();
			if (autonomusCommand != null) {
				autonomusCommand.start();
			}
    		//teleopInit();
    }
    
    public void testPeriodic() {
    		Scheduler.getInstance().run(); //runs execute() of current commands and period() of subsystems.
    		//teleopPeriodic();
    		//System.out.println(ahrs.getAngle());
    		complexDashboardPeriodic();
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
	    	autoChooser.setName("AutonomusControl","Auto Chooser");
	    	SmartDashboard.putData("DriveTeam/Auto Chooser",autoChooser);
	    	SmartDashboard.putString("Drive/Selected Starting Position", startPos.toString());
	    	SmartDashboard.updateValues(); //always run at END of updateAutoDashboard
    }
    
    	public void simpleDashboardPeriodic() {
	    	SmartDashboard.putBoolean("DriveTeam/Box", RobotMap.intake.isBoxPosition(Intake.BoxPosition.OUT));
	    	SmartDashboard.putBoolean("DriveTeam/Elevator Top Limit Switch", RobotMap.elevator.isTopSwitched());
	    	SmartDashboard.putBoolean("DriveTeam/Elevator Bottom Limit Switch", RobotMap.elevator.isBottomSwitched());
	    	
	    	SmartDashboard.updateValues(); //always run at END of simpleDashboardPeriodic
    	}
    
    	public void complexDashboardPeriodic() {
    		
    		
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
    		RobotMap.differentialDrive.setName("DriveTrain", "DifferentialDrive");
    		
    		//General
    		RobotMap.pdp.setName("General", "PDP");
    		RobotMap.gyro.setName("General", "SPI Gyro");
    		RobotMap.ahrs.setName("General", "AHRS Gyro");
    		
	    	//Other sensor gauges and data
    		
    		SmartDashboard.putData("Debug/PDP",RobotMap.pdp);
    		SmartDashboard.putNumber("Debug/Throttle Input", RobotMap.driveGamepad.getLeftJoystickY());
	    	SmartDashboard.putNumber("Debug/Turn Steepness Input", RobotMap.driveGamepad.getRightJoystickX());
    		
    		SmartDashboard.putData("Debug/DifferentialDrive", RobotMap.differentialDrive);
    		
    		SmartDashboard.putNumber("Debug/Left Encoder Distance", RobotMap.driveTrain.left.getDistanceTraveled());
	    	SmartDashboard.putNumber("Debug/Right Encoder Distance", RobotMap.driveTrain.right.getDistanceTraveled());
	    	SmartDashboard.putData("Debug/Left Drive Encoder", RobotMap.driveTrain.left.encoder);
	    	SmartDashboard.putData("Debug/Right Drive Encoder", RobotMap.driveTrain.right.encoder);
	    	
	    	SmartDashboard.putBoolean("Debug/Box In", RobotMap.intake.isBoxPosition(Intake.BoxPosition.IN));
	    	SmartDashboard.putBoolean("Debug/Box Out", RobotMap.intake.isBoxPosition(Intake.BoxPosition.OUT));
	    	SmartDashboard.putBoolean("Debug/Box In and Out At The Same Time", RobotMap.intake.isBoxPosition(Intake.BoxPosition.MOVING));
	    	SmartDashboard.putNumber("Debug/Analog Infrared Voltage", RobotMap.intake.getAnalogInput());
	    	
	    	SmartDashboard.putData("Debug/IntakeAMotorLEFT", RobotMap.intakeTalonA);
	    	SmartDashboard.putData("Debug/IntakeBMotorRIGHT", RobotMap.intakeTalonB);
	    	
	    	SmartDashboard.putBoolean("Debug/Elevator Top Limit Switch", RobotMap.elevator.isTopSwitched());
	    	SmartDashboard.putBoolean("Debug/Elevator Bottom Limit Switch", RobotMap.elevator.isBottomSwitched());
	    
	    	SmartDashboard.putData("Debug/SPI Gyro", RobotMap.gyro);
	    	SmartDashboard.putData("Debug/AHRS Gyro", RobotMap.ahrs);
	    	SmartDashboard.putData("Debug/Ultrasonic", RobotMap.ultrasonic);
	    	
	    	SmartDashboard.putData("Debug/DriveTrainSubsystem", RobotMap.driveTrain);
	    	SmartDashboard.putData("Debug/ElevatorSubsystem", RobotMap.elevator);
	    	SmartDashboard.putData("Debug/IntakeSubsystem", RobotMap.intake);
	    	SmartDashboard.putData("Debug/Scheduler", Scheduler.getInstance());
	    	
	    	SmartDashboard.updateValues(); 
    }

	public void resetSensors() {
    		System.out.println("[SensorReset] Starting gyro calibration. DON'T MOVE THE ROBOT...");
    		RobotMap.gyro.calibrate();
    		RobotMap.ahrs.reset();
    		System.out.println("[SensorReset] Gyro calibration done. Resetting encoders...");
    		RobotMap.driveTrain.resetDistanceTraveled();
    		System.out.println("[SensorReset] complete.");
	}
	
	//ever heard of the tale of last minute code
    //I thought not, it is not a tale the chairman will tell to you
	//(Keep below last function of Robot.java)

}
