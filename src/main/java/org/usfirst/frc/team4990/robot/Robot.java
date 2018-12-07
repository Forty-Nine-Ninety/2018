package org.usfirst.frc.team4990.robot;

import org.usfirst.frc.team4990.robot.commands.AutonomusCommand;
import org.usfirst.frc.team4990.robot.SmartDashboardController;

import edu.wpi.first.wpilibj.CameraServer;
//This entire robot code is dedicated to Kyler Rosen, a friend, visionary, and a hero to the empire that is the Freshmen Union
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

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
		STAY, FORWARD, FORWARD_AND_UP_LEFT, FORWARD_AND_UP_RIGHT, TEST
	};

	public static SendableChooser<StartingPosition> autoChooser;
	public static StartingPosition startPos = StartingPosition.FORWARD;

	public static SendableChooser<Boolean> ejectBoxChooser;
	public static Boolean ejectBoxSelection = false;

	public static Command autonomusCommand;

	public static OI oi;
	public static RobotMap robotMap;

	public static SmartDashboardController smartDashboardController = new SmartDashboardController();

	/**
	 * @author Team 4990
	 */

	public void robotInit() { // This function is run when the robot is first started up and should be used
								// for any initialization code.

		System.out.println("Version 1.29.2018.6.18");

		robotMap = new RobotMap();
		oi = new OI();

		CameraServer.getInstance().startAutomaticCapture();

		SmartDashboardController.updateAutoDashboard();

		SmartDashboardController.smartDashboardInit();

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
			smartDashboardController.smartDashboardPeriodic();
			SmartDashboardController.updateAutoDashboard();
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
		smartDashboardController.smartDashboardPeriodic();
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
		smartDashboardController.smartDashboardPeriodic();
	}

	public void testInit() {
		smartDashboardController.smartDashboardPeriodic();
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
		smartDashboardController.smartDashboardPeriodic();
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
