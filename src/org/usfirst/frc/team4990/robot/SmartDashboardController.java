package org.usfirst.frc.team4990.robot;

import edu.wpi.first.wpilibj.Preferences;

public class SmartDashboardController {

	private Preferences preferences = Preferences.getInstance();

	/**
	 * Retrieves a numerical constant from SmartDashbaord/Shuffleboard.
	 * 
	 * @param key
	 *            string key to identify value
	 * @param def
	 *            number to return if no value is retrieved
	 * @author Deep Blue Robotics (Team 199)
	 */

	public double getConst(String key, double def) {

		if (!preferences.containsKey("Const/" + key)) {
			preferences.putDouble("Const/" + key, def);
			if (preferences.getDouble("Const/ + key", def) != def) {
				System.err.println("pref Key" + "Const/" + key + "already taken by a different type");
				return def;
			}
		}
		return preferences.getDouble("Const/" + key, def);
	}

	/**
	 * Retrieves a string from SmartDashbaord/Shuffleboard.
	 * 
	 * @param key
	 *            string key to identify value
	 * @param def
	 *            string to return if no value is retrieved
	 * @author Deep Blue Robotics (Team 199)
	 */

	
	public String getString(String key, String def) {
		if (!preferences.containsKey("String/" + key)) {
			preferences.putString("String/" + key, def);
			if (preferences.getString("String/ + key", def) != def) {
				System.err.println("pref Key" + "String/" + key + "already taken by a different type");
				return def;
			}
		}
		return preferences.getString("String/" + key, def);
	}

	/**
	 * Adds a constant to SmartDashbaord/Shuffleboard.
	 * 
	 * @param key
	 *            string key to identify value
	 * @param def
	 *            value to be stored
	 * @author Deep Blue Robotics (Team 199)
	 */

	
	public void putConst(String key, double def) {
		preferences.putDouble("Const/" + key, def);
		if (preferences.getDouble("Const/ + key", def) != def) {
			System.err.println("pref Key" + "Const/" + key + "already taken by a different type");
		}
	}

	
	public void putString(String string, String def) {
		preferences.putString("String/" + string, def);
		if (preferences.getString("String/ + key", def) != def) {
			System.err.println("pref Key" + "String/" + def + "already taken by a different type");
		}

	}

	/**
	 * Retrieves a boolean from SmartDashbaord/Shuffleboard.
	 * 
	 * @param key
	 *            string key to identify value
	 * @param def
	 *            boolean to return if no value is retrieved
	 * @author Deep Blue Robotics (Team 199)
	 */

	@SuppressWarnings("unused")
	public boolean getBool(String key, boolean def) {
		if (!preferences.containsKey("Bool/" + key)) {
			preferences.putBoolean("Bool/" + key, def);
			if (preferences.getBoolean("Bool/" + key, def) == def) {
				System.err.println("pref Key" + "Bool/" + key + "already taken by a different type");
				return def;
			}
		}
		return preferences.getBoolean("Bool/" + key, def);
	}

	
	public void putBool(String string, Boolean def) {
		preferences.putBoolean("Boolean/" + string, def);
		if (preferences.getBoolean("Boolean/ + key", def) != def) {
			System.err.println("pref Key" + "Boolean/" + def + "already taken by a different type");
		}

	}



	/**
	 * Adds SendableChooser to SmartDashboard for Auto route choosing.
	 */

	public void updateAutoDashboard() {
		// Auto chooser
		autoChooser = new SendableChooser<StartingPosition>();
		// autoChooser.addObject("Left", StartingPosition.LEFT);
		// autoChooser.addObject("Middle", StartingPosition.MID);
		// autoChooser.addObject("Right", StartingPosition.RIGHT);
		autoChooser.addDefault("Forward (cross line)", StartingPosition.FORWARD);
		autoChooser.addObject("Stay", StartingPosition.STAY);
		autoChooser.addObject("Forward and outake, robot on LEFT", StartingPosition.FORWARD_AND_UP_LEFT);
		autoChooser.addObject("Forward and outake, robot on RIGHT", StartingPosition.FORWARD_AND_UP_RIGHT);

		autoChooser.setName("AutonomusControl", "Auto Chooser");
		startPos = autoChooser.getSelected();
		SmartDashboard.putData("DriveTeam/Auto Chooser", autoChooser);
		SmartDashboard.putString("Drive/Selected Starting Position", startPos.toString());

		ejectBoxChooser = new SendableChooser<Boolean>();
		ejectBoxChooser.addObject("TRUE", true);
		ejectBoxChooser.addDefault("FALSE", false);
		ejectBoxChooser.setName("AutonomusControl", "Eject Box Chooser");
		SmartDashboard.putData("DriveTeam/Eject Box Chooser", ejectBoxChooser);
		SmartDashboard.putString("Drive/Selected Eject Box", ejectBoxSelection.toString());

		SmartDashboard.updateValues(); // always run at END of updateAutoDashboard

	}

	public void smartDashboardInit() {
		// Elevator
		RobotMap.elevatorTalon.setName("Elevator", "Motor");

		// Intake
		RobotMap.intakeTalonA.setName("Intake", "LeftMotor");
		RobotMap.intakeTalonB.setName("Intake", "RightMotor");
		RobotMap.intakeDistanceAnalogInput.setName("Intake", "Infrared");

		// DriveTrain
		RobotMap.driveTrain.left.motorGroup.setName("DriveTrain", "LeftMotors");
		RobotMap.driveTrain.right.motorGroup.setName("DriveTrain", "RightMotors");
		RobotMap.driveTrain.left.encoder.setName("DriveTrain", "LeftEncoder");
		RobotMap.driveTrain.right.encoder.setName("DriveTrain", "RightEncoder");
		// RobotMap.differentialDrive.setName("DriveTrain", "DifferentialDrive");

		// General
		RobotMap.pdp.setName("General", "PDP");
		RobotMap.gyro.setName("General", "SPI Gyro");
		RobotMap.ahrs.setName("General", "AHRS Gyro");
		RobotMap.ultrasonic.setName("General", "Ultrasonic");
	}

	public void smartDashboardPeriodic() {

		SmartDashboard.putData("Debug/PDP", RobotMap.pdp);
		SmartDashboard.putNumber("Debug/Throttle Input", RobotMap.driveGamepad.getLeftJoystickY());
		SmartDashboard.putNumber("Debug/Turn Steepness Input", RobotMap.driveGamepad.getRightJoystickX());
		SmartDashboard.putNumber("Debug/AutoDriveTime", RobotDriveStraight.targetTime);

		// SmartDashboard.putData("Debug/DifferentialDrive",
		// RobotMap.differentialDrive);

		SmartDashboard.putNumber("Debug/Left Encoder Distance", RobotMap.driveTrain.left.getDistanceTraveled());
		SmartDashboard.putNumber("Debug/Right Encoder Distance", RobotMap.driveTrain.right.getDistanceTraveled());
		SmartDashboard.putData("Debug/Left Drive Encoder", RobotMap.driveTrain.left.encoder);
		SmartDashboard.putData("Debug/Right Drive Encoder", RobotMap.driveTrain.right.encoder);

		SmartDashboard.putBoolean("Debug/Box In", RobotMap.intake.isBoxPosition(Intake.BoxPosition.IN));
		SmartDashboard.putBoolean("Debug/Box Out", RobotMap.intake.isBoxPosition(Intake.BoxPosition.OUT));
		SmartDashboard.putBoolean("Debug/Box In and Out At The Same Time",
				RobotMap.intake.isBoxPosition(Intake.BoxPosition.MOVING));
		SmartDashboard.putNumber("Debug/Analog Infrared Voltage", RobotMap.intake.getAnalogInput());

		SmartDashboard.putData("Debug/IntakeAMotorLEFT", RobotMap.intakeTalonA);
		SmartDashboard.putData("Debug/IntakeBMotorRIGHT", RobotMap.intakeTalonB);

		SmartDashboard.putBoolean("Debug/Elevator Top Limit Switch", RobotMap.elevator.isTopSwitched());
		SmartDashboard.putBoolean("Debug/Elevator Bottom Limit Switch", RobotMap.elevator.isBottomSwitched());
		SmartDashboard.putNumber("Debug/Elevator Motor", RobotMap.elevator.setSpeed);

		SmartDashboard.putData("Debug/SPI Gyro", RobotMap.gyro);
		SmartDashboard.putData("Debug/AHRS Gyro", RobotMap.ahrs);
		SmartDashboard.putNumber("Debug/Ultrasonic", RobotMap.ultrasonic.getRangeInches());

		SmartDashboard.putData("Debug/DriveTrainSubsystem", RobotMap.driveTrain);
		SmartDashboard.putData("Debug/ElevatorSubsystem", RobotMap.elevator);
		SmartDashboard.putData("Debug/IntakeSubsystem", RobotMap.intake);
		if (autonomusCommand != null) {
			SmartDashboard.putData("Debug/AutonomusCommand", this.autonomusCommand);
		}
		SmartDashboard.updateValues();
	}
}