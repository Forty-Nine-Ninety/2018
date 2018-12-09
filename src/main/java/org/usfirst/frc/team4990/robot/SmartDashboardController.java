package org.usfirst.frc.team4990.robot;

import org.usfirst.frc.team4990.robot.Robot.StartingPosition;
import org.usfirst.frc.team4990.robot.commands.RobotDriveStraight;
import org.usfirst.frc.team4990.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDashboardController {

	private static Preferences preferences = Preferences.getInstance();

	/**
	 * Retrieves a numerical constant from SmartDashbaord/Shuffleboard.
	 * 
	 * @param key
	 *            string key to identify value
	 * @param def
	 *            number to return if no value is retrieved
	 * @author Deep Blue Robotics (Team 199)
	 */

	public static double getConst(String key, double def) {

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
	 * Adds a constant to SmartDashbaord/Shuffleboard.
	 * 
	 * @param key
	 *            string key to identify value
	 * @param def
	 *            value to be stored
	 * @author Deep Blue Robotics (Team 199)
	 */

	
	public static void putConst(String key, double def) {
		preferences.putDouble("Const/" + key, def);
		if (preferences.getDouble("Const/ + key", def) != def) {
			System.err.println("pref Key" + "Const/" + key + "already taken by a different type");
		}

	}



	/**
	 * Adds SendableChooser to SmartDashboard for Auto route choosing.
	 */

	public static void updateAutoDashboard() {
		// Auto chooser
		Robot.autoChooser = new SendableChooser<StartingPosition>();
		// Robot.autoChooser.addObject("Left", StartingPosition.LEFT);
		// Robot.autoChooser.addObject("Middle", StartingPosition.MID);
		// Robot.autoChooser.addObject("Right", StartingPosition.RIGHT);
		Robot.autoChooser.addDefault("Forward (cross line)", StartingPosition.FORWARD);
		Robot.autoChooser.addObject("Stay", StartingPosition.STAY);
		Robot.autoChooser.addObject("Forward and outake, robot on LEFT", StartingPosition.FORWARD_AND_UP_LEFT);
		Robot.autoChooser.addObject("Forward and outake, robot on RIGHT", StartingPosition.FORWARD_AND_UP_RIGHT);
		Robot.autoChooser.addObject("Testing", StartingPosition.TEST);

		Robot.autoChooser.setName("AutonomusControl", "Auto Chooser");
		Robot.startPos = Robot.autoChooser.getSelected();
		SmartDashboard.putData("DriveTeam/Auto Chooser", Robot.autoChooser);
		SmartDashboard.putString("Drive/Selected Starting Position", Robot.startPos.toString());

		Robot.ejectBoxChooser = new SendableChooser<Boolean>();
		Robot.ejectBoxChooser.addObject("TRUE", true);
		Robot.ejectBoxChooser.addDefault("FALSE", false);
		Robot.ejectBoxChooser.setName("AutonomusControl", "Eject Box Chooser");
		SmartDashboard.putData("DriveTeam/Eject Box Chooser", Robot.ejectBoxChooser);
		SmartDashboard.putString("Drive/Selected Eject Box", Robot.ejectBoxSelection.toString());

		SmartDashboard.updateValues(); // always run at END of updateAutoDashboard

	}

	public static void smartDashboardInit() {
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
		
		SmartDashboard.putData("Debug/AHRS Gyro", RobotMap.ahrs);
		SmartDashboard.putNumber("Debug/Ultrasonic", RobotMap.ultrasonic.getRangeInches());

		SmartDashboard.putData("Debug/DriveTrainSubsystem", RobotMap.driveTrain);
		SmartDashboard.putData("Debug/ElevatorSubsystem", RobotMap.elevator);
		SmartDashboard.putData("Debug/IntakeSubsystem", RobotMap.intake);
		if (Robot.autonomusCommand != null) {
			SmartDashboard.putData("Debug/AutonomusCommand", Robot.autonomusCommand);
		}
		SmartDashboard.updateValues();
	}
}