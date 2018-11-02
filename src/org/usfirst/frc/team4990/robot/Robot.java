package org.usfirst.frc.team4990.robot;

import org.usfirst.frc.team4990.robot.commands.AutonomusCommand;
import org.usfirst.frc.team4990.robot.commands.RobotDriveStraight;
import org.usfirst.frc.team4990.robot.subsystems.Intake;

//This entire robot code is dedicated to Kyler Rosen, a friend, visionary, and a hero to the empire that is the Freshmen Union
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	 * 
	 * @author Class of '21 (created in 2018 season)
	 *
	 */
	public enum StartingPosition {
		// LEFT,
		// MID,
		// RIGHT,
		// ERROR,
		STAY, FORWARD, FORWARD_AND_UP_LEFT, FORWARD_AND_UP_RIGHT
	};

	static public SendableChooser<StartingPosition> autoChooser;
	static public StartingPosition startPos = StartingPosition.FORWARD;

	static public SendableChooser<Boolean> ejectBoxChooser;
	static public Boolean ejectBoxSelection = false;

	public Command autonomusCommand;

	public static OI oi;
	public static RobotMap robotMap;

	/**
	 * COMPLETE credit goes to Deep Blue Robotics. Link:
	 * https://github.com/DeepBlueRobotics/RobotCode2018/blob/master/Robot2018/src/main/java/org/usfirst/frc/team199/Robot2018/Robot.java
	 * Benjamin has received permission to use this code. Thank you team 199!
	 */

	public static SmartDashboardInterface sd = new SmartDashboardInterface();

	/**
	 * @author Team 4990
	 */

	public void robotInit() { // This function is run when the robot is first started up and should be used
								// for any initialization code.

		System.out.println("Version 1.29.2018.6.18");

		robotMap = new RobotMap();
		oi = new OI();

		// CameraServer.getInstance().startAutomaticCapture();

		updateAutoDashboard();

		smartDashboardInit();

		resetSensors();

	}

	public void robotPeriodic() {
		// Don't put anything here or else the robot might lag severely.
	}

	public void disabledInit() {
		System.out.println("ROBOT DISABLED.");
		if (autonomusCommand != null) {
			autonomusCommand.cancel();
		}
	}

	public void disabledPeriodic() { // This function is run periodically when the robot is DISABLED. Be careful.
		if (System.currentTimeMillis() % 200 > 0 && System.currentTimeMillis() % 500 < 50) { // runs around every 1
																								// second
			startPos = autoChooser.getSelected();
			ejectBoxSelection = ejectBoxChooser.getSelected();
			smartDashboardPeriodic();
			updateAutoDashboard();
		}

	}

	public void autonomousInit() { // This function is called at the start of autonomous
		startPos = autoChooser.getSelected();
		autonomusCommand = new AutonomusCommand();
		autonomusCommand.start();

		System.out.println("Auto Init complete");
	}

	public void autonomousPeriodic() { // This function is called periodically during autonomous
		Scheduler.getInstance().run(); // runs execute() of current commands and period() of subsystems.
		smartDashboardPeriodic();
	}

	public void teleopInit() { // This function is called at the start of teleop
		if (autonomusCommand != null) {
			autonomusCommand.cancel();
		}

		RobotMap.driveTrain.left.clearStickyFaults();
		RobotMap.driveTrain.right.clearStickyFaults();

	}

	public void teleopPeriodic() { // This function is called periodically during teleop
		Scheduler.getInstance().run(); // runs execute() of current commands and period() of subsystems.
		smartDashboardPeriodic();
	}

	public void testInit() {
		smartDashboardPeriodic();
		startPos = autoChooser.getSelected();
		if (autonomusCommand != null) {
			autonomusCommand = new AutonomusCommand();
			autonomusCommand.start();
		}
		// teleopInit();
	}

	public void testPeriodic() {
		Scheduler.getInstance().run(); // runs execute() of current commands and period() of subsystems.
		// teleopPeriodic();
		// System.out.println(ahrs.getAngle());
		smartDashboardPeriodic();
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

	public void simpleDashboardPeriodic() {
		SmartDashboard.putBoolean("DriveTeam/Box", RobotMap.intake.isBoxPosition(Intake.BoxPosition.OUT));
		SmartDashboard.putBoolean("DriveTeam/Elevator Top Limit Switch", RobotMap.elevator.isTopSwitched());
		SmartDashboard.putBoolean("DriveTeam/Elevator Bottom Limit Switch", RobotMap.elevator.isBottomSwitched());
		SmartDashboard.putNumber("DriveTeam/AHRS Gyro", RobotMap.ahrs.getAngle());
		SmartDashboard.putString("DriveTeam/Elevator Status", RobotMap.elevator.status);

		SmartDashboard.updateValues(); // always run at END of simpleDashboardPeriodic
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

	public void resetSensors() {
		System.out.println("[SensorReset] Starting gyro calibration. DON'T MOVE THE ROBOT...");
		RobotMap.gyro.calibrate();
		RobotMap.ahrs.reset();
		System.out.println("[SensorReset] Gyro calibration done. Resetting encoders...");
		RobotMap.driveTrain.resetDistanceTraveled();
		System.out.println("[SensorReset] complete.");
	}

	// ever heard of the tale of last minute code
	// I thought not, it is not a tale the chairman will tell to you
	// (Keep below last function of Robot.java)
	
}
