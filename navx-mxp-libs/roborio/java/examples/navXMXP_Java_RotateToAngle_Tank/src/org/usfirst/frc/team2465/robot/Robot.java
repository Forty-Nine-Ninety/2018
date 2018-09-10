package org.usfirst.frc.team2465.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * This is a demo program showing the use of the navX MXP to implement
 * the "rotate to angle", "zero yaw" and "drive straight" on a Tank
 * drive system.
 *
 * If Left Joystick Button 0 is pressed, a "turn" PID controller will 
 * set to point to a target angle, and while the button is held the drive
 * system will rotate to that angle (NOTE:  tank drive systems cannot simultaneously
 * move forward/reverse while rotating).
 *
 * This example also includes a feature allowing the driver to "reset"
 * the "yaw" angle.  When the reset occurs, the new gyro angle will be
 * 0 degrees.  This can be useful in cases when the gyro drifts, which
 * doesn't typically happen during a FRC match, but can occur during
 * long practice sessions.
 *
 * Finally, if Left Joystick button 2 is held, the "turn" PID controller will
 * be set to point to the current heading, and while the button is held,
 * the driver system will continue to point in the direction.  The robot 
 * can drive forward and backward (the magnitude of motion is the average
 * of the Y axis values on the left and right joysticks).
 *
 * Note that the PID Controller coefficients defined below will need to
 * be tuned for your drive system.
 */

public class Robot extends SampleRobot implements PIDOutput {
    DifferentialDrive myRobot;  // class that handles basic drive operations
    Joystick leftStick;  // set to ID 1 in DriverStation
    Joystick rightStick; // set to ID 2 in DriverStation
    AHRS ahrs;

    PIDController turnController;
    double rotateToAngleRate;
    
    /* The following PID Controller coefficients will need to be tuned */
    /* to match the dynamics of your drive system.  Note that the      */
    /* SmartDashboard in Test mode has support for helping you tune    */
    /* controllers by displaying a form where you can enter new P, I,  */
    /* and D constants and test the mechanism.                         */
    
    static final double kP = 0.03;
    static final double kI = 0.00;
    static final double kD = 0.00;
    static final double kF = 0.00;
    
    static final double kToleranceDegrees = 2.0f;    
    
    static final double kTargetAngleDegrees = 90.0f;
    
    // Channels for the wheels
    final static int leftChannel	= 0;
    final static int rightChannel	= 1;
    
    Spark leftMotor;
    Spark rightMotor;

    public Robot() {
    	leftMotor = new Spark(leftChannel);
    	rightMotor = new Spark(rightChannel);
        myRobot = new DifferentialDrive(leftMotor, rightMotor); 
        myRobot.setExpiration(0.1);
        leftStick = new Joystick(0);
        rightStick = new Joystick(1);
        try {
			/***********************************************************************
			 * navX-MXP:
			 * - Communication via RoboRIO MXP (SPI, I2C, TTL UART) and USB.            
			 * - See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface.
			 * 
			 * navX-Micro:
			 * - Communication via I2C (RoboRIO MXP or Onboard) and USB.
			 * - See http://navx-micro.kauailabs.com/guidance/selecting-an-interface.
			 * 
			 * Multiple navX-model devices on a single robot are supported.
			 ************************************************************************/
            ahrs = new AHRS(SPI.Port.kMXP); 
        } catch (RuntimeException ex ) {
            DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
        }
        turnController = new PIDController(kP, kI, kD, kF, ahrs, this);
        turnController.setInputRange(-180.0f,  180.0f);
        turnController.setOutputRange(-1.0, 1.0);
        turnController.setAbsoluteTolerance(kToleranceDegrees);
        turnController.setContinuous(true);
        turnController.disable();
        
        /* Add the PID Controller to the Test-mode dashboard, allowing manual  */
        /* tuning of the Turn Controller's P, I and D coefficients.            */
        /* Typically, only the P value needs to be modified.                   */
        LiveWindow.addActuator("DriveSystem", "RotateController", turnController);        
    }
    
    /**
     * Runs the motors with tank steering.
     */
    public void operatorControl() {
        myRobot.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
        	if ( leftStick.getRawButton(0)) {
        		/* While this button is held down, rotate to target angle.  
        		 * Since a Tank drive system cannot move forward simultaneously 
        		 * while rotating, all joystick input is ignored until this
        		 * button is released.
        		 */
        		if (!turnController.isEnabled()) {
        			turnController.setSetpoint(kTargetAngleDegrees);
        			rotateToAngleRate = 0; // This value will be updated in the pidWrite() method.
        			turnController.enable();
        		}
        		double leftStickValue = rotateToAngleRate;
        		double rightStickValue = rotateToAngleRate;
        		myRobot.tankDrive(leftStickValue,  rightStickValue);
        	} else if ( leftStick.getRawButton(1)) {
        		/* "Zero" the yaw (whatever direction the sensor is 
        		 * pointing now will become the new "Zero" degrees.
        		 */
        		ahrs.zeroYaw();
        	} else if ( leftStick.getRawButton(2)) {
        		/* While this button is held down, the robot is in
        		 * "drive straight" mode.  Whatever direction the robot
        		 * was heading when "drive straight" mode was entered
        		 * will be maintained.  The average speed of both 
        		 * joysticks is the magnitude of motion.
        		 */
        		if(!turnController.isEnabled()) {
        			// Acquire current yaw angle, using this as the target angle.
        			turnController.setSetpoint(ahrs.getYaw());
        			rotateToAngleRate = 0; // This value will be updated in the pidWrite() method.
        			turnController.enable();
        		}
        		double magnitude = (leftStick.getY() + rightStick.getY()) / 2;
        		double leftStickValue = magnitude + rotateToAngleRate;
        		double rightStickValue = magnitude - rotateToAngleRate;
        		myRobot.tankDrive(leftStickValue,  rightStickValue);
        	} else {
        		/* If the turn controller had been enabled, disable it now. */
        		if(turnController.isEnabled()) {
        			turnController.disable();
        		}
        		/* Standard tank drive, no driver assistance. */
        		myRobot.tankDrive(leftStick.getY(), rightStick.getY());
        	}
            Timer.delay(0.005);		// wait for a motor update time
        }
    }

	@Override
    /* This function is invoked periodically by the PID Controller, */
    /* based upon navX MXP yaw angle input and PID Coefficients.    */
    public void pidWrite(double output) {
        rotateToAngleRate = output;
    }
}
